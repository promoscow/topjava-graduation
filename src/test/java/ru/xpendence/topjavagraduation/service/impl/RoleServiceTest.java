package ru.xpendence.topjavagraduation.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.xpendence.topjavagraduation.AbstractTest;
import ru.xpendence.topjavagraduation.entity.type.RoleType;
import ru.xpendence.topjavagraduation.service.RoleService;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class RoleServiceTest extends AbstractTest {

    @Autowired
    private RoleService service;

    @Test
    @DisplayName("getById(): существующий id -> успешное получение роли")
    void getById() {
        assertDoesNotThrow(() -> service.getById(1L));
    }

    @Test
    @DisplayName("getById(): несуществующий id -> NoSuchElementException")
    void getByIdFailsWhenNotFound() {
        assertThrows(NoSuchElementException.class, () -> service.getById(Long.MAX_VALUE));
    }

    @Test
    @DisplayName("getByName(): существующее имя -> успешное получение роли")
    void getByName() {
        var role = service.getByName(RoleType.USER);
        assertEquals(RoleType.USER, role.getName());
    }

    @Test
    @DisplayName("getAll(): роли в БД -> непустой список")
    void getAll() {
        assertFalse(service.getAll().isEmpty());
    }
}
