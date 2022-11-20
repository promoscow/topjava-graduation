package ru.xpendence.topjavagraduation.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "dishes")
@Getter
@Setter
public class  Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "active")
    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    public static void enrichForUpdate(Dish forUpdate, Dish stored) {
        stored.name = forUpdate.name;
        stored.price = forUpdate.price;
        stored.active = forUpdate.active;
    }
}
