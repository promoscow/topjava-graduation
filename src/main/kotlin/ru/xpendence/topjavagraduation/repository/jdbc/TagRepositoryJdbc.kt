package ru.xpendence.topjavagraduation.repository.jdbc

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import ru.xpendence.topjavagraduation.entity.Tag
import ru.xpendence.topjavagraduation.repository.TagRepository
import ru.xpendence.topjavagraduation.repository.jdbc.JdbcSupport.addPaging
import ru.xpendence.topjavagraduation.repository.jdbc.JdbcSupport.limitSql
import ru.xpendence.topjavagraduation.repository.mapper.toTag
import java.util.*

@Repository
class TagRepositoryJdbc(
    private val jdbc: NamedParameterJdbcTemplate,
) : TagRepository {

    override fun save(tag: Tag): Tag {
        return if (tag.id == null) insert(tag) else update(tag)
    }

    private fun insert(tag: Tag): Tag {
        val keyHolder = GeneratedKeyHolder()
        jdbc.update(
            "INSERT INTO tags (name) VALUES (:name)",
            MapSqlParameterSource("name", tag.name),
            keyHolder,
            arrayOf("id"),
        )
        return tag.copy(id = requireNotNull(keyHolder.key).toLong())
    }

    private fun update(tag: Tag): Tag {
        jdbc.update(
            "UPDATE tags SET name = :name WHERE id = :id",
            MapSqlParameterSource()
                .addValue("id", tag.id)
                .addValue("name", tag.name),
        )
        return tag
    }

    override fun findById(id: Long): Optional<Tag> {
        val list = jdbc.query(
            "SELECT id, name FROM tags WHERE id = :id",
            MapSqlParameterSource("id", id),
        ) { rs, _ -> rs.toTag() }
        return Optional.ofNullable(list.firstOrNull())
    }

    override fun findAll(pageable: Pageable): Page<Tag> {
        val params = MapSqlParameterSource()
        val total = jdbc.queryForObject(
            "SELECT COUNT(*) FROM tags",
            params,
            Long::class.java,
        ) ?: 0L
        addPaging(params, pageable)
        val content = jdbc.query(
            """
            SELECT id, name FROM tags
            ORDER BY id
            ${limitSql(pageable)}
            """.trimIndent(),
            params,
        ) { rs, _ -> rs.toTag() }
        return PageImpl(content, pageable, total)
    }

    override fun findByNameContainingIgnoreCase(name: String, pageable: Pageable): Page<Tag> {
        val params = MapSqlParameterSource("name", "%${name.lowercase()}%")
        val total = jdbc.queryForObject(
            "SELECT COUNT(*) FROM tags WHERE LOWER(name) LIKE :name",
            params,
            Long::class.java,
        ) ?: 0L
        addPaging(params, pageable)
        val content = jdbc.query(
            """
            SELECT id, name FROM tags
            WHERE LOWER(name) LIKE :name
            ORDER BY id
            ${limitSql(pageable)}
            """.trimIndent(),
            params,
        ) { rs, _ -> rs.toTag() }
        return PageImpl(content, pageable, total)
    }

    override fun existsByName(name: String): Boolean {
        return jdbc.queryForObject(
            "SELECT COUNT(*) FROM tags WHERE name = :name",
            MapSqlParameterSource("name", name),
            Long::class.java,
        )?.let { it > 0 } ?: false
    }

    override fun deleteById(id: Long) {
        jdbc.update("DELETE FROM tags WHERE id = :id", MapSqlParameterSource("id", id))
    }
}
