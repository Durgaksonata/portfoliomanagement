package com.sonata.portfoliomanagement.model;

import jakarta.persistence.*;

@Entity
@Table(name = "md_PursuitStatus")
public class MD_PursuitStatus {

    public MD_PursuitStatus(Integer id, String PursuitStatus) {
        this.id = id;
        this.PursuitStatus = PursuitStatus;
    }

    public MD_PursuitStatus() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "PursuitStatus")
    private String PursuitStatus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPursuitStatus() {
        return PursuitStatus;
    }

    public void setPursuitStatus(String pursuitStatus) {
        PursuitStatus = pursuitStatus;
    }
}
