package com.sonata.portfoliomanagement.controllers;



import com.sonata.portfoliomanagement.interfaces.MD_ClassificationRepository;
import com.sonata.portfoliomanagement.model.MD_Classification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/classification")
public class MD_ClassificationController {

    @Autowired
    private MD_ClassificationRepository classificationRepository;

    @GetMapping("/get")
    public ResponseEntity<List<MD_Classification>> getAllClassifications() {
        List<MD_Classification> classifications = classificationRepository.findAll();
        return ResponseEntity.ok(classifications);
    }

    @PostMapping("/save")
    public ResponseEntity<List<MD_Classification>> createClassifications(@RequestBody List<MD_Classification> classificationList) {
        List<MD_Classification> createdClassifications = new ArrayList<>();
        for (MD_Classification classification : classificationList) {
            MD_Classification createdClassification = classificationRepository.save(classification);
            createdClassifications.add(createdClassification);
        }
        return new ResponseEntity<>(createdClassifications, HttpStatus.CREATED);
    }

    @PutMapping("/update/{ids}")
    public ResponseEntity<List<MD_Classification>> updateClassifications(@PathVariable List<Integer> ids, @RequestBody List<MD_Classification> updatedClassifications) {
        List<MD_Classification> updatedEntities = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            int id = ids.get(i);
            MD_Classification updatedClassification = updatedClassifications.get(i);
            Optional<MD_Classification> existingClassificationOptional = classificationRepository.findById(id);
            if (existingClassificationOptional.isPresent()) {
                updatedClassification.setId(id);
                updatedEntities.add(classificationRepository.save(updatedClassification));
            }
        }
        return ResponseEntity.ok(updatedEntities);
    }

    @DeleteMapping("/delete/{ids}")
    public ResponseEntity<Void> deleteClassifications(@PathVariable List<Integer> ids) {
        for (Integer id : ids) {
            if (classificationRepository.existsById(id)) {
                classificationRepository.deleteById(id);
            }
        }
        return ResponseEntity.noContent().build();
    }
}