package ru.xpendence.topjavagraduation.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import ru.xpendence.topjavagraduation.AbstractTest;
import ru.xpendence.topjavagraduation.entity.Restaurant;
import ru.xpendence.topjavagraduation.entity.User;
import ru.xpendence.topjavagraduation.service.ReviewService;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    void create() {
        var review = dataBuilder.buildReview(user, restaurant, 4, "Хороший обед");
        assertNotNull(service.create(review).getId());
    }

    @Test
    void createThrowsWhenReviewAlreadyExists() {
        dataBuilder.saveReview(user, restaurant);
        var duplicate = dataBuilder.buildReview(user, restaurant, 3, "Ещё один отзыв");
        assertThrows(IllegalArgumentException.class, () -> service.create(duplicate));
    }

    @Test
    void update() {
        var review = dataBuilder.saveReview(user, restaurant, 2, "Так себе");
        review.setRating(5);
        review.setText("Отлично");
        service.update(review);
        var stored = service.getById(review.getId());
        assertEquals(5, stored.getRating());
        assertEquals("Отлично", stored.getText());
    }

    @Test
    void getById() {
        var review = dataBuilder.saveReview(user, restaurant);
        assertDoesNotThrow(() -> service.getById(review.getId()));
    }

    @Test
    void getByUserIdAndRestaurantId() {
        var review = dataBuilder.saveReview(user, restaurant);
        assertEquals(
                review.getId(),
                service.getByUserIdAndRestaurantId(user.getId(), restaurant.getId()).getId()
        );
    }

    @Test
    void getAllByRestaurantId() {
        dataBuilder.saveReview(user, restaurant);
        var pageable = PageRequest.of(0, 10);
        assertFalse(service.getAllByRestaurantId(restaurant.getId(), pageable).isEmpty());
    }

    @Test
    void getAverageRatingByRestaurantId() {
        dataBuilder.saveReview(user, restaurant, 4, "Норм");
        var anotherUser = dataBuilder.saveUser();
        dataBuilder.saveReview(anotherUser, restaurant, 2, "Не очень");
        assertEquals(3.0, service.getAverageRatingByRestaurantId(restaurant.getId()));
    }

    @Test
    void countByRestaurantId() {
        dataBuilder.saveReview(user, restaurant);
        assertEquals(1, service.countByRestaurantId(restaurant.getId()));
    }

    @Test
    void delete() {
        var review = dataBuilder.saveReview(user, restaurant);
        service.delete(review.getId());
        assertThrows(NoSuchElementException.class, () -> service.getById(review.getId()));
    }
}
