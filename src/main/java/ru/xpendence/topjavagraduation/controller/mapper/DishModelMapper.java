package ru.xpendence.topjavagraduation.controller.mapper;

import org.springframework.stereotype.Component;
import ru.xpendence.topjavagraduation.controller.model.response.DishResponse;
import ru.xpendence.topjavagraduation.entity.Dish;

@Component
public class DishModelMapper {

    DishResponse toResponse(Dish dish) {
        return new DishResponse(
                dish.getId(), dish.getName(), dish.getPrice(), dish.getActive(), dish.getRestaurant().getId()
        );
    }
}
