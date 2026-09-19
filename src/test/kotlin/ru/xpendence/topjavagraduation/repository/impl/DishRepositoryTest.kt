package ru.xpendence.topjavagraduation.repository.impl

import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import ru.xpendence.topjavagraduation.AbstractTest
import ru.xpendence.topjavagraduation.entity.Restaurant
import ru.xpendence.topjavagraduation.repository.DishRepository
import java.util.*

class DishRepositoryTest : AbstractTest() {

    @Autowired
    private lateinit var repository: DishRepository

    private lateinit var restaurant: Restaurant

    @BeforeEach
    fun setUp() {
        restaurant = dataBuilder.saveRestaurant()
    }

    @Test
    @DisplayName("save(): новое блюдо -> id присвоен")
    fun saveInsert() {
        val dish = dataBuilder.buildDish(restaurant)
        assertNotNull(repository.save(dish).id)
    }

    @Test
    @DisplayName("save(): существующее блюдо -> поля обновлены")
    fun saveUpdate() {
        var dish = dataBuilder.saveDish(restaurant)
        val newName = RandomStringUtils.secure().nextAlphanumeric(16)
        dish = dish.copy(name = newName, active = true)
        repository.save(dish)
        assertEquals(newName, repository.findById(checkNotNull(dish.id)).get().name)
        assertEquals(true, repository.findById(checkNotNull(dish.id)).get().active)
    }

    @Test
    @DisplayName("findById(): существующий id -> блюдо найдено")
    fun findById() {
        val dish = dataBuilder.saveDish(restaurant)
        assertTrue(repository.findById(checkNotNull(dish.id)).isPresent)
    }

    @Test
    @DisplayName("findById(): несуществующий id -> empty")
    fun findByIdEmpty() {
        assertTrue(repository.findById(Random().nextLong()).isEmpty)
    }

    @Test
    @DisplayName("findByIdWithRestaurant(): существующий id -> блюдо с рестораном")
    fun findByIdWithRestaurant() {
        val dish = dataBuilder.saveDish(restaurant)
        val found = repository.findByIdWithRestaurant(checkNotNull(dish.id)).get()
        assertEquals(restaurant.id, found.restaurant.id)
        assertEquals(restaurant.name, found.restaurant.name)
    }

    @Test
    @DisplayName("findActiveByIdWithRestaurant(): активное блюдо -> найдено")
    fun findActiveByIdWithRestaurant() {
        val dish = dataBuilder.saveDish(restaurant, true)
        assertTrue(repository.findActiveByIdWithRestaurant(checkNotNull(dish.id)).isPresent)
    }

    @Test
    @DisplayName("findActiveByIdWithRestaurant(): неактивное блюдо -> empty")
    fun findActiveByIdWithRestaurantWhenInactive() {
        val dish = dataBuilder.saveDish(restaurant, false)
        assertTrue(repository.findActiveByIdWithRestaurant(checkNotNull(dish.id)).isEmpty)
    }

    @Test
    @DisplayName("getAllByRestaurantId(): блюда ресторана есть -> непустая страница")
    fun getAllByRestaurantId() {
        dataBuilder.saveDish(restaurant)
        assertFalse(repository.getAllByRestaurantId(checkNotNull(restaurant.id), Pageable.unpaged()).isEmpty)
    }

    @Test
    @DisplayName("getAllActiveByRestaurantId(): активные и неактивные -> только активные")
    fun getAllActiveByRestaurantId() {
        val active = dataBuilder.saveDish(restaurant, true)
        dataBuilder.saveDish(restaurant, false)
        val page = repository.getAllActiveByRestaurantId(checkNotNull(restaurant.id), Pageable.unpaged())
        assertEquals(1, page.totalElements)
        assertEquals(active.id, page.content.first().id)
    }

    @Test
    @DisplayName("setActiveFalseForAllByRestaurantId(): активные блюда -> снята активность")
    fun setActiveFalseForAllByRestaurantId() {
        val dish = dataBuilder.saveDish(restaurant, true)
        val affected = repository.setActiveFalseForAllByRestaurantId(checkNotNull(restaurant.id))
        assertEquals(1, affected)
        assertEquals(false, repository.findById(checkNotNull(dish.id)).get().active)
    }

    @Test
    @DisplayName("setActiveFalseForAllByRestaurantId(): блюд нет -> 0")
    fun setActiveFalseWhenNoDishes() {
        assertEquals(0, repository.setActiveFalseForAllByRestaurantId(checkNotNull(restaurant.id)))
    }

    @Test
    @DisplayName("existsById(): существующий id -> true")
    fun existsById() {
        val dish = dataBuilder.saveDish(restaurant)
        assertTrue(repository.existsById(checkNotNull(dish.id)))
    }

    @Test
    @DisplayName("existsById(): несуществующий id -> false")
    fun existsByIdFalse() {
        assertFalse(repository.existsById(Random().nextLong()))
    }

    @Test
    @DisplayName("deleteById(): существующий id -> блюдо удалено")
    fun deleteById() {
        val dish = dataBuilder.saveDish(restaurant)
        repository.deleteById(checkNotNull(dish.id))
        assertTrue(repository.findById(checkNotNull(dish.id)).isEmpty)
    }
}
