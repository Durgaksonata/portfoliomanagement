package com.sonata.portfoliomanagement.controllers;

import java.util.List;

import java.util.Optional;

import com.sonata.portfoliomanagement.interfaces.MD_RolesRepository;
import com.sonata.portfoliomanagement.model.MD_Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/get/{id}")
    public ResponseEntity<MD_Roles> getRoleById(@PathVariable Integer id) {
        Optional<MD_Roles> role = rolesRepository.findById(id);
        if (role.isPresent()) {
            return ResponseEntity.ok(role.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<MD_Roles> updateRoleById(@PathVariable Integer id, @RequestBody MD_Roles roleDetails) {
        Optional<MD_Roles> existingRole = rolesRepository.findById(id);

        if (existingRole.isPresent()) {
            MD_Roles roleToUpdate = existingRole.get();
            roleToUpdate.setRoles(roleDetails.getRoles());

            MD_Roles updatedRole = rolesRepository.save(roleToUpdate);
            return ResponseEntity.ok(updatedRole);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRoleById(@PathVariable Integer id) {
        Optional<MD_Roles> role = rolesRepository.findById(id);

        if (role.isPresent()) {
            rolesRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}