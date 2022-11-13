package ru.xpendence.topjavagraduation.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.xpendence.topjavagraduation.entity.Restaurant;

public interface RestaurantService {

    Restaurant create(Restaurant restaurant);

    void update(Restaurant restaurant);

    Restaurant getById(Long id);

    Restaurant getByDishId(Long dishId);

    Page<Restaurant> getAll(Pageable pageable);

    // TODO: 12.11.2022 получить выбранный

    void delete(Long id);
}
