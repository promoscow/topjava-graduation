package ru.xpendence.topjavagraduation.service;

import ru.xpendence.topjavagraduation.entity.Role;

import java.util.List;

public interface RoleService {

    Role getById(Long id);

    List<Role> getAll();
}
