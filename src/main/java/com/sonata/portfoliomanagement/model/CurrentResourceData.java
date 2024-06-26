package com.sonata.portfoliomanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "current_resource_data")
public class CurrentResourceData {

    public CurrentResourceData() {}

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

    @Column(name="Offshore")
    private int offshore;

    @Column(name="Onsite")
    private int onsite;

    @Column(name="Billable")
    private float billable;



    public CurrentResourceData(int id, String deliveryDirector, String deliveryManager, String account,
                               String projectManager, String projectsorPursuit, String functionalArea, int offshore, int onsite,
                               float billable) {
        super();
        this.id = id;
        this.deliveryDirector = deliveryDirector;
        this.deliveryManager = deliveryManager;
        this.account = account;
        this.projectManager = projectManager;
        this.projectsorPursuit = projectsorPursuit;
        this.functionalArea = functionalArea;
        this.offshore = offshore;
        this.onsite = onsite;
        this.billable = billable;
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

    public String getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }

    public String getProjectsorPursuit() {
        return projectsorPursuit;
    }

    public void setProjectsorPursuit(String projectsorPursuit) {
        this.projectsorPursuit = projectsorPursuit;
    }

    public String getFunctionalArea() {
        return functionalArea;
    }

    public void setFunctionalArea(String functionalArea) {
        this.functionalArea = functionalArea;
    }

    public int getOffshore() {
        return offshore;
    }

    public void setOffshore(int offshore) {
        this.offshore = offshore;
    }

    public int getOnsite() {
        return onsite;
    }

    public void setOnsite(int onsite) {
        this.onsite = onsite;
    }

    public float getBillable() {
        return billable;
    }

    public void setBillable(float billable) {
        this.billable = billable;
    }



}