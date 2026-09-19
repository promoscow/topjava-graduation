package ru.xpendence.topjavagraduation.repository.mapper

import ru.xpendence.topjavagraduation.entity.Role
import ru.xpendence.topjavagraduation.entity.type.RoleType
import java.sql.ResultSet

fun ResultSet.toRole(): Role =
    Role(
        id = getLong("id"),
        name = RoleType.valueOf(getString("name")),
    )
