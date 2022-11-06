package ru.xpendence.topjavagraduation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.xpendence.topjavagraduation.entity.Dish;

import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Long> {

    List<Dish> getAllByRestaurantId(Long restaurantId);

    void deleteById(Long id);
}
