package com.example.ebankingportal.config;

import com.example.ebankingportal.model.Transaction;
import com.example.ebankingportal.repository.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final TransactionRepository repository;

    public DataLoader(TransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
//        repository.saveAll(List.of(
//                Transaction.builder()
//                        .id("t1")
//                        .customerId("P-0123456789")
//                        .accountIban("CH-IBAN-1")
//                        .amount(new BigDecimal("-50.00"))
//                        .currency("CHF")
//                        .valueDate(LocalDate.now())
//                        .description("Coffee Shop")
//                        .build()


        repository.saveAll(List.of(
                new Transaction(
                        "t1",
                        "P-0123456789",
                        "CH-IBAN-1",
                        new BigDecimal("-50.00"),
                        "CHF", LocalDate.now(),
                        "Coffee Shop"),
                new Transaction(
                        "t2",
                        "P-0123456789",
                        "CH-IBAN-1",
                        new BigDecimal("2500.00"),
                        "CHF",
                        LocalDate.of(2025, 7, 1),
                        "Salary"),
                new Transaction(
                        "t3",
                        "P-0123456789",
                        "GB-IBAN-2",
                        new BigDecimal("-120.50"),
                        "GBP",
                        LocalDate.of(2025, 7, 20),
                        "Online Shopping"),
                new Transaction(
                        "t4",
                        "P-0123456789",
                        "CH-IBAN-1",
                        new BigDecimal("-25.50"),
                        "CHF",
                        LocalDate.of(2025, 6, 28),
                        "Groceries"),
                new Transaction(
                        "t5",
                        "P-0123456789",
                        "CH-IBAN-1",
                        new BigDecimal("-200.00"),
                        "CHF",
                        LocalDate.of(2025, 8, 2),
                        "Concert Tickets"),
                new Transaction(
                        "t6",
                        "P-9876543210",
                        "US-IBAN-3",
                        new BigDecimal("-40.00"),
                        "USD",
                        LocalDate.of(2025, 7, 18),
                        "Dinner")
        ));

        System.out.println("Data loaded! " + repository.count() + " records in the database.");
    }
}