package com.sonata.portfoliomanagement.model;

import java.util.List;

public class RevenueDashboardListDTO {
    private List<String> deliveryDirectors;
    private List<String> deliveryManagers;
    private List<String> accounts;
    private List<Integer> financialYears;

    // Getters and setters
    public List<String> getDeliveryDirectors() {
        return deliveryDirectors;
    }

    public void setDeliveryDirectors(List<String> deliveryDirectors) {
        this.deliveryDirectors = deliveryDirectors;
    }

    public List<String> getDeliveryManagers() {
        return deliveryManagers;
    }

    public void setDeliveryManagers(List<String> deliveryManagers) {
        this.deliveryManagers = deliveryManagers;
    }

    public List<String> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<String> accounts) {
        this.accounts = accounts;
    }

    public List<Integer> getFinancialYears() {
        return financialYears;
    }

    public void setFinancialYears(List<Integer> financialYears) {
        this.financialYears = financialYears;
    }
}
