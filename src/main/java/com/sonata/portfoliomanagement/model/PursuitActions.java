package com.sonata.portfoliomanagement.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "pursuit_actions")
public class PursuitActions {

    public PursuitActions() {
    }

    public PursuitActions(int id, String deliveryManager, String account, String pursuit, String ActionItemNumber,
                         String actionDescription, String actionType, String status, String actionOwner, Date dueDate,
                         String DependentActionItem, String Remarks) {
        super();
        this.id = id;
        this.deliveryManager = deliveryManager;
        this.account = account;
        this.pursuit = pursuit;
        this.ActionItemNumber = ActionItemNumber;
        this.actionDescription = actionDescription;
        this.actionType = actionType;
        this.status = status;
        this.actionOwner = actionOwner;
        this.dueDate = dueDate;
        this.DependentActionItem = DependentActionItem;
        this.Remarks = Remarks;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="Delivery_Manager")
    private String deliveryManager;
    @Column(name="Account")
    private String account;
    @Column(name="Pursuit")
    private String pursuit;
    @Column(name="Action_Item_Number")
    private String ActionItemNumber;
    @Column(name="Action_Description")
    private String actionDescription;
    @Column(name="Action_Type")
    private String actionType;
    @Column(name="Status")
    private String status;
    @Column(name="Action_Owner")
    private String actionOwner;
    @Column(name="Due_Date")
    private Date dueDate;
    @Column(name="Dependent_Action_Item")
    private String DependentActionItem;
    @Column(name="Remarks")
    private String Remarks;
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
        return ActionItemNumber;
    }

    public void setActionItemNumber(String actionItemNumber) {
        ActionItemNumber = actionItemNumber;
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
        return DependentActionItem;
    }

    public void setDependentActionItem(String dependentActionItem) {
        DependentActionItem = dependentActionItem;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }
}
