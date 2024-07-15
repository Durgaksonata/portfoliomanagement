package com.sonata.portfoliomanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "accounts")
public class Accounts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="Delivery_Managers")
    private String deliveryManagers;

    @Column(name="Accounts")
    private String accounts;

    public Accounts() {}

    public Accounts(Integer id, String deliveryManagers, String accounts) {
        super();
        this.id = id;
        this.deliveryManagers = deliveryManagers;
        this.accounts = accounts;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeliveryManagers() {
        return deliveryManagers;
    }

    public void setDeliveryManagers(String deliveryManagers) {
        this.deliveryManagers = deliveryManagers;
    }

    public String getAccounts() {
        return accounts;
    }

    public void setAccounts(String accounts) {
        this.accounts = accounts;
    }
}