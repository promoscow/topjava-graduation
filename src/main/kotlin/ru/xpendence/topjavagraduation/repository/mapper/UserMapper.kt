package ru.xpendence.topjavagraduation.repository.mapper

import ru.xpendence.topjavagraduation.entity.User
import java.sql.ResultSet

fun ResultSet.toUser(): User =
    User(
        id = getLong("id"),
        username = getString("username"),
        password = getString("password"),
    )

fun ResultSet.toUserFromJoin(
    idColumn: String = "user_id",
    usernameColumn: String = "username",
    passwordColumn: String = "password",
): User =
    User(
        id = getLong(idColumn),
        username = getString(usernameColumn),
        password = getString(passwordColumn),
    )

fun Long.toUserStub(): User =
    User(id = this, username = "", password = "")
