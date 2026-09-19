package ru.xpendence.topjavagraduation.service.impl

import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import ru.xpendence.topjavagraduation.AbstractTest
import ru.xpendence.topjavagraduation.service.TagService

class TagServiceTest : AbstractTest() {

    @Autowired
    private lateinit var service: TagService

    @Test
    @DisplayName("create(): валидный тег -> успешное создание")
    fun create() {
        val tag = dataBuilder.buildTag()
        val created = service.create(tag)

        assertNotNull(created.id)
        assertEquals(tag.name, created.name)
    }

    @Test
    @DisplayName("create(): занятое имя -> IllegalArgumentException")
    fun createFailsWhenNameTaken() {
        val existing = dataBuilder.saveTag()
        val tag = dataBuilder.buildTag().copy(name = existing.name)

        assertThrows(IllegalArgumentException::class.java) { service.create(tag) }
    }

    @Test
    @DisplayName("update(): корректные данные -> успешное обновление")
    fun update() {
        var tag = service.create(dataBuilder.buildTag())
        val name = RandomStringUtils.secure().nextAlphanumeric(16)
        tag = tag.copy(name = name)

        service.update(tag)

        assertEquals(name, service.getById(checkNotNull(tag.id)).name)
    }

    @Test
    @DisplayName("update(): id == null -> IllegalArgumentException")
    fun updateFailsWhenIdIsNull() {
        val tag = dataBuilder.buildTag()

        assertThrows(IllegalArgumentException::class.java) { service.update(tag) }
    }

    @Test
    @DisplayName("update(): несуществующий id -> NoSuchElementException")
    fun updateFailsWhenNotFound() {
        val tag = dataBuilder.buildTag().copy(id = Long.MAX_VALUE)

        assertThrows(NoSuchElementException::class.java) { service.update(tag) }
    }

    @Test
    @DisplayName("getById(): существующий id -> успешное получение тега")
    fun get() {
        val tag = dataBuilder.saveTag()
        assertDoesNotThrow { service.getById(checkNotNull(tag.id)) }
    }

    @Test
    @DisplayName("getById(): несуществующий id -> NoSuchElementException")
    fun getFailsWhenNotFound() {
        assertThrows(NoSuchElementException::class.java) { service.getById(Long.MAX_VALUE) }
    }

    @Test
    @DisplayName("getAll(): фильтр по имени -> страница с тегом")
    fun getAll() {
        val tag = dataBuilder.saveTag()
        val page = service.getAll(tag.name.substring(0, 4), PageRequest.of(0, 20))

        assertFalse(page.isEmpty)
        assertTrue(page.content.any { it.id == tag.id })
    }

    @Test
    @DisplayName("getAll(): без фильтра -> непустая страница")
    fun getAllWithoutFilter() {
        dataBuilder.saveTag()
        val page = service.getAll(null, PageRequest.of(0, 20))

        assertFalse(page.isEmpty)
    }

    @Test
    @DisplayName("getAll(): фильтр без совпадений -> пустая страница")
    fun getAllReturnsEmptyWhenFilterDoesNotMatch() {
        dataBuilder.saveTag()
        val page = service.getAll("zzz-no-match-zzz", PageRequest.of(0, 20))

        assertTrue(page.isEmpty)
    }

    @Test
    @DisplayName("delete(): существующий id -> тег удалён")
    fun delete() {
        val tag = dataBuilder.saveTag()
        service.delete(checkNotNull(tag.id))
        assertThrows(NoSuchElementException::class.java) { service.getById(checkNotNull(tag.id)) }
    }
}
