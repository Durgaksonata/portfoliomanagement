package com.sonata.portfoliomanagement.interfaces;

import com.sonata.portfoliomanagement.model.DataEntry;
import com.sonata.portfoliomanagement.model.MD_Users;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface MD_UsersRepository extends JpaRepository<MD_Users, Integer> {

    List<MD_Users> findByFirstNameAndLastName(String firstName,String lastName);


    boolean existsByEmailAndPassword(String email, String password);

    MD_Users findByEmail(String email);

    MD_Users findByEmailAndPassword(String email, String password);

    boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE MD_Users u SET u.isFirstLogin = :isFirstLogin WHERE u.email = :email AND u.password = :password")
    void updateFirstLoginByEmailAndPassword(@Param("email") String email, @Param("password") String password, @Param("isFirstLogin") boolean isFirstLogin);

}