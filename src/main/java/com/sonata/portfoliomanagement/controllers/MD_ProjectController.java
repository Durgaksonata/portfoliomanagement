package com.sonata.portfoliomanagement.controllers;

import com.sonata.portfoliomanagement.interfaces.MD_ProjectRepository;
import com.sonata.portfoliomanagement.model.MD_Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
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
    public ResponseEntity<Object> createProject(@RequestBody MD_Project project) {
        // Check if a project with the same name already exists
        Optional<MD_Project> existingProject = projectRepository.findByProject(project.getProject());
        if (existingProject.isPresent()) {
            // If a duplicate project exists, return a conflict response
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Duplicate entry: Project '" + project.getProject() + "' already exists.");
        }

        // Save the new project
        MD_Project createdProject = projectRepository.save(project);
        // Prepare the response message
        String responseMessage = "Data added: Project '" + createdProject.getProject() + "' added successfully.";
        return new ResponseEntity<>(responseMessage, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateProject(@RequestBody MD_Project updatedProject) {
        // Check if the project ID exists
        if (!projectRepository.existsById(updatedProject.getId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "Project ID " + updatedProject.getId() + " not found."));
        }

        // Check if the new project name already exists in the database
        Optional<MD_Project> projectWithSameName = projectRepository.findByProject(updatedProject.getProject());
        if (projectWithSameName.isPresent() && !projectWithSameName.get().getId().equals(updatedProject.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Collections.singletonMap("message", "Project name '" + updatedProject.getProject() + "' already exists."));
        }

        MD_Project existingProject = projectRepository.findById(updatedProject.getId()).get();
        StringBuilder updateMessage = new StringBuilder("Updated successfully: ");

        // Example: Check and update the project name
        if (!existingProject.getProject().equals(updatedProject.getProject())) {
            updateMessage.append("Project name changed from '")
                    .append(existingProject.getProject())
                    .append("' to '")
                    .append(updatedProject.getProject())
                    .append("'. ");
            existingProject.setProject(updatedProject.getProject());
        }

        // Save the updated project
        MD_Project updatedProjectEntity = projectRepository.save(existingProject);

        Map<String, Object> response = new HashMap<>();
        response.put("message", updateMessage.toString());
        response.put("updatedProject", updatedProjectEntity);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteProjects(@RequestBody List<Integer> ids) {
        Set<Integer> uniqueIds = new HashSet<>(ids);
        if (uniqueIds.size() != ids.size()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Duplicate IDs found in the request.");
        }

        List<Integer> notFoundIds = new ArrayList<>();
        List<String> deletedProjectNames = new ArrayList<>();
        for (Integer id : ids) {
            Optional<MD_Project> projectOptional = projectRepository.findById(id);
            if (projectOptional.isEmpty()) {
                notFoundIds.add(id);
            } else {
                MD_Project project = projectOptional.get();
                deletedProjectNames.add(project.getProject());
                projectRepository.deleteById(id);
            }
        }

        if (!notFoundIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No projects found with IDs: " + notFoundIds.toString());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body("Projects " + deletedProjectNames + " deleted successfully");
    }


}