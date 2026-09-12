package ru.xpendence.topjavagraduation.controller.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import ru.xpendence.topjavagraduation.controller.AbstractControllerTest;
import ru.xpendence.topjavagraduation.controller.model.request.TagCreateRequest;
import ru.xpendence.topjavagraduation.controller.model.request.TagUpdateRequest;
import ru.xpendence.topjavagraduation.service.TagService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.xpendence.topjavagraduation.controller.JwtUserRequestPostProcessors.user;

class TagControllerTest extends AbstractControllerTest {

    @Autowired
    private TagService tagService;

    @Test
    @DisplayName("create(): валидный запрос -> успешное создание тега")
    void create() throws Exception {
        var request = new TagCreateRequest(RandomStringUtils.secure().nextAlphanumeric(16));

        mockMvc.perform(
                        post("/tags")
                                .with(user())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(request.name()))
                .andReturn();
    }

    @Test
    @DisplayName("create(): невалидный запрос -> 400 Bad Request")
    void createFailsWhenRequestInvalid() throws Exception {
        var request = new TagCreateRequest("");

        mockMvc.perform(
                        post("/tags")
                                .with(user())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @DisplayName("create(): занятое имя -> 400 Bad Request")
    void createFailsWhenNameTaken() throws Exception {
        var existing = dataBuilder.saveTag();
        var request = new TagCreateRequest(existing.getName());

        mockMvc.perform(
                        post("/tags")
                                .with(user())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @DisplayName("update(): корректные данные -> успешное обновление")
    void update() throws Exception {
        var tag = tagService.create(dataBuilder.buildTag());
        var request = new TagUpdateRequest(
                tag.getId(),
                RandomStringUtils.secure().nextAlphanumeric(16)
        );

        mockMvc.perform(
                        put("/tags")
                                .with(user())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(get("/tags/{id}", tag.getId()).with(user()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(request.name()))
                .andReturn();
    }

    @Test
    @DisplayName("update(): несуществующий тег -> 404 Not Found")
    void updateFailsWhenTagNotFound() throws Exception {
        var request = new TagUpdateRequest(
                Long.MAX_VALUE,
                RandomStringUtils.secure().nextAlphanumeric(16)
        );

        mockMvc.perform(
                        put("/tags")
                                .with(user())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @DisplayName("get(): существующий id -> успешное получение тега")
    void getById() throws Exception {
        var tag = dataBuilder.saveTag();

        mockMvc.perform(get("/tags/{id}", tag.getId()).with(user()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(tag.getId()))
                .andExpect(jsonPath("$.name").value(tag.getName()))
                .andReturn();
    }

    @Test
    @DisplayName("get(): несуществующий id -> 404 Not Found")
    void getByIdFailsWhenNotFound() throws Exception {
        mockMvc.perform(get("/tags/{id}", Long.MAX_VALUE).with(user()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @DisplayName("getAll(): фильтр по имени -> страница с тегом")
    void getAll() throws Exception {
        var tag = dataBuilder.saveTag();

        mockMvc.perform(
                        get("/tags/all")
                                .with(user())
                                .queryParam("name", tag.getName())
                                .queryParam("page", "0")
                                .queryParam("size", "20")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content[0].id").value(tag.getId()))
                .andReturn();
    }

    @Test
    @DisplayName("getAll(): фильтр без совпадений -> пустая страница")
    void getAllReturnsEmptyWhenFilterDoesNotMatch() throws Exception {
        dataBuilder.saveTag();

        mockMvc.perform(
                        get("/tags/all")
                                .with(user())
                                .queryParam("name", "zzz-no-match-zzz")
                                .queryParam("page", "0")
                                .queryParam("size", "20")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andReturn();
    }

    @Test
    @DisplayName("delete(): существующий id -> тег удалён")
    void deleteById() throws Exception {
        var tag = dataBuilder.saveTag();

        mockMvc.perform(delete("/tags/{id}", tag.getId()).with(user()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(get("/tags/{id}", tag.getId()).with(user()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
