package com.sonata.portfoliomanagement.model;

import jakarta.persistence.*;

import java.util.List;
@Entity
@Table(name="md_admin")
public class MD_Admin {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;
        @Column(name="first_name")
        private String firstName;
        @Column(name="last_name")
        private String lastName;

        @ElementCollection
        @Column(name = "role")
        private List<String> role;


        @Column(name="email",nullable = false,length = 100,unique = false)
        private String email;

        @Column (name="password",nullable = false,length = 100,unique = true)
        private String password;

        @Column(name="is_first_login",length = 100)
        private boolean isFirstLogin;

        // Getter and setter for isFirstLogin
        public boolean isFirstLogin() {
            return isFirstLogin;
        }

        public void setFirstLogin(boolean isFirstLogin) {
            this.isFirstLogin = isFirstLogin;
        }

        public MD_Admin() {}


        public MD_Admin(int id, String firstName, String lastName, List<String> role, String email, String password, boolean isFirstLogin) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.role = role;
            this.email = email;
            this.password = password;
            this.isFirstLogin = isFirstLogin;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
        public List<String> getRole() {
            return role;
        }

        public void setRole(List<String> role) {
            this.role = role;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

