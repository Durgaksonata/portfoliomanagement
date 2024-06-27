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

    @GetMapping("/getall")
    public ResponseEntity<List<MD_Classification>> getAllData() {
        List<MD_Classification> mdClassifications = classificationRepo.findAll();
        return ResponseEntity.ok(mdClassifications);
    }


    @GetMapping("/get")
    public ResponseEntity<?> getUniqueData() {
        List<MD_Classification> mdClassifications = classificationRepo.findAll();

        Set<String> uniqueClassificationsSet = new HashSet<>();
        List<String> uniqueMdClassifications = new ArrayList<>();

        for (MD_Classification classification : mdClassifications) {
            if (uniqueClassificationsSet.add(classification.getClassification())) {
                uniqueMdClassifications.add(classification.getClassification());
            }
        }

        if (!uniqueMdClassifications.isEmpty()) {
            return ResponseEntity.ok(uniqueMdClassifications);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No unique classifications found.");
        }
    }


    @PostMapping("/save")
    public ResponseEntity<Object> createMdClassification(@RequestBody MD_Classification mdClassification) {
        if (mdClassification == null) {
            return ResponseEntity.badRequest().body("MD Classification object cannot be null. Please provide valid data.");
        }

        // Check if classification name is null or empty
        if (mdClassification.getClassification() == null || mdClassification.getClassification().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Classification name cannot be null or empty. Please provide a valid classification name.");
        }

        // Check if a classification with the same name already exists
        Optional<MD_Classification> existingClassification = classificationRepo.findByClassification(mdClassification.getClassification());
        if (existingClassification.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Duplicate entry: Classification '" + mdClassification.getClassification() + "' already exists.");
        }

        // Save the new classification
        MD_Classification createdClassification = classificationRepo.save(mdClassification);
        if (createdClassification == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to save classification. Please try again later.");
        }

        String responseMessage = "Data added: Classification '" + createdClassification.getClassification() + "' added successfully.";
        return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
    }


    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateMdClassification(@RequestBody MD_Classification updatedClassification) {
        if (updatedClassification == null || updatedClassification.getId() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Updated classification or its ID cannot be null. Please provide valid data."));
        }

        // Check if the classification with the given ID exists
        Optional<MD_Classification> classificationOptional = classificationRepo.findById(updatedClassification.getId());
        if (!classificationOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // Check for duplicate classification names if the updated classification name is not null
        if (updatedClassification.getClassification() != null && !updatedClassification.getClassification().isEmpty()) {
            Optional<MD_Classification> duplicateClassification = classificationRepo.findByClassification(updatedClassification.getClassification());
            if (duplicateClassification.isPresent() && !duplicateClassification.get().getId().equals(updatedClassification.getId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("message", "Duplicate entry: Classification '" + updatedClassification.getClassification() + "' already exists."));
            }
        } else {
            // If updated classification name is null or empty, handle as per your application logic
            return ResponseEntity.badRequest().body(Map.of("message", "Classification name cannot be null or empty. Please provide a valid classification name."));
        }

        // Update the existing classification with the new values
        MD_Classification existingClassification = classificationOptional.get();
        StringBuilder updateMessage = new StringBuilder();
        boolean isUpdated = false;

        if (!existingClassification.getClassification().equals(updatedClassification.getClassification())) {
            updateMessage.append("Classification name updated from '")
                    .append(existingClassification.getClassification())
                    .append("' to '")
                    .append(updatedClassification.getClassification())
                    .append("'. ");
            existingClassification.setClassification(updatedClassification.getClassification());
            isUpdated = true;
        }
        if (!isUpdated) {
            return ResponseEntity.ok(Collections.singletonMap("message", "No changes detected"));
        }

        // Save the updated classification
        MD_Classification updatedClassificationEntity = classificationRepo.save(existingClassification);
        if (updatedClassificationEntity == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to update classification. Please try again later."));
        }

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
                .body("Classifications " + deletedClassificationNames + " deleted successfully");
    }
}