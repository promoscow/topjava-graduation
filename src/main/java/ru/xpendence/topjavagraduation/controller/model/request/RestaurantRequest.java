package ru.xpendence.topjavagraduation.controller.model.request;

import ru.xpendence.topjavagraduation.controller.Validation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

public record RestaurantRequest(

        @Null(groups = {Validation.Create.class})
        @NotNull(groups = {Validation.Update.class})
        Long id,

        @NotNull
        @Size(max = 255)
        String name
) {
}
