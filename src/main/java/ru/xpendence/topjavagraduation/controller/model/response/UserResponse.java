package ru.xpendence.topjavagraduation.controller.model.response;

import java.util.List;

public record UserResponse(
        Long id,
        String userName,
        List<RoleResponse> roles
) {
}
