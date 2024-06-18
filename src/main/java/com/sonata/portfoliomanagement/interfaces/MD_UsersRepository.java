package com.sonata.portfoliomanagement.interfaces;

import com.sonata.portfoliomanagement.model.DataEntry;
import com.sonata.portfoliomanagement.model.MD_Users;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;



public interface MD_UsersRepository extends JpaRepository<MD_Users, Integer> {

    List<MD_Users> findByFirstNameAndLastName(String firstName,String lastName);

}