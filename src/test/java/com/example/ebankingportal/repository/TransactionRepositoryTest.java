package com.example.ebankingportal.repository;

import com.example.ebankingportal.model.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TransactionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TransactionRepository transactionRepository;

    @AfterEach
    void tearDown() {
        transactionRepository.deleteAll();
    }

    @Test
    void findByCustomerIdAndValueDateBetween_ReturnsCorrectTransactions() {
        // Arrange
        String customerId = "CUSTOMER-123";
        entityManager.persist(new Transaction("t7", customerId, "IBAN-1", BigDecimal.TEN, "USD", LocalDate.of(2025, Month.JULY, 15), "July Transaction"));
        entityManager.persist(new Transaction("t8", customerId, "IBAN-1", BigDecimal.TEN, "USD", LocalDate.of(2025, Month.JUNE, 15), "June Transaction"));
        entityManager.persist(new Transaction("t9", "CUSTOMER-456", "IBAN-2", BigDecimal.TEN, "USD", LocalDate.of(2025, Month.JULY, 15), "Other Customer Transaction"));

        LocalDate startDate = LocalDate.of(2025, Month.JULY, 1);
        LocalDate endDate = LocalDate.of(2025, Month.JULY, 31);
        PageRequest pageable = PageRequest.of(0, 10);

        // Act
        Page<Transaction> result = transactionRepository.findByCustomerIdAndValueDateBetween(customerId, startDate, endDate, pageable);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals("t7", result.getContent().get(0).getId());
    }
}