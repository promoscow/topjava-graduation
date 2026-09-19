package ru.xpendence.topjavagraduation.service.impl

import org.springframework.stereotype.Service
import ru.xpendence.topjavagraduation.entity.Role
import ru.xpendence.topjavagraduation.entity.type.RoleType
import ru.xpendence.topjavagraduation.repository.RoleRepository
import ru.xpendence.topjavagraduation.service.RoleService

@Service
class RoleServiceImpl(
    private val repository: RoleRepository,
) : RoleService {

    override fun getById(id: Long): Role =
        repository.findById(id)
            .orElseThrow { NoSuchElementException("Role not found by id: $id") }

    override fun getByName(name: RoleType): Role =
        repository.findByName(name)
            .orElseThrow { NoSuchElementException("Role not found by name: $name") }

    override fun getAll(): List<Role> =
        repository.findAll()
}
