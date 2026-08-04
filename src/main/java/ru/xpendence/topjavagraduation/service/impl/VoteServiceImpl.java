package ru.xpendence.topjavagraduation.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.xpendence.topjavagraduation.entity.Vote;
import ru.xpendence.topjavagraduation.repository.VoteRepository;
import ru.xpendence.topjavagraduation.service.RestaurantService;
import ru.xpendence.topjavagraduation.service.UserService;
import ru.xpendence.topjavagraduation.service.VoteService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class VoteServiceImpl implements VoteService {

    private final LocalTime VOTING_AVAILABLE_UNTIL = LocalTime.of(11, 0);
    private final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    private final VoteRepository repository;
    private final UserService userService;
    private final RestaurantService restaurantService;

    public VoteServiceImpl(
            VoteRepository repository,
            UserService userService,
            RestaurantService restaurantService
    ) {
        this.repository = repository;
        this.userService = userService;
        this.restaurantService = restaurantService;
    }

    @Override
    @Transactional
    public Vote create(Vote vote) {
        var now = LocalTime.now();
        if (now.isBefore(VOTING_AVAILABLE_UNTIL)) {
            vote.setUser(userService.getById(vote.getUser().getId()));
            vote.setRestaurant(restaurantService.getById(vote.getRestaurant().getId()));
            return repository.save(vote);
        } else {
            throw new IllegalArgumentException(
                    String.format(
                            "Too late to vote. Voting available until %s.",
                            VOTING_AVAILABLE_UNTIL.format(TIME_FORMAT)
                    )
            );
        }
    }

    @Override
    @Transactional
    public void update(Vote vote) {
        if (Objects.isNull(vote.getId())) {
            throw new IllegalArgumentException("Vote id is null.");
        }
        var stored = repository.findById(vote.getId())
                .orElseThrow(() -> new NoSuchElementException(String.format("Vote not found by id: %d", vote.getId())));
        vote.setRestaurant(restaurantService.getById(vote.getRestaurant().getId()));
        Vote.enrichForUpdate(vote, stored);
        repository.save(stored);
    }

    @Override
    @Transactional(readOnly = true)
    public Vote getById(Long id) {
        return repository.findByIdWithDetails(id)
                .orElseThrow(() -> new NoSuchElementException(String.format("Vote not found by id: %d", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public Vote getByUserId(Long userId, LocalDate date) {
        return repository.findByUserIdAndDate(userId, date)
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("Vote not found by user id: %d and date: %s", userId, date)
                ));
    }
}
