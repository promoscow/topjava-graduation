package ru.xpendence.topjavagraduation.repository.jdbc

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import ru.xpendence.topjavagraduation.entity.Role
import ru.xpendence.topjavagraduation.entity.type.RoleType
import ru.xpendence.topjavagraduation.repository.RoleRepository
import ru.xpendence.topjavagraduation.repository.mapper.toRole
import java.util.*

@Repository
class RoleRepositoryJdbc(
    private val jdbc: NamedParameterJdbcTemplate,
) : RoleRepository {

    override fun findById(id: Long): Optional<Role> {
        val list = jdbc.query(
            "SELECT id, name FROM roles WHERE id = :id",
            MapSqlParameterSource("id", id),
        ) { rs, _ -> rs.toRole() }
        return Optional.ofNullable(list.firstOrNull())
    }

    override fun findByName(name: RoleType): Optional<Role> {
        val list = jdbc.query(
            "SELECT id, name FROM roles WHERE name = :name",
            MapSqlParameterSource("name", name.name),
        ) { rs, _ -> rs.toRole() }
        return Optional.ofNullable(list.firstOrNull())
    }

    override fun findAll(): List<Role> {
        return jdbc.query(
            "SELECT id, name FROM roles ORDER BY id",
            MapSqlParameterSource(),
        ) { rs, _ -> rs.toRole() }
    }
}
