package ru.xpendence.topjavagraduation.controller.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginRequest(

        @NotNull
        @Size(max = 255)
        String username,

        @NotNull
        @Size(max = 255)
        String password
) {
}
