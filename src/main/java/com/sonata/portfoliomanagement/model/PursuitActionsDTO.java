package com.sonata.portfoliomanagement.model;

public class PursuitActionsDTO {
    private String deliveryManager;
    private String deliveryDirector;
    private String account;
    private String pursuit;

    public PursuitActionsDTO(String deliveryManager, String deliveryDirector, String account, String pursuit) {
        this.deliveryManager = deliveryManager;
        this.deliveryDirector = deliveryDirector;
        this.account = account;
        this.pursuit = pursuit;
    }

    public String getDeliveryManager() {
        return deliveryManager;
    }

    public void setDeliveryManager(String deliveryManager) {
        this.deliveryManager = deliveryManager;
    }

    public String getDeliveryDirector() {
        return deliveryDirector;
    }

    public void setDeliveryDirector(String deliveryDirector) {
        this.deliveryDirector = deliveryDirector;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPursuit() {
        return pursuit;
    }

    public void setPursuit(String pursuit) {
        this.pursuit = pursuit;
    }
}
