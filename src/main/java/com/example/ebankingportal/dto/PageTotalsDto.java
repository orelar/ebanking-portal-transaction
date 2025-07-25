package com.example.ebankingportal.dto;

import java.math.BigDecimal;

public record PageTotalsDto(BigDecimal totalCredit, BigDecimal totalDebit, String currency) {}