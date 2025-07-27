package com.example.ebankingportal.repository;

import com.example.ebankingportal.AbstractIntegrationTest;
import com.example.ebankingportal.model.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TransactionRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @AfterEach
    void tearDown() {
        transactionRepository.deleteAll();
    }

    @Test
    void findByCustomerIdAndValueDateBetween_ReturnsCorrectTransactions() {
        String customerId = "CUSTOMER-123";

        transactionRepository.save(new Transaction("t7", customerId, "IBAN-1", BigDecimal.TEN, "USD", LocalDate.of(2025, Month.JULY, 15), "July Transaction"));

        LocalDate startDate = LocalDate.of(2025, Month.JULY, 1);
        LocalDate endDate = LocalDate.of(2025, Month.JULY, 31);
        PageRequest pageable = PageRequest.of(0, 10);

        Page<Transaction> result = transactionRepository.findByCustomerIdAndValueDateBetween(customerId, startDate, endDate, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("t7", result.getContent().get(0).getId());
    }
}