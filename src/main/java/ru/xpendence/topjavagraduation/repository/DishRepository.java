package ru.xpendence.topjavagraduation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.xpendence.topjavagraduation.entity.Dish;

public interface DishRepository extends JpaRepository<Dish, Long> {

    /**
     * Снимает признак активности со всех блюд ресторана.
     *
     * @param restaurantId идентификатор ресторана
     * @return количество затронутых блюд
     */
    @Transactional
    @Modifying
    @Query("UPDATE Dish d SET d.active = false WHERE d.restaurant.id = :restaurantId")
    int setActiveFalseForAllByRestaurantId(@org.springframework.data.repository.query.Param("restaurantId") Long restaurantId);

    Page<Dish> getAllByRestaurantId(Long restaurantId, Pageable pageable);

    void deleteById(Long id);
}
