package ru.xpendence.topjavagraduation.service.impl

import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import ru.xpendence.topjavagraduation.AbstractTest
import ru.xpendence.topjavagraduation.service.LoginService

class LoginServiceTest : AbstractTest() {

    @Autowired
    private lateinit var service: LoginService

    @Test
    @DisplayName("login(): корректные credentials -> успешный вход")
    fun login() {
        assertDoesNotThrow { service.login("admin", "admin") }
    }
}
