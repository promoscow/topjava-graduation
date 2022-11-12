package ru.xpendence.topjavagraduation.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.xpendence.topjavagraduation.entity.Restaurant;
import ru.xpendence.topjavagraduation.repository.RestaurantRepository;
import ru.xpendence.topjavagraduation.service.RestaurantService;

import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository repository;

    public RestaurantServiceImpl(RestaurantRepository repository) {
        this.repository = repository;
    }

    @Override
    public Restaurant create(Restaurant restaurant) {
        return repository.save(restaurant);
    }

    @Override
    public void update(Restaurant restaurant) {
        if (Objects.isNull(restaurant.getId())) {
            throw new IllegalArgumentException("Restaurant id is null.");
        }
        var stored = repository.findById(restaurant.getId())
                .orElseThrow(() ->
                        new NoSuchElementException(String.format("Restaurant not found by id: %d", restaurant.getId()))
                );
        Restaurant.enrichForUpdate(restaurant, stored);
        repository.save(stored);
    }

    @Override
    public Restaurant getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(String.format("Restaurant not found by id: %d", id)));
    }

    @Override
    public Page<Restaurant> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
