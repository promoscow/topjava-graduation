package ru.xpendence.topjavagraduation.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import ru.xpendence.topjavagraduation.entity.User
import java.util.*

/**
 * Доступ к пользователям через JDBC.
 */
interface UserRepository {

    /**
     * Сохраняет пользователя: при вставке пишет запись в users и роли из [User.roles] в users_roles;
     * при обновлении меняет только username и password.
     *
     * @param user пользователь; для вставки — без id
     * @return сохранённый пользователь (с присвоенным id при вставке)
     */
    fun save(user: User): User

    /**
     * Возвращает пользователя по идентификатору с загруженными ролями.
     *
     * @param id идентификатор пользователя
     * @return пользователь с ролями или empty, если не найден
     */
    fun findById(id: Long): Optional<User>

    /**
     * Возвращает пользователя по имени с загруженными ролями.
     *
     * @param username имя пользователя
     * @return пользователь с ролями или empty, если не найден
     */
    fun findByUsername(username: String): Optional<User>

    /**
     * Возвращает страницу пользователей с загруженными ролями.
     *
     * @param pageable параметры страницы
     * @return страница пользователей
     */
    fun findAll(pageable: Pageable): Page<User>

    /**
     * Возвращает страницу пользователей, у которых username содержит подстроку (без учёта регистра),
     * с загруженными ролями.
     *
     * @param username фрагмент имени пользователя
     * @param pageable параметры страницы
     * @return страница пользователей
     */
    fun findByUsernameContainingIgnoreCase(username: String, pageable: Pageable): Page<User>

    /**
     * Проверяет, занято ли имя пользователя.
     *
     * @param username имя пользователя
     * @return true, если пользователь с таким username уже есть
     */
    fun existsByUsername(username: String): Boolean

    /**
     * Удаляет пользователя и его связи в users_roles.
     *
     * @param id идентификатор пользователя
     */
    fun deleteById(id: Long)

    /**
     * Добавляет роль пользователю в таблицу users_roles.
     *
     * @param userId идентификатор пользователя
     * @param roleId идентификатор роли
     */
    fun addRole(userId: Long, roleId: Long)

    /**
     * Удаляет роль пользователя из таблицы users_roles.
     *
     * @param userId идентификатор пользователя
     * @param roleId идентификатор роли
     */
    fun removeRole(userId: Long, roleId: Long)
}
