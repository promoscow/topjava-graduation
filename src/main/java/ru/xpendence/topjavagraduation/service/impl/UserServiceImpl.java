package ru.xpendence.topjavagraduation.service.impl;

import org.springframework.stereotype.Service;
import ru.xpendence.topjavagraduation.entity.User;
import ru.xpendence.topjavagraduation.repository.UserRepository;
import ru.xpendence.topjavagraduation.service.UserService;

import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
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
    public void addRole(Long id, Long roleId) {
        repository.addRole(id, roleId);
    }

    @Override
    public void removeRole(Long id, Long roleId) {
        repository.removeRole(id, roleId);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
