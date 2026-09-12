package ru.xpendence.topjavagraduation.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.xpendence.topjavagraduation.entity.Tag;

/**
 * Операции над тегами ресторанов.
 */
public interface TagService {

    /**
     * Сохраняет новый тег. Имя тега должно быть уникальным.
     *
     * @param tag новый тег
     * @return сохранённый тег с присвоенным идентификатором
     * @throws IllegalArgumentException если имя тега уже занято
     */
    Tag create(Tag tag);

    /**
     * Обновляет существующий тег. При смене имени проверяется уникальность.
     *
     * @param tag тег с заполненным идентификатором и новыми значениями полей
     * @throws IllegalArgumentException если идентификатор не задан или новое имя уже занято
     * @throws java.util.NoSuchElementException если тег с таким идентификатором не найден
     */
    void update(Tag tag);

    /**
     * Возвращает тег по идентификатору.
     *
     * @param id идентификатор тега
     * @return найденный тег
     * @throws java.util.NoSuchElementException если тег не найден
     */
    Tag getById(Long id);

    /**
     * Возвращает страницу тегов. Если {@code name} задан, фильтрует по вхождению
     * подстроки в имя без учёта регистра.
     *
     * @param name     необязательный фильтр по имени; пустая строка или {@code null} — без фильтра
     * @param pageable параметры страницы
     * @return страница тегов
     */
    Page<Tag> getAll(String name, Pageable pageable);

    /**
     * Безвозвратно удаляет тег из базы.
     *
     * @param id идентификатор тега
     */
    void delete(Long id);
}
