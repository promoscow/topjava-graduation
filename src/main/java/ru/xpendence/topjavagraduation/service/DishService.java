package ru.xpendence.topjavagraduation.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.xpendence.topjavagraduation.entity.Dish;

public interface DishService {

    Dish create(Dish dish);

    void update(Dish dish);

    void resetMenu(Long restaurantId);

    Dish getById(Long id);

    Page<Dish> getAllByRestaurantId(Long restaurantId, Pageable pageable);

    void delete(Long id);
}
