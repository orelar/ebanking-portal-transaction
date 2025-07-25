package com.example.ebankingportal.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionDto(String id, BigDecimal amount, String currency, String iban, LocalDate valueDate, String description) {}