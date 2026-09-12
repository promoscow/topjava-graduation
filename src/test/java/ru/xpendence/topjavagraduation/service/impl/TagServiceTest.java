package ru.xpendence.topjavagraduation.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import ru.xpendence.topjavagraduation.AbstractTest;
import ru.xpendence.topjavagraduation.service.TagService;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class TagServiceTest extends AbstractTest {

    @Autowired
    private TagService service;

    @Test
    @DisplayName("create(): валидный тег -> успешное создание")
    void create() {
        var tag = dataBuilder.buildTag();
        var created = service.create(tag);

        assertNotNull(created.getId());
        assertEquals(tag.getName(), created.getName());
    }

    @Test
    @DisplayName("create(): занятое имя -> IllegalArgumentException")
    void createFailsWhenNameTaken() {
        var existing = dataBuilder.saveTag();
        var tag = dataBuilder.buildTag();
        tag.setName(existing.getName());

        assertThrows(IllegalArgumentException.class, () -> service.create(tag));
    }

    @Test
    @DisplayName("update(): корректные данные -> успешное обновление")
    void update() {
        var tag = service.create(dataBuilder.buildTag());
        var name = RandomStringUtils.secure().nextAlphanumeric(16);
        tag.setName(name);

        service.update(tag);

        assertEquals(name, service.getById(tag.getId()).getName());
    }

    @Test
    @DisplayName("update(): id == null -> IllegalArgumentException")
    void updateFailsWhenIdIsNull() {
        var tag = dataBuilder.buildTag();

        assertThrows(IllegalArgumentException.class, () -> service.update(tag));
    }

    @Test
    @DisplayName("update(): несуществующий id -> NoSuchElementException")
    void updateFailsWhenNotFound() {
        var tag = dataBuilder.buildTag();
        tag.setId(Long.MAX_VALUE);

        assertThrows(NoSuchElementException.class, () -> service.update(tag));
    }

    @Test
    @DisplayName("getById(): существующий id -> успешное получение тега")
    void get() {
        var tag = dataBuilder.saveTag();
        assertDoesNotThrow(() -> service.getById(tag.getId()));
    }

    @Test
    @DisplayName("getById(): несуществующий id -> NoSuchElementException")
    void getFailsWhenNotFound() {
        assertThrows(NoSuchElementException.class, () -> service.getById(Long.MAX_VALUE));
    }

    @Test
    @DisplayName("getAll(): фильтр по имени -> страница с тегом")
    void getAll() {
        var tag = dataBuilder.saveTag();
        var page = service.getAll(tag.getName().substring(0, 4), PageRequest.of(0, 20));

        assertFalse(page.isEmpty());
        assertTrue(page.getContent().stream().anyMatch(t -> t.getId().equals(tag.getId())));
    }

    @Test
    @DisplayName("getAll(): без фильтра -> непустая страница")
    void getAllWithoutFilter() {
        dataBuilder.saveTag();
        var page = service.getAll(null, PageRequest.of(0, 20));

        assertFalse(page.isEmpty());
    }

    @Test
    @DisplayName("getAll(): фильтр без совпадений -> пустая страница")
    void getAllReturnsEmptyWhenFilterDoesNotMatch() {
        dataBuilder.saveTag();
        var page = service.getAll("zzz-no-match-zzz", PageRequest.of(0, 20));

        assertTrue(page.isEmpty());
    }

    @Test
    @DisplayName("delete(): существующий id -> тег удалён")
    void delete() {
        var tag = dataBuilder.saveTag();
        service.delete(tag.getId());
        assertThrows(NoSuchElementException.class, () -> service.getById(tag.getId()));
    }
}
