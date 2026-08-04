package ru.xpendence.topjavagraduation.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.xpendence.topjavagraduation.AbstractTest;
import ru.xpendence.topjavagraduation.entity.type.RoleType;
import ru.xpendence.topjavagraduation.service.RoleService;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RoleServiceTest extends AbstractTest {

    @Autowired
    private RoleService service;

    @Test
    void getById() {
        assertDoesNotThrow(() -> service.getById(1L));
    }

    @Test
    void getByIdFailsWhenNotFound() {
        assertThrows(NoSuchElementException.class, () -> service.getById(Long.MAX_VALUE));
    }

    @Test
    void getByName() {
        var role = service.getByName(RoleType.USER);
        assertEquals(RoleType.USER, role.getName());
    }

    @Test
    void getAll() {
        assertFalse(service.getAll().isEmpty());
    }
}