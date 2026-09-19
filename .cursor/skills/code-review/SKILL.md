---
name: code-review
description: >-
  Ревьюит дифф новой сущности против .cursor/rules (слои, инъекция, тесты,
  ошибки). Используй на шаге ревью AI-пайплайна, по PR, после CRUD, когда
  просят разобрать замечания до merge.
---

# Ревью

## Когда применять
- Есть дифф (фича, PR, шаг 7 пайплайна).
- Нужен разбор по контракту проекта, не общий style-nit.

Не применять для написания фичи или рефакторинга — сначала список замечаний, правки только если явно попросили закрыть блокеры.

## Что делает (процедура)
1. Смотреть **только дифф** плюс соседние вызовы (mapper, `SecurityConfig`, `DataBuilder`). Не предлагать соседний рефакторинг вне фичи.
2. Сверить с rules: `architecture-layers`, `controller`, `service`, `repository`, `injection`, `naming`, `error-handling`, `tests`.
3. Выдать список. Каждый пункт: файл:строка, правило, что сломано, как должно быть. Без «можно ещё подумать».
4. Если просят закрыть замечания — править только **блокеры**, затем `./gradlew test`. Ничего не трогать «заодно».

## Чеклист (блокеры)

- Контроллер ходит в репозиторий или содержит бизнес-логику / `try/catch`.
- Сервис принимает/возвращает DTO, отдаёт `Optional`, методы `find*`.
- Сервис инжектит чужой `*Repository` вместо чужого `*Service`.
- Mapper — Spring-бин и/или вызывает сервис (не повторять `RestaurantModelMapper` → `ReviewService`).
- Mapper не static / не в пакете `controller.mapper` при новом коде.
- Entity: не Kotlin `data class`, или ассоциации/коллекции в primary constructor; eager `ManyToOne` без причины.
- Репозиторий: `JpaRepository` / методы «на будущее»; `Page` без отдельного `COUNT`.
- Тесты: `@WebMvcTest`, Mockito, моки слоёв, SQL-фикстуры вместо `DataBuilder`.
- Ошибки: свои exception-классы; not found не `NoSuchElementException`; инвариант не `IllegalArgumentException`.
- Инъекция: `@Autowired` на поле, Lombok `@RequiredArgsConstructor` в проде.

## Не блокеры
Формулировки Swagger, имена тестовых методов при покрытом поведении, копирование `CascadeType.DETACH` с `Vote`.

## Формат ответа

```text
Блокер: `path:line` — <правило>. Сейчас: … Надо: …
Замечание: `path:line` — …
Можно позже: …
```

**Gate:** merge/шаг 8 нельзя, пока есть блокеры. Замечания — по желанию автора.

## Пример
**Вход:** дифф Review.
**Выход:** блокер — `RestaurantModelMapper` инжектит `ReviewService`; замечание — `ReviewModelMapper` как `@Component` вместо static `TagModelMapper`.
