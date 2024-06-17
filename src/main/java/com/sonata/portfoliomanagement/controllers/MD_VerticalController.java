package com.sonata.portfoliomanagement.controllers;

import com.sonata.portfoliomanagement.interfaces.MD_VerticalRepository;
import com.sonata.portfoliomanagement.model.MD_Vertical;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:5173" )
@RestController
@RequestMapping("/mdvertical")
public class MD_VerticalController {

    @Autowired
    private MD_VerticalRepository verticalRepo;

    // Handles GET requests to retrieve all MD_Vertical records
    @GetMapping("/get")
    public ResponseEntity<List<MD_Vertical>> getAllData() {
        List<MD_Vertical> mdVerticals = verticalRepo.findAll();
        return ResponseEntity.ok(mdVerticals);
    }

    // Handles POST requests to create a new MD_Vertical record
    @PostMapping("/save")
    public ResponseEntity<MD_Vertical> createMdVertical(@RequestBody MD_Vertical mdVertical) {
        MD_Vertical createdVertical = verticalRepo.save(mdVertical);
        return new ResponseEntity<>(createdVertical, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<MD_Vertical> updateMdVertical(@RequestBody MD_Vertical updatedVertical) {
        int id = updatedVertical.getId();
        if (!verticalRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        MD_Vertical savedVertical = verticalRepo.save(updatedVertical);
        return ResponseEntity.ok(savedVertical);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteMdVerticalsByIds(@RequestBody List<Integer> verticalIds) {
        try {
            for (Integer verticalId : verticalIds) {
                if (!verticalRepo.existsById(verticalId)) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No verticals found with ID: " + verticalId);
                }
            }
            verticalRepo.deleteAllById(verticalIds);
            return ResponseEntity.status(HttpStatus.OK).body("Verticals with specified IDs have been deleted.");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting verticals.");
        }
    }
}