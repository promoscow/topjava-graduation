package ru.xpendence.topjavagraduation;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.xpendence.topjavagraduation.entity.Restaurant;
import ru.xpendence.topjavagraduation.repository.RestaurantRepository;

@Component
public class DataBuilder {

    @Autowired
    private RestaurantRepository restaurantRepository;

    public Restaurant buildRestaurant() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(RandomStringUtils.randomAlphabetic(16));
        return restaurant;
    }

    public Restaurant saveRestaurant() {
        return restaurantRepository.save(buildRestaurant());
    }
}
