package com.sonata.portfoliomanagement.model;

import jakarta.persistence.*;

@Entity
public class PipelineState {

    public PipelineState() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "Delivery_Director")
    private String deliveryDirector;
    @Column(name = "Delivery_Manager")
    private String deliveryManager;

    private String account;

    @Column(name = "Financial_Year")
    private int financialYear;

    private String quarter;
    @Column(name = "Sum of Pipeline Opportunity")
    private float SumOfPipeline_opportunity;
    @Column(name = "Sum of Pipeline Shaping")
    private float SumOfPipeline_shaping;

    @Column(name = "Sum of Pipeline Pitch")
    private float SumOfPipeline_pitch;

    @Column(name = "Sum of Pipeline Total")
    private float SumOfPipeline_total;


    public PipelineState(int id, String deliveryDirector, String deliveryManager, String account, int financialYear, String quarter, float sumOfPipeline_opportunity, float sumOfPipeline_shaping, float sumOfPipeline_pitch, float sumOfPipeline_total) {
        this.id = id;
        this.deliveryDirector = deliveryDirector;
        this.deliveryManager = deliveryManager;
        this.account = account;
        this.financialYear = financialYear;
        this.quarter = quarter;
        SumOfPipeline_opportunity = sumOfPipeline_opportunity;
        SumOfPipeline_shaping = sumOfPipeline_shaping;
        SumOfPipeline_pitch = sumOfPipeline_pitch;
        SumOfPipeline_total = sumOfPipeline_total;
    }

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

    public float getSumOfPipeline_opportunity() {
        return SumOfPipeline_opportunity;
    }

    public void setSumOfPipeline_opportunity(float sumOfPipeline_opportunity) {
        SumOfPipeline_opportunity = sumOfPipeline_opportunity;
    }

    public float getSumOfPipeline_shaping() {
        return SumOfPipeline_shaping;
    }

    public void setSumOfPipeline_shaping(float sumOfPipeline_shaping) {
        SumOfPipeline_shaping = sumOfPipeline_shaping;
    }

    public float getSumOfPipeline_pitch() {
        return SumOfPipeline_pitch;
    }

    public void setSumOfPipeline_pitch(float sumOfPipeline_pitch) {
        SumOfPipeline_pitch = sumOfPipeline_pitch;
    }

    public float getSumOfPipeline_total() {
        return SumOfPipeline_total;
    }

    public void setSumOfPipeline_total(float sumOfPipeline_total) {
        SumOfPipeline_total = sumOfPipeline_total;
    }
}