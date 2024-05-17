package com.sonata.portfoliomanagement.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pursuit_tracker")
public class PursuitTracker {

    public PursuitTracker() {
        super();
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="Delivery_Manager")
    private String deliveryManager;
    @Column(name="Account")
    private String account;
    @Column(name="Type")
    private String type;
    @Column(name="TCV")
    private float tcv;
    @Column(name="Identified_Month")
    private String identified_month;
    @Column(name="Pursuit_Status")
    private String pursuit_status;
    @Column(name="Stage")
    private String stage;
    @Column(name="Pursuit_Probability(%)")
    private int pursuit_probability;
    @Column(name="project/pursuit")
    private String project_or_pursuit;
    @Column(name="Pursuit/Potential")
    private String pursuit_or_potential;
    @Column(name="Likely Closure/Actual Closure")
    private String likely_closure_or_actual_closure;
    @Column(name="Status/Remarks")
    private String status_or_remarks;
    @Column(name="Year")
    private int year;

    public PursuitTracker(int id, String deliveryManager, String account, String type, float tcv, String identified_month, String pursuit_status, String stage, int pursuit_probability, String project_or_pursuit, String pursuit_or_potential, String likely_closure_or_actual_closure, String status_or_remarks, int year) {
        this.id = id;
        this.deliveryManager = deliveryManager;
        this.account = account;
        this.type = type;
        this.tcv = tcv;
        this.identified_month = identified_month;
        this.pursuit_status = pursuit_status;
        this.stage = stage;
        this.pursuit_probability = pursuit_probability;
        this.project_or_pursuit = project_or_pursuit;
        this.pursuit_or_potential = pursuit_or_potential;
        this.likely_closure_or_actual_closure = likely_closure_or_actual_closure;
        this.status_or_remarks = status_or_remarks;
        this.year = year;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getTcv() {
        return tcv;
    }

    public void setTcv(float tcv) {
        this.tcv = tcv;
    }

    public String getIdentified_month() {
        return identified_month;
    }

    public void setIdentified_month(String identified_month) {
        this.identified_month = identified_month;
    }

    public String getPursuit_status() {
        return pursuit_status;
    }

    public void setPursuit_status(String pursuit_status) {
        this.pursuit_status = pursuit_status;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public int getPursuit_probability() {
        return pursuit_probability;
    }

    public void setPursuit_probability(int pursuit_probability) {
        this.pursuit_probability = pursuit_probability;
    }

    public String getProject_or_pursuit() {
        return project_or_pursuit;
    }

    public void setProject_or_pursuit(String project_or_pursuit) {
        this.project_or_pursuit = project_or_pursuit;
    }

    public String getPursuit_or_potential() {
        return pursuit_or_potential;
    }

    public void setPursuit_or_potential(String pursuit_or_potential) {
        this.pursuit_or_potential = pursuit_or_potential;
    }

    public String getLikely_closure_or_actual_closure() {
        return likely_closure_or_actual_closure;
    }

    public void setLikely_closure_or_actual_closure(String likely_closure_or_actual_closure) {
        this.likely_closure_or_actual_closure = likely_closure_or_actual_closure;
    }

    public String getStatus_or_remarks() {
        return status_or_remarks;
    }

    public void setStatus_or_remarks(String status_or_remarks) {
        this.status_or_remarks = status_or_remarks;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
