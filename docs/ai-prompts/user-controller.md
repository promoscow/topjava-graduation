Ты Senior Java разработчик. Пишешь идиоматичный, тестируемый код.
Необходимо создать регистрацию пользователей, чтобы они могли регистрироваться самостоятельно, менять свои данные и пароль. Создай UserController для сущности User.

Создание, обновление, получение, получение лимитированной выборки (фильтры + пагинация), удаление.

Контекст: сделай по образцу @src/main/java/ru/xpendence/topjavagraduation/controller/impl/user/RestaurantControllerUser.java + @src/main/java/ru/xpendence/topjavagraduation/service/RestaurantService.java + @src/main/java/ru/xpendence/topjavagraduation/controller/mapper/RestaurantModelMapper.java . Затрагиваемые модели: @src/main/java/ru/xpendence/topjavagraduation/entity/User.java , @src/main/java/ru/xpendence/topjavagraduation/repository/UserRepository.java

Правила: слои controller -> service -> repository.

Инъекция через конструктор.
Контроллер тонкий и возвращает UserResponse, принимает UserCreateRequest / UserUpdateRequest. `@Transactional` на сервисных методах там, где это нужно — посмотри, как реализовано в @src/main/java/ru/xpendence/topjavagraduation/service/impl/RestaurantServiceImpl.java .

Правила сериализации на UserResponse, не на User. Умеренная вализация на UserCreateRequest / UserUpdateRequest. Обработка ошибок в @src/main/java/ru/xpendence/topjavagraduation/exception/RestExceptionHandler.java .

Мапинг простой, желательно без создания бина, со статическими методами.

Для каждого созданного публичного метода напиши тесты (позитив + негатив), за пример возьми @src/test/java/ru/xpendence/topjavagraduation/service/impl/UserServiceTest.java , @src/test/java/ru/xpendence/topjavagraduation/controller/impl/user/RestaurantControllerUserTest.java .

Прогони тесты, почини красное. Не выдумывай лишнего, но новая функциональность должна быть самодостаточной.