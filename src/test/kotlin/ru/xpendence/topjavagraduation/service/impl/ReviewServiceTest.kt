package ru.xpendence.topjavagraduation.service.impl

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import ru.xpendence.topjavagraduation.AbstractTest
import ru.xpendence.topjavagraduation.entity.Restaurant
import ru.xpendence.topjavagraduation.entity.User
import ru.xpendence.topjavagraduation.service.ReviewService

class ReviewServiceTest : AbstractTest() {

    @Autowired
    private lateinit var service: ReviewService

    private lateinit var user: User
    private lateinit var restaurant: Restaurant

    @BeforeEach
    fun setUp() {
        user = dataBuilder.saveUser()
        restaurant = dataBuilder.saveRestaurant()
    }

    @Test
    @DisplayName("create(): валидный отзыв -> успешное создание")
    fun create() {
        val review = dataBuilder.buildReview(user, restaurant, 4, "Хороший обед")
        assertNotNull(service.create(review).id)
    }

    @Test
    @DisplayName("create(): отзыв user+restaurant уже есть -> IllegalArgumentException")
    fun createThrowsWhenReviewAlreadyExists() {
        dataBuilder.saveReview(user, restaurant)
        val duplicate = dataBuilder.buildReview(user, restaurant, 3, "Ещё один отзыв")
        assertThrows(IllegalArgumentException::class.java) { service.create(duplicate) }
    }

    @Test
    @DisplayName("update(): владелец отзыва -> успешное обновление")
    fun update() {
        var review = dataBuilder.saveReview(user, restaurant, 2, "Так себе")
        review = review.copy(rating = 5, text = "Отлично")
        service.update(review, checkNotNull(user.id))
        val stored = service.getById(checkNotNull(review.id))
        assertEquals(5, stored.rating)
        assertEquals("Отлично", stored.text)
    }

    @Test
    @DisplayName("update(): чужой пользователь -> IllegalArgumentException")
    fun updateFailsWhenReviewBelongsToAnotherUser() {
        var review = dataBuilder.saveReview(user, restaurant, 2, "Так себе")
        val anotherUser = dataBuilder.saveUser()
        review = review.copy(rating = 5)
        assertThrows(IllegalArgumentException::class.java) { service.update(review, checkNotNull(anotherUser.id)) }
    }

    @Test
    @DisplayName("getById(): существующий id -> успешное получение отзыва")
    fun getById() {
        val review = dataBuilder.saveReview(user, restaurant)
        assertDoesNotThrow { service.getById(checkNotNull(review.id)) }
    }

    @Test
    @DisplayName("getByUserIdAndRestaurantId(): отзыв есть -> успешное получение")
    fun getByUserIdAndRestaurantId() {
        val review = dataBuilder.saveReview(user, restaurant)
        assertEquals(
            review.id,
            service.getByUserIdAndRestaurantId(checkNotNull(user.id), checkNotNull(restaurant.id)).id,
        )
    }

    @Test
    @DisplayName("getAllByRestaurantId(): отзывы есть -> непустая страница")
    fun getAllByRestaurantId() {
        dataBuilder.saveReview(user, restaurant)
        val pageable = PageRequest.of(0, 10)
        assertFalse(service.getAllByRestaurantId(checkNotNull(restaurant.id), pageable).isEmpty)
    }

    @Test
    @DisplayName("getAverageRatingByRestaurantId(): несколько отзывов -> средний рейтинг")
    fun getAverageRatingByRestaurantId() {
        dataBuilder.saveReview(user, restaurant, 4, "Норм")
        val anotherUser = dataBuilder.saveUser()
        dataBuilder.saveReview(anotherUser, restaurant, 2, "Не очень")
        assertEquals(3.0, service.getAverageRatingByRestaurantId(checkNotNull(restaurant.id)))
    }

    @Test
    @DisplayName("countByRestaurantId(): один отзыв -> 1")
    fun countByRestaurantId() {
        dataBuilder.saveReview(user, restaurant)
        assertEquals(1, service.countByRestaurantId(checkNotNull(restaurant.id)))
    }

    @Test
    @DisplayName("delete(): существующий id -> отзыв удалён")
    fun delete() {
        val review = dataBuilder.saveReview(user, restaurant)
        service.delete(checkNotNull(review.id))
        assertThrows(NoSuchElementException::class.java) { service.getById(checkNotNull(review.id)) }
    }
}
