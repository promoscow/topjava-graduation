package ru.xpendence.topjavagraduation.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.xpendence.topjavagraduation.AbstractTest;
import ru.xpendence.topjavagraduation.entity.type.RoleType;
import ru.xpendence.topjavagraduation.service.LoginService;
import ru.xpendence.topjavagraduation.service.UserService;

import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserServiceTest extends AbstractTest {

    @Autowired
    private UserService service;

    @Autowired
    private LoginService loginService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void create() {
        var user = dataBuilder.buildUser();
        var rawPassword = user.getPassword();
        var created = service.create(user);

        assertNotNull(created.getId());
        assertTrue(passwordEncoder.matches(rawPassword, created.getPassword()));
        assertTrue(created.getRoles().stream().anyMatch(role -> RoleType.USER == role.getName()));
        assertDoesNotThrow(() -> loginService.login(created.getUsername(), rawPassword));
    }

    @Test
    void createFailsWhenUsernameTaken() {
        var existing = dataBuilder.saveUser();
        var user = dataBuilder.buildUser();
        user.setUsername(existing.getUsername());

        assertThrows(IllegalArgumentException.class, () -> service.create(user));
    }

    @Test
    void update() {
        var user = service.create(dataBuilder.buildUser());
        var username = RandomStringUtils.randomAlphabetic(16);
        var password = RandomStringUtils.randomAlphabetic(16);
        user.setUsername(username);
        user.setPassword(password);

        service.update(user);

        var updated = service.getById(user.getId());
        assertEquals(username, updated.getUsername());
        assertTrue(passwordEncoder.matches(password, updated.getPassword()));
        assertDoesNotThrow(() -> loginService.login(username, password));
    }

    @Test
    void updateFailsWhenIdIsNull() {
        var user = dataBuilder.buildUser();

        assertThrows(IllegalArgumentException.class, () -> service.update(user));
    }

    @Test
    void get() {
        var user = dataBuilder.saveUser();
        assertDoesNotThrow(() -> service.getById(user.getId()));
    }

    @Test
    void getFailsWhenNotFound() {
        assertThrows(NoSuchElementException.class, () -> service.getById(Long.MAX_VALUE));
    }

    @Test
    void getByUsername() {
        var user = dataBuilder.saveUser();
        assertDoesNotThrow(() -> service.getByUsername(user.getUsername()));
    }

    @Test
    void getByUsernameFailsWhenNotFound() {
        assertThrows(NoSuchElementException.class, () -> service.getByUsername("missing-user"));
    }

    @Test
    void getAll() {
        var user = dataBuilder.saveUser();
        var page = service.getAll(user.getUsername().substring(0, 4), PageRequest.of(0, 20));

        assertFalse(page.isEmpty());
        assertTrue(page.getContent().stream().anyMatch(u -> u.getId().equals(user.getId())));
    }

    @Test
    void getAllWithoutFilter() {
        dataBuilder.saveUser();
        var page = service.getAll(null, PageRequest.of(0, 20));

        assertFalse(page.isEmpty());
    }

    @Test
    void getAllReturnsEmptyWhenFilterDoesNotMatch() {
        dataBuilder.saveUser();
        var page = service.getAll("zzz-no-match-zzz", PageRequest.of(0, 20));

        assertTrue(page.isEmpty());
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
