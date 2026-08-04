package ru.xpendence.topjavagraduation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

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

    @SQLRestriction("date = current_date()")
    @OneToMany(mappedBy = "restaurant", fetch = FetchType.EAGER)
    private Set<Vote> votes = new HashSet<>();

    public static void enrichForUpdate(Restaurant forUpdate, Restaurant stored) {
        stored.name = forUpdate.name;
    }
}
