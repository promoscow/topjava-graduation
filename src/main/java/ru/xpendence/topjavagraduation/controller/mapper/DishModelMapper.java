package ru.xpendence.topjavagraduation.controller.mapper;

import org.springframework.stereotype.Component;
import ru.xpendence.topjavagraduation.controller.model.request.DishCreateRequest;
import ru.xpendence.topjavagraduation.controller.model.request.DishUpdateRequest;
import ru.xpendence.topjavagraduation.controller.model.response.DishResponse;
import ru.xpendence.topjavagraduation.entity.Dish;
import ru.xpendence.topjavagraduation.service.RestaurantService;

@Component
public class DishModelMapper {

    private final RestaurantService restaurantService;

    public DishModelMapper(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

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
        dish.setRestaurant(restaurantService.getById(request.restaurantId()));
        return dish;
    }

    public Dish toDish(DishUpdateRequest request) {
        var dish = new Dish();
        dish.setId(request.id());
        dish.setPrice(request.price());
        dish.setName(request.name());
        dish.setActive(request.active());
        dish.setRestaurant(restaurantService.getByDishId(request.id()));
        return dish;
    }
}
