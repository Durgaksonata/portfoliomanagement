package com.sonata.portfoliomanagement.controllers;

import com.sonata.portfoliomanagement.interfaces.MD_ProjectRepository;
import com.sonata.portfoliomanagement.model.MD_Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;
@CrossOrigin(origins = "http://localhost:5173" )
@RestController
@RequestMapping("/md_project")
public class MD_ProjectController {

    @Autowired
    private MD_ProjectRepository projectRepository;

    @GetMapping("/getall")
    public ResponseEntity<List<MD_Project>> getAllProjects() {
        List<MD_Project> projects = projectRepository.findAll();
        return ResponseEntity.ok(projects);
    }

    // New GET method to retrieve unique project names without repetition
    @GetMapping("/get")
    public ResponseEntity<List<String>> getUniqueProjects() {
        List<MD_Project> projects = projectRepository.findAll();
        Set<String> uniqueProjectsSet = new HashSet<>();
        List<String> uniqueProjects = new ArrayList<>();

        for (MD_Project project : projects) {
            if (uniqueProjectsSet.add(project.getProject())) {
                uniqueProjects.add(project.getProject());
            }
        }

        return ResponseEntity.ok(uniqueProjects);
    }

    @PostMapping("/save")
    public ResponseEntity<Object> createProject(@Valid @RequestBody MD_Project project) {
        if (project == null) {
            return ResponseEntity.badRequest().body("MD Project object cannot be null. Please provide valid data.");
        }

        if (project.getProject() == null || project.getProject().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Project name cannot be null or empty. Please provide a valid project name.");
        }

        Optional<MD_Project> existingProject = projectRepository.findByProject(project.getProject());
        if (existingProject.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Collections.singletonMap("message", "Duplicate entry: Project '" + project.getProject() + "' already exists."));
        }

        MD_Project createdProject = projectRepository.save(project);
        String responseMessage = "Project '" + createdProject.getProject() + "' added successfully.";
        return new ResponseEntity<>(Collections.singletonMap("message", responseMessage), HttpStatus.CREATED);
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
        StringBuilder updateMessage = new StringBuilder();
        boolean isUpdated = false;


        // Example: Check and update the project name
        if (!existingProject.getProject().equals(updatedProject.getProject())) {
            updateMessage.append("Project name updated from '")
                    .append(existingProject.getProject())
                    .append("' to '")
                    .append(updatedProject.getProject())
                    .append("'. ");
            existingProject.setProject(updatedProject.getProject());
            isUpdated = true;
        }

        if (!isUpdated) {
            return ResponseEntity.ok(Collections.singletonMap("message", "No changes detected"));
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
                .body("Projects: " + deletedProjectNames + " deleted successfully");
    }


}