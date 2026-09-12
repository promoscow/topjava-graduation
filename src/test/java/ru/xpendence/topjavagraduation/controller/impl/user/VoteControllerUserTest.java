package ru.xpendence.topjavagraduation.controller.impl.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.xpendence.topjavagraduation.controller.AbstractControllerTest;
import ru.xpendence.topjavagraduation.controller.model.request.VoteRequest;
import ru.xpendence.topjavagraduation.entity.Restaurant;
import ru.xpendence.topjavagraduation.entity.User;
import ru.xpendence.topjavagraduation.entity.type.RoleType;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.xpendence.topjavagraduation.controller.JwtUserRequestPostProcessors.*;

class VoteControllerUserTest extends AbstractControllerTest {

    private final LocalTime VOTING_AVAILABLE_UNTIL = LocalTime.of(11, 0);

    private User voter;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        voter = dataBuilder.saveUser();
        restaurant = dataBuilder.saveRestaurant();
    }

    @Test
    @DisplayName("vote(): до 11:00 -> успешное голосование; после 11:00 -> 400 Bad Request")
    void vote() throws Exception {
        if (LocalTime.now().isBefore(VOTING_AVAILABLE_UNTIL)) {
            mockMvc.perform(
                            post("/user/votes")
                                    .with(jwtUser(voter, RoleType.USER.name()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(toRequest(restaurant)))
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.userId").value(voter.getId()))
                    .andReturn();
        } else {
            mockMvc.perform(
                            post("/user/votes")
                                    .with(jwtUser(voter, RoleType.USER.name()))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(toRequest(restaurant)))
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andReturn();
        }
    }

    @Test
    @DisplayName("getByUserId(): голос на сегодня -> успешное получение")
    void getByUserId() throws Exception {
        var vote = dataBuilder.saveVote(voter, restaurant);
        mockMvc.perform(
                        get("/user/votes")
                                .with(jwtUser(voter, RoleType.USER.name()))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(vote.getId()))
                .andReturn();
    }

    @Test
    @DisplayName("getByUserId(): дата не указана -> голос за сегодня")
    void getByUserIdUsesTodayWhenDateNotProvided() throws Exception {
        var yesterday = LocalDate.now().minusDays(1);
        dataBuilder.saveVote(voter, restaurant, yesterday);
        var todayVote = dataBuilder.saveVote(voter, restaurant, LocalDate.now());

        mockMvc.perform(
                        get("/user/votes")
                                .with(jwtUser(voter, RoleType.USER.name()))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(todayVote.getId()))
                .andExpect(jsonPath("$.date").value(LocalDate.now().toString()))
                .andReturn();
    }

    @Test
    @DisplayName("getByUserId(): указана дата -> голос за эту дату")
    void getByUserIdReturnsVoteForSpecifiedDate() throws Exception {
        var yesterday = LocalDate.now().minusDays(1);
        var yesterdayVote = dataBuilder.saveVote(voter, restaurant, yesterday);
        dataBuilder.saveVote(voter, restaurant, LocalDate.now());

        mockMvc.perform(
                        get("/user/votes")
                                .with(jwtUser(voter, RoleType.USER.name()))
                                .param("date", yesterday.toString())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(yesterdayVote.getId()))
                .andExpect(jsonPath("$.date").value(yesterday.toString()))
                .andReturn();
    }

    @Test
    @DisplayName("vote(): невалидный запрос -> 400 Bad Request")
    void voteThrowsMethodArgumentNotValidException() throws Exception {
        mockMvc.perform(
                        post("/user/votes")
                                .with(jwtUser(voter, RoleType.USER.name()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new VoteRequest(null)))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @DisplayName("vote(): анонимный запрос -> 401 Unauthorized")
    void voteReturnsUnauthorizedWhenAnonymous() throws Exception {
        mockMvc.perform(
                        post("/user/votes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(toRequest(restaurant)))
                )
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    @DisplayName("vote(): ADMIN без USER -> 403 Forbidden")
    void voteReturnsForbiddenWhenAdminWithoutUserAuthority() throws Exception {
        mockMvc.perform(
                        post("/user/votes")
                                .with(admin())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(toRequest(restaurant)))
                )
                .andDo(print())
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @DisplayName("getAll(): USER на admin-эндпоинт -> 403 Forbidden")
    void adminEndpointReturnsForbiddenForUser() throws Exception {
        mockMvc.perform(
                        get("/admin/restaurants/all")
                                .with(user())
                )
                .andDo(print())
                .andExpect(status().isForbidden())
                .andReturn();
    }

    private VoteRequest toRequest(Restaurant restaurant) {
        return new VoteRequest(restaurant.getId());
    }
}
