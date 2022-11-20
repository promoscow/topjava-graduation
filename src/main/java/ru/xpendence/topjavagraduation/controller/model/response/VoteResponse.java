package ru.xpendence.topjavagraduation.controller.model.response;

import java.time.LocalDate;

@Deprecated
public record VoteResponse(
        Long id,
        LocalDate date,
        UserResponse user,
        RestaurantResponse restaurant
) {
}
