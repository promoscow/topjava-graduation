package ru.xpendence.topjavagraduation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.xpendence.topjavagraduation.entity.Dish;

import java.util.Optional;

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
    int setActiveFalseForAllByRestaurantId(@Param("restaurantId") Long restaurantId);

    @Query("SELECT d FROM Dish d JOIN FETCH d.restaurant WHERE d.id = :id")
    Optional<Dish> findByIdWithRestaurant(@Param("id") Long id);

    @Query(
            value = "SELECT d FROM Dish d JOIN FETCH d.restaurant WHERE d.restaurant.id = :restaurantId",
            countQuery = "SELECT COUNT(d) FROM Dish d WHERE d.restaurant.id = :restaurantId"
    )
    Page<Dish> getAllByRestaurantId(@Param("restaurantId") Long restaurantId, Pageable pageable);

    void deleteById(Long id);
}
