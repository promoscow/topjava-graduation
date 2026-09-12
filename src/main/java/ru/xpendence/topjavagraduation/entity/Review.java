package ru.xpendence.topjavagraduation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "reviews",
        uniqueConstraints = @UniqueConstraint(
                name = "ux_user_restaurant",
                columnNames = {"user_id", "restaurant_id"}
        )
)
@Getter
@Setter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Column(name = "text", length = 1000)
    private String text;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    public static void enrichForUpdate(Review forUpdate, Review stored) {
        stored.rating = forUpdate.rating;
        stored.text = forUpdate.text;
    }
}
