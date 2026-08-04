package ru.xpendence.topjavagraduation.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.xpendence.topjavagraduation.entity.User;
import ru.xpendence.topjavagraduation.entity.type.RoleType;
import ru.xpendence.topjavagraduation.repository.UserRepository;
import ru.xpendence.topjavagraduation.service.RoleService;
import ru.xpendence.topjavagraduation.service.UserService;

import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final RoleService roleService;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository,
                           RoleService roleService,
                           BCryptPasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User create(User user) {
        ensureUsernameIsFree(user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRoles().isEmpty()) {
            user.getRoles().add(roleService.getByName(RoleType.USER));
        }
        return repository.save(user);
    }

    @Override
    @Transactional
    public void update(User user) {
        if (Objects.isNull(user.getId())) {
            throw new IllegalArgumentException("User id is null");
        }
        var stored = repository.findById(user.getId())
                .orElseThrow(() -> new NoSuchElementException(String.format("User not found by id: %d", user.getId())));
        if (!Objects.equals(user.getUsername(), stored.getUsername())) {
            ensureUsernameIsFree(user.getUsername());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User.enrichForUpdate(user, stored);
        repository.save(stored);
    }

    @Override
    @Transactional(readOnly = true)
    public User getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(String.format("User not found by id: %d", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException(String.format("User not found by username: %s", username)));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> getAll(String username, Pageable pageable) {
        if (StringUtils.hasText(username)) {
            return repository.findByUsernameContainingIgnoreCase(username, pageable);
        }
        return repository.findAll(pageable);
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

    private void ensureUsernameIsFree(String username) {
        if (repository.existsByUsername(username)) {
            throw new IllegalArgumentException(String.format("Username already taken: %s", username));
        }
    }
}
