package ru.xpendence.topjavagraduation.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import ru.xpendence.topjavagraduation.entity.Tag

/**
 * Операции над тегами ресторанов.
 */
interface TagService {

    /**
     * Сохраняет новый тег. Имя тега должно быть уникальным.
     *
     * @param tag новый тег
     * @return сохранённый тег с присвоенным идентификатором
     * @throws IllegalArgumentException если имя тега уже занято
     */
    fun create(tag: Tag): Tag

    /**
     * Обновляет существующий тег. При смене имени проверяется уникальность.
     *
     * @param tag тег с заполненным идентификатором и новыми значениями полей
     * @throws IllegalArgumentException если идентификатор не задан или новое имя уже занято
     * @throws java.util.NoSuchElementException если тег с таким идентификатором не найден
     */
    fun update(tag: Tag)

    /**
     * Возвращает тег по идентификатору.
     *
     * @param id идентификатор тега
     * @return найденный тег
     * @throws java.util.NoSuchElementException если тег не найден
     */
    fun getById(id: Long): Tag

    /**
     * Возвращает страницу тегов. Если [name] задан, фильтрует по вхождению
     * подстроки в имя без учёта регистра.
     *
     * @param name необязательный фильтр по имени; пустая строка или `null` — без фильтра
     * @param pageable параметры страницы
     * @return страница тегов
     */
    fun getAll(name: String?, pageable: Pageable): Page<Tag>

    /**
     * Безвозвратно удаляет тег из базы.
     *
     * @param id идентификатор тега
     */
    fun delete(id: Long)
}
