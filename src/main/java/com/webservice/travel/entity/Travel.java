package com.webservice.travel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "travels")
public class Travel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String destination;

    @Column(nullable = false, length = 60)
    private String country;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(name = "departure_date", nullable = false)
    private LocalDate departureDate;

    @Column(name = "duration_days", nullable = false)
    private Integer durationDays;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal budget;

    protected Travel() { }

    public Travel(String destination, String country, String city,
                  LocalDate departureDate, Integer durationDays, BigDecimal budget) {
        this.destination = destination;
        this.country = country;
        this.city = city;
        this.departureDate = departureDate;
        this.durationDays = durationDays;
        this.budget = budget;
    }

    public void update(String destination, String country, String city,
                       LocalDate departureDate, Integer durationDays, BigDecimal budget) {
        this.destination = destination;
        this.country = country;
        this.city = city;
        this.departureDate = departureDate;
        this.durationDays = durationDays;
        this.budget = budget;
    }

    public Long getId() { return id; }
    public String getDestination() { return destination; }
    public String getCountry() { return country; }
    public String getCity() { return city; }
    public LocalDate getDepartureDate() { return departureDate; }
    public Integer getDurationDays() { return durationDays; }
    public BigDecimal getBudget() { return budget; }
}
