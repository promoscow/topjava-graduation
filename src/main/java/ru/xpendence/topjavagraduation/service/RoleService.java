package ru.xpendence.topjavagraduation.service;

import ru.xpendence.topjavagraduation.entity.Role;
import ru.xpendence.topjavagraduation.entity.type.RoleType;

import java.util.List;

public interface RoleService {

    Role getById(Long id);

    Role getByName(RoleType name);

    List<Role> getAll();
}
