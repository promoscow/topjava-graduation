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
        var dish = new Dish();
        dish.setPrice(request.price());
        dish.setName(request.name());
        dish.setActive(request.active());
        var restaurant = new Restaurant();
        restaurant.setId(request.restaurantId());
        dish.setRestaurant(restaurant);
        return dish;
    }

    public Dish toDish(DishUpdateRequest request) {
        var dish = new Dish();
        dish.setId(request.id());
        dish.setPrice(request.price());
        dish.setName(request.name());
        dish.setActive(request.active());
        return dish;
    }
}
