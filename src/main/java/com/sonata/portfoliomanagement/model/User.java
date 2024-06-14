package com.sonata.portfoliomanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="users")
public class User {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name="id",nullable = false,unique = true)
    private int id;

    @Column(name="email",nullable = false,length = 100,unique = false)
    private String email;

    @Column (name="password",nullable = false,length = 100,unique = true)
    private String password;


    @Column(name="is_first_login",length = 100)
    private boolean isFirstLogin;


    public boolean isFirstLogin() {
        return isFirstLogin;
    }

    public void setFirstLogin(boolean isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}