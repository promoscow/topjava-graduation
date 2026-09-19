package ru.xpendence.topjavagraduation.repository.jdbc

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import ru.xpendence.topjavagraduation.entity.User
import ru.xpendence.topjavagraduation.repository.UserRepository
import ru.xpendence.topjavagraduation.repository.jdbc.JdbcSupport.addPaging
import ru.xpendence.topjavagraduation.repository.jdbc.JdbcSupport.attachRoles
import ru.xpendence.topjavagraduation.repository.jdbc.JdbcSupport.limitSql
import ru.xpendence.topjavagraduation.repository.mapper.toUser
import java.util.*

@Repository
class UserRepositoryJdbc(
    private val jdbc: NamedParameterJdbcTemplate,
) : UserRepository {

    override fun save(user: User): User {
        return if (user.id == null) insert(user) else update(user)
    }

    private fun insert(user: User): User {
        val keyHolder = GeneratedKeyHolder()
        val params = MapSqlParameterSource()
            .addValue("username", user.username)
            .addValue("password", user.password)
        jdbc.update(
            "INSERT INTO users (username, password) VALUES (:username, :password)",
            params,
            keyHolder,
            arrayOf("id"),
        )
        val saved = user.copy(id = requireNotNull(keyHolder.key).toLong())
        saved.roles.addAll(user.roles)
        for (role in saved.roles) {
            jdbc.update(
                "INSERT INTO users_roles (user_id, role_id) VALUES (:userId, :roleId)",
                MapSqlParameterSource()
                    .addValue("userId", saved.id)
                    .addValue("roleId", role.id),
            )
        }
        return saved
    }

    private fun update(user: User): User {
        jdbc.update(
            "UPDATE users SET username = :username, password = :password WHERE id = :id",
            MapSqlParameterSource()
                .addValue("id", user.id)
                .addValue("username", user.username)
                .addValue("password", user.password),
        )
        return user
    }

    override fun findById(id: Long): Optional<User> {
        val list = jdbc.query(
            "SELECT id, username, password FROM users WHERE id = :id",
            MapSqlParameterSource("id", id),
        ) { rs, _ -> rs.toUser() }
        val user = list.firstOrNull() ?: return Optional.empty()
        attachRoles(jdbc, listOf(user))
        return Optional.of(user)
    }

    override fun findByUsername(username: String): Optional<User> {
        val list = jdbc.query(
            "SELECT id, username, password FROM users WHERE username = :username",
            MapSqlParameterSource("username", username),
        ) { rs, _ -> rs.toUser() }
        val user = list.firstOrNull() ?: return Optional.empty()
        attachRoles(jdbc, listOf(user))
        return Optional.of(user)
    }

    override fun findAll(pageable: Pageable): Page<User> {
        val params = MapSqlParameterSource()
        val total = jdbc.queryForObject(
            "SELECT COUNT(*) FROM users",
            params,
            Long::class.java,
        ) ?: 0L
        addPaging(params, pageable)
        val content = jdbc.query(
            """
            SELECT id, username, password FROM users
            ORDER BY id
            ${limitSql(pageable)}
            """.trimIndent(),
            params,
        ) { rs, _ -> rs.toUser() }
        attachRoles(jdbc, content)
        return PageImpl(content, pageable, total)
    }

    override fun findByUsernameContainingIgnoreCase(username: String, pageable: Pageable): Page<User> {
        val params = MapSqlParameterSource("username", "%${username.lowercase()}%")
        val total = jdbc.queryForObject(
            "SELECT COUNT(*) FROM users WHERE LOWER(username) LIKE :username",
            params,
            Long::class.java,
        ) ?: 0L
        addPaging(params, pageable)
        val content = jdbc.query(
            """
            SELECT id, username, password FROM users
            WHERE LOWER(username) LIKE :username
            ORDER BY id
            ${limitSql(pageable)}
            """.trimIndent(),
            params,
        ) { rs, _ -> rs.toUser() }
        attachRoles(jdbc, content)
        return PageImpl(content, pageable, total)
    }

    override fun existsByUsername(username: String): Boolean {
        return jdbc.queryForObject(
            "SELECT COUNT(*) FROM users WHERE username = :username",
            MapSqlParameterSource("username", username),
            Long::class.java,
        )?.let { it > 0 } ?: false
    }

    override fun deleteById(id: Long) {
        val params = MapSqlParameterSource("id", id)
        jdbc.update("DELETE FROM users_roles WHERE user_id = :id", params)
        jdbc.update("DELETE FROM users WHERE id = :id", params)
    }

    override fun addRole(userId: Long, roleId: Long) {
        jdbc.update(
            "INSERT INTO users_roles (user_id, role_id) VALUES (:userId, :roleId)",
            MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("roleId", roleId),
        )
    }

    override fun removeRole(userId: Long, roleId: Long) {
        jdbc.update(
            "DELETE FROM users_roles WHERE user_id = :userId AND role_id = :roleId",
            MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("roleId", roleId),
        )
    }
}
