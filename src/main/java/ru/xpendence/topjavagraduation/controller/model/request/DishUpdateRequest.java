package ru.xpendence.topjavagraduation.controller.model.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
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
