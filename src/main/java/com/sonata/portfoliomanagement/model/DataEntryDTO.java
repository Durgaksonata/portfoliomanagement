package com.sonata.portfoliomanagement.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class DataEntryDTO {
    private Integer id;
    private String vertical;
    private String classification;
    private String deliveryManager;
    private String account;
    private String projectManager;
    private String projectName;
    private int financialYear;
    private String quarter;
    private String month;
    private String deliveryDirector;
    private String category;
    private String annuityorNonAnnuity;
    private float value;
    private float budget;


    public DataEntryDTO() {
    }

    public DataEntryDTO(Integer id, String vertical, String classification, String deliveryManager, String account, String projectManager, String projectName, int financialYear, String quarter, String month, String deliveryDirector, String category, String annuityorNonAnnuity, float value, float budget) {
       this.id = id;
        this.vertical = vertical;
        this.classification = classification;
        this.deliveryManager = deliveryManager;
        this.account = account;
        this.projectManager = projectManager;
        this.projectName = projectName;
        this.financialYear = financialYear;
        this.quarter = quarter;
        this.month = month;
        this.deliveryDirector = deliveryDirector;
        this.category = category;
        this.annuityorNonAnnuity = annuityorNonAnnuity;
        this.value = value;
        this.budget = budget;
    }




}
