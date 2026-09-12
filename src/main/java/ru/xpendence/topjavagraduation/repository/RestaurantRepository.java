package ru.xpendence.topjavagraduation.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.xpendence.topjavagraduation.entity.Restaurant;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query("select r from Restaurant r left join Dish d on d.restaurant.id = r.id where d.id = ?1")
    Optional<Restaurant> getByDishId(Long dishId);

    @Query("""
            select r from Restaurant r
            join Vote v on v.restaurant.id = r.id
            where v.date = :date
            group by r.id
            order by count(v.id) desc
            """)
    List<Restaurant> findChosenByVoteDate(@Param("date") LocalDate date, Pageable pageable);

    void deleteById(Long id);
}
