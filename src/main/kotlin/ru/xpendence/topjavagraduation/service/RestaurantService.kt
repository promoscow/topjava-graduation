package ru.xpendence.topjavagraduation.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import ru.xpendence.topjavagraduation.entity.Restaurant

/**
 * Операции над ресторанами.
 */
interface RestaurantService {

    /**
     * Сохраняет новый ресторан.
     *
     * @param restaurant новый ресторан
     * @return сохранённый ресторан с присвоенным идентификатором
     */
    fun create(restaurant: Restaurant): Restaurant

    /**
     * Обновляет существующий ресторан.
     *
     * @param restaurant ресторан с заполненным идентификатором и новыми значениями полей
     * @throws IllegalArgumentException если идентификатор не задан
     * @throws java.util.NoSuchElementException если ресторан с таким идентификатором не найден
     */
    fun update(restaurant: Restaurant)

    /**
     * Возвращает ресторан по идентификатору.
     *
     * @param id идентификатор ресторана
     * @return найденный ресторан
     * @throws java.util.NoSuchElementException если ресторан не найден
     */
    fun getById(id: Long): Restaurant

    /**
     * Возвращает ресторан, к которому привязано блюдо.
     *
     * @param dishId идентификатор блюда
     * @return найденный ресторан
     * @throws java.util.NoSuchElementException если ресторан по блюду не найден
     */
    fun getByDishId(dishId: Long): Restaurant

    /**
     * Возвращает страницу всех ресторанов.
     *
     * @param pageable параметры страницы
     * @return страница ресторанов
     */
    fun getAll(pageable: Pageable): Page<Restaurant>

    /**
     * Возвращает ресторан, набравший наибольшее число голосов на текущую дату.
     *
     * @return выбранный ресторан дня
     * @throws java.util.NoSuchElementException если на сегодня нет ни одного голоса
     */
    fun getChosen(): Restaurant

    /**
     * Безвозвратно удаляет ресторан из базы.
     *
     * @param id идентификатор ресторана
     */
    fun delete(id: Long)
}
