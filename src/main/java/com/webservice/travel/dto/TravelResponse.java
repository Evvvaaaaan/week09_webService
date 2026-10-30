package com.webservice.travel.dto;

import com.webservice.travel.entity.Travel;
import java.math.BigDecimal;
import java.time.LocalDate;

public record TravelResponse(
        Long id,
        String destination,
        String country,
        String city,
        LocalDate departureDate,
        Integer durationDays,
        BigDecimal budget
) {
    public static TravelResponse from(Travel travel) {
        return new TravelResponse(travel.getId(), travel.getDestination(), travel.getCountry(),
                travel.getCity(), travel.getDepartureDate(), travel.getDurationDays(), travel.getBudget());
    }
}
