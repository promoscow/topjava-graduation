package ru.xpendence.topjavagraduation.service.impl;

import org.springframework.stereotype.Service;
import ru.xpendence.topjavagraduation.entity.Vote;
import ru.xpendence.topjavagraduation.repository.VoteRepository;
import ru.xpendence.topjavagraduation.service.VoteService;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class VoteServiceImpl implements VoteService {

    private final LocalTime VOTING_AVAILABLE_UNTIL = LocalTime.of(11, 0);
    private final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    private final VoteRepository repository;

    public VoteServiceImpl(VoteRepository repository) {
        this.repository = repository;
    }

    @Override
    public Vote create(Vote vote) {
        var now = LocalTime.now();
        if (now.isBefore(VOTING_AVAILABLE_UNTIL)) {
            return repository.save(vote);
        } else {
            throw new IllegalArgumentException(
                    String.format("Too late to vote. Voting available until %s.", now.format(TIME_FORMAT))
            );
        }
    }

    @Override
    public void update(Vote vote) {
        if (Objects.isNull(vote.getId())) {
            throw new IllegalArgumentException("Vote id is null.");
        }
        var stored = repository.findById(vote.getId())
                .orElseThrow(() -> new NoSuchElementException(String.format("Vote not found by id: %d", vote.getId())));
        Vote.enrichForUpdate(vote, stored);
        repository.save(stored);
    }

    @Override
    public Vote getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(String.format("Vote not found by id: %d", id)));
    }
}
