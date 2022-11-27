package ru.xpendence.topjavagraduation.controller.impl.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.xpendence.topjavagraduation.controller.AbstractControllerTest;
import ru.xpendence.topjavagraduation.controller.model.request.VoteRequest;
import ru.xpendence.topjavagraduation.entity.Restaurant;
import ru.xpendence.topjavagraduation.entity.User;

import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VoteControllerUserTest extends AbstractControllerTest {

    private final LocalTime VOTING_AVAILABLE_UNTIL = LocalTime.of(11, 0);

    private User user;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        user = dataBuilder.saveUser();
        restaurant = dataBuilder.saveRestaurant();
    }

    @Test
    void vote() throws Exception {
        if (LocalTime.now().isBefore(VOTING_AVAILABLE_UNTIL)) {
            mockMvc.perform(
                            post("/user/votes")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(toRequest(user, restaurant)))
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").exists())
                    .andReturn();
        } else {
            mockMvc.perform(
                            post("/user/votes")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(toRequest(user, restaurant)))
                    )
                    .andDo(print())
                    .andExpect(status().isInternalServerError())
                    .andReturn();
        }

    }

    @Test
    void getByUserId() throws Exception {
        var vote = dataBuilder.saveVote(user, restaurant);
        mockMvc.perform(
                get("/user/votes/user/{userId}", vote.getUser().getId())
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(vote.getId()))
                .andReturn();
    }

    @Test
    void vote_throwsMethodArgumentNotValidException() throws Exception {
        mockMvc.perform(
                        post("/user/votes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(toRequest(new User(), restaurant)))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    private VoteRequest toRequest(User user, Restaurant restaurant) {
        return new VoteRequest(
                user.getId(),
                restaurant.getId()
        );
    }
}