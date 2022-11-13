package ru.xpendence.topjavagraduation.controller.model.request;

import java.math.BigDecimal;

public record DishCreateRequest(
        String name,
        BigDecimal price,
        Boolean active,
        Long restaurantId
) {
}
