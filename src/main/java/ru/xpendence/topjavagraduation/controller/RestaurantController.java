package ru.xpendence.topjavagraduation.controller;

import org.springframework.data.domain.Page;
import ru.xpendence.topjavagraduation.controller.model.request.RestaurantRequest;
import ru.xpendence.topjavagraduation.controller.model.response.RestaurantResponse;

public interface RestaurantController {

    RestaurantResponse create(RestaurantRequest request);

    Page<RestaurantResponse> getAll(Integer page, Integer size);
}
