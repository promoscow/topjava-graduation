package ru.xpendence.topjavagraduation.controller.mapper;

import ru.xpendence.topjavagraduation.controller.model.request.UserCreateRequest;
import ru.xpendence.topjavagraduation.controller.model.request.UserUpdateRequest;
import ru.xpendence.topjavagraduation.controller.model.response.UserResponse;
import ru.xpendence.topjavagraduation.entity.User;

public final class UserModelMapper {

    private UserModelMapper() {
    }

    public static User toUser(UserCreateRequest request) {
        return new User(null, request.username(), request.password());
    }

    public static User toUser(UserUpdateRequest request) {
        return new User(request.id(), request.username(), request.password());
    }

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getRoles().stream().map(RoleModelMapper::toResponse).toList()
        );
    }
}
