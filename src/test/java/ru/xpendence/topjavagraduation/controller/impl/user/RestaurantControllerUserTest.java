package ru.xpendence.topjavagraduation.controller.impl.user;

import org.junit.jupiter.api.Test;
import ru.xpendence.topjavagraduation.controller.AbstractControllerTest;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestaurantControllerUserTest extends AbstractControllerTest {

    @Test
    void getById() throws Exception {
        var restaurant = dataBuilder.saveRestaurant();
        mockMvc.perform(
                        get("/user/restaurants/{id}", restaurant.getId())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(restaurant.getId()))
                .andReturn();
    }

    @Test
    void getByIdReturnsOnlyActiveDishes() throws Exception {
        var restaurant = dataBuilder.saveRestaurant();
        var active = dataBuilder.saveDish(restaurant, true);
        dataBuilder.saveDish(restaurant, false);
        mockMvc.perform(
                        get("/user/restaurants/{id}", restaurant.getId())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dishes.length()").value(1))
                .andExpect(jsonPath("$.dishes[0].id").value(active.getId()))
                .andReturn();
    }

    @Test
    void getChosen() throws Exception {
        dataBuilder.clearVotes();
        var restaurant = dataBuilder.saveRestaurant();
        dataBuilder.saveVote(dataBuilder.saveUser(), restaurant);
        mockMvc.perform(get("/user/restaurants/chosen"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(restaurant.getId()))
                .andReturn();
    }

    @Test
    void getChosenReturnsRestaurantWithMostVotesToday() throws Exception {
        dataBuilder.clearVotes();
        var yesterdayLeader = dataBuilder.saveRestaurant();
        var todayLeader = dataBuilder.saveRestaurant();
        var yesterday = LocalDate.now().minusDays(1);

        for (int i = 0; i < 5; i++) {
            dataBuilder.saveVote(dataBuilder.saveUser(), yesterdayLeader, yesterday);
        }
        dataBuilder.saveVote(dataBuilder.saveUser(), todayLeader, LocalDate.now());

        mockMvc.perform(get("/user/restaurants/chosen"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(todayLeader.getId()))
                .andReturn();
    }

    @Test
    void getChosenReturnsRestaurantWithHigherTodayVoteCount() throws Exception {
        dataBuilder.clearVotes();
        var fewerVotesToday = dataBuilder.saveRestaurant();
        var moreVotesToday = dataBuilder.saveRestaurant();
        var today = LocalDate.now();

        dataBuilder.saveVote(dataBuilder.saveUser(), fewerVotesToday, today);
        for (int i = 0; i < 3; i++) {
            dataBuilder.saveVote(dataBuilder.saveUser(), moreVotesToday, today);
        }

        mockMvc.perform(get("/user/restaurants/chosen"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(moreVotesToday.getId()))
                .andReturn();
    }

    @Test
    void getChosenReturnsNotFoundWhenNoVotesToday() throws Exception {
        dataBuilder.clearVotes();
        var restaurant = dataBuilder.saveRestaurant();
        dataBuilder.saveVote(dataBuilder.saveUser(), restaurant, LocalDate.now().minusDays(1));

        mockMvc.perform(get("/user/restaurants/chosen"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void getChosenReturnsNotFoundWhenNoVotesAtAll() throws Exception {
        dataBuilder.clearVotes();
        dataBuilder.saveRestaurant();

        mockMvc.perform(get("/user/restaurants/chosen"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void getAll() throws Exception {
        dataBuilder.saveRestaurant();
        mockMvc.perform(
                        get("/user/restaurants/all")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andReturn();
    }
}