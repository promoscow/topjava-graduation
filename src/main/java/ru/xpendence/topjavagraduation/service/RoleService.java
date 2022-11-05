package ru.xpendence.topjavagraduation.service;

public interface RoleService {

    void bind(Long id, Long userId);

    void unbind(Long id, Long userId);
}
