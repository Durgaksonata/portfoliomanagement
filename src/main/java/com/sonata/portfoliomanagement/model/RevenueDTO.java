package com.sonata.portfoliomanagement.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
public class RevenueDTO {

    private List<String> projectList;
    private List<String> dmList;
    private List<String> accountList;
    private List<String> pmList;
    private List<Integer> financialYear;
    private List<String> verticalList;
    private List<String> classificationList;
    private List<String> quarterList;

    public RevenueDTO(List<String> verticalList, List<String> classificationList) {
        this.verticalList = verticalList;
        this.classificationList = classificationList;
    }
    public RevenueDTO(List<String> projectList, List<String> dmList, List<String> accountList, List<String> pmList, List<Integer> financialYear, List<String> verticalList, List<String> classificationList,List<String> quarterList) {
        this.projectList = projectList;
        this.dmList = dmList;
        this.accountList = accountList;
        this.pmList = pmList;
        this.financialYear = financialYear;
        this.verticalList = verticalList;
        this.classificationList = classificationList;
        this.quarterList = quarterList;
    }



    // Getter and setter methods for myList
    private List<String> myList;

    public RevenueDTO() {

    }


    public List<String> getMyList() {
        return myList;
    }

    public void setMyList(List<String> myList) {
        this.myList = myList;
    }

    // Getter and setter methods for getanotherList
    private List<Integer> getanotherList;

    public List<Integer> getGetanotherList() {
        return getanotherList;
    }

    // Method to check if the DTO is empty
    public boolean isEmpty() {
        return projectList == null && dmList == null && accountList == null && pmList == null && financialYear == null && verticalList == null && classificationList == null;
    }

    public boolean areAllCriteriaMatched() {
        // Check if all criteria are provided and match
        return this.getFinancialYear() != null &&
                this.getProjectList() != null &&
                this.getVerticalList() != null &&
                this.getClassificationList() != null &&
                this.getDmList() != null &&
                this.getAccountList() != null &&
                this.getPmList() != null;
    }

    // Getter and setter methods for dmList
    public List<String> getDmList() {
        return dmList;
    }

    public void setDmList(List<String> dmList) {
        this.dmList = dmList;
    }


    public List<String> getDeliveryManagerList() {
        return dmList;
    }

    public List<String> getDMList() {return dmList;
    }

    private List<String> projectNames;
    public List<String> getProjectNames() {
        return projectNames;
    }


    // Getter and setter methods for verticalList and classificationList
    public List<String> getVerticalList() {
        return verticalList;
    }

    public void setVerticalList(List<String> verticalList) {
        this.verticalList = verticalList;
    }

    public List<String> getClassificationList() {
        return classificationList;
    }

    public void setClassificationList(List<String> classificationList) {
        this.classificationList = classificationList;
    }
}









