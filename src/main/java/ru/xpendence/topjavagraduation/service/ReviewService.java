package ru.xpendence.topjavagraduation.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.xpendence.topjavagraduation.entity.Review;

public interface ReviewService {

    Review create(Review review);

    void update(Review review);

    Review getById(Long id);

    Review getByUserIdAndRestaurantId(Long userId, Long restaurantId);

    Page<Review> getAllByRestaurantId(Long restaurantId, Pageable pageable);

    Double getAverageRatingByRestaurantId(Long restaurantId);

    long countByRestaurantId(Long restaurantId);

    void delete(Long id);
}
