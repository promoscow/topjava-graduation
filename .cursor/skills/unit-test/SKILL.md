---
name: unit-test
description: >-
  Пишет красные тесты сервиса и контроллера в стиле проекта (AbstractTest,
  DataBuilder, без моков) до реализации. Используй на шаге TDD / unit-test
  пайплайна, когда просят тесты на поведение сначала, red, покрытие новой сущности.
---

# Тесты (red)

## Когда применять
- Утверждён план фичи, прод-кода ещё нет (или его нельзя трогать).
- Нужно зафиксировать поведение сервиса и HTTP до реализации.
- Шаг 4 AI-пайплайна.

Не применять, если просят починить уже красный тест или написать CRUD — это другие скиллы.

## Что делает (процедура)
1. По плану собрать список методов сервиса и эндпоинтов. На каждый публичный метод — happy path + негатив (нет сущности, null id, инвариант).
2. Если типов нет — только скелет, чтобы тесты компилировались: entity, интерфейс сервиса, `JpaRepository`, сигнатуры DTO/контроллера без бизнес-логики. Реализацию не писать (`crud-endpoint` — следующий шаг).
3. `DataBuilder`: `buildX()` без сохранения, `saveX()` в БД. Уникальные строки — `RandomStringUtils.secure().nextAlphanumeric(16)`.
4. `*ServiceTest extends AbstractTest` в `service.impl`. `@Autowired` на **интерфейс**. Имена: `create`, `updateFailsWhenIdIsNull`, `getFailsWhenNotFound`. Негатив — `assertThrows(NoSuchElementException|IllegalArgumentException)`.
5. `*ControllerTest extends AbstractControllerTest`. Пакет как в проде: `controller.impl.admin` / `user`. MockMvc: `perform` → `.andDo(print())` → `status()` / `jsonPath`. Create: `isOk` + `$.id`. Update: PUT, затем GET. Delete: DELETE, затем GET → `isNotFound`. Валидация/инвариант: `isBadRequest`.
6. Прогнать `./gradlew test --tests <новые>`. Ожидание: **красные** (assert / 404 / 400 / «not implemented»). Если зелёные — тесты ничего не проверяют, усилить. Если не компилируются — дописать скелет, не реализацию.
7. Тесты не подгонять под будущий код. Прод-логику не реализовывать.

## Правила и ограничения
- `@SpringBootTest` через `AbstractTest`. `@WebMvcTest`, Mockito, моки слоёв — запрещены (даже если так написано в `crud-endpoint`).
- Фикстуры через `dataBuilder` или сервис, не SQL вручную.
- Не вызывать skill `crud-endpoint` на этом шаге.
- Не ослаблять ассерты, не помечать `@Disabled`.

## Пример
**Вход:** план Review — create/update/get, unique user+restaurant, USER/ADMIN.
**Выход:** `ReviewServiceTest`, `ReviewControllerUserTest`, `ReviewControllerAdminTest` + `DataBuilder.buildReview`/`saveReview`. `./gradlew test --tests '*Review*'` красный.
**Образец:** `@TagServiceTest`, `@TagControllerTest`, `@VoteControllerUserTest`.
