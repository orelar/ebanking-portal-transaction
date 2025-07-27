package com.example.ebankingportal.repository;

import com.example.ebankingportal.model.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class TransactionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    void findByCustomerIdAndValueDateBetween_ReturnsCorrectTransactions() {
        entityManager.persist(new Transaction("t1", "CUST-123", "IBAN-1", BigDecimal.TEN, "USD", LocalDate.of(2025, Month.JULY, 15), "July Transaction"));

        LocalDate startDate = LocalDate.of(2025, Month.JULY, 1);
        LocalDate endDate = LocalDate.of(2025, Month.JULY, 31);
        PageRequest pageable = PageRequest.of(0, 10);

        Page<Transaction> result = transactionRepository.findByCustomerIdAndValueDateBetween("CUST-123", startDate, endDate, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("t1", result.getContent().get(0).getId());
    }
}