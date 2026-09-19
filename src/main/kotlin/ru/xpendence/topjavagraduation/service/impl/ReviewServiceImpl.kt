package ru.xpendence.topjavagraduation.service.impl

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.xpendence.topjavagraduation.entity.Review
import ru.xpendence.topjavagraduation.repository.ReviewRepository
import ru.xpendence.topjavagraduation.service.RestaurantService
import ru.xpendence.topjavagraduation.service.ReviewService
import ru.xpendence.topjavagraduation.service.UserService

@Service
class ReviewServiceImpl(
    private val repository: ReviewRepository,
    private val userService: UserService,
    private val restaurantService: RestaurantService,
) : ReviewService {

    @Transactional
    override fun create(review: Review): Review {
        val userId = requireNotNull(review.user.id)
        val restaurantId = requireNotNull(review.restaurant.id)
        if (repository.existsByUserIdAndRestaurantId(userId, restaurantId)) {
            throw IllegalArgumentException(
                "Review already exists for user id: $userId and restaurant id: $restaurantId",
            )
        }
        val user = userService.getById(userId)
        val restaurant = restaurantService.getById(restaurantId)
        return repository.save(review.copy(user = user, restaurant = restaurant))
    }

    @Transactional
    override fun update(review: Review, currentUserId: Long) {
        val id = review.id ?: throw IllegalArgumentException("Review id is null.")
        val stored = repository.findByIdWithDetails(id)
            .orElseThrow { NoSuchElementException("Review not found by id: $id") }
        if (stored.user.id != currentUserId) {
            throw IllegalArgumentException(
                "Review id: $id does not belong to user id: $currentUserId",
            )
        }
        repository.save(Review.enrichForUpdate(review, stored))
    }

    @Transactional(readOnly = true)
    override fun getById(id: Long): Review =
        repository.findByIdWithDetails(id)
            .orElseThrow { NoSuchElementException("Review not found by id: $id") }

    @Transactional(readOnly = true)
    override fun getByUserIdAndRestaurantId(userId: Long, restaurantId: Long): Review =
        repository.findByUserIdAndRestaurantId(userId, restaurantId)
            .orElseThrow {
                NoSuchElementException(
                    "Review not found by user id: $userId and restaurant id: $restaurantId",
                )
            }

    @Transactional(readOnly = true)
    override fun getAllByRestaurantId(restaurantId: Long, pageable: Pageable): Page<Review> =
        repository.findAllByRestaurantId(restaurantId, pageable)

    @Transactional(readOnly = true)
    override fun getAverageRatingByRestaurantId(restaurantId: Long): Double =
        repository.findAverageRatingByRestaurantId(restaurantId) ?: 0.0

    @Transactional(readOnly = true)
    override fun countByRestaurantId(restaurantId: Long): Long =
        repository.countByRestaurantId(restaurantId)

    @Transactional
    override fun delete(id: Long) {
        if (!repository.existsById(id)) {
            throw NoSuchElementException("Review not found by id: $id")
        }
        repository.deleteById(id)
    }
}
