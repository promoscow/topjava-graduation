package ru.xpendence.topjavagraduation.controller.mapper;

import ru.xpendence.topjavagraduation.controller.model.response.RoleResponse;
import ru.xpendence.topjavagraduation.entity.Role;

public final class RoleModelMapper {

    private RoleModelMapper() {
    }

    public static RoleResponse toResponse(Role role) {
        return new RoleResponse(role.getId(), role.getName());
    }
}
