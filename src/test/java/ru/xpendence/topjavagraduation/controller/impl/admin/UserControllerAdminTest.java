package ru.xpendence.topjavagraduation.controller.impl.admin;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import ru.xpendence.topjavagraduation.controller.AbstractControllerTest;
import ru.xpendence.topjavagraduation.controller.model.request.UserCreateRequest;
import ru.xpendence.topjavagraduation.controller.model.request.UserUpdateRequest;
import ru.xpendence.topjavagraduation.entity.type.RoleType;
import ru.xpendence.topjavagraduation.service.UserService;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.xpendence.topjavagraduation.controller.JwtUserRequestPostProcessors.admin;

class UserControllerAdminTest extends AbstractControllerTest {

    private static final Long ADMIN_ROLE_ID = 2L;

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("create(): валидный запрос -> успешное создание пользователя")
    void create() throws Exception {
        var request = new UserCreateRequest(
                RandomStringUtils.secure().nextAlphanumeric(16),
                RandomStringUtils.secure().nextAlphanumeric(16)
        );

        mockMvc.perform(
                        post("/admin/users")
                                .with(admin())
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
    @DisplayName("update(): корректные данные -> успешное обновление")
    void update() throws Exception {
        var user = userService.create(dataBuilder.buildUser());
        var request = new UserUpdateRequest(
                user.getId(),
                RandomStringUtils.secure().nextAlphanumeric(16),
                RandomStringUtils.secure().nextAlphanumeric(16)
        );

        mockMvc.perform(
                        put("/admin/users")
                                .with(admin())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(get("/admin/users/{id}", user.getId()).with(admin()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value(request.username()))
                .andReturn();
    }

    @Test
    @DisplayName("get(): существующий id -> успешное получение пользователя")
    void getById() throws Exception {
        var user = dataBuilder.saveUser();

        mockMvc.perform(get("/admin/users/{id}", user.getId()).with(admin()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.userName").value(user.getUsername()))
                .andReturn();
    }

    @Test
    @DisplayName("getByUsername(): существующий username -> успешное получение пользователя")
    void getByUsername() throws Exception {
        var user = dataBuilder.saveUser();

        mockMvc.perform(get("/admin/users/username/{username}", user.getUsername()).with(admin()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.userName").value(user.getUsername()))
                .andReturn();
    }

    @Test
    @DisplayName("getAll(): фильтр по username -> страница с пользователем")
    void getAll() throws Exception {
        var user = dataBuilder.saveUser();

        mockMvc.perform(
                        get("/admin/users/all")
                                .with(admin())
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
    @DisplayName("addRole(): существующие user и role -> роль добавлена")
    void addRole() throws Exception {
        var user = dataBuilder.saveUser();

        mockMvc.perform(put("/admin/users/{id}/roles/{roleId}", user.getId(), ADMIN_ROLE_ID).with(admin()))
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
    @DisplayName("removeRole(): пользователь с ролью -> роль удалена")
    void removeRole() throws Exception {
        var user = dataBuilder.saveUser();
        userService.addRole(user.getId(), ADMIN_ROLE_ID);

        mockMvc.perform(delete("/admin/users/{id}/roles/{roleId}", user.getId(), ADMIN_ROLE_ID).with(admin()))
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
    @DisplayName("delete(): существующий id -> пользователь удалён")
    void deleteById() throws Exception {
        var user = dataBuilder.saveUser();

        mockMvc.perform(delete("/admin/users/{id}", user.getId()).with(admin()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(get("/admin/users/{id}", user.getId()).with(admin()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
