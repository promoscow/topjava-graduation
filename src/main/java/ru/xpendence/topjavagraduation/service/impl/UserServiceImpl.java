package ru.xpendence.topjavagraduation.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.xpendence.topjavagraduation.entity.User;
import ru.xpendence.topjavagraduation.repository.UserRepository;
import ru.xpendence.topjavagraduation.service.RoleService;
import ru.xpendence.topjavagraduation.service.UserService;

import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final RoleService roleService;

    public UserServiceImpl(UserRepository repository, RoleService roleService) {
        this.repository = repository;
        this.roleService = roleService;
    }

    @Override
    public User create(User user) {
        return repository.save(user);
    }

    @Override
    public void update(User user) {
       if (Objects.isNull(user.getId())) {
           throw new IllegalArgumentException("User id is null");
       }
       var stored = repository.findById(user.getId())
               .orElseThrow(() -> new NoSuchElementException(String.format("User not found by id: %d", user.getId())));
       User.enrichForUpdate(user, stored);
       repository.save(user);
    }

    @Override
    public User getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(String.format("User not found by id: %d", id)));
    }

    @Override
    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException(String.format("User not found by username: %s", username)));
    }

    @Override
    @Transactional
    public void addRole(Long id, Long roleId) {
        var role = roleService.getById(roleId);
        var user = getById(id);
        user.getRoles().add(role);
    }

    @Override
    @Transactional
    public void removeRole(Long id, Long roleId) {
        var role = roleService.getById(roleId);
        var user = getById(id);
        user.getRoles().remove(role);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
