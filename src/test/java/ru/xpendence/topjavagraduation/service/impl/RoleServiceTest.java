package ru.xpendence.topjavagraduation.service.impl;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.xpendence.topjavagraduation.AbstractTest;
import ru.xpendence.topjavagraduation.service.RoleService;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Disabled
class RoleServiceTest extends AbstractTest {

    @Autowired
    private RoleService service;

    @Test
    void getById() {
        assertDoesNotThrow(() -> service.getById(1L));
    }

    @Test
    void getAll() {
        assertFalse(service.getAll().isEmpty());
    }
}