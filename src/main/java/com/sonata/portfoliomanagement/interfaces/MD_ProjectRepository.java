package com.sonata.portfoliomanagement.interfaces;

import com.sonata.portfoliomanagement.model.MD_Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MD_ProjectRepository extends JpaRepository<MD_Project, Integer> {


    Optional<MD_Project> findByProject(String project);
}