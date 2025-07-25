package com.example.ebankingportal.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ExchangeRateService {

    @Cacheable("exchange-rates")
    public BigDecimal getRate(String sourceCurrency, String targetCurrency) {

        System.out.println(">>> FAKE API CALL: Getting exchange rate for " + sourceCurrency + " to " + targetCurrency);

        if (sourceCurrency.equals(targetCurrency)) {
            return BigDecimal.ONE;
        }

        return switch (sourceCurrency) {
            case "CHF" -> new BigDecimal("1.10"); // 1 CHF = 1.10 USD
            case "GBP" -> new BigDecimal("1.25"); // 1 GBP = 1.25 USD
            default -> BigDecimal.ONE;
        };
    }
}