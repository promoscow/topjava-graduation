package ru.xpendence.topjavagraduation.controller.impl;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.xpendence.topjavagraduation.controller.AbstractControllerTest;
import ru.xpendence.topjavagraduation.controller.model.request.RestaurantRequest;
import ru.xpendence.topjavagraduation.entity.Restaurant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestaurantControllerTest extends AbstractControllerTest {

    @Test
    void create() throws Exception {
        var restaurant = dataBuilder.buildRestaurant();
        mockMvc.perform(
                post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toRequest(restaurant)))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();
    }

    @Test
    void getAll() throws Exception {
        dataBuilder.saveRestaurant();
        mockMvc.perform(
                get("/restaurants/all")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andReturn();
    }

    private RestaurantRequest toRequest(Restaurant restaurant) {
        return new RestaurantRequest(
                null,
                restaurant.getName()
        );
    }
}