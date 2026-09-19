package ru.xpendence.topjavagraduation.repository.mapper

import ru.xpendence.topjavagraduation.entity.Restaurant
import java.sql.ResultSet

fun ResultSet.toRestaurant(): Restaurant =
    Restaurant(
        id = getLong("id"),
        name = getString("name"),
    )
