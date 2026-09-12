# Паспорт AI-системы

Карта артефактов Cursor для этого репозитория: что есть, зачем, как связано с пайплайном. Пошаговый процесс — в [ai-pipeline.md](ai-pipeline.md).

Стек проверки: `./gradlew test` (ktlint/detekt нет). При расхождении **rules важнее skills**.

---

## Карта артефактов

```
.cursor/
├── hooks.json
├── mcp.json
├── hooks/
│   ├── mark-code-edit.sh
│   ├── run-tests-on-stop.sh
│   └── state/                 # gitignored
├── rules/                     # 8 × .mdc
└── skills/                    # 4 × SKILL.md
docs/
├── ai-system.md               # этот паспорт
├── ai-pipeline.md             # 9 шагов фичи
└── ai-prompts/
    ├── feedback.md
    ├── tdd.md
    └── user-controller.md
```

Не используются: `.cursor/agents/`, `.cursor/commands/`, `AGENTS.md`.

---

## Rules

| Файл | Scope | Роль |
|------|-------|------|
| `architecture-layers.mdc` | alwaysApply | Controller → Mapper → Service → Repository → Entity |
| `controller.mdc` | `**/controller/**` | тонкий REST, DTO, mapper |
| `service.mdc` | `**/service/**` | entity in/out; чужие сущности через их `*Service` |
| `repository.mdc` | `**/repository/**` | методы только под нужды сервиса |
| `tests.mdc` | `src/test/**` | `@SpringBootTest`, DataBuilder, `@DisplayName` |
| `error-handling.mdc` | `src/main/**` | JDK-исключения, RestExceptionHandler |
| `injection.mdc` | `src/main/**` | конструкторная инъекция, без `@Autowired` |
| `naming.mdc` | `**/*.java` | пакеты и имена классов/DTO |

Источник: [`.cursor/rules/`](../.cursor/rules/).

---

## Skills

| Skill | Шаг пайплайна | Роль |
|-------|---------------|------|
| `unit-test` | 4 (red) | красные тесты сервиса/контроллера до реализации |
| `crud-endpoint` | 5 (green) | слои CRUD + миграция; существующие тесты не трогать |
| `code-review` | 7 | ревью диффа против rules |
| `refactor-srp` | 8 | чистка без смены поведения (static mapper, SRP) |

Источник: [`.cursor/skills/`](../.cursor/skills/).

---

## Hooks

Конфиг: [`.cursor/hooks.json`](../.cursor/hooks.json).

| Событие | Скрипт | Поведение |
|---------|--------|-----------|
| `afterFileEdit` | `hooks/mark-code-edit.sh` | dirty-флаг при правках `src/**` / Gradle (не `.cursor/`, не `*.md`) |
| `stop` | `hooks/run-tests-on-stop.sh` | если dirty — `./gradlew test`; при падении `followup_message` (до 3 раз) |

- Пропуск цикла на один ход: `touch .cursor/skip-test-loop` (файл в `.gitignore`).
- Состояние: `.cursor/hooks/state/` (gitignore).

Hooks усиливают шаг 6 пайплайна (верификация), не заменяют его.

---

## MCP

[`.cursor/mcp.json`](../.cursor/mcp.json) — сервер Postgres (`@modelcontextprotocol/server-postgres`).

Путь к `npx` и URL БД — локальная машина разработчика, не контракт репозитория.

---

## Документы

| Файл | Роль |
|------|------|
| [ai-pipeline.md](ai-pipeline.md) | 9-шаговый процесс новой сущности |
| [ai-prompts/feedback.md](ai-prompts/feedback.md) | вопросы до кода (шаг 1 пайплайна) |
| [ai-prompts/tdd.md](ai-prompts/tdd.md) | «почини метод, тест не трогай» — вне 9 шагов |
| [ai-prompts/user-controller.md](ai-prompts/user-controller.md) | разовый промпт UserController — вне 9 шагов |

---

## Контракт слоёв и образцы

Цепочка: **Controller → Mapper → Service → Repository → Entity**.

- Контроллер не ходит в репозиторий и не содержит бизнес-логики.
- Сервис принимает/возвращает entity, не DTO; чужие сущности — через их `*Service`.
- Mapper в пакете `controller` — граница web/domain; DTO не покидают web-слой.

**Копировать:** `Tag*` (CRUD) + static `TagModelMapper`.

**Не копировать как образец:** `RestaurantModelMapper`, `ReviewModelMapper` (`@Component` + сервис в mapper).

---

## Связь с пайплайном

```text
1 Задача → 2 Контекст → 3 План [GATE] → 4 Тесты(red) → 5 Реализация(green)
→ 6 Верификация (+ hooks) → 7 Ревью [GATE] → 8 Рефакторинг → 9 PR
```

| Шаг | Инструмент |
|-----|------------|
| 1 | промпт + `ai-prompts/feedback.md` |
| 2 | `@` на rules и образцы |
| 3 | режим Plan |
| 4 | skill `unit-test` |
| 5 | skill `crud-endpoint` |
| 6 | `./gradlew test` (+ stop-hook) |
| 7 | skill `code-review` |
| 8 | skill `refactor-srp` |
| 9 | коммит + `gh pr create` |

Детали и промпты — в [ai-pipeline.md](ai-pipeline.md).

---

## Как расширять

| Что | Куда | Обновить паспорт |
|-----|------|------------------|
| rule | `.cursor/rules/*.mdc` (+ frontmatter) | таблица Rules |
| skill | `.cursor/skills/<name>/SKILL.md` | таблица Skills + шаг пайплайна |
| hook | `.cursor/hooks/` + запись в `hooks.json` | раздел Hooks |
| промпт | `docs/ai-prompts/` | таблица Документы |

При смене процесса править [ai-pipeline.md](ai-pipeline.md), затем этот паспорт.

---

## Changelog (week-8)

- Hooks: автопрогон `./gradlew test` после правок кода (`afterFileEdit` + `stop`, loop ≤ 3).
- MCP: локальный Postgres-сервер в `.cursor/mcp.json`.
- `tests.mdc`: обязательный `@DisplayName` на каждый `@Test`.
