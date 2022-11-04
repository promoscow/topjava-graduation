package ru.xpendence.topjavagraduation.entity;

import lombok.Getter;
import lombok.Setter;
import ru.xpendence.topjavagraduation.entity.type.RoleType;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleType name;
}
