package com.sonata.portfoliomanagement.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
public class BaseLine_RevenueGrowthSummary {
    public BaseLine_RevenueGrowthSummary() {
    }

    public BaseLine_RevenueGrowthSummary(int id, String deliveryDirector, String deliveryManager, String account, int financialYear, String quarter, float accountExpected, float forecast, float gap, LocalDate baselineTimestamp) {
        this.id = id;
        this.deliveryDirector = deliveryDirector;
        this.deliveryManager = deliveryManager;
        this.account = account;
        this.financialYear = financialYear;
        this.quarter = quarter;
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

    @Column(name="Financial_Year")
    private int financialYear;

    private String quarter;

    @Column(name = "Account_Expected")
    private float accountExpected;

    private float forecast;

    private float gap;

    @Column(name="Baseline Timestamp")
    private LocalDate baselineTimestamp;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeliveryDirector() {
        return deliveryDirector;
    }

    public void setDeliveryDirector(String deliveryDirector) {
        this.deliveryDirector = deliveryDirector;
    }

    public String getDeliveryManager() {
        return deliveryManager;
    }

    public void setDeliveryManager(String deliveryManager) {
        this.deliveryManager = deliveryManager;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public float getAccountExpected() {
        return accountExpected;
    }

    public void setAccountExpected(float accountExpected) {
        this.accountExpected = accountExpected;
    }

    public float getForecast() {
        return forecast;
    }

    public void setForecast(float forecast) {
        this.forecast = forecast;
    }

    public float getGap() {
        return gap;
    }

    public void setGap(float gap) {
        this.gap = gap;
    }

    public LocalDate getBaselineTimestamp() {
        return baselineTimestamp;
    }

    public void setBaselineTimestamp(LocalDate baselineTimestamp) {
        this.baselineTimestamp = baselineTimestamp;
    }


    public int getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(int financialYear) {
        this.financialYear = financialYear;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }
}