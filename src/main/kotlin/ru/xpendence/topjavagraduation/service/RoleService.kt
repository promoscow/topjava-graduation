package ru.xpendence.topjavagraduation.service

import ru.xpendence.topjavagraduation.entity.Role
import ru.xpendence.topjavagraduation.entity.type.RoleType

/**
 * Операции над ролями пользователей.
 */
interface RoleService {

    /**
     * Возвращает роль по идентификатору.
     *
     * @param id идентификатор роли
     * @return найденная роль
     * @throws java.util.NoSuchElementException если роль не найдена
     */
    fun getById(id: Long): Role

    /**
     * Возвращает роль по типу.
     *
     * @param name тип роли
     * @return найденная роль
     * @throws java.util.NoSuchElementException если роль не найдена
     */
    fun getByName(name: RoleType): Role

    /**
     * Возвращает все роли.
     *
     * @return список ролей
     */
    fun getAll(): List<Role>
}
