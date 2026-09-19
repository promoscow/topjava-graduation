package ru.xpendence.topjavagraduation.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import ru.xpendence.topjavagraduation.entity.Dish
import java.util.*

/**
 * Доступ к блюдам через JDBC.
 */
interface DishRepository {

    /**
     * Сохраняет блюдо: вставляет новую запись или обновляет существующую по [Dish.id].
     *
     * @param dish блюдо; для вставки — без id, с заполненным restaurant.id
     * @return сохранённое блюдо (с присвоенным id при вставке)
     */
    fun save(dish: Dish): Dish

    /**
     * Возвращает блюдо по идентификатору без загрузки ресторана.
     *
     * @param id идентификатор блюда
     * @return блюдо или empty, если не найдено
     */
    fun findById(id: Long): Optional<Dish>

    /**
     * Возвращает блюдо по идентификатору с загруженным рестораном.
     *
     * @param id идентификатор блюда
     * @return блюдо с рестораном или empty, если не найдено
     */
    fun findByIdWithRestaurant(id: Long): Optional<Dish>

    /**
     * Возвращает активное блюдо по идентификатору с загруженным рестораном.
     *
     * @param id идентификатор блюда
     * @return активное блюдо с рестораном или empty, если не найдено или неактивно
     */
    fun findActiveByIdWithRestaurant(id: Long): Optional<Dish>

    /**
     * Возвращает страницу всех блюд ресторана (активных и неактивных) с загруженным рестораном.
     *
     * @param restaurantId идентификатор ресторана
     * @param pageable параметры страницы
     * @return страница блюд
     */
    fun getAllByRestaurantId(restaurantId: Long, pageable: Pageable): Page<Dish>

    /**
     * Возвращает страницу только активных блюд ресторана с загруженным рестораном.
     *
     * @param restaurantId идентификатор ресторана
     * @param pageable параметры страницы
     * @return страница активных блюд
     */
    fun getAllActiveByRestaurantId(restaurantId: Long, pageable: Pageable): Page<Dish>

    /**
     * Снимает признак активности со всех блюд ресторана.
     *
     * @param restaurantId идентификатор ресторана
     * @return количество затронутых строк
     */
    fun setActiveFalseForAllByRestaurantId(restaurantId: Long): Int

    /**
     * Проверяет существование блюда по идентификатору.
     *
     * @param id идентификатор блюда
     * @return true, если блюдо есть в БД
     */
    fun existsById(id: Long): Boolean

    /**
     * Удаляет блюдо по идентификатору.
     *
     * @param id идентификатор блюда
     */
    fun deleteById(id: Long)
}
