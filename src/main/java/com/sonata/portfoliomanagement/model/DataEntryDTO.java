package com.sonata.portfoliomanagement.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataEntryDTO {
    private String vertical;
    private String classification;
    private String deliveryManager;
    private String account;
    private String projectManager;
    private String projectName;
    private int financialYear;
    private String quarter;
    private String Month;
    private String DeliveryDirector;
    private String Category;
    private String AnnuityOrNonAnnuity;
    private float Value;


    public DataEntryDTO() {
    }

    public DataEntryDTO(String vertical, String classification, String deliveryManager, String account, String projectManager, String projectName, int financialYear, String quarter, String month, String deliveryDirector, String category, String annuityOrNonAnnuity, float value) {
        this.vertical = vertical;
        this.classification = classification;
        this.deliveryManager = deliveryManager;
        this.account = account;
        this.projectManager = projectManager;
        this.projectName = projectName;
        this.financialYear = financialYear;
        this.quarter = quarter;
        this.Month = month;
        this.DeliveryDirector = deliveryDirector;
        this.Category = category;
        this.AnnuityOrNonAnnuity = annuityOrNonAnnuity;
        this.Value = value;
    }
}
