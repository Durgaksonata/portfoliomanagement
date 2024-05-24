package com.sonata.portfoliomanagement.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "pursuit_tracker")
public class PursuitTracker {

    public PursuitTracker() {
        super();
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "pursuit_id", unique = true, nullable = false)
    private int pursuitid;
    @Column(name="Delivery_Director")
    private String deliveryDirector;
    @Column(name="Delivery_Manager")
    private String deliveryManager;
    @Column(name="Account")
    private String account;
    @Column(name = "type")
    private String type;
    @Column(name="TCV")
    private float tcv;
    @Column(name="Identified_Month")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM-yy")
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM-yy")
    private Date likelyClosureorActualClosure;
    @Column(name="remarks")
    private String remarks;


    @OneToMany(mappedBy = "pursuitTracker", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PursuitActions> pursuitActions;

    public PursuitTracker(int id, String deliveryDirector, int pursuitid, String deliveryManager, String account, String type, float tcv, Date identifiedmonth, String pursuitstatus, String stage, int pursuitProbability, String projectorPursuit, String pursuitorpotential, Date likelyClosureorActualClosure, String remarks, Set<PursuitActions> pursuitActions) {
        this.id = id;
        this.pursuitid = pursuitid;
        this.deliveryDirector = deliveryDirector;
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
        this.remarks = remarks;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPursuitid() {
        return pursuitid;
    }

    public void setPursuitid(int pursuitid) {
        this.pursuitid = pursuitid;
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
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }





}
