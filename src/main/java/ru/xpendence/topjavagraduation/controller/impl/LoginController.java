package ru.xpendence.topjavagraduation.controller.impl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.xpendence.topjavagraduation.controller.model.request.LoginRequest;
import ru.xpendence.topjavagraduation.service.LoginService;

@RestController
@RequestMapping("/login")
@Tag(name = "Авторизация")
public class LoginController {

    private final LoginService service;

    public LoginController(LoginService service) {
        this.service = service;
    }

    @Operation(summary = "Получение токена по логину и паролю")
    @PostMapping
    public String login(@RequestBody LoginRequest request) {
        return service.login(request.username(), request.password());
    }
}
