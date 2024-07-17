package com.sonata.portfoliomanagement.model;

import java.util.List;

public class RoleAndNameRequest {
    private List<String> role;

    //    private String role;
    private String name;

    // Getters and setters


    public List<String> getRole() {
        return role;
    }

    public void setRole(List<String> role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}