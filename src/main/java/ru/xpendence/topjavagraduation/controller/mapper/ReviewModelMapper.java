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

    public Review toReview(ReviewCreateRequest request) {
        var review = new Review();
        review.setRating(request.rating());
        review.setText(request.text());
        review.setDate(LocalDate.now());
        var user = new User();
        user.setId(request.userId());
        review.setUser(user);
        var restaurant = new Restaurant();
        restaurant.setId(request.restaurantId());
        review.setRestaurant(restaurant);
        return review;
    }

    public Review toReview(ReviewUpdateRequest request) {
        var review = new Review();
        review.setId(request.id());
        review.setRating(request.rating());
        review.setText(request.text());
        return review;
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
