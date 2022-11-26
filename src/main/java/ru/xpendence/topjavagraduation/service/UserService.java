package ru.xpendence.topjavagraduation.service;

import ru.xpendence.topjavagraduation.entity.User;

public interface UserService {

    User create(User user);

    void update(User user);

    User getById(Long id);

    User getByUsername(String username);

    void addRole(Long id, Long roleId);

    void removeRole(Long id, Long roleId);

    void delete(Long id);
}
