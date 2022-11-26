package ru.xpendence.topjavagraduation.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.xpendence.topjavagraduation.config.security.JwtTokenService;
import ru.xpendence.topjavagraduation.service.LoginService;
import ru.xpendence.topjavagraduation.service.UserService;

@Service
public class LoginServiceImpl implements LoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UserService userService;

    public LoginServiceImpl(AuthenticationManager authenticationManager,
                            JwtTokenService jwtTokenService,
                            UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.userService = userService;
    }

    @Override
    public String login(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        var user = userService.getByUsername(username);
        return jwtTokenService.createToken(username, user.getRoles());
    }
}
