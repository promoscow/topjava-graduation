package ru.xpendence.topjavagraduation.service.impl

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import ru.xpendence.topjavagraduation.AbstractTest
import ru.xpendence.topjavagraduation.entity.type.RoleType
import ru.xpendence.topjavagraduation.service.RoleService

class RoleServiceTest : AbstractTest() {

    @Autowired
    private lateinit var service: RoleService

    @Test
    @DisplayName("getById(): существующий id -> успешное получение роли")
    fun getById() {
        assertDoesNotThrow { service.getById(1L) }
    }

    @Test
    @DisplayName("getById(): несуществующий id -> NoSuchElementException")
    fun getByIdFailsWhenNotFound() {
        assertThrows(NoSuchElementException::class.java) { service.getById(Long.MAX_VALUE) }
    }

    @Test
    @DisplayName("getByName(): существующее имя -> успешное получение роли")
    fun getByName() {
        val role = service.getByName(RoleType.USER)
        assertEquals(RoleType.USER, role.name)
    }

    @Test
    @DisplayName("getAll(): роли в БД -> непустой список")
    fun getAll() {
        assertFalse(service.getAll().isEmpty())
    }
}
