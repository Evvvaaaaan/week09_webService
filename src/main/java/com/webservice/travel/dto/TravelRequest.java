package com.webservice.travel.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public record TravelRequest(
        @NotBlank @Size(max = 100) String destination,
        @NotBlank @Size(max = 60) String country,
        @NotBlank @Size(max = 100) String city,
        @NotNull LocalDate departureDate,
        @NotNull @Positive Integer durationDays,
        @NotNull @DecimalMin("0.00") @Digits(integer = 10, fraction = 2) BigDecimal budget
) {
}
