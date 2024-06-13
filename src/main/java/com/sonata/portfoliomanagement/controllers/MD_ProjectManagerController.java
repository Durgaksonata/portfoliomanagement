package com.sonata.portfoliomanagement.controllers;

import com.sonata.portfoliomanagement.interfaces.MD_ProjectManagerRepository;

import com.sonata.portfoliomanagement.model.MD_ProjectManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

    @PutMapping("/update/{ids}")
    public ResponseEntity<List<MD_ProjectManager>> updateProjectManagers(@PathVariable List<Integer> ids, @RequestBody List<MD_ProjectManager> updatedProjectManagers) {
        List<MD_ProjectManager> updatedEntities = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            int id = ids.get(i);
            MD_ProjectManager updatedProjectManager = updatedProjectManagers.get(i);
            Optional<MD_ProjectManager> existingProjectManagerOptional = projectManagerRepository.findById(id);
            if (existingProjectManagerOptional.isPresent()) {
                updatedProjectManager.setId(id);
                updatedEntities.add(projectManagerRepository.save(updatedProjectManager));
            }
        }
        return ResponseEntity.ok(updatedEntities);
    }

    @DeleteMapping("/delete/{ids}")
    public ResponseEntity<Void> deleteProjectManagers(@PathVariable List<Integer> ids) {
        for (Integer id : ids) {
            if (projectManagerRepository.existsById(id)) {
                projectManagerRepository.deleteById(id);
            }
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/dmbypm")
    public ResponseEntity<Set<String>> getDistinctDeliveryManagersByProjectManagers(@RequestBody List<String> pmNames) {
        // Find all Project Managers matching the given PM names
        List<MD_ProjectManager> projectManagers = projectManagerRepository.findAllByProjectManagerIn(pmNames);

        // Collect unique Delivery Managers
        Set<String> distinctDeliveryManagers = projectManagers.stream()
                .map(MD_ProjectManager::getDeliveryManager)
                .collect(Collectors.toSet());

        return ResponseEntity.ok(distinctDeliveryManagers);
    }
}