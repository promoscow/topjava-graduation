package ru.xpendence.topjavagraduation.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import ru.xpendence.topjavagraduation.controller.model.request.DishCreateRequest;
import ru.xpendence.topjavagraduation.controller.model.request.DishUpdateRequest;
import ru.xpendence.topjavagraduation.controller.model.response.DishResponse;

public interface DishController {

    DishResponse create(DishCreateRequest request);

    HttpStatus update(DishUpdateRequest request);

    HttpStatus resetMenu(Long restaurantId);

    DishResponse get(Long id);

    Page<DishResponse> getAllByRestaurantId(Long restaurantId, Integer page, Integer size);
}
