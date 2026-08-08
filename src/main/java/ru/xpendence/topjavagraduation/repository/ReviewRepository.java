package ru.xpendence.topjavagraduation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.xpendence.topjavagraduation.entity.Review;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r JOIN FETCH r.user JOIN FETCH r.restaurant WHERE r.id = :id")
    Optional<Review> findByIdWithDetails(@Param("id") Long id);

    @Query("""
            SELECT r FROM Review r
            JOIN FETCH r.user
            JOIN FETCH r.restaurant
            WHERE r.user.id = :userId AND r.restaurant.id = :restaurantId
            """)
    Optional<Review> findByUserIdAndRestaurantId(
            @Param("userId") Long userId,
            @Param("restaurantId") Long restaurantId
    );

    boolean existsByUserIdAndRestaurantId(Long userId, Long restaurantId);

    @Query(
            value = """
                    SELECT r FROM Review r
                    JOIN FETCH r.user
                    JOIN FETCH r.restaurant
                    WHERE r.restaurant.id = :restaurantId
                    """,
            countQuery = "SELECT COUNT(r) FROM Review r WHERE r.restaurant.id = :restaurantId"
    )
    Page<Review> findAllByRestaurantId(@Param("restaurantId") Long restaurantId, Pageable pageable);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.restaurant.id = :restaurantId")
    Double findAverageRatingByRestaurantId(@Param("restaurantId") Long restaurantId);

    long countByRestaurantId(Long restaurantId);
}
