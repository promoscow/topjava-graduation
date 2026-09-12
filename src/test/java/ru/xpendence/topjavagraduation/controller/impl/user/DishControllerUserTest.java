package ru.xpendence.topjavagraduation.controller.impl.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.xpendence.topjavagraduation.controller.AbstractControllerTest;
import ru.xpendence.topjavagraduation.entity.Restaurant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.xpendence.topjavagraduation.controller.JwtUserRequestPostProcessors.user;

class DishControllerUserTest extends AbstractControllerTest {

    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        restaurant = dataBuilder.saveRestaurant();
    }

    @Test
    @DisplayName("get(): активное блюдо -> успешное получение")
    void getById() throws Exception {
        var dish = dataBuilder.saveDish(restaurant, true);
        mockMvc.perform(
                        get("/user/dishes/{id}", dish.getId())
                                .with(user())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dish.getId()))
                .andReturn();
    }

    @Test
    @DisplayName("get(): неактивное блюдо -> 404 Not Found")
    void getByIdFailsWhenInactive() throws Exception {
        var dish = dataBuilder.saveDish(restaurant, false);
        mockMvc.perform(
                        get("/user/dishes/{id}", dish.getId())
                                .with(user())
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @DisplayName("getAllByRestaurantId(): есть активные и неактивные -> только активные")
    void getAllByRestaurantIdReturnsOnlyActive() throws Exception {
        var active = dataBuilder.saveDish(restaurant, true);
        dataBuilder.saveDish(restaurant, false);
        mockMvc.perform(
                        get("/user/dishes/all/restaurant/{restaurantId}", restaurant.getId())
                                .with(user())
                                .queryParam("page", "0")
                                .queryParam("size", "20")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(active.getId()))
                .andReturn();
    }
}
