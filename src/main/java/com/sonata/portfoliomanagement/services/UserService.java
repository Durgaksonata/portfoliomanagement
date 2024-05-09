package com.sonata.portfoliomanagement.services;

import com.sonata.portfoliomanagement.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    void saveUser(User user);

    User getUserById(Integer id);

    User getUserByEmail(String email);
    List<User> getAllUsers();

    boolean userExistsByEmailAndPassword(String email, String password);

    boolean verifyPassword(String email, String password);

    boolean userExistsByEmail(String email);
}
