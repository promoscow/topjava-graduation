package ru.xpendence.topjavagraduation.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import ru.xpendence.topjavagraduation.entity.Restaurant
import java.time.LocalDate
import java.util.*

/**
 * Доступ к ресторанам через JDBC.
 */
interface RestaurantRepository {

    /**
     * Сохраняет ресторан: вставляет новую запись или обновляет имя существующей по [Restaurant.id].
     *
     * @param restaurant ресторан; для вставки — без id
     * @return сохранённый ресторан (с присвоенным id при вставке)
     */
    fun save(restaurant: Restaurant): Restaurant

    /**
     * Возвращает ресторан по идентификатору с блюдами и голосами за текущую дату.
     *
     * @param id идентификатор ресторана
     * @return ресторан с коллекциями или empty, если не найден
     */
    fun findById(id: Long): Optional<Restaurant>

    /**
     * Возвращает ресторан по идентификатору блюда с блюдами и голосами за текущую дату.
     *
     * @param dishId идентификатор блюда
     * @return ресторан или empty, если блюдо/ресторан не найдены
     */
    fun getByDishId(dishId: Long): Optional<Restaurant>

    /**
     * Возвращает страницу ресторанов с блюдами и голосами за текущую дату.
     *
     * @param pageable параметры страницы
     * @return страница ресторанов
     */
    fun findAll(pageable: Pageable): Page<Restaurant>

    /**
     * Возвращает рестораны, отсортированные по числу голосов на указанную дату (убывание).
     *
     * @param date дата голосования
     * @param pageable параметры страницы (обычно размер 1 — победитель дня)
     * @return список ресторанов с блюдами и голосами за текущую дату; пустой, если голосов нет
     */
    fun findChosenByVoteDate(date: LocalDate, pageable: Pageable): List<Restaurant>

    /**
     * Удаляет ресторан по идентификатору.
     *
     * @param id идентификатор ресторана
     */
    fun deleteById(id: Long)
}
