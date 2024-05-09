package com.sonata.portfoliomanagement.controllers;

import com.sonata.portfoliomanagement.interfaces.UserRepository;
import com.sonata.portfoliomanagement.model.User;
import com.sonata.portfoliomanagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean userExistsByEmailAndPassword(String email, String password) {
        User user = userRepository.findByEmailAndPassword(email, password);
        return user != null; // Return true if user exists, false otherwise
    }
    @Override
    public boolean verifyPassword(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            // Here you would typically compare the hashed password stored in the database with the provided password
            // For simplicity, I'll just compare plaintext passwords (which is not recommended in real-world scenarios)
            return user.getPassword().equals(password);
        }
        return false;
    }

    @Override
    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }



}





