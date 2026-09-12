package ru.xpendence.topjavagraduation.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.xpendence.topjavagraduation.AbstractTest;
import ru.xpendence.topjavagraduation.service.RestaurantService;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantServiceTest extends AbstractTest {

    @Autowired
    private RestaurantService service;

    @Test
    @DisplayName("create(): валидный ресторан -> успешное создание")
    void create() {
        var restaurant = dataBuilder.buildRestaurant();
        assertNotNull(service.create(restaurant).getId());
    }

    @Test
    @DisplayName("update(): корректные данные -> успешное обновление")
    void update() {
        var restaurant = dataBuilder.saveRestaurant();
        var newName = RandomStringUtils.secure().nextAlphanumeric(16);
        restaurant.setName(newName);
        service.update(restaurant);
        assertEquals(newName, service.getById(restaurant.getId()).getName());
    }

    @Test
    @DisplayName("getById(): существующий id -> успешное получение ресторана")
    void get() {
        var restaurant = dataBuilder.saveRestaurant();
        assertDoesNotThrow(() -> service.getById(restaurant.getId()));
    }

    @Test
    @DisplayName("getByDishId(): существующий dishId -> успешное получение ресторана")
    void getByDishId() {
        var restaurant = dataBuilder.saveRestaurant();
        var dish = dataBuilder.saveDish(restaurant);
        assertDoesNotThrow(() -> service.getByDishId(dish.getId()));
    }

    @Test
    @DisplayName("getChosen(): один ресторан с голосами сегодня -> возвращает его")
    void getChosen() {
        dataBuilder.clearVotes();
        var restaurant = dataBuilder.saveRestaurant();
        dataBuilder.saveVote(dataBuilder.saveUser(), restaurant);
        assertEquals(restaurant.getId(), service.getChosen().getId());
    }

    @Test
    @DisplayName("getChosen(): больше голосов вчера, один сегодня -> ресторан с голосами сегодня")
    void getChosenReturnsRestaurantWithMostVotesToday() {
        dataBuilder.clearVotes();
        var yesterdayLeader = dataBuilder.saveRestaurant();
        var todayLeader = dataBuilder.saveRestaurant();
        var yesterday = LocalDate.now().minusDays(1);

        for (int i = 0; i < 5; i++) {
            dataBuilder.saveVote(dataBuilder.saveUser(), yesterdayLeader, yesterday);
        }
        dataBuilder.saveVote(dataBuilder.saveUser(), todayLeader, LocalDate.now());

        assertEquals(todayLeader.getId(), service.getChosen().getId());
    }

    @Test
    @DisplayName("getChosen(): разное число голосов сегодня -> ресторан с максимумом")
    void getChosenReturnsRestaurantWithHigherTodayVoteCount() {
        dataBuilder.clearVotes();
        var fewerVotesToday = dataBuilder.saveRestaurant();
        var moreVotesToday = dataBuilder.saveRestaurant();
        var today = LocalDate.now();

        dataBuilder.saveVote(dataBuilder.saveUser(), fewerVotesToday, today);
        for (int i = 0; i < 3; i++) {
            dataBuilder.saveVote(dataBuilder.saveUser(), moreVotesToday, today);
        }

        assertEquals(moreVotesToday.getId(), service.getChosen().getId());
    }

    @Test
    @DisplayName("getChosen(): голоса только за вчера -> NoSuchElementException")
    void getChosenFailsWhenNoVotesToday() {
        dataBuilder.clearVotes();
        var restaurant = dataBuilder.saveRestaurant();
        dataBuilder.saveVote(dataBuilder.saveUser(), restaurant, LocalDate.now().minusDays(1));

        assertThrows(NoSuchElementException.class, () -> service.getChosen());
    }

    @Test
    @DisplayName("getChosen(): голосов нет -> NoSuchElementException")
    void getChosenFailsWhenNoVotesAtAll() {
        dataBuilder.clearVotes();
        dataBuilder.saveRestaurant();

        assertThrows(NoSuchElementException.class, () -> service.getChosen());
    }

    @Test
    @DisplayName("getAll(): рестораны в БД -> непустая страница")
    void getAll() {
        dataBuilder.saveRestaurant();
        var pageable = PageRequest.of(0, 20, Sort.by(new Sort.Order(Sort.Direction.ASC, "id")));
        assertFalse(service.getAll(pageable).isEmpty());
    }

    @Test
    @DisplayName("delete(): существующий id -> ресторан удалён")
    void delete() {
        var restaurant = dataBuilder.saveRestaurant();
        service.delete(restaurant.getId());
        assertThrows(NoSuchElementException.class, () -> service.getById(restaurant.getId()));
    }
}
