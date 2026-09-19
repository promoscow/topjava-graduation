package ru.xpendence.topjavagraduation.service.impl

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.xpendence.topjavagraduation.entity.Vote
import ru.xpendence.topjavagraduation.repository.VoteRepository
import ru.xpendence.topjavagraduation.service.RestaurantService
import ru.xpendence.topjavagraduation.service.UserService
import ru.xpendence.topjavagraduation.service.VoteService
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Service
class VoteServiceImpl(
    private val repository: VoteRepository,
    private val userService: UserService,
    private val restaurantService: RestaurantService,
) : VoteService {

    private val votingAvailableUntil = LocalTime.of(11, 0)
    private val timeFormat = DateTimeFormatter.ofPattern("HH:mm")

    @Transactional
    override fun create(vote: Vote): Vote {
        val now = LocalTime.now()
        if (!now.isBefore(votingAvailableUntil)) {
            throw IllegalArgumentException(
                "Too late to vote. Voting available until ${votingAvailableUntil.format(timeFormat)}.",
            )
        }
        val user = userService.getById(requireNotNull(vote.user.id))
        val restaurant = restaurantService.getById(requireNotNull(vote.restaurant.id))
        return repository.save(vote.copy(user = user, restaurant = restaurant))
    }

    @Transactional
    override fun update(vote: Vote) {
        val id = vote.id ?: throw IllegalArgumentException("Vote id is null.")
        val stored = repository.findById(id)
            .orElseThrow { NoSuchElementException("Vote not found by id: $id") }
        val restaurant = restaurantService.getById(requireNotNull(vote.restaurant.id))
        val forUpdate = vote.copy(restaurant = restaurant)
        repository.save(Vote.enrichForUpdate(forUpdate, stored))
    }

    @Transactional(readOnly = true)
    override fun getById(id: Long): Vote =
        repository.findByIdWithDetails(id)
            .orElseThrow { NoSuchElementException("Vote not found by id: $id") }

    @Transactional(readOnly = true)
    override fun getByUserId(userId: Long, date: LocalDate): Vote =
        repository.findByUserIdAndDate(userId, date)
            .orElseThrow {
                NoSuchElementException("Vote not found by user id: $userId and date: $date")
            }
}
