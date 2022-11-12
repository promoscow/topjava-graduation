package ru.xpendence.topjavagraduation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.xpendence.topjavagraduation.entity.Vote;

public interface VoteRepository extends JpaRepository<Vote, Long> {
}
