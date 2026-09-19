package ru.xpendence.topjavagraduation.service.impl

import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import ru.xpendence.topjavagraduation.AbstractTest
import ru.xpendence.topjavagraduation.entity.Restaurant
import ru.xpendence.topjavagraduation.service.DishService
import java.util.*

class DishServiceTest : AbstractTest() {

    @Autowired
    private lateinit var service: DishService

    private lateinit var restaurant: Restaurant

    @BeforeEach
    fun setUp() {
        restaurant = dataBuilder.saveRestaurant()
    }

    @Test
    @DisplayName("create(): валидное блюдо -> успешное создание")
    fun create() {
        val dish = dataBuilder.buildDish(restaurant)
        assertNotNull(service.create(dish).id)
    }

    @Test
    @DisplayName("update(): корректные данные -> успешное обновление")
    fun update() {
        var dish = dataBuilder.saveDish(restaurant)
        val newName = RandomStringUtils.secure().nextAlphanumeric(16)
        dish = dish.copy(name = newName)
        service.update(dish)
        assertEquals(newName, service.getById(checkNotNull(dish.id)).name)
    }

    @Test
    @DisplayName("resetMenu(): активные блюда ресторана -> становятся неактивными")
    fun resetMenu() {
        val dish = dataBuilder.saveDish(restaurant, true)
        service.resetMenu(checkNotNull(restaurant.id))
        assertEquals(false, service.getById(checkNotNull(dish.id)).active)
    }

    @Test
    @DisplayName("resetMenu(): у ресторана нет блюд -> NoSuchElementException")
    fun resetMenuThrowsWhenRestaurantHasNoDishes() {
        assertThrows(NoSuchElementException::class.java) { service.resetMenu(checkNotNull(restaurant.id)) }
    }

    @Test
    @DisplayName("getById(): существующий id -> успешное получение блюда")
    fun get() {
        val dish = dataBuilder.saveDish(restaurant)
        assertDoesNotThrow { service.getById(checkNotNull(dish.id)) }
    }

    @Test
    @DisplayName("getAllByRestaurantId(): блюда ресторана есть -> непустая страница")
    fun getAllByRestaurantId() {
        dataBuilder.saveDish(restaurant)
        assertFalse(service.getAllByRestaurantId(checkNotNull(restaurant.id), Pageable.unpaged()).isEmpty)
    }

    @Test
    @DisplayName("getActiveById(): активное блюдо -> успешное получение")
    fun getActiveById() {
        val dish = dataBuilder.saveDish(restaurant, true)
        assertDoesNotThrow { service.getActiveById(checkNotNull(dish.id)) }
    }

    @Test
    @DisplayName("getActiveById(): неактивное блюдо -> NoSuchElementException")
    fun getActiveByIdFailsWhenInactive() {
        val dish = dataBuilder.saveDish(restaurant, false)
        assertThrows(NoSuchElementException::class.java) { service.getActiveById(checkNotNull(dish.id)) }
    }

    @Test
    @DisplayName("getAllActiveByRestaurantId(): есть активные и неактивные -> только активные")
    fun getAllActiveByRestaurantId() {
        val active = dataBuilder.saveDish(restaurant, true)
        dataBuilder.saveDish(restaurant, false)
        val page = service.getAllActiveByRestaurantId(checkNotNull(restaurant.id), Pageable.unpaged())
        assertEquals(1, page.totalElements)
        assertEquals(active.id, page.content.first().id)
    }

    @Test
    @DisplayName("delete(): существующий id -> блюдо удалено")
    fun delete() {
        val dish = dataBuilder.saveDish(restaurant)
        service.delete(checkNotNull(dish.id))
        assertThrows(NoSuchElementException::class.java) { service.getById(checkNotNull(dish.id)) }
    }

    @Test
    @DisplayName("delete(): несуществующий id -> NoSuchElementException")
    fun delete_absentThrowsException() {
        assertThrows(NoSuchElementException::class.java) { service.delete(Random().nextLong()) }
    }
}
