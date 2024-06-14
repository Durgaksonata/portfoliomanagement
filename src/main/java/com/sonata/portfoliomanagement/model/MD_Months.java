package com.sonata.portfoliomanagement.model;

import jakarta.persistence.*;

@Entity
@Table(name = "md_months")
public class MD_Months {

    public MD_Months(Integer id, String months) {
        this.id = id;
        this.months = months;
    }

    public MD_Months() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "months")
    private String months;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMonths() {
        return months;
    }

    public void setMonths(String months) {
        this.months = months;
    }
}