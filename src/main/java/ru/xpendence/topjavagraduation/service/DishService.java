package ru.xpendence.topjavagraduation.service;

import ru.xpendence.topjavagraduation.entity.Dish;

import java.util.List;

public interface DishService {

    Dish create(Dish dish);

    void update(Dish dish);

    Dish get(Long id);

    List<Dish> getAllByRestaurantId(Long restaurantId);

    void delete(Long id);
}
