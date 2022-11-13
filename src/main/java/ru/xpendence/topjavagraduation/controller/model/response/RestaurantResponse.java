package ru.xpendence.topjavagraduation.controller.model.response;

import java.util.List;

public record RestaurantResponse(
        Long id,
        String name,
        List<DishResponse> dishes
) {
}
