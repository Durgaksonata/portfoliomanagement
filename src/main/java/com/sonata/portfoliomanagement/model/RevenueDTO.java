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
    public List<String> getProjectList() {
		return projectList;
	}

	public void setProjectList(List<String> projectList) {
		this.projectList = projectList;
	}

	public List<String> getDmList() {
		return dmList;
	}

	public void setDmList(List<String> dmList) {
		this.dmList = dmList;
	}

	public List<String> getAccountList() {
		return accountList;
	}

	public void setAccountList(List<String> accountList) {
		this.accountList = accountList;
	}

	public List<String> getPmList() {
		return pmList;
	}

	public void setPmList(List<String> pmList) {
		this.pmList = pmList;
	}

	public List<Integer> getFinancialYear() {
		return financialYear;
	}

	public void setFinancialYear(List<Integer> financialYear) {
		this.financialYear = financialYear;
	}

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

	private List<Integer> financialYear;
    private List<String> verticalList;
    private List<String> classificationList;
    private List<String> quarterList;


    public List<String> getQuarterList() {
		return quarterList;
	}

	public void setQuarterList(List<String> quarterList) {
		this.quarterList = quarterList;
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
}






