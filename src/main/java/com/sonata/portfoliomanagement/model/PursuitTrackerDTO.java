package com.sonata.portfoliomanagement.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class PursuitTrackerDTO {

    private int id;
    private String deliveryDirector;
    private String deliveryManager;
    private String account;
    private String type;
    private float tcv;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM-yy")
    private Date identifiedmonth;

    private String pursuitstatus;
    private String projectorPursuit;
    private String pursuitorpotential;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM-yy")
    private Date likelyClosureorActualClosure;

    private String remarks;

    // Constructors, getters, and setters

    public PursuitTrackerDTO(int id, String deliveryDirector, String deliveryManager, String account, String type, float tcv, Date identifiedmonth, String pursuitstatus, String projectorPursuit, String pursuitorpotential, Date likelyClosureorActualClosure, String remarks) {
        this.id = id;
        this.deliveryDirector = deliveryDirector;
        this.deliveryManager = deliveryManager;
        this.account = account;
        this.type = type;
        this.tcv = tcv;
        this.identifiedmonth = identifiedmonth;
        this.pursuitstatus = pursuitstatus;
        this.projectorPursuit = projectorPursuit;
        this.pursuitorpotential = pursuitorpotential;
        this.likelyClosureorActualClosure = likelyClosureorActualClosure;
        this.remarks = remarks;
    }

    // Getters and setters


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
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
