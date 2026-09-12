package ru.xpendence.topjavagraduation.service;

/**
 * Аутентификация пользователей.
 */
public interface LoginService {

    /**
     * Проверяет учётные данные и выдаёт JWT-токен.
     *
     * @param username имя пользователя
     * @param password пароль в открытом виде
     * @return JWT-токен
     * @throws org.springframework.security.core.AuthenticationException если учётные данные неверны
     * @throws java.util.NoSuchElementException если пользователь не найден после успешной аутентификации
     */
    String login(String username, String password);
}
