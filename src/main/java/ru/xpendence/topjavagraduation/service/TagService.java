package ru.xpendence.topjavagraduation.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.xpendence.topjavagraduation.entity.Tag;

public interface TagService {

    Tag create(Tag tag);

    void update(Tag tag);

    Tag getById(Long id);

    Page<Tag> getAll(String name, Pageable pageable);

    void delete(Long id);
}
