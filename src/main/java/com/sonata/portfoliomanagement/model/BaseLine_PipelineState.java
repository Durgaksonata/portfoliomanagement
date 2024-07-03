package com.sonata.portfoliomanagement.model;

import jakarta.persistence.*;

@Entity
public class BaseLine_PipelineState {

    public BaseLine_PipelineState(){}

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
    @Column(name = "Sum of Pipeline Opportunity")
    private int Sumofpipeline_opportunity;
    @Column(name = "Sum of Pipeline Shaping")
    private int Sumofpipeline_shaping;

    @Column(name = "Sum of Pipeline Pitch")
    private int Sumofpipeline_pitch;


    public BaseLine_PipelineState(int id, String deliveryDirector, String deliveryManager, String account, int financialYear, String quarter, int sumofpipeline_opportunity, int sumofpipeline_shaping, int sumofpipeline_pitch) {
        this.id = id;
        this.deliveryDirector = deliveryDirector;
        this.deliveryManager = deliveryManager;
        this.account = account;
        this.financialYear = financialYear;
        this.quarter = quarter;
        Sumofpipeline_opportunity = sumofpipeline_opportunity;
        Sumofpipeline_shaping = sumofpipeline_shaping;
        Sumofpipeline_pitch = sumofpipeline_pitch;
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

    public int getSumofpipeline_opportunity() {
        return Sumofpipeline_opportunity;
    }

    public void setSumofpipeline_opportunity(int sumofpipeline_opportunity) {
        Sumofpipeline_opportunity = sumofpipeline_opportunity;
    }

    public int getSumofpipeline_shaping() {
        return Sumofpipeline_shaping;
    }

    public void setSumofpipeline_shaping(int sumofpipeline_shaping) {
        Sumofpipeline_shaping = sumofpipeline_shaping;
    }

    public int getSumofpipeline_pitch() {
        return Sumofpipeline_pitch;
    }

    public void setSumofpipeline_pitch(int sumofpipeline_pitch) {
        Sumofpipeline_pitch = sumofpipeline_pitch;
    }
}
