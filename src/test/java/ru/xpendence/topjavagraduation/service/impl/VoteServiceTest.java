package ru.xpendence.topjavagraduation.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.xpendence.topjavagraduation.AbstractTest;
import ru.xpendence.topjavagraduation.entity.Restaurant;
import ru.xpendence.topjavagraduation.entity.User;
import ru.xpendence.topjavagraduation.service.VoteService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

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
    @DisplayName("create(): до 11:00 -> успешное создание; после 11:00 -> IllegalArgumentException")
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
    @DisplayName("update(): смена ресторана -> успешное обновление")
    void update() {
        var vote = dataBuilder.saveVote(user, restaurant);
        var newRestaurant = dataBuilder.saveRestaurant();
        vote.setRestaurant(newRestaurant);
        service.update(vote);
        assertEquals(newRestaurant.getId(), service.getById(vote.getId()).getRestaurant().getId());
    }

    @Test
    @DisplayName("getById(): существующий id -> успешное получение голоса")
    void getById() {
        var vote = dataBuilder.saveVote(user, restaurant);
        assertDoesNotThrow(() -> service.getById(vote.getId()));
    }

    @Test
    @DisplayName("getByUserId(): голос на сегодня -> успешное получение")
    void getByUserId() {
        var vote = dataBuilder.saveVote(user, restaurant);
        assertEquals(vote.getId(), service.getByUserId(vote.getUser().getId(), LocalDate.now()).getId());
    }

    @Test
    @DisplayName("getByUserId(): несколько дат -> голос за указанную дату")
    void getByUserIdReturnsVoteForSpecifiedDateWhenUserHasMultipleVotes() {
        var yesterday = LocalDate.now().minusDays(1);
        var todayVote = dataBuilder.saveVote(user, restaurant, LocalDate.now());
        var yesterdayVote = dataBuilder.saveVote(user, restaurant, yesterday);

        assertEquals(todayVote.getId(), service.getByUserId(user.getId(), LocalDate.now()).getId());
        assertEquals(yesterdayVote.getId(), service.getByUserId(user.getId(), yesterday).getId());
    }

    @Test
    @DisplayName("getByUserId(): нет голоса на дату -> NoSuchElementException")
    void getByUserIdThrowsWhenVoteForDateNotFound() {
        dataBuilder.saveVote(user, restaurant, LocalDate.now());
        assertThrows(
                NoSuchElementException.class,
                () -> service.getByUserId(user.getId(), LocalDate.now().minusDays(1))
        );
    }
}
