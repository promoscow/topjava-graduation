package ru.xpendence.topjavagraduation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.xpendence.topjavagraduation.entity.Restaurant;

import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query("select r from Restaurant r left join Dish d on d.restaurant.id = r.id where d.id = ?1")
    Optional<Restaurant> getByDishId(Long dishId);

    void deleteById(Long id);
}
