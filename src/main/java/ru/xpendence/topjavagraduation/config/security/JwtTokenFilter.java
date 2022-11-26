package ru.xpendence.topjavagraduation.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

/**
 * Author: Vyacheslav Chernyshov
 * Date: 17.08.19
 * Time: 12:22
 * e-mail: v.chernyshov@pflb.ru
 */
public class JwtTokenFilter extends GenericFilterBean {

    private final JwtTokenService jwtTokenService;

    public JwtTokenFilter(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = jwtTokenService.resolveToken((HttpServletRequest) request);

        if (Objects.nonNull(token) && jwtTokenService.validateToken(token)) {
            Authentication authentication = jwtTokenService.getAuth(token);
            if (Objects.nonNull(authentication)) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }
}
