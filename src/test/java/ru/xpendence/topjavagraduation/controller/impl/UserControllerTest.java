package ru.xpendence.topjavagraduation.controller.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import ru.xpendence.topjavagraduation.controller.AbstractControllerTest;
import ru.xpendence.topjavagraduation.controller.model.request.UserCreateRequest;
import ru.xpendence.topjavagraduation.controller.model.request.UserUpdateRequest;
import ru.xpendence.topjavagraduation.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.xpendence.topjavagraduation.controller.JwtUserRequestPostProcessors.user;

class UserControllerTest extends AbstractControllerTest {

    @Autowired
    private UserService userService;

    @Test
    void create() throws Exception {
        var request = new UserCreateRequest(
                RandomStringUtils.secure().nextAlphanumeric(16),
                RandomStringUtils.secure().nextAlphanumeric(16)
        );

        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.userName").value(request.username()))
                .andExpect(jsonPath("$.roles").isNotEmpty())
                .andReturn();
    }

    @Test
    void createFailsWhenRequestInvalid() throws Exception {
        var request = new UserCreateRequest("ab", "1");

        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void createFailsWhenUsernameTaken() throws Exception {
        var existing = dataBuilder.saveUser();
        var request = new UserCreateRequest(existing.getUsername(), RandomStringUtils.secure().nextAlphanumeric(16));

        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void update() throws Exception {
        var user = userService.create(dataBuilder.buildUser());
        var request = new UserUpdateRequest(
                user.getId(),
                RandomStringUtils.secure().nextAlphanumeric(16),
                RandomStringUtils.secure().nextAlphanumeric(16)
        );

        mockMvc.perform(
                        put("/users")
                                .with(user())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(get("/users/{id}", user.getId()).with(user()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value(request.username()))
                .andReturn();
    }

    @Test
    void updateFailsWhenUserNotFound() throws Exception {
        var request = new UserUpdateRequest(
                Long.MAX_VALUE,
                RandomStringUtils.secure().nextAlphanumeric(16),
                RandomStringUtils.secure().nextAlphanumeric(16)
        );

        mockMvc.perform(
                        put("/users")
                                .with(user())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void getById() throws Exception {
        var user = dataBuilder.saveUser();

        mockMvc.perform(get("/users/{id}", user.getId()).with(user()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.userName").value(user.getUsername()))
                .andReturn();
    }

    @Test
    void getByIdFailsWhenNotFound() throws Exception {
        mockMvc.perform(get("/users/{id}", Long.MAX_VALUE).with(user()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void getAll() throws Exception {
        var user = dataBuilder.saveUser();

        mockMvc.perform(
                        get("/users/all")
                                .with(user())
                                .queryParam("username", user.getUsername())
                                .queryParam("page", "0")
                                .queryParam("size", "20")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content[0].id").value(user.getId()))
                .andReturn();
    }

    @Test
    void getAllReturnsEmptyWhenFilterDoesNotMatch() throws Exception {
        dataBuilder.saveUser();

        mockMvc.perform(
                        get("/users/all")
                                .with(user())
                                .queryParam("username", "zzz-no-match-zzz")
                                .queryParam("page", "0")
                                .queryParam("size", "20")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andReturn();
    }

    @Test
    void deleteById() throws Exception {
        var user = dataBuilder.saveUser();

        mockMvc.perform(delete("/users/{id}", user.getId()).with(user()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(get("/users/{id}", user.getId()).with(user()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
