package com.example.ebankingportal.dto;

import java.util.List;

public record TransactionPageDto(List<TransactionDto> content, PageTotalsDto pageTotals, int totalPages, long totalElements) {}