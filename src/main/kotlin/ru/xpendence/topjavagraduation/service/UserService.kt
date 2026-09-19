package ru.xpendence.topjavagraduation.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import ru.xpendence.topjavagraduation.entity.User

/**
 * Операции над пользователями.
 */
interface UserService {

    /**
     * Сохраняет нового пользователя. Пароль хешируется; имя должно быть уникальным.
     * Если роли не заданы, назначается роль `USER`.
     *
     * @param user новый пользователь
     * @return сохранённый пользователь с присвоенным идентификатором
     * @throws IllegalArgumentException если имя пользователя уже занято
     */
    fun create(user: User): User

    /**
     * Обновляет существующего пользователя. Пароль хешируется заново;
     * при смене имени проверяется уникальность.
     *
     * @param user пользователь с заполненным идентификатором и новыми значениями полей
     * @throws IllegalArgumentException если идентификатор не задан или новое имя уже занято
     * @throws java.util.NoSuchElementException если пользователь с таким идентификатором не найден
     */
    fun update(user: User)

    /**
     * Возвращает пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return найденный пользователь
     * @throws java.util.NoSuchElementException если пользователь не найден
     */
    fun getById(id: Long): User

    /**
     * Возвращает пользователя по имени.
     *
     * @param username имя пользователя
     * @return найденный пользователь
     * @throws java.util.NoSuchElementException если пользователь не найден
     */
    fun getByUsername(username: String): User

    /**
     * Возвращает страницу пользователей. Если [username] задан, фильтрует по вхождению
     * подстроки в имя без учёта регистра.
     *
     * @param username необязательный фильтр по имени; пустая строка или `null` — без фильтра
     * @param pageable параметры страницы
     * @return страница пользователей
     */
    fun getAll(username: String?, pageable: Pageable): Page<User>

    /**
     * Добавляет роль пользователю.
     *
     * @param id идентификатор пользователя
     * @param roleId идентификатор роли
     * @throws java.util.NoSuchElementException если пользователь или роль не найдены
     */
    fun addRole(id: Long, roleId: Long)

    /**
     * Удаляет роль у пользователя.
     *
     * @param id идентификатор пользователя
     * @param roleId идентификатор роли
     * @throws java.util.NoSuchElementException если пользователь или роль не найдены
     */
    fun removeRole(id: Long, roleId: Long)

    /**
     * Безвозвратно удаляет пользователя из базы.
     *
     * @param id идентификатор пользователя
     */
    fun delete(id: Long)
}
