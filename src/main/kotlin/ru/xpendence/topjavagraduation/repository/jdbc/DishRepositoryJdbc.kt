package ru.xpendence.topjavagraduation.repository.jdbc

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import ru.xpendence.topjavagraduation.entity.Dish
import ru.xpendence.topjavagraduation.repository.DishRepository
import ru.xpendence.topjavagraduation.repository.jdbc.JdbcSupport.addPaging
import ru.xpendence.topjavagraduation.repository.jdbc.JdbcSupport.limitSql
import ru.xpendence.topjavagraduation.repository.mapper.toDish
import ru.xpendence.topjavagraduation.repository.mapper.toDishWithRestaurant
import java.util.*

@Repository
class DishRepositoryJdbc(
    private val jdbc: NamedParameterJdbcTemplate,
) : DishRepository {

    override fun save(dish: Dish): Dish {
        return if (dish.id == null) insert(dish) else update(dish)
    }

    private fun insert(dish: Dish): Dish {
        val keyHolder = GeneratedKeyHolder()
        val params = MapSqlParameterSource()
            .addValue("name", dish.name)
            .addValue("price", dish.price)
            .addValue("active", dish.active)
            .addValue("restaurantId", dish.restaurant.id)
        jdbc.update(
            """
            INSERT INTO dishes (name, price, active, restaurant_id)
            VALUES (:name, :price, :active, :restaurantId)
            """.trimIndent(),
            params,
            keyHolder,
            arrayOf("id"),
        )
        return dish.copy(id = requireNotNull(keyHolder.key).toLong())
    }

    private fun update(dish: Dish): Dish {
        val params = MapSqlParameterSource()
            .addValue("id", dish.id)
            .addValue("name", dish.name)
            .addValue("price", dish.price)
            .addValue("active", dish.active)
        jdbc.update(
            """
            UPDATE dishes
            SET name = :name, price = :price, active = :active
            WHERE id = :id
            """.trimIndent(),
            params,
        )
        return dish
    }

    override fun findById(id: Long): Optional<Dish> {
        val list = jdbc.query(
            "SELECT id, name, price, active, restaurant_id FROM dishes WHERE id = :id",
            MapSqlParameterSource("id", id),
        ) { rs, _ -> rs.toDish() }
        return Optional.ofNullable(list.firstOrNull())
    }

    override fun findByIdWithRestaurant(id: Long): Optional<Dish> {
        val list = jdbc.query(
            """
            SELECT d.id, d.name, d.price, d.active, d.restaurant_id,
                   r.name AS restaurant_name
            FROM dishes d
            JOIN restaurants r ON r.id = d.restaurant_id
            WHERE d.id = :id
            """.trimIndent(),
            MapSqlParameterSource("id", id),
        ) { rs, _ -> rs.toDishWithRestaurant() }
        return Optional.ofNullable(list.firstOrNull())
    }

    override fun findActiveByIdWithRestaurant(id: Long): Optional<Dish> {
        val list = jdbc.query(
            """
            SELECT d.id, d.name, d.price, d.active, d.restaurant_id,
                   r.name AS restaurant_name
            FROM dishes d
            JOIN restaurants r ON r.id = d.restaurant_id
            WHERE d.id = :id AND d.active = true
            """.trimIndent(),
            MapSqlParameterSource("id", id),
        ) { rs, _ -> rs.toDishWithRestaurant() }
        return Optional.ofNullable(list.firstOrNull())
    }

    override fun getAllByRestaurantId(restaurantId: Long, pageable: Pageable): Page<Dish> {
        return pageByRestaurant(restaurantId, pageable, activeOnly = false)
    }

    override fun getAllActiveByRestaurantId(restaurantId: Long, pageable: Pageable): Page<Dish> {
        return pageByRestaurant(restaurantId, pageable, activeOnly = true)
    }

    private fun pageByRestaurant(restaurantId: Long, pageable: Pageable, activeOnly: Boolean): Page<Dish> {
        val where = buildString {
            append("WHERE d.restaurant_id = :restaurantId")
            if (activeOnly) {
                append(" AND d.active = true")
            }
        }
        val params = MapSqlParameterSource("restaurantId", restaurantId)
        val total = jdbc.queryForObject(
            "SELECT COUNT(*) FROM dishes d $where",
            params,
            Long::class.java,
        ) ?: 0L
        addPaging(params, pageable)
        val content = jdbc.query(
            """
            SELECT d.id, d.name, d.price, d.active, d.restaurant_id,
                   r.name AS restaurant_name
            FROM dishes d
            JOIN restaurants r ON r.id = d.restaurant_id
            $where
            ORDER BY d.id
            ${limitSql(pageable)}
            """.trimIndent(),
            params,
        ) { rs, _ -> rs.toDishWithRestaurant() }
        return PageImpl(content, pageable, total)
    }

    override fun setActiveFalseForAllByRestaurantId(restaurantId: Long): Int {
        return jdbc.update(
            "UPDATE dishes SET active = false WHERE restaurant_id = :restaurantId",
            MapSqlParameterSource("restaurantId", restaurantId),
        )
    }

    override fun existsById(id: Long): Boolean {
        return jdbc.queryForObject(
            "SELECT COUNT(*) FROM dishes WHERE id = :id",
            MapSqlParameterSource("id", id),
            Long::class.java,
        )?.let { it > 0 } ?: false
    }

    override fun deleteById(id: Long) {
        jdbc.update("DELETE FROM dishes WHERE id = :id", MapSqlParameterSource("id", id))
    }
}
