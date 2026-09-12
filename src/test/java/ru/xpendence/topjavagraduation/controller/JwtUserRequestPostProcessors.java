package ru.xpendence.topjavagraduation.controller;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import ru.xpendence.topjavagraduation.config.security.model.JwtUser;
import ru.xpendence.topjavagraduation.entity.User;
import ru.xpendence.topjavagraduation.entity.type.RoleType;

import java.util.Arrays;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;

public final class JwtUserRequestPostProcessors {

    private JwtUserRequestPostProcessors() {
    }

    public static RequestPostProcessor jwtUser(User user, String... authorities) {
        var grantedAuthorities = Arrays.stream(authorities)
                .map(SimpleGrantedAuthority::new)
                .toList();
        var jwtUser = new JwtUser(user.getId(), user.getUsername(), "N/A", grantedAuthorities);
        return authentication(new UsernamePasswordAuthenticationToken(jwtUser, null, jwtUser.getAuthorities()));
    }

    public static RequestPostProcessor user() {
        return jwtUser(stubUser(1L), RoleType.USER.name());
    }

    public static RequestPostProcessor admin() {
        return jwtUser(stubUser(1L), RoleType.ADMIN.name());
    }

    private static User stubUser(Long id) {
        var user = new User();
        user.setId(id);
        user.setUsername("test-user-" + id);
        return user;
    }
}
