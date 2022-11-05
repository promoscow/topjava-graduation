package ru.xpendence.topjavagraduation.service;

import ru.xpendence.topjavagraduation.entity.User;

public interface UserService {

    User create(User user);

    void update(User user);

    User get(Long id);

    void delete(Long id);
}
