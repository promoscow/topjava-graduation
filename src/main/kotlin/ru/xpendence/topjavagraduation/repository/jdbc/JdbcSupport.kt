package ru.xpendence.topjavagraduation.repository.jdbc

import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import ru.xpendence.topjavagraduation.entity.Restaurant
import ru.xpendence.topjavagraduation.entity.User
import ru.xpendence.topjavagraduation.repository.mapper.toDish
import ru.xpendence.topjavagraduation.repository.mapper.toRole
import ru.xpendence.topjavagraduation.repository.mapper.toVote

object JdbcSupport {

    fun limitSql(pageable: Pageable): String =
        if (pageable.isUnpaged) "" else " LIMIT :limit OFFSET :offset"

    fun addPaging(params: MapSqlParameterSource, pageable: Pageable) {
        if (!pageable.isUnpaged) {
            params.addValue("limit", pageable.pageSize)
            params.addValue("offset", pageable.offset)
        }
    }

    fun attachDishesAndTodayVotes(jdbc: NamedParameterJdbcTemplate, restaurants: List<Restaurant>) {
        if (restaurants.isEmpty()) {
            return
        }
        val byId = restaurants.mapNotNull { restaurant ->
            restaurant.id?.let { it to restaurant }
        }.toMap()
        val ids = byId.keys.toList()
        if (ids.isEmpty()) {
            return
        }
        val params = MapSqlParameterSource("ids", ids)

        jdbc.query(
            """
            SELECT id, name, price, active, restaurant_id
            FROM dishes
            WHERE restaurant_id IN (:ids)
            """.trimIndent(),
            params,
        ) { rs, _ ->
            val restaurant = byId.getValue(rs.getLong("restaurant_id"))
            restaurant.dishes.add(rs.toDish())
        }

        jdbc.query(
            """
            SELECT id, date, user_id, restaurant_id
            FROM votes
            WHERE restaurant_id IN (:ids) AND date = CURRENT_DATE
            """.trimIndent(),
            params,
        ) { rs, _ ->
            val restaurant = byId.getValue(rs.getLong("restaurant_id"))
            restaurant.votes.add(rs.toVote())
        }
    }

    fun attachRoles(jdbc: NamedParameterJdbcTemplate, users: List<User>) {
        if (users.isEmpty()) {
            return
        }
        val byId = users.mapNotNull { user ->
            user.id?.let { it to user }
        }.toMap()
        val ids = byId.keys.toList()
        if (ids.isEmpty()) {
            return
        }
        val params = MapSqlParameterSource("ids", ids)

        jdbc.query(
            """
            SELECT ur.user_id, r.id, r.name
            FROM users_roles ur
            JOIN roles r ON r.id = ur.role_id
            WHERE ur.user_id IN (:ids)
            """.trimIndent(),
            params,
        ) { rs, _ ->
            val user = byId.getValue(rs.getLong("user_id"))
            user.roles.add(rs.toRole())
        }
    }
}
