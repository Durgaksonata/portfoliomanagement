package com.sonata.portfoliomanagement.controllers;

import com.sonata.portfoliomanagement.interfaces.MD_ProjectRepository;
import com.sonata.portfoliomanagement.model.MD_Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/update/{id}")
    public ResponseEntity<MD_Project> updateProject(@PathVariable int id, @RequestBody MD_Project updatedProject) {
        if (!projectRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        updatedProject.setId(id);
        MD_Project updatedProjectEntity = projectRepository.save(updatedProject);
        return ResponseEntity.ok(updatedProjectEntity);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable int id) {
        if (!projectRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        projectRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}