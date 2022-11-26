package ru.xpendence.topjavagraduation.controller.model.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record LoginRequest(

        @NotNull
        @Size(max = 255)
        String username,

        @NotNull
        @Size(max = 255)
        String password
) {
}
