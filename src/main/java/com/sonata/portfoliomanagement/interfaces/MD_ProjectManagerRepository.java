package com.sonata.portfoliomanagement.interfaces;

import com.sonata.portfoliomanagement.model.MD_ProjectManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MD_ProjectManagerRepository extends JpaRepository<MD_ProjectManager, Integer> {
    List<MD_ProjectManager> findAllByProjectManagerIn(List<String> pmNames);
}