package ru.xpendence.topjavagraduation.service.impl

import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import ru.xpendence.topjavagraduation.AbstractTest
import ru.xpendence.topjavagraduation.entity.type.RoleType
import ru.xpendence.topjavagraduation.service.LoginService
import ru.xpendence.topjavagraduation.service.UserService

class UserServiceTest : AbstractTest() {

    @Autowired
    private lateinit var service: UserService

    @Autowired
    private lateinit var loginService: LoginService

    @Autowired
    private lateinit var passwordEncoder: BCryptPasswordEncoder

    @Test
    @DisplayName("create(): валидный пользователь -> успешное создание с ролью USER")
    fun create() {
        val user = dataBuilder.buildUser()
        val rawPassword = user.password
        val created = service.create(user)

        assertNotNull(created.id)
        assertTrue(passwordEncoder.matches(rawPassword, created.password))
        assertTrue(created.roles.any { RoleType.USER == it.name })
        assertDoesNotThrow { loginService.login(created.username, rawPassword) }
    }

    @Test
    @DisplayName("create(): занятый username -> IllegalArgumentException")
    fun createFailsWhenUsernameTaken() {
        val existing = dataBuilder.saveUser()
        val user = dataBuilder.buildUser().copy(username = existing.username)

        assertThrows(IllegalArgumentException::class.java) { service.create(user) }
    }

    @Test
    @DisplayName("update(): корректные данные -> успешное обновление")
    fun update() {
        var user = service.create(dataBuilder.buildUser())
        val username = RandomStringUtils.secure().nextAlphanumeric(16)
        val password = RandomStringUtils.secure().nextAlphanumeric(16)
        user = user.copy(username = username, password = password)

        service.update(user)

        val updated = service.getById(checkNotNull(user.id))
        assertEquals(username, updated.username)
        assertTrue(passwordEncoder.matches(password, updated.password))
        assertDoesNotThrow { loginService.login(username, password) }
    }

    @Test
    @DisplayName("update(): id == null -> IllegalArgumentException")
    fun updateFailsWhenIdIsNull() {
        val user = dataBuilder.buildUser()

        assertThrows(IllegalArgumentException::class.java) { service.update(user) }
    }

    @Test
    @DisplayName("getById(): существующий id -> успешное получение пользователя")
    fun get() {
        val user = dataBuilder.saveUser()
        assertDoesNotThrow { service.getById(checkNotNull(user.id)) }
    }

    @Test
    @DisplayName("getById(): несуществующий id -> NoSuchElementException")
    fun getFailsWhenNotFound() {
        assertThrows(NoSuchElementException::class.java) { service.getById(Long.MAX_VALUE) }
    }

    @Test
    @DisplayName("getByUsername(): существующий username -> успешное получение пользователя")
    fun getByUsername() {
        val user = dataBuilder.saveUser()
        assertDoesNotThrow { service.getByUsername(user.username) }
    }

    @Test
    @DisplayName("getByUsername(): несуществующий username -> NoSuchElementException")
    fun getByUsernameFailsWhenNotFound() {
        assertThrows(NoSuchElementException::class.java) { service.getByUsername("missing-user") }
    }

    @Test
    @DisplayName("getAll(): фильтр по username -> страница с пользователем")
    fun getAll() {
        val user = dataBuilder.saveUser()
        val page = service.getAll(user.username.substring(0, 4), PageRequest.of(0, 20))

        assertFalse(page.isEmpty)
        assertTrue(page.content.any { it.id == user.id })
    }

    @Test
    @DisplayName("getAll(): без фильтра -> непустая страница")
    fun getAllWithoutFilter() {
        dataBuilder.saveUser()
        val page = service.getAll(null, PageRequest.of(0, 20))

        assertFalse(page.isEmpty)
    }

    @Test
    @DisplayName("getAll(): фильтр без совпадений -> пустая страница")
    fun getAllReturnsEmptyWhenFilterDoesNotMatch() {
        dataBuilder.saveUser()
        val page = service.getAll("zzz-no-match-zzz", PageRequest.of(0, 20))

        assertTrue(page.isEmpty)
    }

    @Test
    @DisplayName("addRole(): существующие user и role -> роль добавлена")
    fun addRole() {
        val user = dataBuilder.saveUser()
        service.addRole(checkNotNull(user.id), 2L)
        assertTrue(
            service.getById(checkNotNull(user.id))
                .roles
                .any { it.name in RoleType.entries },
        )
    }

    @Test
    @DisplayName("removeRole(): пользователь с ролью -> роль удалена")
    fun removeRole() {
        val user = dataBuilder.saveUser()
        service.removeRole(checkNotNull(user.id), 2L)
        assertTrue(
            service.getById(checkNotNull(user.id))
                .roles
                .none { it.name in RoleType.entries },
        )
    }

    @Test
    @DisplayName("delete(): существующий id -> пользователь удалён")
    fun delete() {
        val user = dataBuilder.saveUser()
        service.delete(checkNotNull(user.id))
        assertThrows(NoSuchElementException::class.java) { service.getById(checkNotNull(user.id)) }
    }
}
