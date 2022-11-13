package ru.xpendence.topjavagraduation.controller.model.response;

import java.math.BigDecimal;

public record DishResponse(
        Long id,
        String name,
        BigDecimal price,
        Boolean active,
        Long restaurantId
) {
}
