package ru.xpendence.topjavagraduation.controller.impl.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.xpendence.topjavagraduation.controller.AbstractControllerTest;
import ru.xpendence.topjavagraduation.controller.model.request.ReviewCreateRequest;
import ru.xpendence.topjavagraduation.controller.model.request.ReviewUpdateRequest;
import ru.xpendence.topjavagraduation.entity.Restaurant;
import ru.xpendence.topjavagraduation.entity.User;
import ru.xpendence.topjavagraduation.entity.type.RoleType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.xpendence.topjavagraduation.controller.JwtUserRequestPostProcessors.jwtUser;

class ReviewControllerUserTest extends AbstractControllerTest {

    private User reviewer;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        reviewer = dataBuilder.saveUser();
        restaurant = dataBuilder.saveRestaurant();
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(
                        post("/user/reviews")
                                .with(jwtUser(reviewer, RoleType.USER.name()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(
                                        new ReviewCreateRequest(restaurant.getId(), 5, "Отлично")
                                ))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.userId").value(reviewer.getId()))
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.text").value("Отлично"))
                .andReturn();
    }

    @Test
    void update() throws Exception {
        var review = dataBuilder.saveReview(reviewer, restaurant, 3, "Средне");
        mockMvc.perform(
                        put("/user/reviews")
                                .with(jwtUser(reviewer, RoleType.USER.name()))
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
    void updateFailsWhenReviewBelongsToAnotherUser() throws Exception {
        var review = dataBuilder.saveReview(reviewer, restaurant, 3, "Средне");
        var anotherUser = dataBuilder.saveUser();
        mockMvc.perform(
                        put("/user/reviews")
                                .with(jwtUser(anotherUser, RoleType.USER.name()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(
                                        new ReviewUpdateRequest(review.getId(), 4, "Чужой")
                                ))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void getById() throws Exception {
        var review = dataBuilder.saveReview(reviewer, restaurant);
        mockMvc.perform(
                        get("/user/reviews/{id}", review.getId())
                                .with(jwtUser(reviewer, RoleType.USER.name()))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(review.getId()))
                .andReturn();
    }

    @Test
    void getByUserIdAndRestaurantId() throws Exception {
        var review = dataBuilder.saveReview(reviewer, restaurant);
        mockMvc.perform(
                        get("/user/reviews/me/restaurant/{restaurantId}", restaurant.getId())
                                .with(jwtUser(reviewer, RoleType.USER.name()))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(review.getId()))
                .andReturn();
    }

    @Test
    void getAllByRestaurantId() throws Exception {
        dataBuilder.saveReview(reviewer, restaurant);
        mockMvc.perform(
                        get("/user/reviews/restaurant/{restaurantId}", restaurant.getId())
                                .with(jwtUser(reviewer, RoleType.USER.name()))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andReturn();
    }

    @Test
    void createThrowsMethodArgumentNotValidException() throws Exception {
        mockMvc.perform(
                        post("/user/reviews")
                                .with(jwtUser(reviewer, RoleType.USER.name()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(
                                        new ReviewCreateRequest(restaurant.getId(), 6, "Плохая оценка")
                                ))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void createReturnsUnauthorizedWhenAnonymous() throws Exception {
        mockMvc.perform(
                        post("/user/reviews")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(
                                        new ReviewCreateRequest(restaurant.getId(), 5, "Отлично")
                                ))
                )
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andReturn();
    }
}
