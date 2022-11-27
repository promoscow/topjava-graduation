package ru.xpendence.topjavagraduation.controller.impl.admin;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.xpendence.topjavagraduation.controller.AbstractControllerTest;
import ru.xpendence.topjavagraduation.controller.model.request.RestaurantRequest;
import ru.xpendence.topjavagraduation.entity.Restaurant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestaurantControllerAdminTest extends AbstractControllerTest {

    @Test
    void create() throws Exception {
        var restaurant = dataBuilder.buildRestaurant();
        mockMvc.perform(
                post("/admin/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toRequest(restaurant)))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();
    }

    @Test
    void update() throws Exception {
        var restaurant = dataBuilder.saveRestaurant();
        var name = RandomStringUtils.randomAlphabetic(16);
        restaurant.setName(name);
        mockMvc.perform(
                put("/admin/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toRequest(restaurant)))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

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

    private RestaurantRequest toRequest(Restaurant restaurant) {
        return new RestaurantRequest(
                restaurant.getId(),
                restaurant.getName()
        );
    }
}