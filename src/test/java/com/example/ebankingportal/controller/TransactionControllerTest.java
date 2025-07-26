package com.example.ebankingportal.controller;

import com.example.ebankingportal.AbstractIntegrationTest;
import com.example.ebankingportal.dto.PageTotalsDto;
import com.example.ebankingportal.dto.TransactionDto;
import com.example.ebankingportal.dto.TransactionPageDto;
import com.example.ebankingportal.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionService transactionService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public TransactionService transactionService() {
            return Mockito.mock(TransactionService.class);
        }
    }

    @Test
    @WithMockUser(username = "P-123-TEST")
    void getTransactions_ReturnsCorrectData() throws Exception {
        TransactionDto transactionDto = new TransactionDto("t1", BigDecimal.TEN, "USD", "IBAN1", LocalDate.now(), "Test");
        PageTotalsDto pageTotalsDto = new PageTotalsDto(BigDecimal.TEN, BigDecimal.ZERO, "USD");
        TransactionPageDto mockResponse = new TransactionPageDto(List.of(transactionDto), pageTotalsDto, 1, 1);

        when(transactionService.getMonthlyTransactions(anyString(), anyInt(), anyInt(), anyString(), any(Pageable.class)))
                .thenReturn(mockResponse);

        mockMvc.perform(get("/v1/transactions")
                        .param("year", "2025")
                        .param("month", "7")
                        .param("targetCurrency", "USD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("t1"))
                .andExpect(jsonPath("$.pageTotals.totalCredit").value(10));
    }
}