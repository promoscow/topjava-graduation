## Выпускной проект TopJava

-----------------------------
##  Technical requirement:
Design and implement a REST API using Hibernate/Spring/SpringMVC (Spring-Boot preferred!) **without frontend**.

The task is:

Build a voting system for deciding where to have lunch.

* 2 types of users: admin and regular users
* Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
* Menu changes each day (admins do the updates)
* Users can vote for a restaurant they want to have lunch at today
* Only one vote counted per user
* If user votes again the same day:
    - If it is before 11:00 we assume that he changed his mind.
    - If it is after 11:00 then it is too late, vote can't be changed

Each restaurant provides a new menu each day.

As a result, provide a link to github repository. It should contain the code, README.md with API documentation and couple curl commands to test it (**better - link to Swagger**).

-----------------------------

## Swagger

Ссылка на сваггер:
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

API разделено на 2 части - админскую и пользовательскую. Выбор API осуществляется при помощи выпадающего меню справа сверху:

![Выбор сваггера](readme-resources/profiles-swagger.jpg)

Поскольку в проекте реализована авторизация, для осуществления запросов необходимо использовать токен.

Получить токен можно двумя способами:
1. Воспользоваться запросами авторизации по адресу: `test/http/login.http`
2. Воспользоваться готовыми токенами, представленными ниже.

Админский токен до 6 марта:

`eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbIlVTRVIiLCJBRE1JTiJdLCJpYXQiOjE2Njk0Njk0MzEsImV4cCI6MTY3ODEwOTQzMX0.P92PWExTw9o7mVfBXaGnqApT6TL8SFa_pawWs34lJKc`

Пользовательский токен до 6 марта:

`eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjaGlsbGVyMiIsInJvbGVzIjpbIlVTRVIiXSwiaWF0IjoxNjY5NDY5NTA1LCJleHAiOjE2NzgxMDk1MDV9.xySCafYAPKHWm6y-nfkMxWlUlxcbgZ27pAZJrIt3sBM`

Токен можно вставить для всех запросов сразу и для каждого запроса в отдельности:

![Куда вставить токен](readme-resources/insert-token.jpg)

-----------------------------

## Технологический стек

* Spring Boot 2.7.5
* Spring Security
* Hibernate
* Flyway
* Swagger
* H2
* Gradle