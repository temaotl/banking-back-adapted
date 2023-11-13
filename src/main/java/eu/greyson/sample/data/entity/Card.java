package eu.greyson.sample.data.entity;


import lombok.Data;

import jakarta.persistence.*;
import java.time.LocalDate;


@Data
@Entity
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean blocked;

    @Column
    private LocalDate dateLocked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

}