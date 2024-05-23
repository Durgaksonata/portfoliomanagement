package com.sonata.portfoliomanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "md_delivery_managers")
public class MD_DeliveryManager {


    public MD_DeliveryManager(Integer id, String delivery_Managers) {
        super();
        this.id = id;
        deliveryManagers = delivery_Managers;
    }
    public MD_DeliveryManager() {}
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="Delivery_Managers")
    private String deliveryManagers;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getDelivery_Managers() {
        return deliveryManagers;
    }
    public void setDelivery_Managers(String delivery_Managers) {
        deliveryManagers = delivery_Managers;
    }


}