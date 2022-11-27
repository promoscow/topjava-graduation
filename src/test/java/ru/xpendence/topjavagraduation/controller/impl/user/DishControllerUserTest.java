package ru.xpendence.topjavagraduation.controller.impl.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.xpendence.topjavagraduation.controller.AbstractControllerTest;
import ru.xpendence.topjavagraduation.entity.Restaurant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DishControllerUserTest extends AbstractControllerTest {

    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        restaurant = dataBuilder.saveRestaurant();
    }

    @Test
    void getById() throws Exception {
        var dish = dataBuilder.saveDish(restaurant);
        mockMvc.perform(
                        get("/user/dishes/{id}", dish.getId())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dish.getId()))
                .andReturn();
    }

    @Test
    void getAllByRestaurantId() throws Exception {
        var dish = dataBuilder.saveDish(restaurant);
        mockMvc.perform(
                        get("/user/dishes/all/restaurant/{restaurantId}", restaurant.getId())
                                .queryParam("page", "0")
                                .queryParam("size", "20")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }
}