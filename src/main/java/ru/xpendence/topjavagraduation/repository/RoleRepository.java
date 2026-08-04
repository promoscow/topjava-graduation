package ru.xpendence.topjavagraduation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.xpendence.topjavagraduation.entity.Role;
import ru.xpendence.topjavagraduation.entity.type.RoleType;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleType name);
}
