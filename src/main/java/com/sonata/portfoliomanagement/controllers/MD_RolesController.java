package com.sonata.portfoliomanagement.controllers;

import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/getAll")
    public ResponseEntity<List<MD_Roles>> getAllRoles() {
        List<MD_Roles> rolesList = rolesRepository.findAll();
        return ResponseEntity.ok(rolesList);
    }

    @PostMapping("/save")
    public ResponseEntity<MD_Roles> createRole(@RequestBody MD_Roles role) {
        MD_Roles savedRole = rolesRepository.save(role);
        return ResponseEntity.ok(savedRole);
    }

//	   @GetMapping("/get/{id}")
//	    public ResponseEntity<MD_Roles> getRoleById(@PathVariable Integer id) {
//	        Optional<MD_Roles> role = rolesRepository.findById(id);
//	        if (role.isPresent()) {
//	            return ResponseEntity.ok(role.get());
//	        } else {
//	            return ResponseEntity.notFound().build();
//	        }
//	    }

    @PutMapping("/update")
    public ResponseEntity<String> updateRoles(@RequestBody List<MD_Roles> roles) {
        List<Integer> notFoundIds = roles.stream()
                .filter(roleDetails -> {
                    Integer id = roleDetails.getId();
                    if (id == null) {
                        return true;
                    }
                    Optional<MD_Roles> existingRole = rolesRepository.findById(id);
                    if (existingRole.isPresent()) {
                        MD_Roles roleToUpdate = existingRole.get();
                        roleToUpdate.setRoles(roleDetails.getRoles());
                        rolesRepository.save(roleToUpdate);
                        return false;
                    } else {
                        return true;
                    }
                })
                .map(MD_Roles::getId)
                .collect(Collectors.toList());

        if (!notFoundIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No roles found with IDs: " + notFoundIds.toString());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body("Roles have been successfully updated.");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteRolesByIds(@RequestBody List<Integer> ids) {
        List<Integer> notFoundIds = ids.stream()
                .filter(id -> {
                    Optional<MD_Roles> role = rolesRepository.findById(id);
                    if (role.isEmpty()) {
                        return true;
                    }
                    rolesRepository.deleteById(id);
                    return false;
                })
                .collect(Collectors.toList());

        if (!notFoundIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No roles found with IDs: " + notFoundIds.toString());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body("Roles with specified IDs have been deleted.");
    }
}