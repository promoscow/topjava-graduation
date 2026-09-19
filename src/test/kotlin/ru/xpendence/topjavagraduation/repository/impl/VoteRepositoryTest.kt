package ru.xpendence.topjavagraduation.repository.impl

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import ru.xpendence.topjavagraduation.AbstractTest
import ru.xpendence.topjavagraduation.entity.Restaurant
import ru.xpendence.topjavagraduation.entity.User
import ru.xpendence.topjavagraduation.repository.VoteRepository
import java.time.LocalDate
import java.util.*

class VoteRepositoryTest : AbstractTest() {

    @Autowired
    private lateinit var repository: VoteRepository

    private lateinit var user: User
    private lateinit var restaurant: Restaurant

    @BeforeEach
    fun setUp() {
        user = dataBuilder.saveUser()
        restaurant = dataBuilder.saveRestaurant()
    }

    @Test
    @DisplayName("save(): новый голос -> id присвоен")
    fun saveInsert() {
        val vote = dataBuilder.buildVote(user, restaurant)
        assertNotNull(repository.save(vote).id)
    }

    @Test
    @DisplayName("save(): существующий голос -> ресторан обновлён")
    fun saveUpdate() {
        var vote = dataBuilder.saveVote(user, restaurant)
        val newRestaurant = dataBuilder.saveRestaurant()
        vote = vote.copy(restaurant = newRestaurant, date = LocalDate.now())
        repository.save(vote)
        assertEquals(newRestaurant.id, repository.findById(checkNotNull(vote.id)).get().restaurant.id)
    }

    @Test
    @DisplayName("findById(): существующий id -> голос найден")
    fun findById() {
        val vote = dataBuilder.saveVote(user, restaurant)
        assertTrue(repository.findById(checkNotNull(vote.id)).isPresent)
    }

    @Test
    @DisplayName("findById(): несуществующий id -> empty")
    fun findByIdEmpty() {
        assertTrue(repository.findById(Random().nextLong()).isEmpty)
    }

    @Test
    @DisplayName("findByIdWithDetails(): существующий id -> голос с user и restaurant")
    fun findByIdWithDetails() {
        val vote = dataBuilder.saveVote(user, restaurant)
        val found = repository.findByIdWithDetails(checkNotNull(vote.id)).get()
        assertEquals(user.id, found.user.id)
        assertEquals(restaurant.id, found.restaurant.id)
        assertNotNull(found.user.username)
        assertNotNull(found.restaurant.name)
    }

    @Test
    @DisplayName("findByUserIdAndDate(): голос на дату -> найден")
    fun findByUserIdAndDate() {
        val today = LocalDate.now()
        val yesterday = today.minusDays(1)
        val todayVote = dataBuilder.saveVote(user, restaurant, today)
        dataBuilder.saveVote(user, restaurant, yesterday)
        assertEquals(todayVote.id, repository.findByUserIdAndDate(checkNotNull(user.id), today).get().id)
    }

    @Test
    @DisplayName("findByUserIdAndDate(): нет голоса на дату -> empty")
    fun findByUserIdAndDateEmpty() {
        dataBuilder.saveVote(user, restaurant, LocalDate.now())
        assertTrue(repository.findByUserIdAndDate(checkNotNull(user.id), LocalDate.now().minusDays(1)).isEmpty)
    }
}
