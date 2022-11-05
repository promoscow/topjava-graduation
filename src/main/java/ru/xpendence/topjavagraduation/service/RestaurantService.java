package ru.xpendence.topjavagraduation.service;

import ru.xpendence.topjavagraduation.entity.Restaurant;

import java.util.List;

public interface RestaurantService {

    Restaurant create(Restaurant restaurant);

    void update(Restaurant restaurant);

    Restaurant get(Long id);

    List<Restaurant> getAll(); // TODO: 05.11.2022 пагинация, сортировка, фильтрация

    void delete(Long id);
}
