package ru.xpendence.topjavagraduation.service.impl

import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import ru.xpendence.topjavagraduation.AbstractTest
import ru.xpendence.topjavagraduation.service.RestaurantService
import java.time.LocalDate

class RestaurantServiceTest : AbstractTest() {

    @Autowired
    private lateinit var service: RestaurantService

    @Test
    @DisplayName("create(): валидный ресторан -> успешное создание")
    fun create() {
        val restaurant = dataBuilder.buildRestaurant()
        assertNotNull(service.create(restaurant).id)
    }

    @Test
    @DisplayName("update(): корректные данные -> успешное обновление")
    fun update() {
        var restaurant = dataBuilder.saveRestaurant()
        val newName = RandomStringUtils.secure().nextAlphanumeric(16)
        restaurant = restaurant.copy(name = newName)
        service.update(restaurant)
        assertEquals(newName, service.getById(checkNotNull(restaurant.id)).name)
    }

    @Test
    @DisplayName("getById(): существующий id -> успешное получение ресторана")
    fun get() {
        val restaurant = dataBuilder.saveRestaurant()
        assertDoesNotThrow { service.getById(checkNotNull(restaurant.id)) }
    }

    @Test
    @DisplayName("getByDishId(): существующий dishId -> успешное получение ресторана")
    fun getByDishId() {
        val restaurant = dataBuilder.saveRestaurant()
        val dish = dataBuilder.saveDish(restaurant)
        assertDoesNotThrow { service.getByDishId(checkNotNull(dish.id)) }
    }

    @Test
    @DisplayName("getChosen(): один ресторан с голосами сегодня -> возвращает его")
    fun getChosen() {
        dataBuilder.clearVotes()
        val restaurant = dataBuilder.saveRestaurant()
        dataBuilder.saveVote(dataBuilder.saveUser(), restaurant)
        assertEquals(restaurant.id, service.getChosen().id)
    }

    @Test
    @DisplayName("getChosen(): больше голосов вчера, один сегодня -> ресторан с голосами сегодня")
    fun getChosenReturnsRestaurantWithMostVotesToday() {
        dataBuilder.clearVotes()
        val yesterdayLeader = dataBuilder.saveRestaurant()
        val todayLeader = dataBuilder.saveRestaurant()
        val yesterday = LocalDate.now().minusDays(1)

        repeat(5) {
            dataBuilder.saveVote(dataBuilder.saveUser(), yesterdayLeader, yesterday)
        }
        dataBuilder.saveVote(dataBuilder.saveUser(), todayLeader, LocalDate.now())

        assertEquals(todayLeader.id, service.getChosen().id)
    }

    @Test
    @DisplayName("getChosen(): разное число голосов сегодня -> ресторан с максимумом")
    fun getChosenReturnsRestaurantWithHigherTodayVoteCount() {
        dataBuilder.clearVotes()
        val fewerVotesToday = dataBuilder.saveRestaurant()
        val moreVotesToday = dataBuilder.saveRestaurant()
        val today = LocalDate.now()

        dataBuilder.saveVote(dataBuilder.saveUser(), fewerVotesToday, today)
        repeat(3) {
            dataBuilder.saveVote(dataBuilder.saveUser(), moreVotesToday, today)
        }

        assertEquals(moreVotesToday.id, service.getChosen().id)
    }

    @Test
    @DisplayName("getChosen(): голоса только за вчера -> NoSuchElementException")
    fun getChosenFailsWhenNoVotesToday() {
        dataBuilder.clearVotes()
        val restaurant = dataBuilder.saveRestaurant()
        dataBuilder.saveVote(dataBuilder.saveUser(), restaurant, LocalDate.now().minusDays(1))

        assertThrows(NoSuchElementException::class.java) { service.getChosen() }
    }

    @Test
    @DisplayName("getChosen(): голосов нет -> NoSuchElementException")
    fun getChosenFailsWhenNoVotesAtAll() {
        dataBuilder.clearVotes()
        dataBuilder.saveRestaurant()

        assertThrows(NoSuchElementException::class.java) { service.getChosen() }
    }

    @Test
    @DisplayName("getAll(): рестораны в БД -> непустая страница")
    fun getAll() {
        dataBuilder.saveRestaurant()
        val pageable = PageRequest.of(0, 20, Sort.by(Sort.Order(Sort.Direction.ASC, "id")))
        assertFalse(service.getAll(pageable).isEmpty)
    }

    @Test
    @DisplayName("delete(): существующий id -> ресторан удалён")
    fun delete() {
        val restaurant = dataBuilder.saveRestaurant()
        service.delete(checkNotNull(restaurant.id))
        assertThrows(NoSuchElementException::class.java) { service.getById(checkNotNull(restaurant.id)) }
    }
}
