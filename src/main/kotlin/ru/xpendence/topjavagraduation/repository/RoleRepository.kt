package ru.xpendence.topjavagraduation.repository

import ru.xpendence.topjavagraduation.entity.Role
import ru.xpendence.topjavagraduation.entity.type.RoleType
import java.util.*

/**
 * Доступ к ролям через JDBC.
 */
interface RoleRepository {

    /**
     * Возвращает роль по идентификатору.
     *
     * @param id идентификатор роли
     * @return роль или empty, если не найдена
     */
    fun findById(id: Long): Optional<Role>

    /**
     * Возвращает роль по типу.
     *
     * @param name тип роли ([RoleType.USER], [RoleType.ADMIN])
     * @return роль или empty, если не найдена
     */
    fun findByName(name: RoleType): Optional<Role>

    /**
     * Возвращает все роли.
     *
     * @return список ролей
     */
    fun findAll(): List<Role>
}
