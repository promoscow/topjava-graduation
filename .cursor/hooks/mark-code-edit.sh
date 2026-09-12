#!/usr/bin/env bash
# afterFileEdit: mark conversation dirty when agent edits code files.
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/../.." && pwd)"
STATE_DIR="$ROOT/.cursor/hooks/state"

input=$(cat)
conversation_id=$(echo "$input" | jq -r '.conversation_id // empty')
file_path=$(echo "$input" | jq -r '.file_path // empty')

if [[ -z "$conversation_id" || -z "$file_path" ]]; then
  echo '{}'
  exit 0
fi

# Only track real code / build config edits.
is_code=0
case "$file_path" in
  */.cursor/*) ;;
  *.md) ;;
  */src/*) is_code=1 ;;
  *.gradle|*.gradle.kts) is_code=1 ;;
  */gradle.properties|gradle.properties) is_code=1 ;;
  */settings.gradle|settings.gradle|*/settings.gradle.kts|settings.gradle.kts) is_code=1 ;;
esac

if [[ "$is_code" -eq 1 ]]; then
  mkdir -p "$STATE_DIR"
  touch "$STATE_DIR/dirty-$conversation_id"
fi

echo '{}'
exit 0
