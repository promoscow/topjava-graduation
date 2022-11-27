package ru.xpendence.topjavagraduation.controller.impl.admin;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import ru.xpendence.topjavagraduation.controller.AbstractControllerTest;
import ru.xpendence.topjavagraduation.controller.model.request.DishCreateRequest;
import ru.xpendence.topjavagraduation.controller.model.request.DishUpdateRequest;
import ru.xpendence.topjavagraduation.entity.Dish;
import ru.xpendence.topjavagraduation.entity.Restaurant;
import ru.xpendence.topjavagraduation.service.DishService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DishControllerAdminTest extends AbstractControllerTest {

    @Autowired
    private DishService service;

    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        restaurant = dataBuilder.saveRestaurant();
    }

    @Test
    void create() throws Exception {
        var dish = dataBuilder.buildDish(restaurant);
        mockMvc.perform(
                post("/admin/dishes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toCreateRequest(dish)))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();
    }

    @Test
    void update() throws Exception {
        var dish = dataBuilder.saveDish(restaurant);
        mockMvc.perform(
                put("/admin/dishes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toUpdateRequest(dish)))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void resetMenu() throws Exception {
        var dish = dataBuilder.saveDish(restaurant);
        dish.setActive(true);
        service.update(dish);
        mockMvc.perform(
                put("/admin/dishes/reset/restaurant/{restaurantId}", restaurant.getId())
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        assertFalse(service.getById(dish.getId()).getActive());
    }

    @Test
    void getById() throws Exception {
        var dish = dataBuilder.saveDish(restaurant);
        mockMvc.perform(
                        get("/admin/dishes/{id}", dish.getId())
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
                        get("/admin/dishes/all/restaurant/{restaurantId}", restaurant.getId())
                                .queryParam("page", "0")
                                .queryParam("size", "20")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    private DishCreateRequest toCreateRequest(Dish dish) {
        return new DishCreateRequest(
                RandomStringUtils.randomAlphabetic(16),
                BigDecimal.TEN,
                true,
                dish.getRestaurant().getId()
        );
    }

    private DishUpdateRequest toUpdateRequest(Dish dish) {
        return new DishUpdateRequest(
                dish.getId(),
                RandomStringUtils.randomAlphabetic(16),
                BigDecimal.TEN,
                true
        );
    }
}