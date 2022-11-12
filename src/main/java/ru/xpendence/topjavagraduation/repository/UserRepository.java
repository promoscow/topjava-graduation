package ru.xpendence.topjavagraduation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.xpendence.topjavagraduation.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    @Transactional
    @Modifying
    @Query(
            value = "insert into users_roles (user_id, role_id) values (?1, ?2)",
            nativeQuery = true
    )
    void addRole(Long id, Long roleId);

    @Transactional
    @Modifying
    @Query(
            value = "delete from users_roles where user_id = ?1 and role_id = ?2",
            nativeQuery = true
    )
    void removeRole(Long id, Long roleId);
}
