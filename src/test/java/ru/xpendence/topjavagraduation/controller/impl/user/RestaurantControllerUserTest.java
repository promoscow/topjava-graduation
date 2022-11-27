package ru.xpendence.topjavagraduation.controller.impl.user;

import org.junit.jupiter.api.Test;
import ru.xpendence.topjavagraduation.controller.AbstractControllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestaurantControllerUserTest extends AbstractControllerTest {

    @Test
    void getById() throws Exception {
        var restaurant = dataBuilder.saveRestaurant();
        mockMvc.perform(
                        get("/admin/restaurants/{id}", restaurant.getId())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(restaurant.getId()))
                .andReturn();
    }

    @Test
    void getChosen() throws Exception {
        var restaurant = dataBuilder.saveRestaurant();
        for (int i = 0; i < 20; i++) {
            var user = dataBuilder.saveUser();
            dataBuilder.saveVote(user, restaurant);
        }
        mockMvc.perform(get("/user/restaurants/chosen"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(restaurant.getId()))
                .andReturn();
    }

    @Test
    void getAll() throws Exception {
        dataBuilder.saveRestaurant();
        mockMvc.perform(
                        get("/admin/restaurants/all")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andReturn();
    }
}