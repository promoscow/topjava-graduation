package ru.xpendence.topjavagraduation.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import ru.xpendence.topjavagraduation.entity.Review
import java.util.*

/**
 * Доступ к отзывам через JDBC.
 */
interface ReviewRepository {

    /**
     * Сохраняет отзыв: вставляет новую запись или обновляет существующую по [Review.id].
     *
     * @param review отзыв; для вставки — без id, с user.id и restaurant.id
     * @return сохранённый отзыв (с присвоенным id при вставке)
     */
    fun save(review: Review): Review

    /**
     * Возвращает отзыв по идентификатору с загруженными пользователем и рестораном.
     *
     * @param id идентификатор отзыва
     * @return отзыв с деталями или empty, если не найден
     */
    fun findByIdWithDetails(id: Long): Optional<Review>

    /**
     * Возвращает отзыв пользователя о ресторане с загруженными пользователем и рестораном.
     *
     * @param userId идентификатор пользователя
     * @param restaurantId идентификатор ресторана
     * @return отзыв или empty, если не найден
     */
    fun findByUserIdAndRestaurantId(userId: Long, restaurantId: Long): Optional<Review>

    /**
     * Проверяет наличие отзыва пользователя о ресторане.
     *
     * @param userId идентификатор пользователя
     * @param restaurantId идентификатор ресторана
     * @return true, если отзыв уже существует
     */
    fun existsByUserIdAndRestaurantId(userId: Long, restaurantId: Long): Boolean

    /**
     * Возвращает страницу отзывов ресторана с загруженными пользователем и рестораном.
     *
     * @param restaurantId идентификатор ресторана
     * @param pageable параметры страницы
     * @return страница отзывов
     */
    fun findAllByRestaurantId(restaurantId: Long, pageable: Pageable): Page<Review>

    /**
     * Возвращает средний рейтинг отзывов ресторана.
     *
     * @param restaurantId идентификатор ресторана
     * @return среднее значение или null, если отзывов нет
     */
    fun findAverageRatingByRestaurantId(restaurantId: Long): Double?

    /**
     * Возвращает число отзывов ресторана.
     *
     * @param restaurantId идентификатор ресторана
     * @return количество отзывов
     */
    fun countByRestaurantId(restaurantId: Long): Long

    /**
     * Проверяет существование отзыва по идентификатору.
     *
     * @param id идентификатор отзыва
     * @return true, если отзыв есть в БД
     */
    fun existsById(id: Long): Boolean

    /**
     * Удаляет отзыв по идентификатору.
     *
     * @param id идентификатор отзыва
     */
    fun deleteById(id: Long)
}
