package com.sonata.portfoliomanagement.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RevenueDTO {

    private List<String> projectList;
    private List<String> dmList;
    private List<String> accountList;
    private List<String> pmList;
    private List<Integer> financialYear;
    private List<String> verticalList;
    private List<String> classificationList;

    public RevenueDTO(List<String> projectList, List<String> dmList, List<String> accountList, List<String> pmList, List<Integer> financialYear, List<String> verticalList, List<String> classificationList) {
        this.projectList = projectList;
        this.dmList = dmList;
        this.accountList = accountList;
        this.pmList = pmList;
        this.financialYear = financialYear;
        this.verticalList = verticalList;
        this.classificationList = classificationList;
    }

    public RevenueDTO() {
    }

    // Getter and setter methods for myList
    private List<String> myList;

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

    public void setGetanotherList(List<Integer> getanotherList) {
        this.getanotherList = getanotherList;
    }

    // Method to check if the DTO is empty
    public boolean isEmpty() {
        return projectList == null && dmList == null && accountList == null && pmList == null && financialYear == null && verticalList == null && classificationList == null;
    }
}






