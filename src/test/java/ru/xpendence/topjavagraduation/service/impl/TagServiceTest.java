package ru.xpendence.topjavagraduation.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import ru.xpendence.topjavagraduation.AbstractTest;
import ru.xpendence.topjavagraduation.service.TagService;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TagServiceTest extends AbstractTest {

    @Autowired
    private TagService service;

    @Test
    void create() {
        var tag = dataBuilder.buildTag();
        var created = service.create(tag);

        assertNotNull(created.getId());
        assertEquals(tag.getName(), created.getName());
    }

    @Test
    void createFailsWhenNameTaken() {
        var existing = dataBuilder.saveTag();
        var tag = dataBuilder.buildTag();
        tag.setName(existing.getName());

        assertThrows(IllegalArgumentException.class, () -> service.create(tag));
    }

    @Test
    void update() {
        var tag = service.create(dataBuilder.buildTag());
        var name = RandomStringUtils.secure().nextAlphanumeric(16);
        tag.setName(name);

        service.update(tag);

        assertEquals(name, service.getById(tag.getId()).getName());
    }

    @Test
    void updateFailsWhenIdIsNull() {
        var tag = dataBuilder.buildTag();

        assertThrows(IllegalArgumentException.class, () -> service.update(tag));
    }

    @Test
    void updateFailsWhenNotFound() {
        var tag = dataBuilder.buildTag();
        tag.setId(Long.MAX_VALUE);

        assertThrows(NoSuchElementException.class, () -> service.update(tag));
    }

    @Test
    void get() {
        var tag = dataBuilder.saveTag();
        assertDoesNotThrow(() -> service.getById(tag.getId()));
    }

    @Test
    void getFailsWhenNotFound() {
        assertThrows(NoSuchElementException.class, () -> service.getById(Long.MAX_VALUE));
    }

    @Test
    void getAll() {
        var tag = dataBuilder.saveTag();
        var page = service.getAll(tag.getName().substring(0, 4), PageRequest.of(0, 20));

        assertFalse(page.isEmpty());
        assertTrue(page.getContent().stream().anyMatch(t -> t.getId().equals(tag.getId())));
    }

    @Test
    void getAllWithoutFilter() {
        dataBuilder.saveTag();
        var page = service.getAll(null, PageRequest.of(0, 20));

        assertFalse(page.isEmpty());
    }

    @Test
    void getAllReturnsEmptyWhenFilterDoesNotMatch() {
        dataBuilder.saveTag();
        var page = service.getAll("zzz-no-match-zzz", PageRequest.of(0, 20));

        assertTrue(page.isEmpty());
    }

    @Test
    void delete() {
        var tag = dataBuilder.saveTag();
        service.delete(tag.getId());
        assertThrows(NoSuchElementException.class, () -> service.getById(tag.getId()));
    }
}
