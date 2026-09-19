package ru.xpendence.topjavagraduation.controller.mapper;

import org.springframework.stereotype.Component;
import ru.xpendence.topjavagraduation.controller.model.request.DishCreateRequest;
import ru.xpendence.topjavagraduation.controller.model.request.DishUpdateRequest;
import ru.xpendence.topjavagraduation.controller.model.response.DishResponse;
import ru.xpendence.topjavagraduation.entity.Dish;
import ru.xpendence.topjavagraduation.entity.Restaurant;

@Component
public class DishModelMapper {

    public DishResponse toResponse(Dish dish) {
        return new DishResponse(
                dish.getId(), dish.getName(), dish.getPrice(), dish.getActive(), dish.getRestaurant().getId()
        );
    }

    public Dish toDish(DishCreateRequest request) {
        return new Dish(
                null,
                request.name(),
                request.price(),
                request.active(),
                new Restaurant(request.restaurantId(), null)
        );
    }

    public Dish toDish(DishUpdateRequest request) {
        return new Dish(
                request.id(),
                request.name(),
                request.price(),
                request.active(),
                new Restaurant()
        );
    }
}
