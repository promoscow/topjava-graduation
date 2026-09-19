package ru.xpendence.topjavagraduation.service.impl

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.xpendence.topjavagraduation.entity.Restaurant
import ru.xpendence.topjavagraduation.repository.RestaurantRepository
import ru.xpendence.topjavagraduation.service.RestaurantService
import java.time.LocalDate

@Service
class RestaurantServiceImpl(
    private val repository: RestaurantRepository,
) : RestaurantService {

    override fun create(restaurant: Restaurant): Restaurant =
        repository.save(restaurant)

    override fun update(restaurant: Restaurant) {
        val id = restaurant.id ?: throw IllegalArgumentException("Restaurant id is null.")
        val stored = repository.findById(id)
            .orElseThrow { NoSuchElementException("Restaurant not found by id: $id") }
        repository.save(Restaurant.enrichForUpdate(restaurant, stored))
    }

    @Transactional(readOnly = true)
    override fun getById(id: Long): Restaurant =
        repository.findById(id)
            .orElseThrow { NoSuchElementException("Restaurant not found by id: $id") }

    @Transactional(readOnly = true)
    override fun getByDishId(dishId: Long): Restaurant =
        repository.getByDishId(dishId)
            .orElseThrow { NoSuchElementException("Restaurant not found by dish id: $dishId") }

    @Transactional(readOnly = true)
    override fun getAll(pageable: Pageable): Page<Restaurant> =
        repository.findAll(pageable)

    @Transactional(readOnly = true)
    override fun getChosen(): Restaurant {
        val today = LocalDate.now()
        return repository.findChosenByVoteDate(today, Pageable.ofSize(1)).firstOrNull()
            ?: throw NoSuchElementException("Chosen restaurant not found for date: $today")
    }

    override fun delete(id: Long) {
        repository.deleteById(id)
    }
}
