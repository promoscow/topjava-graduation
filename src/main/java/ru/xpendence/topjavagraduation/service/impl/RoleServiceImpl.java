package ru.xpendence.topjavagraduation.service.impl;

import org.springframework.stereotype.Service;
import ru.xpendence.topjavagraduation.entity.Role;
import ru.xpendence.topjavagraduation.repository.RoleRepository;
import ru.xpendence.topjavagraduation.service.RoleService;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;

    public RoleServiceImpl(RoleRepository repository) {
        this.repository = repository;
    }

    @Override
    public Role getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(String.format("Role not found by id: %d", id)));
    }

    @Override
    public List<Role> getAll() {
        return repository.findAll();
    }
}
