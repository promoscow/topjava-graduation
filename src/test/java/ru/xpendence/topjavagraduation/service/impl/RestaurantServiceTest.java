package ru.xpendence.topjavagraduation.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.xpendence.topjavagraduation.AbstractTest;
import ru.xpendence.topjavagraduation.service.RestaurantService;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantServiceTest extends AbstractTest {

    @Autowired
    private RestaurantService service;

    @Test
    void create() {
        var restaurant = dataBuilder.buildRestaurant();
        assertNotNull(service.create(restaurant).getId());
    }

    @Test
    void update() {
        var restaurant = dataBuilder.saveRestaurant();
        var newName = RandomStringUtils.randomAlphabetic(16);
        restaurant.setName(newName);
        service.update(restaurant);
        assertEquals(newName, service.getById(restaurant.getId()).getName());
    }

    @Test
    void get() {
        var restaurant = dataBuilder.saveRestaurant();
        assertDoesNotThrow(() -> service.getById(restaurant.getId()));
    }

    @Test
    void getByDishId() {
        var restaurant = dataBuilder.saveRestaurant();
        var dish = dataBuilder.saveDish(restaurant);
        assertDoesNotThrow(() -> service.getByDishId(dish.getId()));
    }

    @Test
    void getChosen() {
        var restaurant = dataBuilder.saveRestaurant();
        var user = dataBuilder.saveUser();
        dataBuilder.saveVote(user, restaurant);
        assertDoesNotThrow(() -> service.getChosen());
    }

    @Test
    void getAll() {
        dataBuilder.saveRestaurant();
        var pageable = PageRequest.of(0, 20, Sort.by(new Sort.Order(Sort.Direction.ASC, "id")));
        assertFalse(service.getAll(pageable).isEmpty());
    }

    @Test
    void delete() {
        var restaurant = dataBuilder.saveRestaurant();
        service.delete(restaurant.getId());
        assertThrows(NoSuchElementException.class, () -> service.getById(restaurant.getId()));
    }
}