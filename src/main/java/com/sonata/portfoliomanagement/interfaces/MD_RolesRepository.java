package com.sonata.portfoliomanagement.interfaces;

import com.sonata.portfoliomanagement.model.MD_Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MD_RolesRepository extends JpaRepository<MD_Role,Integer>{


    Optional<MD_Role> findByRole(String role);
}