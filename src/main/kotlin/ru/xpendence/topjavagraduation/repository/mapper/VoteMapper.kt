package ru.xpendence.topjavagraduation.repository.mapper

import ru.xpendence.topjavagraduation.entity.Restaurant
import ru.xpendence.topjavagraduation.entity.Vote
import java.sql.ResultSet

fun ResultSet.toVote(): Vote =
    Vote(
        id = getLong("id"),
        date = getDate("date").toLocalDate(),
        user = getLong("user_id").toUserStub(),
        restaurant = Restaurant(id = getLong("restaurant_id")),
    )

fun ResultSet.toVoteWithDetails(): Vote =
    Vote(
        id = getLong("id"),
        date = getDate("date").toLocalDate(),
        user = toUserFromJoin(),
        restaurant = Restaurant(
            id = getLong("restaurant_id"),
            name = getString("restaurant_name"),
        ),
    )
