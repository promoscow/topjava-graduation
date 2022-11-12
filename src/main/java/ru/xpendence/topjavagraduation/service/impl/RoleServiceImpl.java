package ru.xpendence.topjavagraduation.service.impl;

import org.springframework.stereotype.Service;
import ru.xpendence.topjavagraduation.entity.Role;
import ru.xpendence.topjavagraduation.repository.RoleRepository;
import ru.xpendence.topjavagraduation.service.RoleService;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;

    public RoleServiceImpl(RoleRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Role> getAll() {
        return repository.findAll();
    }
}
