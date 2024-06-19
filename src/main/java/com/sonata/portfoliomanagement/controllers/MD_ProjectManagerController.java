package com.sonata.portfoliomanagement.controllers;

import com.sonata.portfoliomanagement.interfaces.MD_ProjectManagerRepository;

import com.sonata.portfoliomanagement.model.MD_ProjectManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173" )
@RestController
@RequestMapping("/pm")
public class MD_ProjectManagerController {

    @Autowired
    private MD_ProjectManagerRepository projectManagerRepository;

    @GetMapping("/get")
    public ResponseEntity<List<MD_ProjectManager>> getAllProjectManagers() {
        List<MD_ProjectManager> projectManagers = projectManagerRepository.findAll();
        return ResponseEntity.ok(projectManagers);
    }
    @PostMapping("/save")
    public ResponseEntity<List<MD_ProjectManager>> createProjectManagers(@RequestBody List<MD_ProjectManager> projectManagerList) {
        List<MD_ProjectManager> createdProjectManagers = new ArrayList<>();
        for (MD_ProjectManager projectManager : projectManagerList) {
            MD_ProjectManager createdProjectManager = projectManagerRepository.save(projectManager);
            createdProjectManagers.add(createdProjectManager);
        }
        return new ResponseEntity<>(createdProjectManagers, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateProjectManagers(@RequestBody List<MD_ProjectManager> projectManagers) {
        List<Integer> notFoundIds = projectManagers.stream()
                .filter(manager -> {
                    Integer id = manager.getId();
                    if (id == null) {
                        return true;
                    }
                    Optional<MD_ProjectManager> existingManager = projectManagerRepository.findById(id);
                    if (existingManager.isPresent()) {
                        MD_ProjectManager managerToUpdate = existingManager.get();
                        managerToUpdate.setProjectManager(manager.getProjectManager());
                        projectManagerRepository.save(managerToUpdate);
                        return false;
                    } else {
                        return true;
                    }
                })
                .map(MD_ProjectManager::getId)
                .collect(Collectors.toList());

        if (!notFoundIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No project managers found with IDs: " + notFoundIds.toString());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body("Project managers have been successfully updated.");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteProjectManagersByIds(@RequestBody List<Integer> ids) {
        List<Integer> notFoundIds = ids.stream()
                .filter(id -> {
                    Optional<MD_ProjectManager> manager = projectManagerRepository.findById(id);
                    if (manager.isPresent()) {
                        projectManagerRepository.deleteById(id);
                        return false;
                    } else {
                        return true;
                    }
                })
                .collect(Collectors.toList());

        if (!notFoundIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No project managers found with IDs: " + notFoundIds.toString());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body("Project managers have been deleted successfully.");
    }
}