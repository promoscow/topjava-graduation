package ru.xpendence.topjavagraduation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.xpendence.topjavagraduation.entity.Vote;

import java.time.LocalDate;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    @Query("SELECT v FROM Vote v JOIN FETCH v.user JOIN FETCH v.restaurant WHERE v.id = :id")
    Optional<Vote> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT v FROM Vote v JOIN FETCH v.user JOIN FETCH v.restaurant WHERE v.user.id = :userId AND v.date = :date")
    Optional<Vote> findByUserIdAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);
}
