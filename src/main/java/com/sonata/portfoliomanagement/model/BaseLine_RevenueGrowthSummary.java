package com.sonata.portfoliomanagement.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class BaseLine_RevenueGrowthSummary {
    public BaseLine_RevenueGrowthSummary() {
    }

    public BaseLine_RevenueGrowthSummary(int id, String deliveryDirector, String deliveryManager, String account, float accountExpected, float forecast, float gap, LocalDate baselineTimestamp) {
        this.id = id;
        this.deliveryDirector = deliveryDirector;
        this.deliveryManager = deliveryManager;
        this.account = account;
        this.accountExpected = accountExpected;
        this.forecast = forecast;
        this.gap = gap;
        this.baselineTimestamp = baselineTimestamp;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="Delivery_Director")
    private String deliveryDirector;

    @Column(name="Delivery_Manager")
    private String deliveryManager;

    private String account;

    @Column(name = "Account_Expected")
    private float accountExpected;

    private float forecast;

    private float gap;

    @Column(name="Baseline Timestamp")
    private LocalDate baselineTimestamp;
}