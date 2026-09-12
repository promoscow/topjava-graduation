package ru.xpendence.topjavagraduation.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import ru.xpendence.topjavagraduation.AbstractTest;
import ru.xpendence.topjavagraduation.entity.Restaurant;
import ru.xpendence.topjavagraduation.service.DishService;

import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DishServiceTest extends AbstractTest {

    @Autowired
    private DishService service;

    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        restaurant = dataBuilder.saveRestaurant();
    }

    @Test
    @DisplayName("create(): валидное блюдо -> успешное создание")
    void create() {
        var dish = dataBuilder.buildDish(restaurant);
        assertNotNull(service.create(dish).getId());
    }

    @Test
    @DisplayName("update(): корректные данные -> успешное обновление")
    void update() {
        var dish = dataBuilder.saveDish(restaurant);
        var newName = RandomStringUtils.secure().nextAlphanumeric(16);
        dish.setName(newName);
        service.update(dish);
        assertEquals(newName, service.getById(dish.getId()).getName());
    }

    @Test
    @DisplayName("resetMenu(): активные блюда ресторана -> становятся неактивными")
    void resetMenu() {
        var dish = dataBuilder.saveDish(restaurant);
        dish.setActive(true);
        service.update(dish);
        service.resetMenu(restaurant.getId());
        assertFalse(service.getById(dish.getId()).getActive());
    }

    @Test
    @DisplayName("resetMenu(): у ресторана нет блюд -> NoSuchElementException")
    void resetMenuThrowsWhenRestaurantHasNoDishes() {
        assertThrows(NoSuchElementException.class, () -> service.resetMenu(restaurant.getId()));
    }

    @Test
    @DisplayName("getById(): существующий id -> успешное получение блюда")
    void get() {
        var dish = dataBuilder.saveDish(restaurant);
        assertDoesNotThrow(() -> service.getById(dish.getId()));
    }

    @Test
    @DisplayName("getAllByRestaurantId(): блюда ресторана есть -> непустая страница")
    void getAllByRestaurantId() {
        dataBuilder.saveDish(restaurant);
        assertFalse(service.getAllByRestaurantId(restaurant.getId(), Pageable.unpaged()).isEmpty());
    }

    @Test
    @DisplayName("getActiveById(): активное блюдо -> успешное получение")
    void getActiveById() {
        var dish = dataBuilder.saveDish(restaurant, true);
        assertDoesNotThrow(() -> service.getActiveById(dish.getId()));
    }

    @Test
    @DisplayName("getActiveById(): неактивное блюдо -> NoSuchElementException")
    void getActiveByIdFailsWhenInactive() {
        var dish = dataBuilder.saveDish(restaurant, false);
        assertThrows(NoSuchElementException.class, () -> service.getActiveById(dish.getId()));
    }

    @Test
    @DisplayName("getAllActiveByRestaurantId(): есть активные и неактивные -> только активные")
    void getAllActiveByRestaurantId() {
        var active = dataBuilder.saveDish(restaurant, true);
        dataBuilder.saveDish(restaurant, false);
        var page = service.getAllActiveByRestaurantId(restaurant.getId(), Pageable.unpaged());
        assertEquals(1, page.getTotalElements());
        assertEquals(active.getId(), page.getContent().getFirst().getId());
    }

    @Test
    @DisplayName("delete(): существующий id -> блюдо удалено")
    void delete() {
        var dish = dataBuilder.saveDish(restaurant);
        service.delete(dish.getId());
        assertThrows(NoSuchElementException.class, () -> service.getById(dish.getId()));
    }

    @Test
    @DisplayName("delete(): несуществующий id -> NoSuchElementException")
    void delete_absentThrowsException() {
        assertThrows(NoSuchElementException.class, () -> service.delete(new Random().nextLong()));
    }
}
