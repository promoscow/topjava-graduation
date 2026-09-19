package ru.xpendence.topjavagraduation.service.impl

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils
import ru.xpendence.topjavagraduation.entity.User
import ru.xpendence.topjavagraduation.entity.type.RoleType
import ru.xpendence.topjavagraduation.repository.UserRepository
import ru.xpendence.topjavagraduation.service.RoleService
import ru.xpendence.topjavagraduation.service.UserService

@Service
class UserServiceImpl(
    private val repository: UserRepository,
    private val roleService: RoleService,
    private val passwordEncoder: BCryptPasswordEncoder,
) : UserService {

    @Transactional
    override fun create(user: User): User {
        ensureUsernameIsFree(user.username)
        val toSave = User(
            id = user.id,
            username = user.username,
            password = passwordEncoder.encode(user.password),
        )
        toSave.roles.addAll(user.roles)
        if (toSave.roles.isEmpty()) {
            toSave.roles.add(roleService.getByName(RoleType.USER))
        }
        return repository.save(toSave)
    }

    @Transactional
    override fun update(user: User) {
        val id = user.id ?: throw IllegalArgumentException("User id is null")
        val stored = repository.findById(id)
            .orElseThrow { NoSuchElementException("User not found by id: $id") }
        if (user.username != stored.username) {
            ensureUsernameIsFree(user.username)
        }
        val encoded = User(
            id = user.id,
            username = user.username,
            password = passwordEncoder.encode(user.password),
        )
        repository.save(User.enrichForUpdate(encoded, stored))
    }

    @Transactional(readOnly = true)
    override fun getById(id: Long): User =
        repository.findById(id)
            .orElseThrow { NoSuchElementException("User not found by id: $id") }

    @Transactional(readOnly = true)
    override fun getByUsername(username: String): User =
        repository.findByUsername(username)
            .orElseThrow { NoSuchElementException("User not found by username: $username") }

    @Transactional(readOnly = true)
    override fun getAll(username: String?, pageable: Pageable): Page<User> =
        if (StringUtils.hasText(username)) {
            repository.findByUsernameContainingIgnoreCase(checkNotNull(username), pageable)
        } else {
            repository.findAll(pageable)
        }

    @Transactional
    override fun addRole(id: Long, roleId: Long) {
        roleService.getById(roleId)
        getById(id)
        repository.addRole(id, roleId)
    }

    @Transactional
    override fun removeRole(id: Long, roleId: Long) {
        roleService.getById(roleId)
        getById(id)
        repository.removeRole(id, roleId)
    }

    override fun delete(id: Long) {
        repository.deleteById(id)
    }

    private fun ensureUsernameIsFree(username: String) {
        if (repository.existsByUsername(username)) {
            throw IllegalArgumentException("Username already taken: $username")
        }
    }
}
