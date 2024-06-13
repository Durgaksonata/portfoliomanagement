package com.sonata.portfoliomanagement.controllers;

import com.sonata.portfoliomanagement.interfaces.MD_PursuitStatusRepository;
import com.sonata.portfoliomanagement.model.MD_PursuitStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@CrossOrigin(origins = "http://localhost:5173" )
@RestController
@RequestMapping("/mdpursuit")
public class MD_PursuitStatusController {

    @Autowired
    MD_PursuitStatusRepository pursuitRepo;

    @GetMapping("/get")
    public ResponseEntity<List<MD_PursuitStatus>> getAllData() {
        List<MD_PursuitStatus> mdmonths = pursuitRepo.findAll();
        return ResponseEntity.ok(mdmonths);
    }

    @PostMapping("/save")
    public ResponseEntity<MD_PursuitStatus> createMdPursuitStatus(@RequestBody MD_PursuitStatus mdMdPursuitStatus) {
        MD_PursuitStatus createdPursuitStatus = pursuitRepo.save(mdMdPursuitStatus);
        return new ResponseEntity<>(createdPursuitStatus, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<MD_PursuitStatus> updateMdcategory(@PathVariable int id, @RequestBody MD_PursuitStatus updatedMdPursuitStatus) {
        // Check if PursuitAction with given id exists
        if (!pursuitRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        // Set the id of updatedPursuitAction
        updatedMdPursuitStatus.setId(id);
        // Save the updated PursuitAction
        MD_PursuitStatus updateMdPursuitStatus = pursuitRepo.save(updatedMdPursuitStatus);
        return ResponseEntity.ok(updateMdPursuitStatus);
    }
}
