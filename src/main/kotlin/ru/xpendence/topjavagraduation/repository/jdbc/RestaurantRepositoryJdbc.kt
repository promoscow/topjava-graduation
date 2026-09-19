package ru.xpendence.topjavagraduation.repository.jdbc

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import ru.xpendence.topjavagraduation.entity.Restaurant
import ru.xpendence.topjavagraduation.repository.RestaurantRepository
import ru.xpendence.topjavagraduation.repository.jdbc.JdbcSupport.addPaging
import ru.xpendence.topjavagraduation.repository.jdbc.JdbcSupport.attachDishesAndTodayVotes
import ru.xpendence.topjavagraduation.repository.jdbc.JdbcSupport.limitSql
import ru.xpendence.topjavagraduation.repository.mapper.toRestaurant
import java.time.LocalDate
import java.util.*

@Repository
class RestaurantRepositoryJdbc(
    private val jdbc: NamedParameterJdbcTemplate,
) : RestaurantRepository {

    override fun save(restaurant: Restaurant): Restaurant {
        return if (restaurant.id == null) insert(restaurant) else update(restaurant)
    }

    private fun insert(restaurant: Restaurant): Restaurant {
        val keyHolder = GeneratedKeyHolder()
        val params = MapSqlParameterSource("name", restaurant.name)
        jdbc.update(
            "INSERT INTO restaurants (name) VALUES (:name)",
            params,
            keyHolder,
            arrayOf("id"),
        )
        return restaurant.copy(id = requireNotNull(keyHolder.key).toLong())
    }

    private fun update(restaurant: Restaurant): Restaurant {
        jdbc.update(
            "UPDATE restaurants SET name = :name WHERE id = :id",
            MapSqlParameterSource()
                .addValue("id", restaurant.id)
                .addValue("name", restaurant.name),
        )
        return restaurant
    }

    override fun findById(id: Long): Optional<Restaurant> {
        val list = jdbc.query(
            "SELECT id, name FROM restaurants WHERE id = :id",
            MapSqlParameterSource("id", id),
        ) { rs, _ -> rs.toRestaurant() }
        val restaurant = list.firstOrNull() ?: return Optional.empty()
        attachDishesAndTodayVotes(jdbc, listOf(restaurant))
        return Optional.of(restaurant)
    }

    override fun getByDishId(dishId: Long): Optional<Restaurant> {
        val list = jdbc.query(
            """
            SELECT r.id, r.name
            FROM restaurants r
            JOIN dishes d ON d.restaurant_id = r.id
            WHERE d.id = :dishId
            """.trimIndent(),
            MapSqlParameterSource("dishId", dishId),
        ) { rs, _ -> rs.toRestaurant() }
        val restaurant = list.firstOrNull() ?: return Optional.empty()
        attachDishesAndTodayVotes(jdbc, listOf(restaurant))
        return Optional.of(restaurant)
    }

    override fun findAll(pageable: Pageable): Page<Restaurant> {
        val params = MapSqlParameterSource()
        val total = jdbc.queryForObject(
            "SELECT COUNT(*) FROM restaurants",
            params,
            Long::class.java,
        ) ?: 0L
        addPaging(params, pageable)
        val content = jdbc.query(
            """
            SELECT id, name FROM restaurants
            ORDER BY id
            ${limitSql(pageable)}
            """.trimIndent(),
            params,
        ) { rs, _ -> rs.toRestaurant() }
        attachDishesAndTodayVotes(jdbc, content)
        return PageImpl(content, pageable, total)
    }

    override fun findChosenByVoteDate(date: LocalDate, pageable: Pageable): List<Restaurant> {
        val params = MapSqlParameterSource("date", date)
        addPaging(params, pageable)
        val content = jdbc.query(
            """
            SELECT r.id, r.name
            FROM restaurants r
            JOIN votes v ON v.restaurant_id = r.id
            WHERE v.date = :date
            GROUP BY r.id, r.name
            ORDER BY COUNT(v.id) DESC
            ${limitSql(pageable)}
            """.trimIndent(),
            params,
        ) { rs, _ -> rs.toRestaurant() }
        attachDishesAndTodayVotes(jdbc, content)
        return content
    }

    override fun deleteById(id: Long) {
        jdbc.update("DELETE FROM restaurants WHERE id = :id", MapSqlParameterSource("id", id))
    }
}
