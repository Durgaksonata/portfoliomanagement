package com.sonata.portfoliomanagement.model;

import jakarta.persistence.*;

@Entity
@Table(name = "md_PursuitStatus")
public class MD_PursuitStatus {

    public MD_PursuitStatus(int id, String pursuitStatus) {
        this.id = id;
        this.pursuitStatus = pursuitStatus;
    }

    public MD_PursuitStatus() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "PursuitStatus")
    private String pursuitStatus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPursuitStatus() {
        return pursuitStatus;
    }

    public void setPursuitStatus(String pursuitStatus) {
        this.pursuitStatus = pursuitStatus;
    }
}