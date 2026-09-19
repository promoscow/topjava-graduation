package ru.xpendence.topjavagraduation.repository.jdbc

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import ru.xpendence.topjavagraduation.entity.Review
import ru.xpendence.topjavagraduation.repository.ReviewRepository
import ru.xpendence.topjavagraduation.repository.jdbc.JdbcSupport.addPaging
import ru.xpendence.topjavagraduation.repository.jdbc.JdbcSupport.limitSql
import ru.xpendence.topjavagraduation.repository.mapper.toReviewWithDetails
import java.util.*

@Repository
class ReviewRepositoryJdbc(
    private val jdbc: NamedParameterJdbcTemplate,
) : ReviewRepository {

    override fun save(review: Review): Review {
        return if (review.id == null) insert(review) else update(review)
    }

    private fun insert(review: Review): Review {
        val keyHolder = GeneratedKeyHolder()
        val params = MapSqlParameterSource()
            .addValue("rating", review.rating)
            .addValue("text", review.text)
            .addValue("date", review.date)
            .addValue("userId", review.user.id)
            .addValue("restaurantId", review.restaurant.id)
        jdbc.update(
            """
            INSERT INTO reviews (rating, text, date, user_id, restaurant_id)
            VALUES (:rating, :text, :date, :userId, :restaurantId)
            """.trimIndent(),
            params,
            keyHolder,
            arrayOf("id"),
        )
        return review.copy(id = requireNotNull(keyHolder.key).toLong())
    }

    private fun update(review: Review): Review {
        jdbc.update(
            """
            UPDATE reviews
            SET rating = :rating, text = :text
            WHERE id = :id
            """.trimIndent(),
            MapSqlParameterSource()
                .addValue("id", review.id)
                .addValue("rating", review.rating)
                .addValue("text", review.text),
        )
        return review
    }

    override fun findByIdWithDetails(id: Long): Optional<Review> {
        return Optional.ofNullable(
            queryWithDetails(
                "WHERE rev.id = :id",
                MapSqlParameterSource("id", id),
            ).firstOrNull(),
        )
    }

    override fun findByUserIdAndRestaurantId(userId: Long, restaurantId: Long): Optional<Review> {
        return Optional.ofNullable(
            queryWithDetails(
                "WHERE rev.user_id = :userId AND rev.restaurant_id = :restaurantId",
                MapSqlParameterSource()
                    .addValue("userId", userId)
                    .addValue("restaurantId", restaurantId),
            ).firstOrNull(),
        )
    }

    override fun existsByUserIdAndRestaurantId(userId: Long, restaurantId: Long): Boolean {
        return jdbc.queryForObject(
            """
            SELECT COUNT(*) FROM reviews
            WHERE user_id = :userId AND restaurant_id = :restaurantId
            """.trimIndent(),
            MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("restaurantId", restaurantId),
            Long::class.java,
        )?.let { it > 0 } ?: false
    }

    override fun findAllByRestaurantId(restaurantId: Long, pageable: Pageable): Page<Review> {
        val params = MapSqlParameterSource("restaurantId", restaurantId)
        val total = jdbc.queryForObject(
            "SELECT COUNT(*) FROM reviews WHERE restaurant_id = :restaurantId",
            params,
            Long::class.java,
        ) ?: 0L
        addPaging(params, pageable)
        val content = queryWithDetails(
            "WHERE rev.restaurant_id = :restaurantId ORDER BY rev.id ${limitSql(pageable)}",
            params,
        )
        return PageImpl(content, pageable, total)
    }

    override fun findAverageRatingByRestaurantId(restaurantId: Long): Double? {
        return jdbc.query(
            "SELECT AVG(rating) FROM reviews WHERE restaurant_id = :restaurantId",
            MapSqlParameterSource("restaurantId", restaurantId),
        ) { rs, _ ->
            rs.getObject(1)?.let { (it as Number).toDouble() }
        }.firstOrNull()
    }

    override fun countByRestaurantId(restaurantId: Long): Long {
        return jdbc.queryForObject(
            "SELECT COUNT(*) FROM reviews WHERE restaurant_id = :restaurantId",
            MapSqlParameterSource("restaurantId", restaurantId),
            Long::class.java,
        ) ?: 0L
    }

    override fun existsById(id: Long): Boolean {
        return jdbc.queryForObject(
            "SELECT COUNT(*) FROM reviews WHERE id = :id",
            MapSqlParameterSource("id", id),
            Long::class.java,
        )?.let { it > 0 } ?: false
    }

    override fun deleteById(id: Long) {
        jdbc.update("DELETE FROM reviews WHERE id = :id", MapSqlParameterSource("id", id))
    }

    private fun queryWithDetails(whereAndOrder: String, params: MapSqlParameterSource): List<Review> {
        return jdbc.query(
            """
            SELECT rev.id, rev.rating, rev.text, rev.date,
                   u.id AS user_id, u.username, u.password,
                   r.id AS restaurant_id, r.name AS restaurant_name
            FROM reviews rev
            JOIN users u ON u.id = rev.user_id
            JOIN restaurants r ON r.id = rev.restaurant_id
            $whereAndOrder
            """.trimIndent(),
            params,
        ) { rs, _ -> rs.toReviewWithDetails() }
    }
}
