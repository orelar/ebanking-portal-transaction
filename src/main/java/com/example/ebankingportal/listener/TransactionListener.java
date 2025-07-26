package com.example.ebankingportal.listener;

import com.example.ebankingportal.model.Transaction;
import com.example.ebankingportal.repository.TransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TransactionListener {

    private final TransactionRepository transactionRepository;
    private final ObjectMapper objectMapper;

    public TransactionListener(TransactionRepository transactionRepository, ObjectMapper objectMapper) {
        this.transactionRepository = transactionRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "transactions-topic")
    public void listen(String message) {
        try {
            log.info("Received Kafka message: {}", message);
            Transaction transaction = objectMapper.readValue(message, Transaction.class);
            transactionRepository.save(transaction);
            log.info("Saved transaction {} to the database.", transaction.getId());
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage());
        }
    }
}