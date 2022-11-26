package ru.xpendence.topjavagraduation.controller.impl;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.xpendence.topjavagraduation.controller.LoginController;
import ru.xpendence.topjavagraduation.controller.model.request.LoginRequest;
import ru.xpendence.topjavagraduation.service.LoginService;

@RestController
@RequestMapping("/login")
public class LoginControllerImpl implements LoginController {

    private final LoginService service;

    public LoginControllerImpl(LoginService service) {
        this.service = service;
    }

    @Override
    @PostMapping
    public String login(@RequestBody LoginRequest request) {
        return service.login(request.username(), request.password());
    }
}
