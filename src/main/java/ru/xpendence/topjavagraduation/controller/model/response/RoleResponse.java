package ru.xpendence.topjavagraduation.controller.model.response;

import ru.xpendence.topjavagraduation.entity.type.RoleType;

public record RoleResponse(
        Long id,
        RoleType name
) {
}
