package ru.xpendence.topjavagraduation.controller.mapper;

import ru.xpendence.topjavagraduation.controller.model.request.UserCreateRequest;
import ru.xpendence.topjavagraduation.controller.model.request.UserUpdateRequest;
import ru.xpendence.topjavagraduation.controller.model.response.UserResponse;
import ru.xpendence.topjavagraduation.entity.User;

public final class UserModelMapper {

    private UserModelMapper() {
    }

    public static User toUser(UserCreateRequest request) {
        var user = new User();
        user.setUsername(request.username());
        user.setPassword(request.password());
        return user;
    }

    public static User toUser(UserUpdateRequest request) {
        var user = new User();
        user.setId(request.id());
        user.setUsername(request.username());
        user.setPassword(request.password());
        return user;
    }

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getRoles().stream().map(RoleModelMapper::toResponse).toList()
        );
    }
}
