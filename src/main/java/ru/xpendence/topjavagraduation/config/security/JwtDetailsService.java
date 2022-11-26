package ru.xpendence.topjavagraduation.config.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.xpendence.topjavagraduation.config.security.model.JwtUser;
import ru.xpendence.topjavagraduation.service.UserService;

@Service
public class JwtDetailsService implements UserDetailsService {

    private final UserService userService;

    public JwtDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return JwtUser.of(userService.getByUsername(username));
    }
}
