package com.sonata.portfoliomanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="md_users")
public class MD_Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="Delivery_Director")
    private String deliveryDirector;
    @Column(name="Delivery_Manager")
    private String deliveryManager;
    @Column(name="Project_Manager")
    private  String projectManager;

    public MD_Users() {}
    public MD_Users(int id, String deliveryDirector, String deliveryManager, String projectManager) {
        super();
        this.id = id;
        this.deliveryDirector = deliveryDirector;
        this.deliveryManager = deliveryManager;
        this.projectManager = projectManager;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getDeliveryDirector() {
        return deliveryDirector;
    }
    public void setDeliveryDirector(String deliveryDirector) {
        this.deliveryDirector = deliveryDirector;
    }
    public String getDeliveryManager() {
        return deliveryManager;
    }
    public void setDeliveryManager(String deliveryManager) {
        this.deliveryManager = deliveryManager;
    }
    public String getProjectManager() {
        return projectManager;
    }
    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }


}