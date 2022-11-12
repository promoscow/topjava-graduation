package ru.xpendence.topjavagraduation.service.impl;

import org.springframework.stereotype.Service;
import ru.xpendence.topjavagraduation.entity.Vote;
import ru.xpendence.topjavagraduation.repository.VoteRepository;
import ru.xpendence.topjavagraduation.service.VoteService;

import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class VoteServiceImpl implements VoteService {

    private final VoteRepository repository;

    public VoteServiceImpl(VoteRepository repository) {
        this.repository = repository;
    }

    @Override
    public Vote create(Vote vote) {
        return repository.save(vote);
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
