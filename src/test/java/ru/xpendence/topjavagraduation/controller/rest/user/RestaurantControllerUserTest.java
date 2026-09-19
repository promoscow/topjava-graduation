package ru.xpendence.topjavagraduation.controller.rest.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.xpendence.topjavagraduation.controller.AbstractControllerTest;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.xpendence.topjavagraduation.controller.JwtUserRequestPostProcessors.user;

class RestaurantControllerUserTest extends AbstractControllerTest {

    @Test
    @DisplayName("get(): существующий id -> успешное получение ресторана")
    void getById() throws Exception {
        var restaurant = dataBuilder.saveRestaurant();
        mockMvc.perform(
                        get("/user/restaurants/{id}", restaurant.getId())
                                .with(user())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(restaurant.getId()))
                .andReturn();
    }

    @Test
    @DisplayName("get(): есть активные и неактивные блюда -> только активные в ответе")
    void getByIdReturnsOnlyActiveDishes() throws Exception {
        var restaurant = dataBuilder.saveRestaurant();
        var active = dataBuilder.saveDish(restaurant, true);
        dataBuilder.saveDish(restaurant, false);
        mockMvc.perform(
                        get("/user/restaurants/{id}", restaurant.getId())
                                .with(user())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dishes.length()").value(1))
                .andExpect(jsonPath("$.dishes[0].id").value(active.getId()))
                .andReturn();
    }

    @Test
    @DisplayName("getChosen(): один ресторан с голосами сегодня -> возвращает его")
    void getChosen() throws Exception {
        dataBuilder.clearVotes();
        var restaurant = dataBuilder.saveRestaurant();
        dataBuilder.saveVote(dataBuilder.saveUser(), restaurant);
        mockMvc.perform(get("/user/restaurants/chosen").with(user()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(restaurant.getId()))
                .andReturn();
    }

    @Test
    @DisplayName("getChosen(): больше голосов вчера, один сегодня -> ресторан с голосами сегодня")
    void getChosenReturnsRestaurantWithMostVotesToday() throws Exception {
        dataBuilder.clearVotes();
        var yesterdayLeader = dataBuilder.saveRestaurant();
        var todayLeader = dataBuilder.saveRestaurant();
        var yesterday = LocalDate.now().minusDays(1);

        for (int i = 0; i < 5; i++) {
            dataBuilder.saveVote(dataBuilder.saveUser(), yesterdayLeader, yesterday);
        }
        dataBuilder.saveVote(dataBuilder.saveUser(), todayLeader, LocalDate.now());

        mockMvc.perform(get("/user/restaurants/chosen").with(user()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(todayLeader.getId()))
                .andReturn();
    }

    @Test
    @DisplayName("getChosen(): разное число голосов сегодня -> ресторан с максимумом")
    void getChosenReturnsRestaurantWithHigherTodayVoteCount() throws Exception {
        dataBuilder.clearVotes();
        var fewerVotesToday = dataBuilder.saveRestaurant();
        var moreVotesToday = dataBuilder.saveRestaurant();
        var today = LocalDate.now();

        dataBuilder.saveVote(dataBuilder.saveUser(), fewerVotesToday, today);
        for (int i = 0; i < 3; i++) {
            dataBuilder.saveVote(dataBuilder.saveUser(), moreVotesToday, today);
        }

        mockMvc.perform(get("/user/restaurants/chosen").with(user()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(moreVotesToday.getId()))
                .andReturn();
    }

    @Test
    @DisplayName("getChosen(): голоса только за вчера -> 404 Not Found")
    void getChosenReturnsNotFoundWhenNoVotesToday() throws Exception {
        dataBuilder.clearVotes();
        var restaurant = dataBuilder.saveRestaurant();
        dataBuilder.saveVote(dataBuilder.saveUser(), restaurant, LocalDate.now().minusDays(1));

        mockMvc.perform(get("/user/restaurants/chosen").with(user()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @DisplayName("getChosen(): голосов нет -> 404 Not Found")
    void getChosenReturnsNotFoundWhenNoVotesAtAll() throws Exception {
        dataBuilder.clearVotes();
        dataBuilder.saveRestaurant();

        mockMvc.perform(get("/user/restaurants/chosen").with(user()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @DisplayName("getAll(): рестораны в БД -> непустая страница")
    void getAll() throws Exception {
        dataBuilder.saveRestaurant();
        mockMvc.perform(
                        get("/user/restaurants/all")
                                .with(user())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andReturn();
    }
}
