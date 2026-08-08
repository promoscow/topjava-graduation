package ru.xpendence.topjavagraduation.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.xpendence.topjavagraduation.entity.Review;
import ru.xpendence.topjavagraduation.repository.ReviewRepository;
import ru.xpendence.topjavagraduation.service.RestaurantService;
import ru.xpendence.topjavagraduation.service.ReviewService;
import ru.xpendence.topjavagraduation.service.UserService;

import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository repository;
    private final UserService userService;
    private final RestaurantService restaurantService;

    public ReviewServiceImpl(
            ReviewRepository repository,
            UserService userService,
            RestaurantService restaurantService
    ) {
        this.repository = repository;
        this.userService = userService;
        this.restaurantService = restaurantService;
    }

    @Override
    @Transactional
    public Review create(Review review) {
        var userId = review.getUser().getId();
        var restaurantId = review.getRestaurant().getId();
        if (repository.existsByUserIdAndRestaurantId(userId, restaurantId)) {
            throw new IllegalArgumentException(
                    String.format("Review already exists for user id: %d and restaurant id: %d", userId, restaurantId)
            );
        }
        review.setUser(userService.getById(userId));
        review.setRestaurant(restaurantService.getById(restaurantId));
        return repository.save(review);
    }

    @Override
    @Transactional
    public void update(Review review) {
        if (Objects.isNull(review.getId())) {
            throw new IllegalArgumentException("Review id is null.");
        }
        var stored = repository.findById(review.getId())
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("Review not found by id: %d", review.getId())
                ));
        Review.enrichForUpdate(review, stored);
        repository.save(stored);
    }

    @Override
    @Transactional(readOnly = true)
    public Review getById(Long id) {
        return repository.findByIdWithDetails(id)
                .orElseThrow(() -> new NoSuchElementException(String.format("Review not found by id: %d", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public Review getByUserIdAndRestaurantId(Long userId, Long restaurantId) {
        return repository.findByUserIdAndRestaurantId(userId, restaurantId)
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("Review not found by user id: %d and restaurant id: %d", userId, restaurantId)
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Review> getAllByRestaurantId(Long restaurantId, Pageable pageable) {
        return repository.findAllByRestaurantId(restaurantId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Double getAverageRatingByRestaurantId(Long restaurantId) {
        var average = repository.findAverageRatingByRestaurantId(restaurantId);
        return Objects.requireNonNullElse(average, 0.0);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByRestaurantId(Long restaurantId) {
        return repository.countByRestaurantId(restaurantId);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException(String.format("Review not found by id: %d", id));
        }
        repository.deleteById(id);
    }
}
