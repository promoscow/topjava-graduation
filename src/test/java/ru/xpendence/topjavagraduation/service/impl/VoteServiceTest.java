package ru.xpendence.topjavagraduation.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.xpendence.topjavagraduation.AbstractTest;
import ru.xpendence.topjavagraduation.entity.Restaurant;
import ru.xpendence.topjavagraduation.entity.User;
import ru.xpendence.topjavagraduation.service.VoteService;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
class VoteServiceTest extends AbstractTest {

    @Autowired
    private VoteService service;

    private User user;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        user = dataBuilder.saveUser();
        restaurant = dataBuilder.saveRestaurant();
    }

    @Test
    void create() {
        var vote = dataBuilder.buildVote(user, restaurant);
        assertNotNull(service.create(vote).getId());
    }

    @Test
    void update() {
        var vote = dataBuilder.saveVote(user, restaurant);
        var newRestaurant = dataBuilder.saveRestaurant();
        vote.setRestaurant(newRestaurant);
        service.update(vote);
        assertEquals(newRestaurant.getId(), service.getById(vote.getId()).getRestaurant().getId());
    }

    @Test
    void getById() {
        var vote = dataBuilder.saveVote(user, restaurant);
        assertDoesNotThrow(() -> service.getById(vote.getId()));
    }
}