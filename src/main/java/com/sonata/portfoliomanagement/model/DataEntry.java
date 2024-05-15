package com.sonata.portfoliomanagement.model;

import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Data_Entry")
public class DataEntry {
	public DataEntry(Integer id, String month, String vertical, String classification, String deliveryDirector, String deliveryManager, String account, String projectManager, String projectName, String category, String annuityOrNonAnnuity, float value, String type, int financialYear, String quarter, int probability, String projectsOrPursuitStage, float confirmed, float upside, float likely, float annuityRevenue, float nonAnnuityRevenue, float offshoreCost, float onsiteCost, float totalCost, float offshoreProjectManager, float onsiteProjectManager, float billableProjectManager) {
		this.id = id;
		this.month = month;
		this.vertical = vertical;
		this.classification = classification;
		this.deliveryDirector = deliveryDirector;
		this.deliveryManager = deliveryManager;
		this.account = account;
		this.projectManager = projectManager;
		this.projectName = projectName;
		this.category = category;
		this.annuityOrNonAnnuity = annuityOrNonAnnuity;
		this.value = value;
		this.type = type;
		this.financialYear = financialYear;
		this.quarter = quarter;
		this.probability = probability;
		this.projectsOrPursuitStage = projectsOrPursuitStage;
		this.confirmed = confirmed;
		this.upside = upside;
		this.likely = likely;
		this.annuityRevenue = annuityRevenue;
		this.nonAnnuityRevenue = nonAnnuityRevenue;
		this.offshoreCost = offshoreCost;
		this.onsiteCost = onsiteCost;
		this.totalCost = totalCost;
		this.offshoreProjectManager = offshoreProjectManager;
		this.onsiteProjectManager = onsiteProjectManager;
		this.billableProjectManager = billableProjectManager;
	}

	public DataEntry() {
		super();
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	private String month;
	private String vertical;
	private String classification;

	@Column(name="Delivery_Director")
	private String deliveryDirector;
	@Column(name="Delivery_Manager")
	private String deliveryManager;
	private String account;
	@Column(name="Project_Manager")
	private  String projectManager;
	@Column(name="Project_Name")
	private  String projectName;
	private  String category;
	@Column(name="Annuity_or_Non-Annuity")
	private  String annuityOrNonAnnuity;
	private float value;
	private String type;
	@Column(name="Financial_Year")
	private int financialYear;
	private String quarter;
	@Column(name="Probability(%)")
	private int probability;
	@Column(name="Projects_or_Pursuit_Stage")
	private String projectsOrPursuitStage;
	private float confirmed;
	private float upside;
	private float likely;
	@Column(name="Annuity_Revenue")
	private float annuityRevenue;
	@Column(name="Non_Annuity_Revenue")
	private float nonAnnuityRevenue;
	@Column(name="Offshore_Cost")
	private float offshoreCost;
	@Column(name="Onsite_Cost")
	private float onsiteCost;
	@Column(name="Total_Cost")
	private float totalCost;
	@Column(name="Offshore_Project_Manager")
	private float offshoreProjectManager;
	@Column(name="Onsite_Project_Manager")
	private float onsiteProjectManager;
	@Column(name="Billable_Project_Manager")
	private float billableProjectManager;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getVertical() {
		return vertical;
	}
	public void setVertical(String vertical) {
		this.vertical = vertical;
	}
	public String getClassification() {
		return classification;
	}
	public void setClassification(String classification) {
		this.classification = classification;
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
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getCategory() {
		return category;
	}

	public String getDeliveryDirector() {
		return deliveryDirector;
	}

	public void setDeliveryDirector(String deliveryDirector) {
		this.deliveryDirector = deliveryDirector;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	public String getAnnuityOrNonAnnuity() {
		return annuityOrNonAnnuity;
	}
	public void setAnnuityOrNonAnnuity(String annuityOrNonAnnuity) {
		this.annuityOrNonAnnuity = annuityOrNonAnnuity;
	}
	public float getValue() {
		return value;
	}
	public void setValue(float value) {
		this.value = value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getFinancialYear() {
		return financialYear;
	}
	public void setFinancialYear(int financialYear) {
		this.financialYear = financialYear;
	}
	public String getQuarter() {
		return quarter;
	}
	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}
	public int getProbability() {
		return probability;
	}
	public void setProbability(int probability) {
		this.probability = probability;
	}
	public String getProjectsOrPursuitStage() {
		return projectsOrPursuitStage;
	}
	public void setProjectsOrPursuitStage(String projectsOrPursuitStage) {
		this.projectsOrPursuitStage = projectsOrPursuitStage;
	}
	public float getConfirmed() {
		return confirmed;
	}
	public void setConfirmed(float confirmed) {
		this.confirmed = confirmed;
	}
	public float getUpside() {
		return upside;
	}
	public void setUpside(float upside) {
		this.upside = upside;
	}
	public float getLikely() {
		return likely;
	}
	public void setLikely(float likely) {
		this.likely = likely;
	}
	public float getAnnuityRevenue() {
		return annuityRevenue;
	}
	public void setAnnuityRevenue(float annuityRevenue) {
		this.annuityRevenue = annuityRevenue;
	}
	public float getNonAnnuityRevenue() {
		return nonAnnuityRevenue;
	}
	public void setNonAnnuityRevenue(float nonAnnuityRevenue) {
		this.nonAnnuityRevenue = nonAnnuityRevenue;
	}
	public float getOffshoreCost() {
		return offshoreCost;
	}
	public void setOffshoreCost(float offshoreCost) {
		this.offshoreCost = offshoreCost;
	}
	public float getOnsiteCost() {
		return onsiteCost;
	}
	public void setOnsiteCost(float onsiteCost) {
		this.onsiteCost = onsiteCost;
	}
	public float getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(float totalCost) {
		this.totalCost = totalCost;
	}
	public float getOffshoreProjectManager() {
		return offshoreProjectManager;
	}
	public void setOffshoreProjectManager(float offshoreProjectManager) {
		this.offshoreProjectManager = offshoreProjectManager;
	}
	public float getOnsiteProjectManager() {
		return onsiteProjectManager;
	}
	public void setOnsiteProjectManager(float onsiteProjectManager) {
		this.onsiteProjectManager = onsiteProjectManager;
	}
	public float getBillableProjectManager() {
		return billableProjectManager;
	}
	public void setBillableProjectManager(float billableProjectManager) {
		this.billableProjectManager = billableProjectManager;
	}

}
