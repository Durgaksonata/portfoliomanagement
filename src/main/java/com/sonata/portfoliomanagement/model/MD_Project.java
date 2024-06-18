package com.sonata.portfoliomanagement.model;
import jakarta.persistence.*;

@Entity
@Table(name = "md_projects")
public class MD_Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String project;


    public MD_Project() {
    }

    public MD_Project(String account, String project, Integer id) {

        this.project = project;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

}