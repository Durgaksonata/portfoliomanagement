package com.sonata.portfoliomanagement.services;

import com.sonata.portfoliomanagement.model.MD_Users;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    void saveUser(MD_Users user);

    MD_Users getUserById(Integer id);

    MD_Users getUserByEmail(String email);
    List<MD_Users> getAllUsers();

    boolean userExistsByEmailAndPassword(String email, String password);

    boolean verifyPassword(String email, String password) throws Exception;

    boolean userExistsByEmail(String email);
    void updateFirstLogin(String email, String password, boolean isFirstLogin);



}
