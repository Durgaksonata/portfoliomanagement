package com.sonata.portfoliomanagement.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sonata.portfoliomanagement.model.MD_Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sonata.portfoliomanagement.interfaces.MD_RolesRepository;

@CrossOrigin(origins = "http://localhost:5173" )

@RestController
@RequestMapping("/roles")
public class MD_RolesController {

    @Autowired
    private MD_RolesRepository rolesRepository;

    @GetMapping("/get")
    public ResponseEntity<List<MD_Role>> getAllRoles() {
        List<MD_Role> rolesList = rolesRepository.findAll();
        return ResponseEntity.ok(rolesList);
    }


    @PostMapping("/save")
    public ResponseEntity<Object> createRole(@RequestBody MD_Role newRole) {
        // Check if a role with the same name already exists
        Optional<MD_Role> existingRole = rolesRepository.findByRole(newRole.getRole());
        if (existingRole.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Duplicate entry: Role '" + newRole.getRole() + "' already exists.");
        }

        // Save the new role
        MD_Role createdRole = rolesRepository.save(newRole);
        return new ResponseEntity<>(createdRole, HttpStatus.CREATED);
    }



    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateRole(@RequestBody MD_Role updatedRole) {
        Optional<MD_Role> roleOptional = rolesRepository.findById(updatedRole.getId());

        if (!roleOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        MD_Role existingRole = roleOptional.get();
        StringBuilder updateMessage = new StringBuilder("Updated successfully: ");

        // Check if the new role name already exists in the database
        Optional<MD_Role> roleWithSameName = rolesRepository.findByRole(updatedRole.getRole());

        if (roleWithSameName.isPresent() && !roleWithSameName.get().getId().equals(updatedRole.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Collections.singletonMap("message", "Role name '" + updatedRole.getRole() + "' already exists."));
        }

        // Check and update the role name if different
        if (!existingRole.getRole().equals(updatedRole.getRole())) {
            updateMessage.append("Role name changed from '")
                    .append(existingRole.getRole())
                    .append("' to '")
                    .append(updatedRole.getRole())
                    .append("'. ");
            existingRole.setRole(updatedRole.getRole());
        }

        // Save the updated role
        MD_Role updatedRoleEntity = rolesRepository.save(existingRole);

        Map<String, Object> response = new HashMap<>();
        response.put("message", updateMessage.toString());
        response.put("updatedRole", updatedRoleEntity);

        return ResponseEntity.ok(response);
    }



    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteRolesByIds(@RequestBody List<Integer> ids) {
        List<Integer> notFoundIds = new ArrayList<>();
        List<String> deletedRoleNames = new ArrayList<>();

        for (Integer id : ids) {
            Optional<MD_Role> roleOptional = rolesRepository.findById(id);
            if (roleOptional.isEmpty()) {
                notFoundIds.add(id);
            } else {
                MD_Role role = roleOptional.get();
                deletedRoleNames.add(role.getRole());
                rolesRepository.deleteById(id);
            }
        }

        if (!notFoundIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No roles found with IDs: " + notFoundIds.toString());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body("Roles " + deletedRoleNames + " deleted successfully");
    }
}