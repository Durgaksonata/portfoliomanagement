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
    private String identifiedmonth;
    @Column(name="Pursuit_Status")
    private String pursuitstatus;
    @Column(name="Stage")
    private String stage;
    @Column(name="Pursuit_Probability(%)")
    private int pursuitProbability;
    @Column(name="Project_or_Pursuit")
    private String projectorPursuit;
    @Column(name="pursuit_or_potential")
    private String pursuitorpotential;
    @Column(name="likely_Closure_Or_Actual_Closure")
    private String likelyClosureorActualClosure;
    @Column(name="status_or_remarks")
    private String statusorRemarks;
    @Column(name="Year")
    private int year;

    public PursuitTracker(int id, String deliveryManager, String account, String type, float tcv, String identifiedmonth, String pursuitstatus, String stage, int pursuitprobability, String projectOrPursuit, String pursuitOrpotential, String likelyClosureOrActualClosure, String statusOrRemarks, int year) {
        this.id = id;
        this.deliveryManager = deliveryManager;
        this.account = account;
        this.type = type;
        this.tcv = tcv;
        this.identifiedmonth = identifiedmonth;
        this.pursuitstatus = pursuitstatus;
        this.stage = stage;
        this.pursuitProbability = pursuitprobability;
        this.projectorPursuit = projectOrPursuit;
        this.pursuitorpotential = pursuitOrpotential;
        this.likelyClosureorActualClosure = likelyClosureOrActualClosure;
        this.statusorRemarks = statusOrRemarks;
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

    public String getIdentifiedmonth() {
        return identifiedmonth;
    }

    public void setIdentifiedmonth(String identifiedmonth) {
        this.identifiedmonth = identifiedmonth;
    }

    public String getPursuitstatus() {
        return pursuitstatus;
    }

    public void setPursuitstatus(String pursuitstatus) {
        this.pursuitstatus = pursuitstatus;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public int getPursuitProbability() {
        return pursuitProbability;
    }

    public void setPursuitProbability(int pursuitProbability) {
        this.pursuitProbability = pursuitProbability;
    }

    public String getProjectorPursuit() {
        return projectorPursuit;
    }

    public void setProjectorPursuit(String projectorPursuit) {
        this.projectorPursuit = projectorPursuit;
    }

    public String getPursuitorpotential() {
        return pursuitorpotential;
    }

    public void setPursuitorpotential(String pursuitorpotential) {
        this.pursuitorpotential = pursuitorpotential;
    }

    public String getLikelyClosureorActualClosure() {
        return likelyClosureorActualClosure;
    }

    public void setLikelyClosureorActualClosure(String likelyClosureorActualClosure) {
        this.likelyClosureorActualClosure = likelyClosureorActualClosure;
    }

    public String getStatusorRemarks() {
        return statusorRemarks;
    }

    public void setStatusorRemarks(String statusorRemarks) {
        this.statusorRemarks = statusorRemarks;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }



}
