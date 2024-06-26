package com.sonata.portfoliomanagement.model;

import jakarta.persistence.*;

@Entity
@Table(name = "resource_data")
public class ResourceData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "Delivery_Director")
    private String deliveryDirector;

    @Column(name = "Delivery_Manager")
    private String deliveryManager;

    @Column(name = "Account")
    private String account;

    @Column(name = "Project_Manager")
    private String projectManager;

    @Column(name = "Projects_or_Pursuit")
    private String projectsorPursuit;

    @Column(name = "Functional_Area")
    private String functionalArea;

    @Column(name = "Financial_Year")
    private int financialYear;

    @Column(name = "Quarter")
    private String quarter;

    @Column(name = "`Replace`")
    private int replace;

    @Column(name = "`Lateral`")
    private int lateral;

    @Column(name = "Cross_Trained")
    private int crossTrained;

    @Column(name = "ETGs_or_MBAs")
    private int etgsorMBAs;

    @Column(name = "Onsite")
    private int onsite;



    public ResourceData(int id, String deliveryDirector, String deliveryManager, String account, String projectManager,
                        String projectsorPursuit, String functionalArea, int financialYear, String quarter, int replace,
                        int lateral, int crossTrained, int etgsorMBAs, int onsite) {
        super();
        this.id = id;
        this.deliveryDirector = deliveryDirector;
        this.deliveryManager = deliveryManager;
        this.account = account;
        this.projectManager = projectManager;
        this.projectsorPursuit = projectsorPursuit;
        this.functionalArea = functionalArea;
        this.financialYear = financialYear;
        this.quarter = quarter;
        this.replace = replace;
        this.lateral = lateral;
        this.crossTrained = crossTrained;
        this.etgsorMBAs = etgsorMBAs;
        this.onsite = onsite;
    }

    public ResourceData() {}

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

    public String getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }

    public String getFunctionalArea() {
        return functionalArea;
    }

    public void setFunctionalArea(String functionalArea) {
        this.functionalArea = functionalArea;
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

    public int getReplace() {
        return replace;
    }

    public void setReplace(int replace) {
        this.replace = replace;
    }

    public int getLateral() {
        return lateral;
    }

    public void setLateral(int lateral) {
        this.lateral = lateral;
    }

    public int getCrossTrained() {
        return crossTrained;
    }

    public void setCrossTrained(int crossTrained) {
        this.crossTrained = crossTrained;
    }

    public String getProjectsorPursuit() {
        return projectsorPursuit;
    }

    public void setProjectsorPursuit(String projectsorPursuit) {
        this.projectsorPursuit = projectsorPursuit;
    }

    public int getEtgsorMBAs() {
        return etgsorMBAs;
    }

    public void setEtgsorMBAs(int etgsorMBAs) {
        this.etgsorMBAs = etgsorMBAs;
    }

    public int getOnsite() {
        return onsite;
    }

    public void setOnsite(int onsite) {
        this.onsite = onsite;
    }



}