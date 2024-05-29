package com.sonata.portfoliomanagement.model;

import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Data_Entry")
public class DataEntry {
	public DataEntry(Integer id, String month, String vertical, String classification, String deliveryDirector, String deliveryManager,
			String account, String projectManager, String projectName, String category, String annuityorNonAnnuity,
			float value, String type, int financialYear, String quarter, int probability, String projectsOrPursuitStage,
			float confirmed, float upside, float likely, float annuityRevenue, float nonAnnuityRevenue,
			float offshoreCost, float onsiteCost, float totalCost, float offshoreProjectManager,
			float onsiteProjectManager, float billableProjectManager,float budget) {
		super();
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
		this.annuityorNonAnnuity = annuityorNonAnnuity;
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
		this.budget = budget;
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
	private  String annuityorNonAnnuity;
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
	private float budget;
}
