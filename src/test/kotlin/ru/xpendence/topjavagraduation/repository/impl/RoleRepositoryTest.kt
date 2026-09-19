package ru.xpendence.topjavagraduation.repository.impl

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import ru.xpendence.topjavagraduation.AbstractTest
import ru.xpendence.topjavagraduation.entity.type.RoleType
import ru.xpendence.topjavagraduation.repository.RoleRepository

class RoleRepositoryTest : AbstractTest() {

    @Autowired
    private lateinit var repository: RoleRepository

    @Test
    @DisplayName("findById(): существующий id -> роль найдена")
    fun findById() {
        assertTrue(repository.findById(1L).isPresent)
    }

    @Test
    @DisplayName("findById(): несуществующий id -> empty")
    fun findByIdEmpty() {
        assertTrue(repository.findById(Long.MAX_VALUE).isEmpty)
    }

    @Test
    @DisplayName("findByName(): USER -> роль с именем USER")
    fun findByName() {
        val role = repository.findByName(RoleType.USER).get()
        assertEquals(RoleType.USER, role.name)
    }

    @Test
    @DisplayName("findByName(): ADMIN -> роль с именем ADMIN")
    fun findByNameAdmin() {
        assertEquals(RoleType.ADMIN, repository.findByName(RoleType.ADMIN).get().name)
    }

    @Test
    @DisplayName("findAll(): роли в БД -> непустой список")
    fun findAll() {
        assertFalse(repository.findAll().isEmpty())
    }
}
