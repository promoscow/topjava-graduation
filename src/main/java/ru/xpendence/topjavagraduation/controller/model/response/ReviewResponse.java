package ru.xpendence.topjavagraduation.controller.model.response;

import java.time.LocalDate;

public record ReviewResponse(
        Long id,
        Integer rating,
        String text,
        LocalDate date,
        Long userId,
        Long restaurantId
) {
}
