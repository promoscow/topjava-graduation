package ru.xpendence.topjavagraduation.controller;

import ru.xpendence.topjavagraduation.controller.model.request.LoginRequest;

public interface LoginController {

    String login(LoginRequest request);
}
