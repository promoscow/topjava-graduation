package ru.xpendence.topjavagraduation.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "restaurants")
@Getter
@Setter
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.EAGER)
    private Set<Dish> dishes = new HashSet<>();

    @Where(clause = "date = current_date()")
    @OneToMany(mappedBy = "restaurant", fetch = FetchType.EAGER)
    private Set<Vote> votes = new HashSet<>();

    public static void enrichForUpdate(Restaurant forUpdate, Restaurant stored) {
        stored.name = forUpdate.name;
    }
}
