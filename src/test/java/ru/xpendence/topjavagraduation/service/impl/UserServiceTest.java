package ru.xpendence.topjavagraduation.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
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

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest extends AbstractTest {

    @Autowired
    private UserService service;

    @Autowired
    private LoginService loginService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    @DisplayName("create(): валидный пользователь -> успешное создание с ролью USER")
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
    @DisplayName("create(): занятый username -> IllegalArgumentException")
    void createFailsWhenUsernameTaken() {
        var existing = dataBuilder.saveUser();
        var user = dataBuilder.buildUser();
        user.setUsername(existing.getUsername());

        assertThrows(IllegalArgumentException.class, () -> service.create(user));
    }

    @Test
    @DisplayName("update(): корректные данные -> успешное обновление")
    void update() {
        var user = service.create(dataBuilder.buildUser());
        var username = RandomStringUtils.secure().nextAlphanumeric(16);
        var password = RandomStringUtils.secure().nextAlphanumeric(16);
        user.setUsername(username);
        user.setPassword(password);

        service.update(user);

        var updated = service.getById(user.getId());
        assertEquals(username, updated.getUsername());
        assertTrue(passwordEncoder.matches(password, updated.getPassword()));
        assertDoesNotThrow(() -> loginService.login(username, password));
    }

    @Test
    @DisplayName("update(): id == null -> IllegalArgumentException")
    void updateFailsWhenIdIsNull() {
        var user = dataBuilder.buildUser();

        assertThrows(IllegalArgumentException.class, () -> service.update(user));
    }

    @Test
    @DisplayName("getById(): существующий id -> успешное получение пользователя")
    void get() {
        var user = dataBuilder.saveUser();
        assertDoesNotThrow(() -> service.getById(user.getId()));
    }

    @Test
    @DisplayName("getById(): несуществующий id -> NoSuchElementException")
    void getFailsWhenNotFound() {
        assertThrows(NoSuchElementException.class, () -> service.getById(Long.MAX_VALUE));
    }

    @Test
    @DisplayName("getByUsername(): существующий username -> успешное получение пользователя")
    void getByUsername() {
        var user = dataBuilder.saveUser();
        assertDoesNotThrow(() -> service.getByUsername(user.getUsername()));
    }

    @Test
    @DisplayName("getByUsername(): несуществующий username -> NoSuchElementException")
    void getByUsernameFailsWhenNotFound() {
        assertThrows(NoSuchElementException.class, () -> service.getByUsername("missing-user"));
    }

    @Test
    @DisplayName("getAll(): фильтр по username -> страница с пользователем")
    void getAll() {
        var user = dataBuilder.saveUser();
        var page = service.getAll(user.getUsername().substring(0, 4), PageRequest.of(0, 20));

        assertFalse(page.isEmpty());
        assertTrue(page.getContent().stream().anyMatch(u -> u.getId().equals(user.getId())));
    }

    @Test
    @DisplayName("getAll(): без фильтра -> непустая страница")
    void getAllWithoutFilter() {
        dataBuilder.saveUser();
        var page = service.getAll(null, PageRequest.of(0, 20));

        assertFalse(page.isEmpty());
    }

    @Test
    @DisplayName("getAll(): фильтр без совпадений -> пустая страница")
    void getAllReturnsEmptyWhenFilterDoesNotMatch() {
        dataBuilder.saveUser();
        var page = service.getAll("zzz-no-match-zzz", PageRequest.of(0, 20));

        assertTrue(page.isEmpty());
    }

    @Test
    @DisplayName("addRole(): существующие user и role -> роль добавлена")
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
    @DisplayName("removeRole(): пользователь с ролью -> роль удалена")
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
    @DisplayName("delete(): существующий id -> пользователь удалён")
    void delete() {
        var user = dataBuilder.saveUser();
        service.delete(user.getId());
        assertThrows(NoSuchElementException.class, () -> service.getById(user.getId()));
    }
}
