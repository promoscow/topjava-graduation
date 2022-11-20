package ru.xpendence.topjavagraduation.controller.model.request;

public record VoteRequest(
        Long userId,
        Long restaurantId
) {
}
