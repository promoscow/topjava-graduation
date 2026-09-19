package ru.xpendence.topjavagraduation.controller.mapper;

import org.springframework.stereotype.Component;
import ru.xpendence.topjavagraduation.controller.model.request.ReviewCreateRequest;
import ru.xpendence.topjavagraduation.controller.model.request.ReviewUpdateRequest;
import ru.xpendence.topjavagraduation.controller.model.response.ReviewResponse;
import ru.xpendence.topjavagraduation.entity.Restaurant;
import ru.xpendence.topjavagraduation.entity.Review;
import ru.xpendence.topjavagraduation.entity.User;

import java.time.LocalDate;

@Component
public class ReviewModelMapper {

    public Review toReview(ReviewCreateRequest request, Long userId) {
        return new Review(
                null,
                request.rating(),
                request.text(),
                LocalDate.now(),
                new User(userId, "", ""),
                new Restaurant(request.restaurantId(), null)
        );
    }

    public Review toReview(ReviewUpdateRequest request) {
        return new Review(
                request.id(),
                request.rating(),
                request.text(),
                LocalDate.now(),
                new User(null, "", ""),
                new Restaurant()
        );
    }

    public ReviewResponse toResponse(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getRating(),
                review.getText(),
                review.getDate(),
                review.getUser().getId(),
                review.getRestaurant().getId()
        );
    }
}
