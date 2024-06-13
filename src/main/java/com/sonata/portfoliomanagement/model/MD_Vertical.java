package com.sonata.portfoliomanagement.model;

import jakarta.persistence.*;

@Entity
@Table(name = "md_verticals")
public class MD_Vertical {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "vertical")
    private String vertical;

    public MD_Vertical(Integer id, String vertical) {
        this.id = id;
        this.vertical = vertical;
    }

    public MD_Vertical() {}

    public int getId() {
        return id;
    }

    public String getVertical() {
        return vertical;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setVertical(String vertical) {
        this.vertical = vertical;
    }
}