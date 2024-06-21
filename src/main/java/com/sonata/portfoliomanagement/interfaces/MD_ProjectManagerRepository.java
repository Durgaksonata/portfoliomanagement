package com.sonata.portfoliomanagement.interfaces;

import com.sonata.portfoliomanagement.model.MD_ProjectManager;
import com.sonata.portfoliomanagement.model.MD_Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MD_ProjectManagerRepository extends JpaRepository<MD_ProjectManager, Integer> {
    List<MD_ProjectManager> findAllByProjectManagerIn(List<String> pmNames);

    void deleteByProjectManager(String projectManager);

    List<MD_ProjectManager> findByProjectManager(String projectManager);


}