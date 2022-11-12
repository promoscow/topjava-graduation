package ru.xpendence.topjavagraduation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.xpendence.topjavagraduation.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
