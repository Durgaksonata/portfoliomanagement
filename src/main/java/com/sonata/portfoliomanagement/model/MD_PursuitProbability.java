package com.sonata.portfoliomanagement.model;

import jakarta.persistence.*;

@Entity
@Table(name = "md-pursuit_probability")
public class MD_PursuitProbability {

    public MD_PursuitProbability() {
        super();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="Pursuit_Status")
    private String pursuitStatus;
    private String type;
    @Column(name="Probability(%)")
    private int probability;
    private String Stage;

    public MD_PursuitProbability(Integer id, String pursuitStatus, String type, int probability, String stage) {
        this.id = id;
        this.pursuitStatus = pursuitStatus;
        this.type = type;
        this.probability = probability;
        this.Stage = stage;
    }

    public String getPursuitStatus() {
        return pursuitStatus;
    }

    public void setPursuitStatus(String pursuitStatus) {
        this.pursuitStatus = pursuitStatus;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getProbability() {
        return probability;
    }

    public void setProbability(int probability) {
        this.probability = probability;
    }

    public String getStage() {
        return Stage;
    }

    public void setStage(String stage) {
        Stage = stage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
