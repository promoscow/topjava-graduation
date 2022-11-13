package ru.xpendence.topjavagraduation.controller.model.request;

import java.math.BigDecimal;

public record DishUpdateRequest(
        Long id,
        String name,
        BigDecimal price,
        Boolean active
) {
}
