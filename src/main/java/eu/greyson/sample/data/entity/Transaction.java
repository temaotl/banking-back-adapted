package eu.greyson.sample.data.entity;

import lombok.Data;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "creditor")
    private String creditor;

    @Column(name = "debtor")
    private String debtor;

    @Column(nullable = false)
    private LocalDateTime dateCreated;

    @Column(nullable = true)
    private LocalDateTime dateExecuted;

    @Column(nullable = false, length = 3)
    private String currency;

}

