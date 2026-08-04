package ru.xpendence.topjavagraduation.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.xpendence.topjavagraduation.entity.Dish;
import ru.xpendence.topjavagraduation.repository.DishRepository;
import ru.xpendence.topjavagraduation.service.DishService;
import ru.xpendence.topjavagraduation.service.RestaurantService;

import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class DishServiceImpl implements DishService {

    private final DishRepository repository;
    private final RestaurantService restaurantService;

    public DishServiceImpl(DishRepository repository, RestaurantService restaurantService) {
        this.repository = repository;
        this.restaurantService = restaurantService;
    }

    @Override
    @Transactional
    public Dish create(Dish dish) {
        dish.setRestaurant(restaurantService.getById(dish.getRestaurant().getId()));
        return repository.save(dish);
    }

    @Override
    @Transactional
    public void update(Dish dish) {
        if (Objects.isNull(dish.getId())) {
            throw new IllegalArgumentException("Dish id is null.");
        }
        var stored = repository.findById(dish.getId())
                .orElseThrow(() -> new NoSuchElementException(String.format("Dish not found by id: %d", dish.getId())));
        Dish.enrichForUpdate(dish, stored);
        repository.save(stored);
    }

    @Override
    public void resetMenu(Long restaurantId) {
        var affected = repository.setActiveFalseForAllByRestaurantId(restaurantId);
        if (affected == 0) {
            throw new NoSuchElementException(
                    String.format("Dishes not found by restaurant id: %d", restaurantId)
            );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Dish getById(Long id) {
        return repository.findByIdWithRestaurant(id)
                .orElseThrow(() -> new NoSuchElementException(String.format("Dish not found by id: %d", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Dish> getAllByRestaurantId(Long restaurantId, Pageable pageable) {
        return repository.getAllByRestaurantId(restaurantId, pageable);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
