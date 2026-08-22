package ru.xpendence.topjavagraduation.controller.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TagCreateRequest(

        @Schema(description = "Название тега, от 1 до 255 символов.")
        @NotNull
        @Size(min = 1, max = 255)
        String name
) {
}
