package ru.xpendence.topjavagraduation;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.xpendence.topjavagraduation.entity.Dish;
import ru.xpendence.topjavagraduation.entity.Restaurant;
import ru.xpendence.topjavagraduation.repository.DishRepository;
import ru.xpendence.topjavagraduation.repository.RestaurantRepository;

import java.math.BigDecimal;
import java.util.Random;

@Component
public class DataBuilder {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private DishRepository dishRepository;

    private final Random RANDOM = new Random();

    public Restaurant buildRestaurant() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(RandomStringUtils.randomAlphabetic(16));
        return restaurant;
    }

    public Restaurant saveRestaurant() {
        return restaurantRepository.save(buildRestaurant());
    }

    public Dish buildDish(Restaurant restaurant) {
        Dish dish = new Dish();
        dish.setName(RandomStringUtils.randomAlphabetic(16));
        dish.setPrice(BigDecimal.valueOf(RANDOM.nextDouble()));
        dish.setActive(RANDOM.nextBoolean());
        dish.setRestaurant(restaurant);
        return dish;
    }

    public Dish saveDish(Restaurant restaurant) {
        return dishRepository.save(buildDish(restaurant));
    }
}
