package ru.xpendence.topjavagraduation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.xpendence.topjavagraduation.entity.Dish;

public interface DishRepository extends JpaRepository<Dish, Long> {

    Page<Dish> getAllByRestaurantId(Long restaurantId, Pageable pageable);

    void deleteById(Long id);
}
