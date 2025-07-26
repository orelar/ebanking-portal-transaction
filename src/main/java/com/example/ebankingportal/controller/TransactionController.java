package com.example.ebankingportal.controller;

import com.example.ebankingportal.dto.TransactionDto;
import com.example.ebankingportal.dto.TransactionPageDto;
import com.example.ebankingportal.model.Transaction;
import com.example.ebankingportal.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
public class TransactionController {

    private final TransactionService transactionService;
    private final KafkaTemplate<String, Transaction> kafkaTemplate;

    public TransactionController(TransactionService transactionService, KafkaTemplate<String, Transaction> kafkaTemplate) {
        this.transactionService = transactionService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get monthly transactions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved transactions",
                    content = @Content(schema = @Schema(implementation = TransactionPageDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/v1/transactions")
    public TransactionPageDto getTransactions(
            @Parameter(description = "The calendar year") @RequestParam int year,
            @Parameter(description = "The calendar month (1-12)") @RequestParam int month,
            @Parameter(description = "The currency for totals") @RequestParam String targetCurrency,
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
    @Operation(summary = "Get a single transaction by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved transaction",
                    content = @Content(schema = @Schema(implementation = TransactionDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @GetMapping("/v1/transactions/{transactionId}")
    public TransactionDto getTransactionById(
            @Parameter(description = "The unique ID of the transaction") @PathVariable String transactionId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String customerId = jwt.getSubject();
        return transactionService.getTransactionById(transactionId, customerId);
    }

    @Operation(summary = "[Test] Send a transaction to Kafka")
    @PostMapping("/v1/test/send-transaction")
    public String sendTestTransaction(@RequestBody Transaction transaction) {
        kafkaTemplate.send("transactions-topic", transaction);
        return "Transaction sent to Kafka topic!";
    }
}