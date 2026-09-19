package ru.xpendence.topjavagraduation.repository.impl

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import ru.xpendence.topjavagraduation.AbstractTest
import ru.xpendence.topjavagraduation.entity.Restaurant
import ru.xpendence.topjavagraduation.entity.User
import ru.xpendence.topjavagraduation.repository.ReviewRepository
import java.util.*

class ReviewRepositoryTest : AbstractTest() {

    @Autowired
    private lateinit var repository: ReviewRepository

    private lateinit var user: User
    private lateinit var restaurant: Restaurant

    @BeforeEach
    fun setUp() {
        user = dataBuilder.saveUser()
        restaurant = dataBuilder.saveRestaurant()
    }

    @Test
    @DisplayName("save(): новый отзыв -> id присвоен")
    fun saveInsert() {
        val review = dataBuilder.buildReview(user, restaurant, 4, "Хорошо")
        assertNotNull(repository.save(review).id)
    }

    @Test
    @DisplayName("save(): существующий отзыв -> поля обновлены")
    fun saveUpdate() {
        var review = dataBuilder.saveReview(user, restaurant, 2, "Так себе")
        review = review.copy(rating = 5, text = "Отлично")
        repository.save(review)
        val stored = repository.findByIdWithDetails(checkNotNull(review.id)).get()
        assertEquals(5, stored.rating)
        assertEquals("Отлично", stored.text)
    }

    @Test
    @DisplayName("findByIdWithDetails(): существующий id -> отзыв с user и restaurant")
    fun findByIdWithDetails() {
        val review = dataBuilder.saveReview(user, restaurant)
        val found = repository.findByIdWithDetails(checkNotNull(review.id)).get()
        assertEquals(user.id, found.user.id)
        assertEquals(restaurant.id, found.restaurant.id)
        assertNotNull(found.user.username)
        assertNotNull(found.restaurant.name)
    }

    @Test
    @DisplayName("findByIdWithDetails(): несуществующий id -> empty")
    fun findByIdWithDetailsEmpty() {
        assertTrue(repository.findByIdWithDetails(Random().nextLong()).isEmpty)
    }

    @Test
    @DisplayName("findByUserIdAndRestaurantId(): отзыв есть -> найден")
    fun findByUserIdAndRestaurantId() {
        val review = dataBuilder.saveReview(user, restaurant)
        assertEquals(
            review.id,
            repository.findByUserIdAndRestaurantId(checkNotNull(user.id), checkNotNull(restaurant.id)).get().id,
        )
    }

    @Test
    @DisplayName("existsByUserIdAndRestaurantId(): отзыв есть -> true")
    fun existsByUserIdAndRestaurantId() {
        dataBuilder.saveReview(user, restaurant)
        assertTrue(repository.existsByUserIdAndRestaurantId(checkNotNull(user.id), checkNotNull(restaurant.id)))
    }

    @Test
    @DisplayName("existsByUserIdAndRestaurantId(): отзыва нет -> false")
    fun existsByUserIdAndRestaurantIdFalse() {
        assertFalse(repository.existsByUserIdAndRestaurantId(checkNotNull(user.id), checkNotNull(restaurant.id)))
    }

    @Test
    @DisplayName("findAllByRestaurantId(): отзывы есть -> непустая страница")
    fun findAllByRestaurantId() {
        dataBuilder.saveReview(user, restaurant)
        assertFalse(repository.findAllByRestaurantId(checkNotNull(restaurant.id), PageRequest.of(0, 10)).isEmpty)
    }

    @Test
    @DisplayName("findAverageRatingByRestaurantId(): несколько отзывов -> средний рейтинг")
    fun findAverageRatingByRestaurantId() {
        dataBuilder.saveReview(user, restaurant, 4, "Норм")
        dataBuilder.saveReview(dataBuilder.saveUser(), restaurant, 2, "Не очень")
        assertEquals(3.0, repository.findAverageRatingByRestaurantId(checkNotNull(restaurant.id)))
    }

    @Test
    @DisplayName("findAverageRatingByRestaurantId(): отзывов нет -> null")
    fun findAverageRatingByRestaurantIdWhenEmpty() {
        assertNull(repository.findAverageRatingByRestaurantId(checkNotNull(restaurant.id)))
    }

    @Test
    @DisplayName("countByRestaurantId(): один отзыв -> 1")
    fun countByRestaurantId() {
        dataBuilder.saveReview(user, restaurant)
        assertEquals(1, repository.countByRestaurantId(checkNotNull(restaurant.id)))
    }

    @Test
    @DisplayName("existsById(): существующий id -> true")
    fun existsById() {
        val review = dataBuilder.saveReview(user, restaurant)
        assertTrue(repository.existsById(checkNotNull(review.id)))
    }

    @Test
    @DisplayName("deleteById(): существующий id -> отзыв удалён")
    fun deleteById() {
        val review = dataBuilder.saveReview(user, restaurant)
        repository.deleteById(checkNotNull(review.id))
        assertTrue(repository.findByIdWithDetails(checkNotNull(review.id)).isEmpty)
        assertFalse(repository.existsById(checkNotNull(review.id)))
    }
}
