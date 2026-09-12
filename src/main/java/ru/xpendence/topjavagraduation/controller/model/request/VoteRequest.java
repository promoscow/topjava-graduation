package ru.xpendence.topjavagraduation.controller.model.request;

import jakarta.validation.constraints.NotNull;

public record VoteRequest(

        @NotNull
        Long restaurantId
) {
}
