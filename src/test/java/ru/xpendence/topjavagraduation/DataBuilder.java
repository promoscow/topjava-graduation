package ru.xpendence.topjavagraduation;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.xpendence.topjavagraduation.entity.Dish;
import ru.xpendence.topjavagraduation.entity.Restaurant;
import ru.xpendence.topjavagraduation.entity.Review;
import ru.xpendence.topjavagraduation.entity.Tag;
import ru.xpendence.topjavagraduation.entity.User;
import ru.xpendence.topjavagraduation.entity.Vote;
import ru.xpendence.topjavagraduation.repository.DishRepository;
import ru.xpendence.topjavagraduation.repository.RestaurantRepository;
import ru.xpendence.topjavagraduation.repository.ReviewRepository;
import ru.xpendence.topjavagraduation.repository.TagRepository;
import ru.xpendence.topjavagraduation.repository.UserRepository;
import ru.xpendence.topjavagraduation.repository.VoteRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;

@Component
public class DataBuilder {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private TagRepository tagRepository;

    private final Random RANDOM = new Random();

    public Restaurant buildRestaurant() {
        var restaurant = new Restaurant();
        restaurant.setName(RandomStringUtils.secure().nextAlphanumeric(16));
        return restaurant;
    }

    public Restaurant saveRestaurant() {
        return restaurantRepository.save(buildRestaurant());
    }

    public Dish buildDish(Restaurant restaurant) {
        var dish = new Dish();
        dish.setName(RandomStringUtils.secure().nextAlphanumeric(16));
        dish.setPrice(BigDecimal.valueOf(RANDOM.nextDouble()));
        dish.setActive(RANDOM.nextBoolean());
        dish.setRestaurant(restaurant);
        return dish;
    }

    public Dish saveDish(Restaurant restaurant) {
        return dishRepository.save(buildDish(restaurant));
    }

    public User buildUser() {
        var user = new User();
        user.setUsername(RandomStringUtils.secure().nextAlphanumeric(16));
        user.setPassword(RandomStringUtils.secure().nextAlphanumeric(16));
        return user;
    }

    public User saveUser() {
        return userRepository.save(buildUser());
    }

    public Vote buildVote(User user, Restaurant restaurant) {
        return buildVote(user, restaurant, LocalDate.now());
    }

    public Vote buildVote(User user, Restaurant restaurant, LocalDate date) {
        var vote = new Vote();
        vote.setDate(date);
        vote.setUser(user);
        vote.setRestaurant(restaurant);
        return vote;
    }

    public Vote saveVote(User user, Restaurant restaurant) {
        return voteRepository.save(buildVote(user, restaurant));
    }

    public Vote saveVote(User user, Restaurant restaurant, LocalDate date) {
        return voteRepository.save(buildVote(user, restaurant, date));
    }

    public Review buildReview(User user, Restaurant restaurant) {
        return buildReview(user, restaurant, 5, RandomStringUtils.secure().nextAlphanumeric(32));
    }

    public Review buildReview(User user, Restaurant restaurant, Integer rating, String text) {
        var review = new Review();
        review.setRating(rating);
        review.setText(text);
        review.setDate(LocalDate.now());
        review.setUser(user);
        review.setRestaurant(restaurant);
        return review;
    }

    public Review saveReview(User user, Restaurant restaurant) {
        return reviewRepository.save(buildReview(user, restaurant));
    }

    public Review saveReview(User user, Restaurant restaurant, Integer rating, String text) {
        return reviewRepository.save(buildReview(user, restaurant, rating, text));
    }

    public Tag buildTag() {
        var tag = new Tag();
        tag.setName(RandomStringUtils.secure().nextAlphanumeric(16));
        return tag;
    }

    public Tag saveTag() {
        return tagRepository.save(buildTag());
    }
}
