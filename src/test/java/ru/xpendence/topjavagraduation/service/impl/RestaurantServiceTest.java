package ru.xpendence.topjavagraduation.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.xpendence.topjavagraduation.AbstractTest;
import ru.xpendence.topjavagraduation.DataBuilder;
import ru.xpendence.topjavagraduation.service.RestaurantService;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantServiceTest extends AbstractTest {

    @Autowired
    private DataBuilder dataBuilder;

    @Autowired
    private RestaurantService restaurantService;

    @Test
    void create() {
        var restaurant = dataBuilder.buildRestaurant();
        assertNotNull(restaurantService.create(restaurant).getId());
    }

    @Test
    void update() {
        var restaurant = dataBuilder.saveRestaurant();
        var newName = RandomStringUtils.randomAlphabetic(16);
        restaurant.setName(newName);
        restaurantService.update(restaurant);
        assertEquals(newName, restaurantService.get(restaurant.getId()).getName());
    }

    @Test
    void get() {
        var restaurant = dataBuilder.saveRestaurant();
        assertDoesNotThrow(() -> restaurantService.get(restaurant.getId()));
    }

    @Test
    void getAll() {
        dataBuilder.saveRestaurant();
        var pageable = PageRequest.of(0, 20, Sort.by(new Sort.Order(Sort.Direction.ASC, "id")));
        assertFalse(restaurantService.getAll(pageable).isEmpty());
    }

    @Test
    void delete() {
        var restaurant = dataBuilder.saveRestaurant();
        restaurantService.delete(restaurant.getId());
        assertThrows(NoSuchElementException.class, () -> restaurantService.get(restaurant.getId()));
    }
}