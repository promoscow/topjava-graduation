package ru.xpendence.topjavagraduation.controller.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(

        @Schema(description = "Имя пользователя, от 3 до 255 символов.")
        @NotNull
        @Size(min = 3, max = 255)
        String username,

        @Schema(description = "Пароль, от 4 до 255 символов.")
        @NotNull
        @Size(min = 4, max = 255)
        String password
) {
}
