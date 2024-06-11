package com.sonata.portfoliomanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "md_accounts")
public class MD_Accounts {



    public MD_Accounts() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String accounts;

    @Column(name="Delivery_Managers")
    private String deliveryManager;
    public MD_Accounts(Integer id, String accounts, String deliveryManager) {
        super();
        this.id = id;
        this.accounts = accounts;
        this.deliveryManager = deliveryManager;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccounts() {
        return accounts;
    }

    public void setAccounts(String accounts) {
        this.accounts = accounts;
    }

    public String getDeliveryManager() {
        return deliveryManager;
    }

    public void setDeliveryManager(String deliveryManager) {
        this.deliveryManager = deliveryManager;
    }
}
