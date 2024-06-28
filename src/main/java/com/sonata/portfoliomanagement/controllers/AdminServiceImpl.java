package com.sonata.portfoliomanagement.controllers;

import com.sonata.portfoliomanagement.interfaces.MD_AdminRepositoy;
import com.sonata.portfoliomanagement.model.MD_Admin;
import com.sonata.portfoliomanagement.services.AESUtil;
import com.sonata.portfoliomanagement.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.List;
import java.util.Objects;
@Service
public class AdminServiceImpl implements AdminService {

    private final MD_AdminRepositoy adminRepository;
    private SecretKey secretKey;
    @Autowired
    public AdminServiceImpl(MD_AdminRepositoy adminRepository) {
        this.adminRepository = adminRepository;
    }

    // Registration: Save the user with a hashed password
    @Override
    public void saveUser(MD_Admin user) {
        try {
            // Encrypt the user's password before saving
            if(!user.isFirstLogin()){
                String encryptedPassword = AESUtil.decrypt(user.getPassword());
                user.setPassword(encryptedPassword);
                adminRepository.save(user);
            }
            String encryptedPassword = AESUtil.encrypt(user.getPassword());
            user.setPassword(encryptedPassword);
            adminRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Login: Retrieve the user and compare hashed passwords
    @Override
    public boolean userExistsByEmailAndPassword(String email, String password) {
        MD_Admin user = adminRepository.findByEmailAndPassword(email, password);
        return user != null; // Return true if user exists, false otherwise
    }



    @Override
    public MD_Admin getUserById(Integer id) {
        return adminRepository.findById(id).orElse(null);
    }

    @Override
    public MD_Admin getUserByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    @Override
    public List<MD_Admin> getAllUsers() {
        return adminRepository.findAll();
    }


    @Override
    public boolean verifyPassword(String email, String password) throws Exception {
        MD_Admin user = adminRepository.findByEmail(email);
        if (user != null) {
            // Use BCrypt's built-in method to verify the password
            String decryptedEncryptedPasswordFromDB = AESUtil.decrypt(user.getPassword());
            return Objects.equals(password, decryptedEncryptedPasswordFromDB);
        }
        return false;
    }


    @Override
    public boolean userExistsByEmail(String email) {
        return adminRepository.existsByEmail(email);
    }

    @Override
    public void updateFirstLogin(String email, String password, boolean isFirstLogin) {
        adminRepository.updateFirstLoginByEmailAndPassword(email, password, isFirstLogin);
    }



}
