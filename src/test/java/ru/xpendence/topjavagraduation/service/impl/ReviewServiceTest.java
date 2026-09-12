package ru.xpendence.topjavagraduation.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import ru.xpendence.topjavagraduation.AbstractTest;
import ru.xpendence.topjavagraduation.entity.Restaurant;
import ru.xpendence.topjavagraduation.entity.User;
import ru.xpendence.topjavagraduation.service.ReviewService;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ReviewServiceTest extends AbstractTest {

    @Autowired
    private ReviewService service;

    private User user;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        user = dataBuilder.saveUser();
        restaurant = dataBuilder.saveRestaurant();
    }

    @Test
    @DisplayName("create(): валидный отзыв -> успешное создание")
    void create() {
        var review = dataBuilder.buildReview(user, restaurant, 4, "Хороший обед");
        assertNotNull(service.create(review).getId());
    }

    @Test
    @DisplayName("create(): отзыв user+restaurant уже есть -> IllegalArgumentException")
    void createThrowsWhenReviewAlreadyExists() {
        dataBuilder.saveReview(user, restaurant);
        var duplicate = dataBuilder.buildReview(user, restaurant, 3, "Ещё один отзыв");
        assertThrows(IllegalArgumentException.class, () -> service.create(duplicate));
    }

    @Test
    @DisplayName("update(): владелец отзыва -> успешное обновление")
    void update() {
        var review = dataBuilder.saveReview(user, restaurant, 2, "Так себе");
        review.setRating(5);
        review.setText("Отлично");
        service.update(review, user.getId());
        var stored = service.getById(review.getId());
        assertEquals(5, stored.getRating());
        assertEquals("Отлично", stored.getText());
    }

    @Test
    @DisplayName("update(): чужой пользователь -> IllegalArgumentException")
    void updateFailsWhenReviewBelongsToAnotherUser() {
        var review = dataBuilder.saveReview(user, restaurant, 2, "Так себе");
        var anotherUser = dataBuilder.saveUser();
        review.setRating(5);
        assertThrows(IllegalArgumentException.class, () -> service.update(review, anotherUser.getId()));
    }

    @Test
    @DisplayName("getById(): существующий id -> успешное получение отзыва")
    void getById() {
        var review = dataBuilder.saveReview(user, restaurant);
        assertDoesNotThrow(() -> service.getById(review.getId()));
    }

    @Test
    @DisplayName("getByUserIdAndRestaurantId(): отзыв есть -> успешное получение")
    void getByUserIdAndRestaurantId() {
        var review = dataBuilder.saveReview(user, restaurant);
        assertEquals(
                review.getId(),
                service.getByUserIdAndRestaurantId(user.getId(), restaurant.getId()).getId()
        );
    }

    @Test
    @DisplayName("getAllByRestaurantId(): отзывы есть -> непустая страница")
    void getAllByRestaurantId() {
        dataBuilder.saveReview(user, restaurant);
        var pageable = PageRequest.of(0, 10);
        assertFalse(service.getAllByRestaurantId(restaurant.getId(), pageable).isEmpty());
    }

    @Test
    @DisplayName("getAverageRatingByRestaurantId(): несколько отзывов -> средний рейтинг")
    void getAverageRatingByRestaurantId() {
        dataBuilder.saveReview(user, restaurant, 4, "Норм");
        var anotherUser = dataBuilder.saveUser();
        dataBuilder.saveReview(anotherUser, restaurant, 2, "Не очень");
        assertEquals(3.0, service.getAverageRatingByRestaurantId(restaurant.getId()));
    }

    @Test
    @DisplayName("countByRestaurantId(): один отзыв -> 1")
    void countByRestaurantId() {
        dataBuilder.saveReview(user, restaurant);
        assertEquals(1, service.countByRestaurantId(restaurant.getId()));
    }

    @Test
    @DisplayName("delete(): существующий id -> отзыв удалён")
    void delete() {
        var review = dataBuilder.saveReview(user, restaurant);
        service.delete(review.getId());
        assertThrows(NoSuchElementException.class, () -> service.getById(review.getId()));
    }
}
