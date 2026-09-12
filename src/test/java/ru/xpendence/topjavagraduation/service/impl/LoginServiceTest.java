package ru.xpendence.topjavagraduation.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.xpendence.topjavagraduation.AbstractTest;
import ru.xpendence.topjavagraduation.service.LoginService;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class LoginServiceTest extends AbstractTest {

    @Autowired
    private LoginService service;

    @Test
    @DisplayName("login(): корректные credentials -> успешный вход")
    void login() {
        assertDoesNotThrow(() -> service.login("admin", "admin"));
    }
}
