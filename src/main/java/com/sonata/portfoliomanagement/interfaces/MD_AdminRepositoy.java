package com.sonata.portfoliomanagement.interfaces;



import com.sonata.portfoliomanagement.model.MD_Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MD_AdminRepositoy extends JpaRepository<MD_Admin, Integer> {

    List<MD_Admin> findByFirstNameAndLastName(String firstName, String lastName);


    MD_Admin findByEmail(String email);

    MD_Admin findByEmailAndPassword(String email, String password);

    boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE MD_Admin u SET u.isFirstLogin = :isFirstLogin WHERE u.email = :email AND u.password = :password")
    void updateFirstLoginByEmailAndPassword(@Param("email") String email, @Param("password") String password, @Param("isFirstLogin") boolean isFirstLogin);

}
