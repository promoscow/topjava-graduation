package ru.xpendence.topjavagraduation.controller.impl.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.xpendence.topjavagraduation.controller.AbstractControllerTest;
import ru.xpendence.topjavagraduation.entity.Restaurant;
import ru.xpendence.topjavagraduation.entity.User;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.xpendence.topjavagraduation.controller.JwtUserRequestPostProcessors.admin;

class ReviewControllerAdminTest extends AbstractControllerTest {

    private User user;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        user = dataBuilder.saveUser();
        restaurant = dataBuilder.saveRestaurant();
    }

    @Test
    void getById() throws Exception {
        var review = dataBuilder.saveReview(user, restaurant);
        mockMvc.perform(get("/admin/reviews/{id}", review.getId()).with(admin()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(review.getId()))
                .andReturn();
    }

    @Test
    void getAllByRestaurantId() throws Exception {
        dataBuilder.saveReview(user, restaurant);
        mockMvc.perform(get("/admin/reviews/restaurant/{restaurantId}", restaurant.getId()).with(admin()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andReturn();
    }

    @Test
    void deleteReview() throws Exception {
        var review = dataBuilder.saveReview(user, restaurant);
        mockMvc.perform(delete("/admin/reviews/{id}", review.getId()).with(admin()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(get("/admin/reviews/{id}", review.getId()).with(admin()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
