package com.sonata.portfoliomanagement.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "pursuit_actions")
public class PursuitActions {

    public PursuitActions() {
    }

    public PursuitActions(int id, String deliveryDirector, int pursuitid, String deliveryManager, String account, String pursuit, String actionItemNumber, String actionDescription, String actionType, String status, String actionOwner, Date dueDate, String dependentActionItem, String remarks, PursuitTracker pursuitTracker) {
        this.id = id;
        this.pursuitid = pursuitid;
        this.deliveryDirector = deliveryDirector;
        this.deliveryManager = deliveryManager;
        this.account = account;
        this.pursuit = pursuit;
        this.actionItemNumber = actionItemNumber;
        this.actionDescription = actionDescription;
        this.actionType = actionType;
        this.status = status;
        this.actionOwner = actionOwner;
        this.dueDate = dueDate;
        this.dependentActionItem = dependentActionItem;
        this.remarks = remarks;
        this.pursuitTracker = pursuitTracker;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "pursuit_id", nullable = false)
    private int pursuitid;

    @Column(name="Delivery_Director")
    private String deliveryDirector;
    @Column(name="Delivery_Manager")
    private String deliveryManager;
    @Column(name="account")
    private String account;
    @Column(name="pursuit")
    private String pursuit;
    @Column(name="action_Item_Number")
    private String actionItemNumber;
    @Column(name="action_Description",length = 1000)
    private String actionDescription;
    @Column(name="action_Type")
    private String actionType;
    @Column(name="status")
    private String status;
    @Column(name="action_Owner")
    private String actionOwner;
    @Column(name="due_Date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    @Temporal(TemporalType.DATE)
    private Date dueDate;
    @Column(name="dependent_Action_Item")
    private String dependentActionItem;
    @Column(name="remarks", length = 5000)
    private String remarks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pursuit_id", referencedColumnName = "pursuit_id", insertable = false, updatable = false)
    private PursuitTracker pursuitTracker;

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

    public void setPursuitTracker(PursuitTracker pursuitTracker) {
        this.pursuitTracker = pursuitTracker;
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

    public String getPursuit() {
        return pursuit;
    }

    public void setPursuit(String pursuit) {
        this.pursuit = pursuit;
    }

    public String getActionItemNumber() {
        return actionItemNumber;
    }

    public void setActionItemNumber(String actionItemNumber) {
        this.actionItemNumber = actionItemNumber;
    }

    public String getActionDescription() {
        return actionDescription;
    }

    public void setActionDescription(String actionDescription) {
        this.actionDescription = actionDescription;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getActionOwner() {
        return actionOwner;
    }

    public void setActionOwner(String actionOwner) {
        this.actionOwner = actionOwner;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getDependentActionItem() {
        return dependentActionItem;
    }

    public void setDependentActionItem(String dependentActionItem) {
        this.dependentActionItem = dependentActionItem;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
