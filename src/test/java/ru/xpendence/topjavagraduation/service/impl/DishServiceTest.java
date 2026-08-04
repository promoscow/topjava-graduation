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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DishServiceTest extends AbstractTest {

    @Autowired
    private DishService service;

    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        restaurant = dataBuilder.saveRestaurant();
    }

    @Test
    void create() {
        var dish = dataBuilder.buildDish(restaurant);
        assertNotNull(service.create(dish).getId());
    }

    @Test
    void update() {
        var dish = dataBuilder.saveDish(restaurant);
        var newName = RandomStringUtils.secure().nextAlphanumeric(16);
        dish.setName(newName);
        service.update(dish);
        assertEquals(newName, service.getById(dish.getId()).getName());
    }

    @Test
    void resetMenu() {
        var dish = dataBuilder.saveDish(restaurant);
        dish.setActive(true);
        service.update(dish);
        service.resetMenu(restaurant.getId());
        assertFalse(service.getById(dish.getId()).getActive());
    }

    @Test
    void resetMenuThrowsWhenRestaurantHasNoDishes() {
        assertThrows(NoSuchElementException.class, () -> service.resetMenu(restaurant.getId()));
    }

    @Test
    void get() {
        var dish = dataBuilder.saveDish(restaurant);
        assertDoesNotThrow(() -> service.getById(dish.getId()));
    }

    @Test
    void getAllByRestaurantId() {
        dataBuilder.saveDish(restaurant);
        assertFalse(service.getAllByRestaurantId(restaurant.getId(), Pageable.unpaged()).isEmpty());
    }

    @Test
    void delete() {
        var dish = dataBuilder.saveDish(restaurant);
        service.delete(dish.getId());
        assertThrows(NoSuchElementException.class, () -> service.getById(dish.getId()));
    }

    @Test
    @DisplayName("delete(): удаление несуществующего блюда, выбрасывает NoSuchElementException")
    void delete_absentThrowsException() {
        assertThrows(NoSuchElementException.class, () -> service.delete(new Random().nextLong()));
    }
}