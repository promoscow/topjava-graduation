package ru.xpendence.topjavagraduation.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.xpendence.topjavagraduation.AbstractTest;
import ru.xpendence.topjavagraduation.entity.type.RoleType;
import ru.xpendence.topjavagraduation.service.UserService;

import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest extends AbstractTest {

    @Autowired
    private UserService service;

    @Test
    void create() {
        var user = dataBuilder.buildUser();
        assertNotNull(service.create(user).getId());
    }

    @Test
    void update() {
        var user = dataBuilder.saveUser();
        var password = RandomStringUtils.randomAlphabetic(16);
        user.setPassword(password);
        service.update(user);
        assertEquals(password, service.getById(user.getId()).getPassword());
    }

    @Test
    void get() {
        var user = dataBuilder.saveUser();
        assertDoesNotThrow(() -> service.getById(user.getId()));
    }

    @Test
    void getByUsername() {
        var user = dataBuilder.saveUser();
        assertDoesNotThrow(() -> service.getByUsername(user.getUsername()));
    }

    @Test
    void addRole() {
        var user = dataBuilder.saveUser();
        service.addRole(user.getId(), 2L);
        assertTrue(
                service.getById(user.getId())
                        .getRoles()
                        .stream()
                        .anyMatch(r -> Arrays.asList(RoleType.values()).contains(r.getName()))
        );
    }

    @Test
    void removeRole() {
        var user = dataBuilder.saveUser();
        service.removeRole(user.getId(), 2L);
        assertTrue(
                service.getById(user.getId())
                        .getRoles()
                        .stream()
                        .noneMatch(r -> Arrays.asList(RoleType.values()).contains(r.getName()))
        );
    }

    @Test
    void delete() {
        var user = dataBuilder.saveUser();
        service.delete(user.getId());
        assertThrows(NoSuchElementException.class, () -> service.getById(user.getId()));
    }
}