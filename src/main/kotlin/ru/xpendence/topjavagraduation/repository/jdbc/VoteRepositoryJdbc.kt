package ru.xpendence.topjavagraduation.repository.jdbc

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import ru.xpendence.topjavagraduation.entity.Vote
import ru.xpendence.topjavagraduation.repository.VoteRepository
import ru.xpendence.topjavagraduation.repository.mapper.toVote
import ru.xpendence.topjavagraduation.repository.mapper.toVoteWithDetails
import java.time.LocalDate
import java.util.*

@Repository
class VoteRepositoryJdbc(
    private val jdbc: NamedParameterJdbcTemplate,
) : VoteRepository {

    override fun save(vote: Vote): Vote {
        return if (vote.id == null) insert(vote) else update(vote)
    }

    private fun insert(vote: Vote): Vote {
        val keyHolder = GeneratedKeyHolder()
        val params = MapSqlParameterSource()
            .addValue("date", vote.date)
            .addValue("userId", vote.user.id)
            .addValue("restaurantId", vote.restaurant.id)
        jdbc.update(
            """
            INSERT INTO votes (date, user_id, restaurant_id)
            VALUES (:date, :userId, :restaurantId)
            """.trimIndent(),
            params,
            keyHolder,
            arrayOf("id"),
        )
        return vote.copy(id = requireNotNull(keyHolder.key).toLong())
    }

    private fun update(vote: Vote): Vote {
        jdbc.update(
            """
            UPDATE votes
            SET date = :date, restaurant_id = :restaurantId
            WHERE id = :id
            """.trimIndent(),
            MapSqlParameterSource()
                .addValue("id", vote.id)
                .addValue("date", vote.date)
                .addValue("restaurantId", vote.restaurant.id),
        )
        return vote
    }

    override fun findById(id: Long): Optional<Vote> {
        val list = jdbc.query(
            "SELECT id, date, user_id, restaurant_id FROM votes WHERE id = :id",
            MapSqlParameterSource("id", id),
        ) { rs, _ -> rs.toVote() }
        return Optional.ofNullable(list.firstOrNull())
    }

    override fun findByIdWithDetails(id: Long): Optional<Vote> {
        return Optional.ofNullable(
            queryWithDetails("WHERE v.id = :id", MapSqlParameterSource("id", id)).firstOrNull(),
        )
    }

    override fun findByUserIdAndDate(userId: Long, date: LocalDate): Optional<Vote> {
        return Optional.ofNullable(
            queryWithDetails(
                "WHERE v.user_id = :userId AND v.date = :date",
                MapSqlParameterSource()
                    .addValue("userId", userId)
                    .addValue("date", date),
            ).firstOrNull(),
        )
    }

    private fun queryWithDetails(where: String, params: MapSqlParameterSource): List<Vote> {
        return jdbc.query(
            """
            SELECT v.id, v.date,
                   u.id AS user_id, u.username, u.password,
                   r.id AS restaurant_id, r.name AS restaurant_name
            FROM votes v
            JOIN users u ON u.id = v.user_id
            JOIN restaurants r ON r.id = v.restaurant_id
            $where
            """.trimIndent(),
            params,
        ) { rs, _ -> rs.toVoteWithDetails() }
    }
}
