package com.sonata.portfoliomanagement.interfaces;

import com.sonata.portfoliomanagement.model.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserLogin,Integer> {
    public UserLogin findByEmailAndPassword(String email, String password);

//    public UserLogin findById(int id);

}
