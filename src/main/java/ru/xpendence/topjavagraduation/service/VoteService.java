package ru.xpendence.topjavagraduation.service;

import ru.xpendence.topjavagraduation.entity.Vote;

public interface VoteService {

    Vote create(Vote vote);

    void update(Vote vote);

    Vote getById(Long id);

    Vote getByUserId(Long userId);
}
