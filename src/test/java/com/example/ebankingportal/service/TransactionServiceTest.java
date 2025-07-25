package com.example.ebankingportal.service;

import com.example.ebankingportal.dto.TransactionPageDto;
import com.example.ebankingportal.model.Transaction;
import com.example.ebankingportal.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ExchangeRateService exchangeRateService;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void getMonthlyTransactions_CalculatesTotalsCorrectly() {
        String customerId = "P-123";
        String targetCurrency = "USD";

        List<Transaction> transactions = List.of(
                new Transaction("t7", customerId, "IBAN1", new BigDecimal("100.00"), "CHF", LocalDate.now(), "Credit"),
                new Transaction("t8", customerId, "IBAN1", new BigDecimal("-50.00"), "CHF", LocalDate.now(), "Debit")
        );
        Page<Transaction> transactionPage = new PageImpl<>(transactions);

        when(transactionRepository.findByCustomerIdAndValueDateBetween(any(), any(), any(), any(Pageable.class)))
                .thenReturn(transactionPage);

        when(exchangeRateService.getRate("CHF", "USD")).thenReturn(new BigDecimal("1.10"));

        TransactionPageDto result = transactionService.getMonthlyTransactions(customerId, 2025, 7, targetCurrency, Pageable.unpaged());

        assertNotNull(result, "The result should not be null.");
        assertEquals(2, result.content().size(), "There should be two transactions in the result.");

        assertEquals(new BigDecimal("110.0000"), result.pageTotals().totalCredit(), "The total credit is incorrect.");
        assertEquals(new BigDecimal("55.0000"), result.pageTotals().totalDebit(), "The total debit is incorrect.");
        assertEquals("USD", result.pageTotals().currency(), "The currency of the totals should be USD.");
    }
}