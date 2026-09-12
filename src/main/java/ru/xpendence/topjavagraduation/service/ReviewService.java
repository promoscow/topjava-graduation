package ru.xpendence.topjavagraduation.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.xpendence.topjavagraduation.entity.Review;

/**
 * Операции над отзывами пользователей о ресторанах.
 */
public interface ReviewService {

    /**
     * Сохраняет новый отзыв. У пользователя может быть не более одного отзыва на ресторан.
     *
     * @param review новый отзыв с идентификаторами пользователя и ресторана
     * @return сохранённый отзыв с присвоенным идентификатором
     * @throws IllegalArgumentException если отзыв пользователя на этот ресторан уже существует
     * @throws java.util.NoSuchElementException если пользователь или ресторан не найдены
     */
    Review create(Review review);

    /**
     * Обновляет существующий отзыв. Изменять отзыв может только его автор.
     *
     * @param review        отзыв с заполненным идентификатором и новыми значениями полей
     * @param currentUserId идентификатор текущего пользователя
     * @throws IllegalArgumentException если идентификатор не задан или отзыв принадлежит другому пользователю
     * @throws java.util.NoSuchElementException если отзыв не найден
     */
    void update(Review review, Long currentUserId);

    /**
     * Возвращает отзыв по идентификатору вместе со связанными пользователем и рестораном.
     *
     * @param id идентификатор отзыва
     * @return найденный отзыв
     * @throws java.util.NoSuchElementException если отзыв не найден
     */
    Review getById(Long id);

    /**
     * Возвращает отзыв пользователя о конкретном ресторане.
     *
     * @param userId       идентификатор пользователя
     * @param restaurantId идентификатор ресторана
     * @return найденный отзыв
     * @throws java.util.NoSuchElementException если отзыв не найден
     */
    Review getByUserIdAndRestaurantId(Long userId, Long restaurantId);

    /**
     * Возвращает страницу отзывов о ресторане.
     *
     * @param restaurantId идентификатор ресторана
     * @param pageable     параметры страницы
     * @return страница отзывов
     */
    Page<Review> getAllByRestaurantId(Long restaurantId, Pageable pageable);

    /**
     * Возвращает средний рейтинг ресторана. Если отзывов нет, возвращает {@code 0.0}.
     *
     * @param restaurantId идентификатор ресторана
     * @return средний рейтинг или {@code 0.0}
     */
    Double getAverageRatingByRestaurantId(Long restaurantId);

    /**
     * Возвращает число отзывов о ресторане.
     *
     * @param restaurantId идентификатор ресторана
     * @return количество отзывов
     */
    long countByRestaurantId(Long restaurantId);

    /**
     * Безвозвратно удаляет отзыв из базы.
     *
     * @param id идентификатор отзыва
     * @throws java.util.NoSuchElementException если отзыв не найден
     */
    void delete(Long id);
}
