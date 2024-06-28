package com.sonata.portfoliomanagement.services;

import com.sonata.portfoliomanagement.model.MD_Admin;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface AdminService {

    void saveUser(MD_Admin user);

    MD_Admin getUserById(Integer id);

    MD_Admin getUserByEmail(String email);
    List<MD_Admin> getAllUsers();

    boolean userExistsByEmailAndPassword(String email, String password);

    boolean verifyPassword(String email, String password) throws Exception;

    boolean userExistsByEmail(String email);


    void updateFirstLogin(String email, String password, boolean isFirstLogin);

}
