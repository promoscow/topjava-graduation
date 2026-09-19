package ru.xpendence.topjavagraduation

import org.apache.commons.lang3.RandomStringUtils
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import ru.xpendence.topjavagraduation.entity.*
import ru.xpendence.topjavagraduation.repository.*
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@Component
class DataBuilder(
    private val restaurantRepository: RestaurantRepository,
    private val dishRepository: DishRepository,
    private val userRepository: UserRepository,
    private val voteRepository: VoteRepository,
    private val reviewRepository: ReviewRepository,
    private val tagRepository: TagRepository,
    private val jdbcTemplate: JdbcTemplate,
) {
    private val random = Random()

    fun buildRestaurant(): Restaurant =
        Restaurant(name = RandomStringUtils.secure().nextAlphanumeric(16))

    fun saveRestaurant(): Restaurant =
        restaurantRepository.save(buildRestaurant())

    fun buildDish(restaurant: Restaurant): Dish =
        Dish(
            name = RandomStringUtils.secure().nextAlphanumeric(16),
            price = BigDecimal.valueOf(random.nextDouble()),
            active = random.nextBoolean(),
            restaurant = restaurant,
        )

    fun saveDish(restaurant: Restaurant): Dish =
        dishRepository.save(buildDish(restaurant))

    fun saveDish(restaurant: Restaurant, active: Boolean): Dish =
        dishRepository.save(buildDish(restaurant).copy(active = active))

    fun buildUser(): User =
        User(
            username = RandomStringUtils.secure().nextAlphanumeric(16),
            password = RandomStringUtils.secure().nextAlphanumeric(16),
        )

    fun saveUser(): User =
        userRepository.save(buildUser())

    fun buildVote(user: User, restaurant: Restaurant): Vote =
        buildVote(user, restaurant, LocalDate.now())

    fun buildVote(user: User, restaurant: Restaurant, date: LocalDate): Vote =
        Vote(date = date, user = user, restaurant = restaurant)

    fun saveVote(user: User, restaurant: Restaurant): Vote =
        voteRepository.save(buildVote(user, restaurant))

    fun saveVote(user: User, restaurant: Restaurant, date: LocalDate): Vote =
        voteRepository.save(buildVote(user, restaurant, date))

    fun clearVotes() {
        jdbcTemplate.update("DELETE FROM votes")
    }

    fun buildReview(user: User, restaurant: Restaurant): Review =
        buildReview(user, restaurant, 5, RandomStringUtils.secure().nextAlphanumeric(32))

    fun buildReview(user: User, restaurant: Restaurant, rating: Int, text: String): Review =
        Review(
            rating = rating,
            text = text,
            date = LocalDate.now(),
            user = user,
            restaurant = restaurant,
        )

    fun saveReview(user: User, restaurant: Restaurant): Review =
        reviewRepository.save(buildReview(user, restaurant))

    fun saveReview(user: User, restaurant: Restaurant, rating: Int, text: String): Review =
        reviewRepository.save(buildReview(user, restaurant, rating, text))

    fun buildTag(): Tag =
        Tag(name = RandomStringUtils.secure().nextAlphanumeric(16))

    fun saveTag(): Tag =
        tagRepository.save(buildTag())
}
