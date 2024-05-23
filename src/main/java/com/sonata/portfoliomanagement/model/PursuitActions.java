package com.sonata.portfoliomanagement.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "pursuit_actions")
public class PursuitActions {

    public PursuitActions() {
    }

    public PursuitActions(int id, String deliveryManager, String account, String pursuit, String actionItemNumber,
                         String actionDescription, String actionType, String status, String actionOwner, Date dueDate,
                         String dependentActionItem, String remarks) {
        super();
        this.id = id;
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
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="Delivery_Manager")
    private String deliveryManager;
    @Column(name="account")
    private String account;
    @Column(name="pursuit")
    private String pursuit;
    @Column(name="action_Item_Number")
    private String actionItemNumber;
    @Column(name="action_Description")
    private String actionDescription;
    @Column(name="action_Type")
    private String actionType;
    @Column(name="status")
    private String status;
    @Column(name="action_Owner")
    private String actionOwner;
    @Column(name="due_Date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    private Date dueDate;
    @Column(name="dependent_Action_Item")
    private String dependentActionItem;
    @Column(name="remarks")
    private String remarks;

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
