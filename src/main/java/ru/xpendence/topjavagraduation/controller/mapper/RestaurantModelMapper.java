package ru.xpendence.topjavagraduation.controller.mapper;

import org.springframework.stereotype.Component;
import ru.xpendence.topjavagraduation.controller.model.request.RestaurantRequest;
import ru.xpendence.topjavagraduation.controller.model.response.RestaurantResponse;
import ru.xpendence.topjavagraduation.entity.Restaurant;

import java.util.stream.Collectors;

@Component
public class RestaurantModelMapper {

    private final DishModelMapper dishModelMapper;

    public RestaurantModelMapper(DishModelMapper dishModelMapper) {
        this.dishModelMapper = dishModelMapper;
    }

    public Restaurant toRestaurant(RestaurantRequest request) {
        var restaurant = new Restaurant();
        restaurant.setName(request.name());
        return restaurant;
    }

    public RestaurantResponse toResponse(Restaurant restaurant) {
        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getDishes().stream().map(dishModelMapper::toResponse).collect(Collectors.toList())
        );
    }
}
