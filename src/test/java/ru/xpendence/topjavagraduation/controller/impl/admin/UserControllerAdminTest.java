package ru.xpendence.topjavagraduation.controller.impl.admin;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import ru.xpendence.topjavagraduation.controller.AbstractControllerTest;
import ru.xpendence.topjavagraduation.controller.model.request.UserCreateRequest;
import ru.xpendence.topjavagraduation.controller.model.request.UserUpdateRequest;
import ru.xpendence.topjavagraduation.entity.type.RoleType;
import ru.xpendence.topjavagraduation.service.UserService;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerAdminTest extends AbstractControllerTest {

    private static final Long ADMIN_ROLE_ID = 2L;

    @Autowired
    private UserService userService;

    @Test
    void create() throws Exception {
        var request = new UserCreateRequest(
                RandomStringUtils.secure().nextAlphanumeric(16),
                RandomStringUtils.secure().nextAlphanumeric(16)
        );

        mockMvc.perform(
                        post("/admin/users")
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
    void update() throws Exception {
        var user = userService.create(dataBuilder.buildUser());
        var request = new UserUpdateRequest(
                user.getId(),
                RandomStringUtils.secure().nextAlphanumeric(16),
                RandomStringUtils.secure().nextAlphanumeric(16)
        );

        mockMvc.perform(
                        put("/admin/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(get("/admin/users/{id}", user.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value(request.username()))
                .andReturn();
    }

    @Test
    void getById() throws Exception {
        var user = dataBuilder.saveUser();

        mockMvc.perform(get("/admin/users/{id}", user.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.userName").value(user.getUsername()))
                .andReturn();
    }

    @Test
    void getByUsername() throws Exception {
        var user = dataBuilder.saveUser();

        mockMvc.perform(get("/admin/users/username/{username}", user.getUsername()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.userName").value(user.getUsername()))
                .andReturn();
    }

    @Test
    void getAll() throws Exception {
        var user = dataBuilder.saveUser();

        mockMvc.perform(
                        get("/admin/users/all")
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
    void addRole() throws Exception {
        var user = dataBuilder.saveUser();

        mockMvc.perform(put("/admin/users/{id}/roles/{roleId}", user.getId(), ADMIN_ROLE_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(
                userService.getById(user.getId())
                        .getRoles()
                        .stream()
                        .anyMatch(role -> RoleType.ADMIN == role.getName())
        );
    }

    @Test
    void removeRole() throws Exception {
        var user = dataBuilder.saveUser();
        userService.addRole(user.getId(), ADMIN_ROLE_ID);

        mockMvc.perform(delete("/admin/users/{id}/roles/{roleId}", user.getId(), ADMIN_ROLE_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(
                userService.getById(user.getId())
                        .getRoles()
                        .stream()
                        .noneMatch(role -> RoleType.ADMIN == role.getName())
        );
    }

    @Test
    void deleteById() throws Exception {
        var user = dataBuilder.saveUser();

        mockMvc.perform(delete("/admin/users/{id}", user.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(get("/admin/users/{id}", user.getId()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
