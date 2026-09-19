package ru.xpendence.topjavagraduation.repository.impl

import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import ru.xpendence.topjavagraduation.AbstractTest
import ru.xpendence.topjavagraduation.repository.RestaurantRepository
import java.time.LocalDate
import java.util.*

class RestaurantRepositoryTest : AbstractTest() {

    @Autowired
    private lateinit var repository: RestaurantRepository

    @Test
    @DisplayName("save(): новый ресторан -> id присвоен")
    fun saveInsert() {
        val restaurant = dataBuilder.buildRestaurant()
        assertNotNull(repository.save(restaurant).id)
    }

    @Test
    @DisplayName("save(): существующий ресторан -> имя обновлено")
    fun saveUpdate() {
        var restaurant = dataBuilder.saveRestaurant()
        val newName = RandomStringUtils.secure().nextAlphanumeric(16)
        restaurant = restaurant.copy(name = newName)
        repository.save(restaurant)
        assertEquals(newName, repository.findById(checkNotNull(restaurant.id)).get().name)
    }

    @Test
    @DisplayName("findById(): существующий id -> ресторан с блюдами и голосами за сегодня")
    fun findById() {
        dataBuilder.clearVotes()
        val restaurant = dataBuilder.saveRestaurant()
        val dish = dataBuilder.saveDish(restaurant)
        dataBuilder.saveVote(dataBuilder.saveUser(), restaurant, LocalDate.now())

        val found = repository.findById(checkNotNull(restaurant.id)).get()
        assertTrue(found.dishes.any { it.id == dish.id })
        assertEquals(1, found.votes.size)
    }

    @Test
    @DisplayName("findById(): несуществующий id -> empty")
    fun findByIdEmpty() {
        assertTrue(repository.findById(Random().nextLong()).isEmpty)
    }

    @Test
    @DisplayName("getByDishId(): существующий dishId -> ресторан найден")
    fun getByDishId() {
        val restaurant = dataBuilder.saveRestaurant()
        val dish = dataBuilder.saveDish(restaurant)
        assertEquals(restaurant.id, repository.getByDishId(checkNotNull(dish.id)).get().id)
    }

    @Test
    @DisplayName("getByDishId(): несуществующий dishId -> empty")
    fun getByDishIdEmpty() {
        assertTrue(repository.getByDishId(Random().nextLong()).isEmpty)
    }

    @Test
    @DisplayName("findAll(): рестораны в БД -> непустая страница")
    fun findAll() {
        dataBuilder.saveRestaurant()
        assertFalse(repository.findAll(PageRequest.of(0, 20)).isEmpty)
    }

    @Test
    @DisplayName("findChosenByVoteDate(): голоса сегодня -> ресторан с максимумом голосов")
    fun findChosenByVoteDate() {
        dataBuilder.clearVotes()
        val fewer = dataBuilder.saveRestaurant()
        val more = dataBuilder.saveRestaurant()
        val today = LocalDate.now()
        dataBuilder.saveVote(dataBuilder.saveUser(), fewer, today)
        repeat(3) {
            dataBuilder.saveVote(dataBuilder.saveUser(), more, today)
        }
        val chosen = repository.findChosenByVoteDate(today, Pageable.ofSize(1))
        assertEquals(1, chosen.size)
        assertEquals(more.id, chosen.first().id)
    }

    @Test
    @DisplayName("findChosenByVoteDate(): голосов на дату нет -> пустой список")
    fun findChosenByVoteDateEmpty() {
        dataBuilder.clearVotes()
        dataBuilder.saveRestaurant()
        assertTrue(repository.findChosenByVoteDate(LocalDate.now(), Pageable.ofSize(1)).isEmpty())
    }

    @Test
    @DisplayName("deleteById(): существующий id -> ресторан удалён")
    fun deleteById() {
        val restaurant = dataBuilder.saveRestaurant()
        repository.deleteById(checkNotNull(restaurant.id))
        assertTrue(repository.findById(checkNotNull(restaurant.id)).isEmpty)
    }
}
