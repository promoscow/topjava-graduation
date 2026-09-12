#!/usr/bin/env bash
# stop: run ./gradlew test after a completed agent turn that edited code.
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/../.." && pwd)"
STATE_DIR="$ROOT/.cursor/hooks/state"
SKIP_FILE="$ROOT/.cursor/skip-test-loop"
LOOP_LIMIT=3

input=$(cat)
status=$(echo "$input" | jq -r '.status // empty')
loop_count=$(echo "$input" | jq -r '.loop_count // 0')
conversation_id=$(echo "$input" | jq -r '.conversation_id // empty')

emit_empty() {
  echo '{}'
  exit 0
}

if [[ "$status" != "completed" ]]; then
  emit_empty
fi

if [[ -z "$conversation_id" ]]; then
  emit_empty
fi

dirty="$STATE_DIR/dirty-$conversation_id"
if [[ ! -f "$dirty" ]]; then
  emit_empty
fi

if [[ -f "$SKIP_FILE" ]]; then
  rm -f "$dirty"
  emit_empty
fi

if [[ "$loop_count" -ge "$LOOP_LIMIT" ]]; then
  rm -f "$dirty"
  emit_empty
fi

LOG=$(mktemp)
trap 'rm -f "$LOG"' EXIT

cd "$ROOT"
set +e
./gradlew test >"$LOG" 2>&1
exit_code=$?
set -e

if [[ "$exit_code" -eq 0 ]]; then
  rm -f "$dirty"
  echo "[run-tests-on-stop] tests passed" >&2
  emit_empty
fi

# Keep dirty on failure so the next stop (after follow-up) re-runs tests.

# Prefer failure-looking lines; fall back to tail.
body=$(grep -E 'FAILED|FAILURE|BUILD FAILED|Tests run:|AssertionError|expected:|but was:|Caused by:' "$LOG" | head -n 40 || true)
if [[ -z "$body" ]]; then
  body=$(tail -n 60 "$LOG")
fi

jq -n --arg failures "$body" --arg loop "$loop_count" \
  '{followup_message:
    ("Stop hook: ./gradlew test failed after your code changes (loop "
     + $loop + "/" + "3). Fix the failing tests below, then stop. "
     + "To skip the test loop for one turn: `touch .cursor/skip-test-loop`.\n\n"
     + "----- gradle test output -----\n" + $failures)}'

exit 0
