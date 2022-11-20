package ru.xpendence.topjavagraduation.controller.mapper;

import org.springframework.stereotype.Component;
import ru.xpendence.topjavagraduation.controller.model.request.RestaurantRequest;
import ru.xpendence.topjavagraduation.controller.model.response.RestaurantResponse;
import ru.xpendence.topjavagraduation.entity.Restaurant;

import java.util.stream.Collectors;

@Component
public class RestaurantModelMapper {

    private final DishModelMapper dishModelMapper;
    private final VoteMapper voteMapper;

    public RestaurantModelMapper(DishModelMapper dishModelMapper, VoteMapper voteMapper) {
        this.dishModelMapper = dishModelMapper;
        this.voteMapper = voteMapper;
    }

    public Restaurant toRestaurantForCreate(RestaurantRequest request) {
        var restaurant = new Restaurant();
        restaurant.setName(request.name());
        return restaurant;
    }

    public Restaurant toRestaurantForUpdate(RestaurantRequest request) {
        var restaurant = new Restaurant();
        restaurant.setId(request.id());
        restaurant.setName(request.name());
        return restaurant;
    }

    public RestaurantResponse toResponse(Restaurant restaurant) {
        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getDishes().stream().map(dishModelMapper::toResponse).collect(Collectors.toList()),
                restaurant.getVotes().stream().map(voteMapper::toResponse).collect(Collectors.toList())
        );
    }
}
