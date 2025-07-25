package com.example.ebankingportal.service;

import com.example.ebankingportal.dto.PageTotalsDto;
import com.example.ebankingportal.dto.TransactionDto;
import com.example.ebankingportal.dto.TransactionPageDto;
import com.example.ebankingportal.model.Transaction;
import com.example.ebankingportal.repository.TransactionRepository;
import com.example.ebankingportal.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final ExchangeRateService exchangeRateService;

    public TransactionService(TransactionRepository transactionRepository, ExchangeRateService exchangeRateService) {
        this.transactionRepository = transactionRepository;
        this.exchangeRateService = exchangeRateService;
    }

    public TransactionPageDto getMonthlyTransactions(String customerId, int year, int month, String targetCurrency, Pageable pageable) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        Page<Transaction> transactionPage = transactionRepository.findByCustomerIdAndValueDateBetween(customerId, startDate, endDate, pageable);

        BigDecimal totalCredit = BigDecimal.ZERO;
        BigDecimal totalDebit = BigDecimal.ZERO;

        List<TransactionDto> transactionDtos = transactionPage.getContent().stream()
                .map(tx -> new TransactionDto(
                        tx.getId(),
                        tx.getAmount(),
                        tx.getCurrency(),
                        tx.getAccountIban(),
                        tx.getValueDate(),
                        tx.getDescription()))
                .collect(Collectors.toList());

        for (Transaction tx : transactionPage.getContent()) {
            BigDecimal rate = exchangeRateService.getRate(tx.getCurrency(), targetCurrency);
            BigDecimal convertedAmount = tx.getAmount().multiply(rate);

            if (convertedAmount.compareTo(BigDecimal.ZERO) > 0) {
                totalCredit = totalCredit.add(convertedAmount);
            } else {
                totalDebit = totalDebit.add(convertedAmount);
            }
        }

        var totals = new PageTotalsDto(totalCredit, totalDebit.abs(), targetCurrency);
        return new TransactionPageDto(transactionDtos, totals, transactionPage.getTotalPages(), transactionPage.getTotalElements());
    }

    public TransactionDto getTransactionById(String transactionId, String customerId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + transactionId));

        if (!transaction.getCustomerId().equals(customerId)) {
            throw new SecurityException("User is not authorized to view this transaction.");
        }

        return new TransactionDto(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getAccountIban(),
                transaction.getValueDate(),
                transaction.getDescription()
        );
    }
}