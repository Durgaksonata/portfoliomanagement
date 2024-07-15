package com.sonata.portfoliomanagement.model;

import java.util.List;

public class BaselineData {

    private List<String> deliveryDirector;
    private List<String> deliveryManager;
    private List<String> account;
    private List<SummaryData> previousData;
    private List<SummaryData> currentData;

    // Constructors, getters, and setters

    public BaselineData() {
    }

    public BaselineData(List<String> deliveryDirector, List<String> deliveryManager, List<String> account, List<SummaryData> previousData, List<SummaryData> currentData) {
        this.deliveryDirector = deliveryDirector;
        this.deliveryManager = deliveryManager;
        this.account = account;
        this.previousData = previousData;
        this.currentData = currentData;
    }

    public List<String> getDeliveryDirector() {
        return deliveryDirector;
    }

    public void setDeliveryDirector(List<String> deliveryDirector) {
        this.deliveryDirector = deliveryDirector;
    }

    public List<String> getDeliveryManager() {
        return deliveryManager;
    }

    public void setDeliveryManager(List<String> deliveryManager) {
        this.deliveryManager = deliveryManager;
    }

    public List<String> getAccount() {
        return account;
    }

    public void setAccount(List<String> account) {
        this.account = account;
    }

    public List<SummaryData> getPreviousData() {
        return previousData;
    }

    public void setPreviousData(List<SummaryData> previousData) {
        this.previousData = previousData;
    }

    public List<SummaryData> getCurrentData() {
        return currentData;
    }

    public void setCurrentData(List<SummaryData> currentData) {
        this.currentData = currentData;
    }

    @Override
    public String toString() {
        return "BaselineData{" +
                "deliveryDirector=" + deliveryDirector +
                ", deliveryManager=" + deliveryManager +
                ", account=" + account +
                ", previousData=" + previousData +
                ", currentData=" + currentData +
                '}';
    }
}