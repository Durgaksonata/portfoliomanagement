package com.sonata.portfoliomanagement.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sonata.portfoliomanagement.interfaces.MD_DeliveryDirectorRepository;
import com.sonata.portfoliomanagement.model.MD_DeliveryDirector;
@CrossOrigin(origins = "http://localhost:5173" )
@RestController
@RequestMapping("/dd")
public class MD_DeliveryDirectorController {

    @Autowired
    private MD_DeliveryDirectorRepository deliveryDirectorRepository;

    @GetMapping("/get")
    public ResponseEntity<List<MD_DeliveryDirector>> getAllDeliveryDirectors() {
        List<MD_DeliveryDirector> directorsList = deliveryDirectorRepository.findAll();
        return ResponseEntity.ok(directorsList);
    }

    @PostMapping("/save")
    public ResponseEntity<List<MD_DeliveryDirector>> createDeliveryDirectors(@RequestBody List<MD_DeliveryDirector> deliveryDirectorsList) {
        List<MD_DeliveryDirector> createdDeliveryDirectors = new ArrayList<>();
        for (MD_DeliveryDirector deliveryDirector : deliveryDirectorsList) {
            MD_DeliveryDirector createdDeliveryDirector = deliveryDirectorRepository.save(deliveryDirector);
            createdDeliveryDirectors.add(createdDeliveryDirector);
        }
        return new ResponseEntity<>(createdDeliveryDirectors, HttpStatus.CREATED);
    }


    @PutMapping("/update")
    public ResponseEntity<String> updateDeliveryDirectors(@RequestBody List<MD_DeliveryDirector> directors) {
        List<Integer> notFoundIds = directors.stream()
                .filter(directorDetails -> {
                    Integer id = directorDetails.getId();
                    if (id == null) {
                        return true;
                    }
                    Optional<MD_DeliveryDirector> existingDirector = deliveryDirectorRepository.findById(id);
                    if (existingDirector.isPresent()) {
                        MD_DeliveryDirector directorToUpdate = existingDirector.get();
                        directorToUpdate.setDeliveryDirector(directorDetails.getDeliveryDirector());
                        deliveryDirectorRepository.save(directorToUpdate);
                        return false;
                    } else {
                        return true;
                    }
                })
                .map(MD_DeliveryDirector::getId)
                .collect(Collectors.toList());

        if (!notFoundIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No delivery directors found with IDs: " + notFoundIds.toString());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body("Delivery directors have been successfully updated.");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteDeliveryDirectorsByIds(@RequestBody List<Integer> ids) {
        List<Integer> notFoundIds = ids.stream()
                .filter(id -> {
                    Optional<MD_DeliveryDirector> director = deliveryDirectorRepository.findById(id);
                    if (director.isEmpty()) {
                        return true;
                    }
                    deliveryDirectorRepository.deleteById(id);
                    return false;
                })
                .collect(Collectors.toList());

        if (!notFoundIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No delivery directors found with IDs: " + notFoundIds.toString());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body("Delivery directors with specified IDs have been deleted.");
    }
}