package ru.xpendence.topjavagraduation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.xpendence.topjavagraduation.entity.Restaurant;

import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query("select r from Restaurant r left join Dish d on d.restaurant.id = r.id where d.id = ?1")
    Optional<Restaurant> getByDishId(Long dishId);

    @Query("select r from Restaurant r left join Vote v on v.restaurant.id = r.id group by r.id order by count(v.id) desc")
    Page<Restaurant> getIdsWithVotesCount(Pageable pageable);

    void deleteById(Long id);
}
