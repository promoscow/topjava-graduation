package ru.xpendence.topjavagraduation.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.xpendence.topjavagraduation.AbstractTest;
import ru.xpendence.topjavagraduation.entity.Restaurant;
import ru.xpendence.topjavagraduation.entity.User;
import ru.xpendence.topjavagraduation.service.VoteService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VoteServiceTest extends AbstractTest {

    @Autowired
    private VoteService service;

    private final LocalTime VOTING_AVAILABLE_UNTIL = LocalTime.of(11, 0);

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
        var now = LocalTime.now();
        if (now.isBefore(VOTING_AVAILABLE_UNTIL)) {
            assertNotNull(service.create(vote).getId());
        } else {
            assertThrows(IllegalArgumentException.class, () -> service.create(vote));
        }
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

    @Test
    void getByUserId() {
        var vote = dataBuilder.saveVote(user, restaurant);
        assertEquals(vote.getId(), service.getByUserId(vote.getUser().getId(), LocalDate.now()).getId());
    }

    @Test
    void getByUserIdReturnsVoteForSpecifiedDateWhenUserHasMultipleVotes() {
        var yesterday = LocalDate.now().minusDays(1);
        var todayVote = dataBuilder.saveVote(user, restaurant, LocalDate.now());
        var yesterdayVote = dataBuilder.saveVote(user, restaurant, yesterday);

        assertEquals(todayVote.getId(), service.getByUserId(user.getId(), LocalDate.now()).getId());
        assertEquals(yesterdayVote.getId(), service.getByUserId(user.getId(), yesterday).getId());
    }

    @Test
    void getByUserIdThrowsWhenVoteForDateNotFound() {
        dataBuilder.saveVote(user, restaurant, LocalDate.now());
        assertThrows(
                NoSuchElementException.class,
                () -> service.getByUserId(user.getId(), LocalDate.now().minusDays(1))
        );
    }
}