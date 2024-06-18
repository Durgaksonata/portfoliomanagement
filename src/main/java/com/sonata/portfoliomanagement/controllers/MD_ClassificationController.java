package com.sonata.portfoliomanagement.controllers;


import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sonata.portfoliomanagement.interfaces.MD_ClassificationRepository;
import com.sonata.portfoliomanagement.model.MD_Classification;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/mdclassification")
public class MD_ClassificationController {

    @Autowired
    MD_ClassificationRepository classificationRepo;

    @GetMapping("/get")
    public ResponseEntity<List<MD_Classification>> getAllData() {
        List<MD_Classification> mdClassifications = classificationRepo.findAll();
        return ResponseEntity.ok(mdClassifications);
    }



    @PostMapping("/save")
    public ResponseEntity<Object> createMdClassification(@RequestBody MD_Classification mdClassification) {
        // Check if a classification with the same name already exists
        Optional<MD_Classification> existingClassification = classificationRepo.findByClassification(mdClassification.getClassification());
        if (existingClassification.isPresent()) {
            // If a duplicate classification exists, return a conflict response
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Duplicate entry: Classification '" + mdClassification.getClassification() + "' already exists.");
        }

        // Save the new classification
        MD_Classification createdClassification = classificationRepo.save(mdClassification);

        // Prepare the response message
        String responseMessage = "Data added: Classification '" + createdClassification.getClassification() + "' added successfully.";

        return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
    }




    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateMdClassification(@RequestBody MD_Classification updatedClassification) {
        // Check if the classification with the given ID exists
        Optional<MD_Classification> classificationOptional = classificationRepo.findById(updatedClassification.getId());

        if (!classificationOptional.isPresent()) {
            // If classification with the given ID is not found, return 404 Not Found
            return ResponseEntity.notFound().build();
        }

        // Check for duplicate classification names
        Optional<MD_Classification> duplicateClassification = classificationRepo.findByClassification(updatedClassification.getClassification());
        if (duplicateClassification.isPresent() && !duplicateClassification.get().getId().equals(updatedClassification.getId())) {
            // If a duplicate classification exists and it's not the current classification, return a conflict response
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Duplicate entry: Classification '" + updatedClassification.getClassification() + "' already exists."));
        }

        // Update the existing classification with the new values
        MD_Classification existingClassification = classificationOptional.get();
        StringBuilder updateMessage = new StringBuilder("Updated successfully: ");


        if (!existingClassification.getClassification().equals(updatedClassification.getClassification())) {
            updateMessage.append("Classification name changed from '")
                    .append(existingClassification.getClassification())
                    .append("' to '")
                    .append(updatedClassification.getClassification())
                    .append("'. ");
            existingClassification.setClassification(updatedClassification.getClassification());
        }

        // Save the updated classification
        MD_Classification updatedClassificationEntity = classificationRepo.save(existingClassification);

        Map<String, Object> response = new HashMap<>();
        response.put("message", updateMessage.toString());
        response.put("updatedClassification", updatedClassificationEntity);


        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteClassificationsByIds(@RequestBody List<Integer> ids) {
        List<Integer> notFoundIds = new ArrayList<>();
        List<String> deletedClassificationNames = new ArrayList<>();

        for (Integer id : ids) {
            Optional<MD_Classification> classificationOptional = classificationRepo.findById(id);
            if (classificationOptional.isEmpty()) {
                notFoundIds.add(id);
            } else {
                MD_Classification classification = classificationOptional.get();
                deletedClassificationNames.add(classification.getClassification());
                classificationRepo.deleteById(id);
            }
        }

        if (!notFoundIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No classifications found with IDs: " + notFoundIds.toString());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body("Classifications: " + deletedClassificationNames + " deleted successfully");
    }
}