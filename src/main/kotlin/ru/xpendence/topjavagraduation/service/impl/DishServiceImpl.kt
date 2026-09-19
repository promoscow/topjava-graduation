package ru.xpendence.topjavagraduation.service.impl

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.xpendence.topjavagraduation.entity.Dish
import ru.xpendence.topjavagraduation.repository.DishRepository
import ru.xpendence.topjavagraduation.service.DishService
import ru.xpendence.topjavagraduation.service.RestaurantService

@Service
class DishServiceImpl(
    private val repository: DishRepository,
    private val restaurantService: RestaurantService,
) : DishService {

    @Transactional
    override fun create(dish: Dish): Dish {
        val restaurant = restaurantService.getById(requireNotNull(dish.restaurant.id))
        return repository.save(
            dish.copy(restaurant = restaurant),
        )
    }

    @Transactional
    override fun update(dish: Dish) {
        val id = dish.id ?: throw IllegalArgumentException("Dish id is null.")
        val stored = repository.findById(id)
            .orElseThrow { NoSuchElementException("Dish not found by id: $id") }
        repository.save(Dish.enrichForUpdate(dish, stored))
    }

    override fun resetMenu(restaurantId: Long) {
        val affected = repository.setActiveFalseForAllByRestaurantId(restaurantId)
        if (affected == 0) {
            throw NoSuchElementException("Dishes not found by restaurant id: $restaurantId")
        }
    }

    @Transactional(readOnly = true)
    override fun getById(id: Long): Dish =
        repository.findByIdWithRestaurant(id)
            .orElseThrow { NoSuchElementException("Dish not found by id: $id") }

    @Transactional(readOnly = true)
    override fun getActiveById(id: Long): Dish =
        repository.findActiveByIdWithRestaurant(id)
            .orElseThrow { NoSuchElementException("Dish not found by id: $id") }

    @Transactional(readOnly = true)
    override fun getAllByRestaurantId(restaurantId: Long, pageable: Pageable): Page<Dish> =
        repository.getAllByRestaurantId(restaurantId, pageable)

    @Transactional(readOnly = true)
    override fun getAllActiveByRestaurantId(restaurantId: Long, pageable: Pageable): Page<Dish> =
        repository.getAllActiveByRestaurantId(restaurantId, pageable)

    override fun delete(id: Long) {
        if (!repository.existsById(id)) {
            throw NoSuchElementException("Dish not found by id: $id")
        }
        repository.deleteById(id)
    }
}
