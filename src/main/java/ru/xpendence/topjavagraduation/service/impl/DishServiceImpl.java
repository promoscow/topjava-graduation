package ru.xpendence.topjavagraduation.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.xpendence.topjavagraduation.entity.Dish;
import ru.xpendence.topjavagraduation.repository.DishRepository;
import ru.xpendence.topjavagraduation.service.DishService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class DishServiceImpl implements DishService {

    private final DishRepository repository;

    @Autowired
    public DishServiceImpl(DishRepository repository) {
        this.repository = repository;
    }

    @Override
    public Dish create(Dish dish) {
        return repository.save(dish);
    }

    @Override
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
    public Dish get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(String.format("Dish not found by id: %d", id)));
    }

    @Override
    public List<Dish> getAllByRestaurantId(Long restaurantId) {
        return repository.getAllByRestaurantId(restaurantId);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
