package com.sonata.portfoliomanagement.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sonata.portfoliomanagement.interfaces.MD_RolesRepository;
import com.sonata.portfoliomanagement.model.MD_Roles;


@RestController
@RequestMapping("/roles")
public class MD_RolesController {

    @Autowired
    private MD_RolesRepository rolesRepository;

    @GetMapping("/get")
    public ResponseEntity<List<MD_Roles>> getAllRoles() {
        List<MD_Roles> rolesList = rolesRepository.findAll();
        return ResponseEntity.ok(rolesList);
    }


    @PostMapping("/save")
    public ResponseEntity<Object> createRole(@RequestBody MD_Roles newRole) {
        // Check if a role with the same name already exists
        Optional<MD_Roles> existingRole = rolesRepository.findByRoles(newRole.getRoles());
        if (existingRole.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Duplicate entry: Role '" + newRole.getRoles() + "' already exists.");
        }

        // Save the new role
        MD_Roles createdRole = rolesRepository.save(newRole);
        return new ResponseEntity<>(createdRole, HttpStatus.CREATED);
    }



    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateRole(@RequestBody MD_Roles updatedRole) {
        Optional<MD_Roles> roleOptional = rolesRepository.findById(updatedRole.getId());

        if (!roleOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        MD_Roles existingRole = roleOptional.get();
        StringBuilder updateMessage = new StringBuilder("Updated successfully: ");

        // Check if the new role name already exists in the database
        Optional<MD_Roles> roleWithSameName = rolesRepository.findByRoles(updatedRole.getRoles());

        if (roleWithSameName.isPresent() && !roleWithSameName.get().getId().equals(updatedRole.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Collections.singletonMap("message", "Role name '" + updatedRole.getRoles() + "' already exists."));
        }

        // Check and update the role name if different
        if (!existingRole.getRoles().equals(updatedRole.getRoles())) {
            updateMessage.append("Role name changed from '")
                    .append(existingRole.getRoles())
                    .append("' to '")
                    .append(updatedRole.getRoles())
                    .append("'. ");
            existingRole.setRoles(updatedRole.getRoles());
        }

        // Save the updated role
        MD_Roles updatedRoleEntity = rolesRepository.save(existingRole);

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
            Optional<MD_Roles> roleOptional = rolesRepository.findById(id);
            if (roleOptional.isEmpty()) {
                notFoundIds.add(id);
            } else {
                MD_Roles role = roleOptional.get();
                deletedRoleNames.add(role.getRoles());
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