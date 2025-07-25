package com.example.ebankingportal.repository;

import com.example.ebankingportal.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

    Page<Transaction> findByCustomerIdAndValueDateBetween(
            String customerId,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    );
}