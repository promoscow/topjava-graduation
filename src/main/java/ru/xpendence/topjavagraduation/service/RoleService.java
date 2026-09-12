package ru.xpendence.topjavagraduation.service;

import ru.xpendence.topjavagraduation.entity.Role;
import ru.xpendence.topjavagraduation.entity.type.RoleType;

import java.util.List;

/**
 * Операции над ролями пользователей.
 */
public interface RoleService {

    /**
     * Возвращает роль по идентификатору.
     *
     * @param id идентификатор роли
     * @return найденная роль
     * @throws java.util.NoSuchElementException если роль не найдена
     */
    Role getById(Long id);

    /**
     * Возвращает роль по типу.
     *
     * @param name тип роли
     * @return найденная роль
     * @throws java.util.NoSuchElementException если роль не найдена
     */
    Role getByName(RoleType name);

    /**
     * Возвращает все роли.
     *
     * @return список ролей
     */
    List<Role> getAll();
}
