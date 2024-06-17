package com.sonata.portfoliomanagement.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "md_rateconversion")
public class MD_RateConversion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "financialyear")
    @JsonProperty("financialYear")
    private String financialYear;
    @Column(name = "month")
    @JsonProperty("month")
    private String month;
    @Column(name = "quarter")
    @JsonProperty("quarter")
    private String quarter;
    @Column(name = "usd_to_inr")
    @JsonProperty("USDToINR")
    private float usdToInr;
    @Column(name = "gbp_to_usd")
    @JsonProperty("GBPToUSD")
    private float gbpToUsd;

    // Constructors
    public MD_RateConversion() {}

    public MD_RateConversion(int id, String financialYear, String month, String quarter, float usdToInr, float gbpToUsd) {
        this.id = id;
        this.financialYear = financialYear;
        this.month = month;
        this.quarter = quarter;
        this.usdToInr = usdToInr;
        this.gbpToUsd = gbpToUsd;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(String financialYear) {
        this.financialYear = financialYear;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public float getUsdToInr() {
        return usdToInr;
    }

    public void setUsdToInr(float usdToInr) {
        this.usdToInr = usdToInr;
    }

    public float getGbpToUsd() {
        return gbpToUsd;
    }

    public void setGbpToUsd(float gbpToUsd) {
        this.gbpToUsd = gbpToUsd;
    }

    @Override
    public String toString() {
        return "MDRateConversion{" +
                "id=" + id +
                ", financialYear='" + financialYear + '\'' +
                ", month='" + month + '\'' +
                ", quarter='" + quarter + '\'' +
                ", usdToInr=" + usdToInr +
                ", gbpToUsd=" + gbpToUsd +
                '}';
    }
}