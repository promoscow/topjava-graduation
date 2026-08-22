package ru.xpendence.topjavagraduation.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.xpendence.topjavagraduation.entity.Tag;
import ru.xpendence.topjavagraduation.repository.TagRepository;
import ru.xpendence.topjavagraduation.service.TagService;

import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository repository;

    public TagServiceImpl(TagRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Tag create(Tag tag) {
        ensureNameIsFree(tag.getName());
        return repository.save(tag);
    }

    @Override
    @Transactional
    public void update(Tag tag) {
        if (Objects.isNull(tag.getId())) {
            throw new IllegalArgumentException("Tag id is null");
        }
        var stored = repository.findById(tag.getId())
                .orElseThrow(() -> new NoSuchElementException(String.format("Tag not found by id: %d", tag.getId())));
        if (!Objects.equals(tag.getName(), stored.getName())) {
            ensureNameIsFree(tag.getName());
        }
        Tag.enrichForUpdate(tag, stored);
        repository.save(stored);
    }

    @Override
    @Transactional(readOnly = true)
    public Tag getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(String.format("Tag not found by id: %d", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Tag> getAll(String name, Pageable pageable) {
        if (StringUtils.hasText(name)) {
            return repository.findByNameContainingIgnoreCase(name, pageable);
        }
        return repository.findAll(pageable);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private void ensureNameIsFree(String name) {
        if (repository.existsByName(name)) {
            throw new IllegalArgumentException(String.format("Tag name already taken: %s", name));
        }
    }
}
