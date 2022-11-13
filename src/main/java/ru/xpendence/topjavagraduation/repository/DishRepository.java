package ru.xpendence.topjavagraduation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.xpendence.topjavagraduation.entity.Dish;

public interface DishRepository extends JpaRepository<Dish, Long> {

    @Transactional
    @Modifying
    @Query("update Dish d set d.active = false where d.restaurant.id = ?1")
    void setActiveFalseForAllByRestaurantId(Long restaurantId);

    Page<Dish> getAllByRestaurantId(Long restaurantId, Pageable pageable);

    void deleteById(Long id);
}
