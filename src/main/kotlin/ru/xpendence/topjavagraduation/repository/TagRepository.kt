package ru.xpendence.topjavagraduation.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import ru.xpendence.topjavagraduation.entity.Tag
import java.util.*

/**
 * Доступ к тегам через JDBC.
 */
interface TagRepository {

    /**
     * Сохраняет тег: вставляет новую запись или обновляет имя существующей по [Tag.id].
     *
     * @param tag тег; для вставки — без id
     * @return сохранённый тег (с присвоенным id при вставке)
     */
    fun save(tag: Tag): Tag

    /**
     * Возвращает тег по идентификатору.
     *
     * @param id идентификатор тега
     * @return тег или empty, если не найден
     */
    fun findById(id: Long): Optional<Tag>

    /**
     * Возвращает страницу всех тегов.
     *
     * @param pageable параметры страницы
     * @return страница тегов
     */
    fun findAll(pageable: Pageable): Page<Tag>

    /**
     * Возвращает страницу тегов, у которых имя содержит подстроку (без учёта регистра).
     *
     * @param name фрагмент имени тега
     * @param pageable параметры страницы
     * @return страница тегов
     */
    fun findByNameContainingIgnoreCase(name: String, pageable: Pageable): Page<Tag>

    /**
     * Проверяет, занято ли имя тега.
     *
     * @param name имя тега
     * @return true, если тег с таким именем уже есть
     */
    fun existsByName(name: String): Boolean

    /**
     * Удаляет тег по идентификатору.
     *
     * @param id идентификатор тега
     */
    fun deleteById(id: Long)
}
