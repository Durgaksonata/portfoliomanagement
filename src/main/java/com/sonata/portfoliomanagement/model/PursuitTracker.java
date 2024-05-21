package com.sonata.portfoliomanagement.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.util.Date;

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
    @Column(name = "type")
    private String type;
    @Column(name="TCV")
    private float tcv;
    @Column(name="Identified_Month")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    private Date identifiedmonth;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    private Date likelyClosureorActualClosure;
    @Column(name="Remarks")
    private String Remarks;
    @Column(name="Year")
    private int year;

    public PursuitTracker(int id, String deliveryManager, String account, String type, float tcv, Date identifiedmonth, String pursuitstatus, String stage, int pursuitProbability, String projectorPursuit, String pursuitorpotential, Date likelyClosureorActualClosure, String remarks, int year) {
        this.id = id;
        this.deliveryManager = deliveryManager;
        this.account = account;
        this.type = type;
        this.tcv = tcv;
        this.identifiedmonth = identifiedmonth;
        this.pursuitstatus = pursuitstatus;
        this.stage = stage;
        this.pursuitProbability = pursuitProbability;
        this.projectorPursuit = projectorPursuit;
        this.pursuitorpotential = pursuitorpotential;
        this.likelyClosureorActualClosure = likelyClosureorActualClosure;
        this.Remarks = remarks;
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

    public Date getIdentifiedmonth() {
        return identifiedmonth;
    }

    public void setIdentifiedmonth(Date identifiedmonth) {
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

    public Date getLikelyClosureorActualClosure() {
        return likelyClosureorActualClosure;
    }

    public void setLikelyClosureorActualClosure(Date likelyClosureorActualClosure) {
        this.likelyClosureorActualClosure = likelyClosureorActualClosure;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }



}
