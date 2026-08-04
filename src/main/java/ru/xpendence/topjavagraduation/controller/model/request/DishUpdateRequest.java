package ru.xpendence.topjavagraduation.controller.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record DishUpdateRequest(

        @NotNull
        Long id,

        @NotNull
        @Size(max = 255)
        String name,

        @NotNull
        @PositiveOrZero
        BigDecimal price,

        @NotNull
        Boolean active
) {
}
