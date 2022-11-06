package ru.xpendence.topjavagraduation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.xpendence.topjavagraduation.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    void deleteById(Long id);
}
