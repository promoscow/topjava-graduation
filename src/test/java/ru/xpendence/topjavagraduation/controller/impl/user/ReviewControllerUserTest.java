package ru.xpendence.topjavagraduation.controller.impl.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.xpendence.topjavagraduation.controller.AbstractControllerTest;
import ru.xpendence.topjavagraduation.controller.model.request.ReviewCreateRequest;
import ru.xpendence.topjavagraduation.controller.model.request.ReviewUpdateRequest;
import ru.xpendence.topjavagraduation.entity.Restaurant;
import ru.xpendence.topjavagraduation.entity.User;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReviewControllerUserTest extends AbstractControllerTest {

    private User user;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        user = dataBuilder.saveUser();
        restaurant = dataBuilder.saveRestaurant();
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(
                        post("/user/reviews")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(
                                        new ReviewCreateRequest(user.getId(), restaurant.getId(), 5, "Отлично")
                                ))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.text").value("Отлично"))
                .andReturn();
    }

    @Test
    void update() throws Exception {
        var review = dataBuilder.saveReview(user, restaurant, 3, "Средне");
        mockMvc.perform(
                        put("/user/reviews")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(
                                        new ReviewUpdateRequest(review.getId(), 4, "Лучше")
                                ))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void getById() throws Exception {
        var review = dataBuilder.saveReview(user, restaurant);
        mockMvc.perform(get("/user/reviews/{id}", review.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(review.getId()))
                .andReturn();
    }

    @Test
    void getByUserIdAndRestaurantId() throws Exception {
        var review = dataBuilder.saveReview(user, restaurant);
        mockMvc.perform(
                        get("/user/reviews/user/{userId}/restaurant/{restaurantId}", user.getId(), restaurant.getId())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(review.getId()))
                .andReturn();
    }

    @Test
    void getAllByRestaurantId() throws Exception {
        dataBuilder.saveReview(user, restaurant);
        mockMvc.perform(get("/user/reviews/restaurant/{restaurantId}", restaurant.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andReturn();
    }

    @Test
    void createThrowsMethodArgumentNotValidException() throws Exception {
        mockMvc.perform(
                        post("/user/reviews")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(
                                        new ReviewCreateRequest(user.getId(), restaurant.getId(), 6, "Плохая оценка")
                                ))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}
