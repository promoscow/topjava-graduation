package ru.xpendence.topjavagraduation.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import ru.xpendence.topjavagraduation.controller.model.request.RestaurantRequest;
import ru.xpendence.topjavagraduation.controller.model.response.RestaurantResponse;

public interface RestaurantController {

    RestaurantResponse create(RestaurantRequest request);

    HttpStatus update(RestaurantRequest request);

    RestaurantResponse get(Long id);

    RestaurantResponse getChosen();

    Page<RestaurantResponse> getAll(Integer page, Integer size);
}
