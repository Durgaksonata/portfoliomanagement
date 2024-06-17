package com.sonata.portfoliomanagement.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sonata.portfoliomanagement.interfaces.MD_ClassificationRepository;
import com.sonata.portfoliomanagement.model.MD_Classification;
@CrossOrigin(origins = "http://localhost:5173" )
@RestController
@RequestMapping("/classifications")
public class MD_ClassificationController {

    @Autowired
    private MD_ClassificationRepository classificationRepo;

    @GetMapping("/get")
    public ResponseEntity<List<MD_Classification>> getAllClassifications() {
        List<MD_Classification> classifications = classificationRepo.findAll();
        return ResponseEntity.ok(classifications);
    }

    @PostMapping("/save")
    public ResponseEntity<List<MD_Classification>> createClassifications(@RequestBody List<MD_Classification> classificationList) {
        List<MD_Classification> createdClassifications = new ArrayList<>();
        for (MD_Classification classification : classificationList) {
            MD_Classification createdClassification = classificationRepo.save(classification);
            createdClassifications.add(createdClassification);
        }
        return new ResponseEntity<>(createdClassifications, HttpStatus.CREATED);
    }
    @PutMapping("/update")
    public ResponseEntity<String> updateClassifications(@RequestBody List<MD_Classification> classifications) {
        List<Integer> notFoundIds = classifications.stream()
                .filter(classificationDetails -> {
                    Integer id = classificationDetails.getId();
                    if (id == null) {
                        return true;
                    }
                    Optional<MD_Classification> existingClassification = classificationRepo.findById(id);
                    if (existingClassification.isPresent()) {
                        MD_Classification classificationToUpdate = existingClassification.get();
                        classificationToUpdate.setClassification(classificationDetails.getClassification());
                        classificationRepo.save(classificationToUpdate);
                        return false;
                    } else {
                        return true;
                    }
                })
                .map(MD_Classification::getId)
                .collect(Collectors.toList());

        if (!notFoundIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No classifications found with IDs: " + notFoundIds.toString());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body("Classifications have been successfully updated.");
    }


    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteClassificationsByIds(@RequestBody List<Integer> ids) {
        List<Integer> notFoundIds = ids.stream()
                .filter(id -> {
                    Optional<MD_Classification> classification = classificationRepo.findById(id);
                    if (classification.isEmpty()) {
                        return true;
                    }
                    classificationRepo.deleteById(id);
                    return false;
                })
                .collect(Collectors.toList());

        if (!notFoundIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No classifications found with IDs: " + notFoundIds.toString());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body("Classifications with specified IDs have been deleted.");
    }



}