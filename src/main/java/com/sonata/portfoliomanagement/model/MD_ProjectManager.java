package com.sonata.portfoliomanagement.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "md_projectManager")
public class MD_ProjectManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="projectManager")
    private String projectManager;

    @Column(name="deliveryManager")
    private String deliveryManager;

    public MD_ProjectManager() {}

    public MD_ProjectManager(Integer id, String projectManager, String deliveryManager ) {
        super();
        this.id = id;
        this.projectManager = projectManager;
        this.deliveryManager=deliveryManager;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }

    public String getDeliveryManager() {
        return deliveryManager;
    }

    public void setDeliveryManager(String deliveryManager) {
        this.deliveryManager = deliveryManager;
    }
}