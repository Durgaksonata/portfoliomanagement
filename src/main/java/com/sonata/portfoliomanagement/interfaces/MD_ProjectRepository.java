package com.sonata.portfoliomanagement.interfaces;

import com.sonata.portfoliomanagement.model.MD_Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MD_ProjectRepository extends JpaRepository<MD_Project, Integer> {
}