package ru.xpendence.topjavagraduation;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.xpendence.topjavagraduation.entity.Dish;
import ru.xpendence.topjavagraduation.entity.Restaurant;
import ru.xpendence.topjavagraduation.entity.User;
import ru.xpendence.topjavagraduation.entity.Vote;
import ru.xpendence.topjavagraduation.repository.DishRepository;
import ru.xpendence.topjavagraduation.repository.RestaurantRepository;
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

    private final Random RANDOM = new Random();

    public Restaurant buildRestaurant() {
        var restaurant = new Restaurant();
        restaurant.setName(RandomStringUtils.randomAlphabetic(16));
        return restaurant;
    }

    public Restaurant saveRestaurant() {
        return restaurantRepository.save(buildRestaurant());
    }

    public Dish buildDish(Restaurant restaurant) {
        var dish = new Dish();
        dish.setName(RandomStringUtils.randomAlphabetic(16));
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
        user.setUsername(RandomStringUtils.randomAlphabetic(16));
        user.setPassword(RandomStringUtils.randomAlphabetic(16));
        return user;
    }

    public User saveUser() {
        return userRepository.save(buildUser());
    }

    public Vote buildVote(User user, Restaurant restaurant) {
        var vote = new Vote();
        vote.setDate(LocalDate.now());
        vote.setUser(user);
        vote.setRestaurant(restaurant);
        return vote;
    }

    public Vote saveVote(User user, Restaurant restaurant) {
        return voteRepository.save(buildVote(user, restaurant));
    }
}
