package com.sonata.portfoliomanagement.interfaces;

import com.sonata.portfoliomanagement.model.MD_Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MD_RolesRepository extends JpaRepository<MD_Roles,Integer>{


    Optional<MD_Roles> findByRoles(String roles);
}