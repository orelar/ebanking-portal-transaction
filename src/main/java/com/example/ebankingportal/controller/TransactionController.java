package com.example.ebankingportal.controller;

import com.example.ebankingportal.dto.TransactionPageDto;
import com.example.ebankingportal.dto.TransactionDto;
import com.example.ebankingportal.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get monthly transactions", description = "Returns a paginated list of transactions for the authenticated user for a given month and year.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved transactions",
                    content = @Content(schema = @Schema(implementation = TransactionPageDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid parameters supplied",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid",
                    content = @Content)
    })
    @GetMapping("/v1/transactions")
    public TransactionPageDto getTransactions(
            @Parameter(description = "The calendar year, e.g., 2025") @RequestParam int year,
            @Parameter(description = "The calendar month, from 1 to 12") @RequestParam int month,
            @Parameter(description = "The currency for the calculated totals, e.g., USD") @RequestParam String targetCurrency,
            Pageable pageable,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String customerId;
        if (jwt == null) {
            customerId = "P-0123456789";
        } else {
            customerId = jwt.getSubject();
        }
        return transactionService.getMonthlyTransactions(customerId, year, month, targetCurrency, pageable);
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get a single transaction by its ID", description = "Returns the details for a single transaction if it belongs to the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved transaction",
                    content = @Content(schema = @Schema(implementation = TransactionDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid"),
            @ApiResponse(responseCode = "404", description = "Not Found - Transaction with the given ID does not exist or does not belong to the user")
    })
    @GetMapping("/v1/transactions/{transactionId}")
    public TransactionDto getTransactionById(
            @Parameter(description = "The unique ID of the transaction") @PathVariable String transactionId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String customerId = jwt.getSubject();
        return transactionService.getTransactionById(transactionId, customerId);
    }
}