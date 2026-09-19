package ru.xpendence.topjavagraduation.service.impl

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import ru.xpendence.topjavagraduation.AbstractTest
import ru.xpendence.topjavagraduation.entity.Restaurant
import ru.xpendence.topjavagraduation.entity.User
import ru.xpendence.topjavagraduation.service.VoteService
import java.time.LocalDate
import java.time.LocalTime

class VoteServiceTest : AbstractTest() {

    @Autowired
    private lateinit var service: VoteService

    private val votingAvailableUntil = LocalTime.of(11, 0)

    private lateinit var user: User
    private lateinit var restaurant: Restaurant

    @BeforeEach
    fun setUp() {
        user = dataBuilder.saveUser()
        restaurant = dataBuilder.saveRestaurant()
    }

    @Test
    @DisplayName("create(): до 11:00 -> успешное создание; после 11:00 -> IllegalArgumentException")
    fun create() {
        val vote = dataBuilder.buildVote(user, restaurant)
        val now = LocalTime.now()
        if (now.isBefore(votingAvailableUntil)) {
            assertNotNull(service.create(vote).id)
        } else {
            assertThrows(IllegalArgumentException::class.java) { service.create(vote) }
        }
    }

    @Test
    @DisplayName("update(): смена ресторана -> успешное обновление")
    fun update() {
        var vote = dataBuilder.saveVote(user, restaurant)
        val newRestaurant = dataBuilder.saveRestaurant()
        vote = vote.copy(restaurant = newRestaurant)
        service.update(vote)
        assertEquals(newRestaurant.id, service.getById(checkNotNull(vote.id)).restaurant.id)
    }

    @Test
    @DisplayName("getById(): существующий id -> успешное получение голоса")
    fun getById() {
        val vote = dataBuilder.saveVote(user, restaurant)
        assertDoesNotThrow { service.getById(checkNotNull(vote.id)) }
    }

    @Test
    @DisplayName("getByUserId(): голос на сегодня -> успешное получение")
    fun getByUserId() {
        val vote = dataBuilder.saveVote(user, restaurant)
        assertEquals(vote.id, service.getByUserId(checkNotNull(vote.user.id), LocalDate.now()).id)
    }

    @Test
    @DisplayName("getByUserId(): несколько дат -> голос за указанную дату")
    fun getByUserIdReturnsVoteForSpecifiedDateWhenUserHasMultipleVotes() {
        val yesterday = LocalDate.now().minusDays(1)
        val todayVote = dataBuilder.saveVote(user, restaurant, LocalDate.now())
        val yesterdayVote = dataBuilder.saveVote(user, restaurant, yesterday)

        assertEquals(todayVote.id, service.getByUserId(checkNotNull(user.id), LocalDate.now()).id)
        assertEquals(yesterdayVote.id, service.getByUserId(checkNotNull(user.id), yesterday).id)
    }

    @Test
    @DisplayName("getByUserId(): нет голоса на дату -> NoSuchElementException")
    fun getByUserIdThrowsWhenVoteForDateNotFound() {
        dataBuilder.saveVote(user, restaurant, LocalDate.now())
        assertThrows(NoSuchElementException::class.java) {
            service.getByUserId(checkNotNull(user.id), LocalDate.now().minusDays(1))
        }
    }
}
