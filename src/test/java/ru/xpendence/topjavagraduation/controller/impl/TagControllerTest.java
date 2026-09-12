package ru.xpendence.topjavagraduation.controller.impl;

import org.apache.commons.lang3.RandomStringUtils;
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
    void getByIdFailsWhenNotFound() throws Exception {
        mockMvc.perform(get("/tags/{id}", Long.MAX_VALUE).with(user()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
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
