package ru.xpendence.topjavagraduation.controller.rest.admin;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.xpendence.topjavagraduation.controller.AbstractControllerTest;
import ru.xpendence.topjavagraduation.controller.model.request.RestaurantRequest;
import ru.xpendence.topjavagraduation.entity.Restaurant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.xpendence.topjavagraduation.controller.JwtUserRequestPostProcessors.admin;

class RestaurantControllerAdminTest extends AbstractControllerTest {

    @Test
    @DisplayName("create(): валидный запрос -> успешное создание ресторана")
    void create() throws Exception {
        var restaurant = dataBuilder.buildRestaurant();
        mockMvc.perform(
                post("/admin/restaurants")
                        .with(admin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toRequest(restaurant)))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();
    }

    @Test
    @DisplayName("update(): корректные данные -> успешное обновление")
    void update() throws Exception {
        var restaurant = dataBuilder.saveRestaurant();
        var name = RandomStringUtils.secure().nextAlphanumeric(16);
        restaurant = restaurant.copy(restaurant.getId(), name);
        mockMvc.perform(
                put("/admin/restaurants")
                        .with(admin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toRequest(restaurant)))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("get(): существующий id -> успешное получение ресторана")
    void getById() throws Exception {
        var restaurant = dataBuilder.saveRestaurant();
        mockMvc.perform(
                get("/admin/restaurants/{id}", restaurant.getId())
        .with(admin())
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(restaurant.getId()))
                .andReturn();
    }

    @Test
    @DisplayName("getAll(): рестораны в БД -> непустая страница")
    void getAll() throws Exception {
        dataBuilder.saveRestaurant();
        mockMvc.perform(
                get("/admin/restaurants/all")
        .with(admin())
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
