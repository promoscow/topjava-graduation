package ru.xpendence.topjavagraduation.repository.mapper

import ru.xpendence.topjavagraduation.entity.Dish
import ru.xpendence.topjavagraduation.entity.Restaurant
import java.sql.ResultSet

fun ResultSet.toDish(): Dish =
    Dish(
        id = getLong("id"),
        name = getString("name"),
        price = getBigDecimal("price"),
        active = getObject("active") as Boolean?,
        restaurant = Restaurant(id = getLong("restaurant_id")),
    )

fun ResultSet.toDishWithRestaurant(): Dish =
    Dish(
        id = getLong("id"),
        name = getString("name"),
        price = getBigDecimal("price"),
        active = getObject("active") as Boolean?,
        restaurant = Restaurant(
            id = getLong("restaurant_id"),
            name = getString("restaurant_name"),
        ),
    )
