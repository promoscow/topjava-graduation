package ru.xpendence.topjavagraduation.repository.impl

import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import ru.xpendence.topjavagraduation.AbstractTest
import ru.xpendence.topjavagraduation.entity.Role
import ru.xpendence.topjavagraduation.entity.type.RoleType
import ru.xpendence.topjavagraduation.repository.UserRepository
import java.util.*

class UserRepositoryTest : AbstractTest() {

    @Autowired
    private lateinit var repository: UserRepository

    @Test
    @DisplayName("save(): новый пользователь -> id присвоен")
    fun saveInsert() {
        val user = dataBuilder.buildUser()
        assertNotNull(repository.save(user).id)
    }

    @Test
    @DisplayName("save(): новый пользователь с ролями -> роли сохранены")
    fun saveInsertWithRoles() {
        val user = dataBuilder.buildUser()
        user.roles.add(Role(id = 1L, name = RoleType.USER))
        val saved = repository.save(user)
        val found = repository.findById(checkNotNull(saved.id)).get()
        assertTrue(found.roles.any { it.name == RoleType.USER })
    }

    @Test
    @DisplayName("save(): существующий пользователь -> username и password обновлены")
    fun saveUpdate() {
        var user = dataBuilder.saveUser()
        val username = RandomStringUtils.secure().nextAlphanumeric(16)
        val password = RandomStringUtils.secure().nextAlphanumeric(16)
        user = user.copy(username = username, password = password)
        repository.save(user)
        val found = repository.findById(checkNotNull(user.id)).get()
        assertEquals(username, found.username)
        assertEquals(password, found.password)
    }

    @Test
    @DisplayName("findById(): существующий id -> пользователь найден")
    fun findById() {
        val user = dataBuilder.saveUser()
        assertTrue(repository.findById(checkNotNull(user.id)).isPresent)
    }

    @Test
    @DisplayName("findById(): несуществующий id -> empty")
    fun findByIdEmpty() {
        assertTrue(repository.findById(Random().nextLong()).isEmpty)
    }

    @Test
    @DisplayName("findByUsername(): существующий username -> пользователь найден")
    fun findByUsername() {
        val user = dataBuilder.saveUser()
        assertEquals(user.id, repository.findByUsername(user.username).get().id)
    }

    @Test
    @DisplayName("findByUsername(): несуществующий username -> empty")
    fun findByUsernameEmpty() {
        assertTrue(repository.findByUsername("missing-user").isEmpty)
    }

    @Test
    @DisplayName("findAll(): пользователи в БД -> непустая страница")
    fun findAll() {
        dataBuilder.saveUser()
        assertFalse(repository.findAll(PageRequest.of(0, 20)).isEmpty)
    }

    @Test
    @DisplayName("findByUsernameContainingIgnoreCase(): фильтр совпадает -> страница с пользователем")
    fun findByUsernameContainingIgnoreCase() {
        val user = dataBuilder.saveUser()
        val page = repository.findByUsernameContainingIgnoreCase(
            user.username.substring(0, 4),
            PageRequest.of(0, 20),
        )
        assertTrue(page.content.any { it.id == user.id })
    }

    @Test
    @DisplayName("findByUsernameContainingIgnoreCase(): нет совпадений -> пустая страница")
    fun findByUsernameContainingIgnoreCaseEmpty() {
        dataBuilder.saveUser()
        assertTrue(
            repository.findByUsernameContainingIgnoreCase("zzz-no-match-zzz", PageRequest.of(0, 20)).isEmpty,
        )
    }

    @Test
    @DisplayName("existsByUsername(): username занят -> true")
    fun existsByUsername() {
        val user = dataBuilder.saveUser()
        assertTrue(repository.existsByUsername(user.username))
    }

    @Test
    @DisplayName("existsByUsername(): username свободен -> false")
    fun existsByUsernameFalse() {
        assertFalse(repository.existsByUsername("free-username-${Random().nextLong()}"))
    }

    @Test
    @DisplayName("addRole(): роль добавлена в users_roles")
    fun addRole() {
        val user = dataBuilder.saveUser()
        repository.addRole(checkNotNull(user.id), 2L)
        assertTrue(repository.findById(checkNotNull(user.id)).get().roles.any { it.name == RoleType.ADMIN })
    }

    @Test
    @DisplayName("removeRole(): роль удалена из users_roles")
    fun removeRole() {
        val user = dataBuilder.saveUser()
        repository.addRole(checkNotNull(user.id), 1L)
        repository.removeRole(checkNotNull(user.id), 1L)
        assertTrue(repository.findById(checkNotNull(user.id)).get().roles.none { it.name == RoleType.USER })
    }

    @Test
    @DisplayName("deleteById(): существующий id -> пользователь удалён")
    fun deleteById() {
        val user = dataBuilder.saveUser()
        repository.deleteById(checkNotNull(user.id))
        assertTrue(repository.findById(checkNotNull(user.id)).isEmpty)
    }
}
