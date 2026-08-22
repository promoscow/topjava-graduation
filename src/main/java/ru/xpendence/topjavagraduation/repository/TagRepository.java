package ru.xpendence.topjavagraduation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.xpendence.topjavagraduation.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {

    boolean existsByName(String name);

    Page<Tag> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
