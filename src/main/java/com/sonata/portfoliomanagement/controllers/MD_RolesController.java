package com.sonata.portfoliomanagement.controllers;


import com.sonata.portfoliomanagement.interfaces.MD_RolesRepository;
import com.sonata.portfoliomanagement.model.MD_Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import javax.validation.Valid;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/mdrole")
public class MD_RolesController {

    private static final Logger logger = LoggerFactory.getLogger(MD_RolesController.class);

    @Autowired
    private MD_RolesRepository roleRepo;

    @GetMapping("/get")
    public ResponseEntity<List<MD_Role>> getAllRoles() {
        logger.info("Fetching all roles.");
        List<MD_Role> roles = roleRepo.findAll();
        return ResponseEntity.ok(roles);
    }

    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> createRole(@Valid @RequestBody MD_Role newRole) {
        logger.info("Received request to create role: {}", newRole);

        List<MD_Role> existingRoles = roleRepo.findByRole(newRole.getRole());

        if (!existingRoles.isEmpty()) {
            logger.warn("Role creation failed. Role with name '{}' already exists.", newRole.getRole());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Collections.singletonMap("message", "Duplicate Entry: '" + newRole.getRole() + "' already exists."));
        }

        MD_Role createdRole = roleRepo.save(newRole);

        Map<String, Object> response = new HashMap<>();
        response.put("message", " '" + createdRole.getRole() + "'created successfully.");
        response.put("createdRole", createdRole);
        logger.info("Role created successfully: {}", createdRole);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateRole(@Valid @RequestBody MD_Role updatedRole) {
        logger.info("Received request to update role: {}", updatedRole);

        Optional<MD_Role> roleOptional = roleRepo.findById(updatedRole.getId());

        if (!roleOptional.isPresent()) {
            logger.warn("Role update failed. Role with ID '{}' not found.", updatedRole.getId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "Role with ID '" + updatedRole.getId() + "' not found."));
        }

        MD_Role existingRole = roleOptional.get();

        // Find other roles with the same name, excluding the current role being updated
        List<MD_Role> rolesWithSameName = roleRepo.findByRole(updatedRole.getRole()).stream()
                .filter(role -> role.getId() != updatedRole.getId())  // Use '==' for primitive int comparison
                .collect(Collectors.toList());

        if (!rolesWithSameName.isEmpty()) {
            logger.warn("Role update failed. Role with name '{}' already exists.", updatedRole.getRole());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Collections.singletonMap("message", "Role with name '" + updatedRole.getRole() + "' already exists."));
        }

        StringBuilder updateMessage = new StringBuilder("Updated successfully: ");
        boolean isUpdated = false;

        // Update role name if it is different
        if (!existingRole.getRole().equals(updatedRole.getRole())) {
            updateMessage.append("Role name changed from '")
                    .append(existingRole.getRole())
                    .append("' to '")
                    .append(updatedRole.getRole())
                    .append("'. ");
            existingRole.setRole(updatedRole.getRole());
            isUpdated = true;
        }

        if (!isUpdated) {
            logger.info("No changes detected. The provided data is identical to the current record.");
            return ResponseEntity.ok(Collections.singletonMap("message", "Duplicate Entry: '"+ updatedRole.getRole() + " ' already exists."));
        }

        MD_Role updatedRoleEntity = roleRepo.save(existingRole);
        Map<String, Object> response = new HashMap<>();
        response.put("message", updateMessage.toString());
        response.put("updatedRole", updatedRoleEntity);
        logger.info("Role updated successfully: {}", updatedRoleEntity);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteRolesByIds(@RequestBody List<Integer> ids) {
        logger.info("Received request to delete roles with IDs: {}", ids);

        List<Integer> notFoundIds = new ArrayList<>();
        List<String> deletedRoleNames = new ArrayList<>();

        for (Integer id : ids) {
            Optional<MD_Role> roleOptional = roleRepo.findById(id);
            if (roleOptional.isEmpty()) {
                notFoundIds.add(id);
            } else {
                MD_Role role = roleOptional.get();
                deletedRoleNames.add(role.getRole());
                roleRepo.deleteById(id);
            }
        }

        Map<String, Object> response = new HashMap<>();
        if (!notFoundIds.isEmpty()) {
            logger.warn("No roles found with IDs: {}", notFoundIds);
            response.put("message", "Invalid Entries: '" + notFoundIds.toString() + "' doesnot exists.");
            response.put("deletedRoles", Collections.emptyList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        logger.info("Roles deleted successfully: {}", deletedRoleNames);
        response.put("message", "Roles " + deletedRoleNames + " deleted successfully.");
        response.put("deletedRoles", deletedRoleNames);

        return ResponseEntity.ok(response);
    }
}