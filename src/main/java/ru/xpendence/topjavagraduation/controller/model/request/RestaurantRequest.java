package ru.xpendence.topjavagraduation.controller.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import ru.xpendence.topjavagraduation.controller.Validation;

public record RestaurantRequest(

        @Schema(description = "ID ресторана. Нулевой при создании, требуется при обновлении.")
        @Null(groups = {Validation.Create.class})
        @NotNull(groups = {Validation.Update.class})
        Long id,

        @Schema(description = "Название ресторана, не более 255 символов.")
        @NotNull
        @Size(max = 255)
        String name
) {
}
