package ru.xpendence.topjavagraduation.controller.model.response;

import java.time.LocalDate;

public record VoteResponse(
        Long id,
        LocalDate date,
        Long userId,
        Long restaurantId
) {
}
