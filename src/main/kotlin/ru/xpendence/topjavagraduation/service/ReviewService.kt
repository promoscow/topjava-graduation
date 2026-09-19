package ru.xpendence.topjavagraduation.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import ru.xpendence.topjavagraduation.entity.Review

/**
 * Операции над отзывами пользователей о ресторанах.
 */
interface ReviewService {

    /**
     * Сохраняет новый отзыв. У пользователя может быть не более одного отзыва на ресторан.
     *
     * @param review новый отзыв с идентификаторами пользователя и ресторана
     * @return сохранённый отзыв с присвоенным идентификатором
     * @throws IllegalArgumentException если отзыв пользователя на этот ресторан уже существует
     * @throws java.util.NoSuchElementException если пользователь или ресторан не найдены
     */
    fun create(review: Review): Review

    /**
     * Обновляет существующий отзыв. Изменять отзыв может только его автор.
     *
     * @param review отзыв с заполненным идентификатором и новыми значениями полей
     * @param currentUserId идентификатор текущего пользователя
     * @throws IllegalArgumentException если идентификатор не задан или отзыв принадлежит другому пользователю
     * @throws java.util.NoSuchElementException если отзыв не найден
     */
    fun update(review: Review, currentUserId: Long)

    /**
     * Возвращает отзыв по идентификатору вместе со связанными пользователем и рестораном.
     *
     * @param id идентификатор отзыва
     * @return найденный отзыв
     * @throws java.util.NoSuchElementException если отзыв не найден
     */
    fun getById(id: Long): Review

    /**
     * Возвращает отзыв пользователя о конкретном ресторане.
     *
     * @param userId идентификатор пользователя
     * @param restaurantId идентификатор ресторана
     * @return найденный отзыв
     * @throws java.util.NoSuchElementException если отзыв не найден
     */
    fun getByUserIdAndRestaurantId(userId: Long, restaurantId: Long): Review

    /**
     * Возвращает страницу отзывов о ресторане.
     *
     * @param restaurantId идентификатор ресторана
     * @param pageable параметры страницы
     * @return страница отзывов
     */
    fun getAllByRestaurantId(restaurantId: Long, pageable: Pageable): Page<Review>

    /**
     * Возвращает средний рейтинг ресторана. Если отзывов нет, возвращает `0.0`.
     *
     * @param restaurantId идентификатор ресторана
     * @return средний рейтинг или `0.0`
     */
    fun getAverageRatingByRestaurantId(restaurantId: Long): Double

    /**
     * Возвращает число отзывов о ресторане.
     *
     * @param restaurantId идентификатор ресторана
     * @return количество отзывов
     */
    fun countByRestaurantId(restaurantId: Long): Long

    /**
     * Безвозвратно удаляет отзыв из базы.
     *
     * @param id идентификатор отзыва
     * @throws java.util.NoSuchElementException если отзыв не найден
     */
    fun delete(id: Long)
}
