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
    private int id;
    private String accounts;



    public MD_Accounts(Integer id, String accounts) {
        super();
        this.id = id;
        this.accounts = accounts;
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



}