package ru.xpendence.topjavagraduation.controller.rest.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("create(): валидный запрос -> успешное создание отзыва")
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
    @DisplayName("update(): владелец отзыва -> успешное обновление")
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
    @DisplayName("update(): чужой пользователь -> 400 Bad Request")
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
    @DisplayName("get(): существующий id -> успешное получение отзыва")
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
    @DisplayName("getByUserIdAndRestaurantId(): отзыв есть -> успешное получение")
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
    @DisplayName("getAllByRestaurantId(): отзывы есть -> непустая страница")
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
    @DisplayName("create(): невалидный rating -> 400 Bad Request")
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
    @DisplayName("create(): анонимный запрос -> 401 Unauthorized")
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
