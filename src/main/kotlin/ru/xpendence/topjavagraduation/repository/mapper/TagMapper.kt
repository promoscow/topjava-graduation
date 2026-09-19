package ru.xpendence.topjavagraduation.repository.mapper

import ru.xpendence.topjavagraduation.entity.Tag
import java.sql.ResultSet

fun ResultSet.toTag(): Tag =
    Tag(
        id = getLong("id"),
        name = getString("name"),
    )
