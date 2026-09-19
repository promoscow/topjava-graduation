package ru.xpendence.topjavagraduation.repository.mapper

import ru.xpendence.topjavagraduation.entity.Restaurant
import ru.xpendence.topjavagraduation.entity.Review
import java.sql.ResultSet

fun ResultSet.toReviewWithDetails(): Review =
    Review(
        id = getLong("id"),
        rating = getInt("rating"),
        text = getString("text"),
        date = getDate("date").toLocalDate(),
        user = toUserFromJoin(),
        restaurant = Restaurant(
            id = getLong("restaurant_id"),
            name = getString("restaurant_name"),
        ),
    )
