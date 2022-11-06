package ru.xpendence.topjavagraduation.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.xpendence.topjavagraduation.AbstractTest;
import ru.xpendence.topjavagraduation.entity.Restaurant;
import ru.xpendence.topjavagraduation.service.DishService;

import java.util.NoSuchElementException;

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
    void create() {
        var dish = dataBuilder.buildDish(restaurant);
        assertNotNull(service.create(dish).getId());
    }

    @Test
    void update() {
        var dish = dataBuilder.saveDish(restaurant);
        var newName = RandomStringUtils.randomAlphabetic(16);
        dish.setName(newName);
        service.update(dish);
        assertEquals(newName, service.get(dish.getId()).getName());
    }

    @Test
    void get() {
        var dish = dataBuilder.saveDish(restaurant);
        assertDoesNotThrow(() -> service.get(dish.getId()));
    }

    @Test
    void getAllByRestaurantId() {
        dataBuilder.saveDish(restaurant);
        assertFalse(service.getAllByRestaurantId(restaurant.getId()).isEmpty());
    }

    @Test
    void delete() {
        var dish = dataBuilder.saveDish(restaurant);
        service.delete(dish.getId());
        assertThrows(NoSuchElementException.class, () -> service.get(dish.getId()));
    }
}