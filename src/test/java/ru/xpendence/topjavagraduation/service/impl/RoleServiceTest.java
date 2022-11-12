package ru.xpendence.topjavagraduation.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.xpendence.topjavagraduation.AbstractTest;
import ru.xpendence.topjavagraduation.service.RoleService;

import static org.junit.jupiter.api.Assertions.assertFalse;

class RoleServiceTest extends AbstractTest {

    @Autowired
    private RoleService service;

    @Test
    void getAll() {
        assertFalse(service.getAll().isEmpty());
    }
}