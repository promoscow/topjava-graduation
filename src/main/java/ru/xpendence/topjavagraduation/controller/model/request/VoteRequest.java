package ru.xpendence.topjavagraduation.controller.model.request;

import javax.validation.constraints.NotNull;

public record VoteRequest(

        @NotNull
        Long userId,

        @NotNull
        Long restaurantId
) {
}
