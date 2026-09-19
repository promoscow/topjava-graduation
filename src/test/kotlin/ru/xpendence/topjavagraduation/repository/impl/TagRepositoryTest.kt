package ru.xpendence.topjavagraduation.repository.impl

import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import ru.xpendence.topjavagraduation.AbstractTest
import ru.xpendence.topjavagraduation.repository.TagRepository
import java.util.*

class TagRepositoryTest : AbstractTest() {

    @Autowired
    private lateinit var repository: TagRepository

    @Test
    @DisplayName("save(): новый тег -> id присвоен")
    fun saveInsert() {
        val tag = dataBuilder.buildTag()
        assertNotNull(repository.save(tag).id)
    }

    @Test
    @DisplayName("save(): существующий тег -> имя обновлено")
    fun saveUpdate() {
        var tag = dataBuilder.saveTag()
        val name = RandomStringUtils.secure().nextAlphanumeric(16)
        tag = tag.copy(name = name)
        repository.save(tag)
        assertEquals(name, repository.findById(checkNotNull(tag.id)).get().name)
    }

    @Test
    @DisplayName("findById(): существующий id -> тег найден")
    fun findById() {
        val tag = dataBuilder.saveTag()
        assertTrue(repository.findById(checkNotNull(tag.id)).isPresent)
    }

    @Test
    @DisplayName("findById(): несуществующий id -> empty")
    fun findByIdEmpty() {
        assertTrue(repository.findById(Random().nextLong()).isEmpty)
    }

    @Test
    @DisplayName("findAll(): теги в БД -> непустая страница")
    fun findAll() {
        dataBuilder.saveTag()
        assertFalse(repository.findAll(PageRequest.of(0, 20)).isEmpty)
    }

    @Test
    @DisplayName("findByNameContainingIgnoreCase(): фильтр совпадает -> страница с тегом")
    fun findByNameContainingIgnoreCase() {
        val tag = dataBuilder.saveTag()
        val page = repository.findByNameContainingIgnoreCase(
            tag.name.substring(0, 4),
            PageRequest.of(0, 20),
        )
        assertTrue(page.content.any { it.id == tag.id })
    }

    @Test
    @DisplayName("findByNameContainingIgnoreCase(): нет совпадений -> пустая страница")
    fun findByNameContainingIgnoreCaseEmpty() {
        dataBuilder.saveTag()
        assertTrue(
            repository.findByNameContainingIgnoreCase("zzz-no-match-zzz", PageRequest.of(0, 20)).isEmpty,
        )
    }

    @Test
    @DisplayName("existsByName(): имя занято -> true")
    fun existsByName() {
        val tag = dataBuilder.saveTag()
        assertTrue(repository.existsByName(tag.name))
    }

    @Test
    @DisplayName("existsByName(): имя свободно -> false")
    fun existsByNameFalse() {
        assertFalse(repository.existsByName("free-tag-${Random().nextLong()}"))
    }

    @Test
    @DisplayName("deleteById(): существующий id -> тег удалён")
    fun deleteById() {
        val tag = dataBuilder.saveTag()
        repository.deleteById(checkNotNull(tag.id))
        assertTrue(repository.findById(checkNotNull(tag.id)).isEmpty)
    }
}
