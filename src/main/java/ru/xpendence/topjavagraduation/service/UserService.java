package ru.xpendence.topjavagraduation.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.xpendence.topjavagraduation.entity.User;

/**
 * Операции над пользователями.
 */
public interface UserService {

    /**
     * Сохраняет нового пользователя. Пароль хешируется; имя должно быть уникальным.
     * Если роли не заданы, назначается роль {@code USER}.
     *
     * @param user новый пользователь
     * @return сохранённый пользователь с присвоенным идентификатором
     * @throws IllegalArgumentException если имя пользователя уже занято
     */
    User create(User user);

    /**
     * Обновляет существующего пользователя. Пароль хешируется заново;
     * при смене имени проверяется уникальность.
     *
     * @param user пользователь с заполненным идентификатором и новыми значениями полей
     * @throws IllegalArgumentException если идентификатор не задан или новое имя уже занято
     * @throws java.util.NoSuchElementException если пользователь с таким идентификатором не найден
     */
    void update(User user);

    /**
     * Возвращает пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return найденный пользователь
     * @throws java.util.NoSuchElementException если пользователь не найден
     */
    User getById(Long id);

    /**
     * Возвращает пользователя по имени.
     *
     * @param username имя пользователя
     * @return найденный пользователь
     * @throws java.util.NoSuchElementException если пользователь не найден
     */
    User getByUsername(String username);

    /**
     * Возвращает страницу пользователей. Если {@code username} задан, фильтрует по вхождению
     * подстроки в имя без учёта регистра.
     *
     * @param username необязательный фильтр по имени; пустая строка или {@code null} — без фильтра
     * @param pageable параметры страницы
     * @return страница пользователей
     */
    Page<User> getAll(String username, Pageable pageable);

    /**
     * Добавляет роль пользователю.
     *
     * @param id     идентификатор пользователя
     * @param roleId идентификатор роли
     * @throws java.util.NoSuchElementException если пользователь или роль не найдены
     */
    void addRole(Long id, Long roleId);

    /**
     * Удаляет роль у пользователя.
     *
     * @param id     идентификатор пользователя
     * @param roleId идентификатор роли
     * @throws java.util.NoSuchElementException если пользователь или роль не найдены
     */
    void removeRole(Long id, Long roleId);

    /**
     * Безвозвратно удаляет пользователя из базы.
     *
     * @param id идентификатор пользователя
     */
    void delete(Long id);
}
