package com.sonata.portfoliomanagement.model;


import jakarta.persistence.*;


@Entity
@Table(name = "cost_data")
public class CostData {


    public CostData(Integer id, String deliveryDirector, String deliveryManager, String account, String projectManager, String projectsorPursuit, int financialYear, String month, float onsPM, float offPM, float billablePM, float totalPM, float offCost, float onsCost, float otherCosts, float total) {
        this.id = id;
        this.deliveryDirector = deliveryDirector;
        this.deliveryManager = deliveryManager;
        this.account = account;
        this.projectManager = projectManager;
        this.projectsorPursuit = projectsorPursuit;
        this.financialYear = financialYear;
        this.month = month;
        this.OnsPM = onsPM;
        this.OffPM = offPM;
        this.BillablePM = billablePM;
        this.TotalPM = totalPM;
        this.OffCost = offCost;
        this.OnsCost = onsCost;
        this.OtherCosts = otherCosts;
        this.Total = total;
    }

    public CostData() {
        super();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="Delivery_Director")
    private String deliveryDirector;

    @Column(name="Delivery_Manager")
    private  String deliveryManager;

    private String account;

    @Column(name="Project_Manager")
    private String projectManager;

    @Column(name="Projects_or_Pursuit")
    private String projectsorPursuit;

    @Column(name="Financial_Year")
    private int financialYear;
    private  String month;


    @Column(name="Onsite_PM(Effort)")
    private float OnsPM;

    @Column(name="Offsite_PM(Effort)")
    private float OffPM ;

    @Column(name="Billable_PM(Effort)")
    private float BillablePM ;

    @Column(name="Total_PM(Effort)")
    private float TotalPM;

    @Column(name="Offshore_Cost")
    private float OffCost;

    @Column(name="Onsite_Cost")
    private float OnsCost;

    @Column(name="Other_Cost")
    private float OtherCosts;

    @Column(name="Total_Cost")
    private float Total;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public int getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(int financialYear) {
        this.financialYear = financialYear;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public float getOnsPM() {
        return OnsPM;
    }

    public void setOnsPM(float onsPM) {
        OnsPM = onsPM;
    }

    public float getOffPM() {
        return OffPM;
    }

    public void setOffPM(float offPM) {
        OffPM = offPM;
    }

    public float getBillablePM() {
        return BillablePM;
    }

    public void setBillablePM(float billablePM) {
        BillablePM = billablePM;
    }

    public float getTotalPM() {
        return TotalPM;
    }

    public void setTotalPM(float totalPM) {
        TotalPM = totalPM;
    }

    public float getOffCost() {
        return OffCost;
    }

    public void setOffCost(float offCost) {
        OffCost = offCost;
    }

    public float getOnsCost() {
        return OnsCost;
    }

    public void setOnsCost(float onsCost) {
        OnsCost = onsCost;
    }

    public float getOtherCosts() {
        return OtherCosts;
    }

    public void setOtherCosts(float otherCosts) {
        OtherCosts = otherCosts;
    }

    public float getTotal() {
        return Total;
    }

    public void setTotal(float total) {
        Total = total;
    }
}