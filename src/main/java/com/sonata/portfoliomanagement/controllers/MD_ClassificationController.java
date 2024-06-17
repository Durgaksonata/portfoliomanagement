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

//    @PostMapping("/save")
//    public ResponseEntity<List<MD_Classification>> createClassifications(@RequestBody List<MD_Classification> classificationList) {
//        List<MD_Classification> createdClassifications = new ArrayList<>();
//        for (MD_Classification classification : classificationList) {
//            MD_Classification createdClassification = classificationRepo.save(classification);
//            createdClassifications.add(createdClassification);
//        }
//        return new ResponseEntity<>(createdClassifications, HttpStatus.CREATED);
//    }

    @PostMapping("/save")
    public ResponseEntity<MD_Classification> createClassification(@RequestBody MD_Classification classification) {
        MD_Classification createdClassification = classificationRepo.save(classification);
        return new ResponseEntity<>(createdClassification, HttpStatus.CREATED);
    }



    @PutMapping("/update")
    public ResponseEntity<String> updateClassification(@RequestBody MD_Classification classification) {
        Integer id = classification.getId();
        if (id == null) {
            return ResponseEntity.badRequest().body("ID must be provided for update.");
        }

        Optional<MD_Classification> existingClassificationOptional = classificationRepo.findById(id);
        if (existingClassificationOptional.isPresent()) {
            MD_Classification existingClassification = existingClassificationOptional.get();
            existingClassification.setClassification(classification.getClassification());
            classificationRepo.save(existingClassification);
            return ResponseEntity.ok("Classification with ID " + id + " has been successfully updated.");
        } else {
            return ResponseEntity.notFound().build();
        }
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