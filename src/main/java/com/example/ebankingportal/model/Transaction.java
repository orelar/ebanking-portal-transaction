package com.example.ebankingportal.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    private String id;

    private String customerId;

    private String accountIban;

    private BigDecimal amount;

    private String currency;

    private LocalDate valueDate;

    private String description;
}