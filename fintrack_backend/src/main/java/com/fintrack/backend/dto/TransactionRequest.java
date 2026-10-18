package com.fintrack.backend.dto;

import com.fintrack.backend.entity.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Inbound DTO for creating or updating a transaction.
 *
 * <p>The owner UID is intentionally absent — it is derived exclusively from
 * the verified Firebase principal in the service layer.
 */
public record TransactionRequest(

        @NotNull(message = "type is required")
        TransactionType type,

        @NotNull(message = "amount is required")
        @DecimalMin(value = "0.01", message = "amount must be greater than zero")
        @Digits(integer = 15, fraction = 4, message = "amount has too many digits")
        BigDecimal amount,

        @NotBlank(message = "description is required")
        @Size(max = 255, message = "description must not exceed 255 characters")
        String description,

        @NotBlank(message = "category is required")
        @Size(max = 100, message = "category must not exceed 100 characters")
        String category,

        @NotNull(message = "transactionDate is required")
        LocalDate transactionDate

) {}
