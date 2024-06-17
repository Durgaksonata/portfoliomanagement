package com.sonata.portfoliomanagement.controllers;

import com.sonata.portfoliomanagement.interfaces.MD_ProjectRepository;
import com.sonata.portfoliomanagement.model.MD_Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@CrossOrigin(origins = "http://localhost:5173" )
@RestController
@RequestMapping("/md_project")
public class MD_ProjectController {

    @Autowired
    private MD_ProjectRepository projectRepository;

    @GetMapping("/get")
    public ResponseEntity<List<MD_Project>> getAllProjects() {
        List<MD_Project> projects = projectRepository.findAll();
        return ResponseEntity.ok(projects);
    }

    @PostMapping("/save")
    public ResponseEntity<MD_Project> createProject(@RequestBody MD_Project project) {
        MD_Project createdProject = projectRepository.save(project);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<List<MD_Project>> updateProjects(@RequestBody List<MD_Project> updatedProjects) {
        for (MD_Project project : updatedProjects) {
            if (!projectRepository.existsById(project.getId())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        }
        List<MD_Project> updatedProjectEntities = projectRepository.saveAll(updatedProjects);
        return ResponseEntity.ok(updatedProjectEntities);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteProjects(@RequestBody List<Integer> ids) {
        for (Integer id : ids) {
            if (!projectRepository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        }
        projectRepository.deleteAllById(ids);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/getAccountsByProjects")
    public ResponseEntity<Set<String>> getDistinctAccountsByProjects(@RequestBody List<String> projectNames) {
        List<MD_Project> projects = projectRepository.findAll();
        Set<String> accounts = projects.stream()
                .filter(project -> projectNames.contains(project.getProject()))
                .map(MD_Project::getAccount)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(accounts);
    }
}