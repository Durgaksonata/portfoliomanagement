package com.sonata.portfoliomanagement.interfaces;

import com.sonata.portfoliomanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmailAndPassword(String email, String password);

    User findByEmail(String email);

    User findByEmailAndPassword(String email, String password);

    boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.isFirstLogin = :isFirstLogin WHERE u.email = :email AND u.password = :password")
    void updateFirstLoginByEmailAndPassword(@Param("email") String email, @Param("password") String password, @Param("isFirstLogin") boolean isFirstLogin);


}