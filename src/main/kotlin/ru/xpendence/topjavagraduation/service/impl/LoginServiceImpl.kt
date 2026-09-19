package ru.xpendence.topjavagraduation.service.impl

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import ru.xpendence.topjavagraduation.config.security.JwtTokenService
import ru.xpendence.topjavagraduation.service.LoginService
import ru.xpendence.topjavagraduation.service.UserService

@Service
class LoginServiceImpl(
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenService: JwtTokenService,
    private val userService: UserService,
) : LoginService {

    override fun login(username: String, password: String): String {
        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, password))
        val user = userService.getByUsername(username)
        return jwtTokenService.createToken(username, user.roles)
    }
}
