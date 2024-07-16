package com.sonata.portfoliomanagement.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DeliveryManagerDataDTO {

    public DeliveryManagerDataDTO() { }

    public DeliveryManagerDataDTO(String deliveryDirector, String account, List<DataDTO> previous, List<DataDTO> current, List<String> deliveryManager) {
        this.deliveryDirector = deliveryDirector;
        this.account = account;
        this.previous = previous;
        this.current = current;
        this.deliveryManager = deliveryManager;
    }

    private String deliveryDirector;
    private String account;
    private List<DataDTO> previous;
    private List<DataDTO> current;
    private List<String> deliveryManager;


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

    public List<DataDTO> getPrevious() {
        return previous;
    }

    public void setPrevious(List<DataDTO> previous) {
        this.previous = previous;
    }

    public List<DataDTO> getCurrent() {
        return current;
    }

    public void setCurrent(List<DataDTO> current) {
        this.current = current;
    }

    public List<String> getDeliveryManager() {
        return deliveryManager;
    }

    public void setDeliveryManager(List<String> deliveryManager) {
        this.deliveryManager = deliveryManager;
    }
}