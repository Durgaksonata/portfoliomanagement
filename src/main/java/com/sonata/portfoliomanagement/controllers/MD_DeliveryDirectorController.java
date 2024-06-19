package com.sonata.portfoliomanagement.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sonata.portfoliomanagement.interfaces.MD_DeliveryDirectorRepository;
import com.sonata.portfoliomanagement.model.MD_DeliveryDirector;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/mddeliverydirector")
public class MD_DeliveryDirectorController {

    @Autowired
    MD_DeliveryDirectorRepository deliveryDirectorRepo;

    @GetMapping("/get")
    public ResponseEntity<List<MD_DeliveryDirector>> getAllData() {
        List<MD_DeliveryDirector> deliveryDirectors = deliveryDirectorRepo.findAll();
        return ResponseEntity.ok(deliveryDirectors);
    }

//    @PostMapping("/save")
//    public ResponseEntity<Object> createMdDeliveryDirector(@RequestBody MD_DeliveryDirector mdDeliveryDirector) {
//        // Check if the provided MD_DeliveryDirector object is null
//        if (mdDeliveryDirector == null) {
//            return ResponseEntity.badRequest().body("MD Delivery Director object cannot be null. Please provide valid data.");
//        }
//
//        // Check if delivery director name is null or empty
//        if (mdDeliveryDirector.getDeliveryDirector() == null || mdDeliveryDirector.getDeliveryDirector().trim().isEmpty()) {
//            return ResponseEntity.badRequest().body("Delivery Director name cannot be null or empty. Please provide a valid name.");
//        }
//
//        // Check if a delivery director with the same name already exists
//        Optional<MD_DeliveryDirector> existingDeliveryDirector = Optional.ofNullable(deliveryDirectorRepo.findByDeliveryDirector(mdDeliveryDirector.getDeliveryDirector()));
//        if (existingDeliveryDirector.isPresent()) {
//            return ResponseEntity.status(HttpStatus.CONFLICT)
//                    .body("Duplicate entry: Delivery Director '" + mdDeliveryDirector.getDeliveryDirector() + "' already exists.");
//        }
//
//        // Save the new delivery director
//        MD_DeliveryDirector createdDeliveryDirector = deliveryDirectorRepo.save(mdDeliveryDirector);
//        if (createdDeliveryDirector == null) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Failed to save delivery director. Please try again later.");
//        }
//
//        String responseMessage = "Data added: Delivery Director '" + createdDeliveryDirector.getDeliveryDirector() + "' added successfully.";
//        return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
//    }
//
//
//    @PutMapping("/update")
//    public ResponseEntity<Object> updateMdDeliveryDirector(@RequestBody MD_DeliveryDirector mdDeliveryDirector) {
//        // Check if the provided MD_DeliveryDirector object is null
//        if (mdDeliveryDirector == null) {
//            return ResponseEntity.badRequest().body("MD Delivery Director object cannot be null. Please provide valid data.");
//        }
//
//        // Check if delivery director name is null or empty
//        if (mdDeliveryDirector.getDeliveryDirector() == null || mdDeliveryDirector.getDeliveryDirector().trim().isEmpty()) {
//            return ResponseEntity.badRequest().body("Delivery Director name cannot be null or empty. Please provide a valid name.");
//        }
//
//        // Check if the provided ID is null or <= 0
//        if (mdDeliveryDirector.getId() == null || mdDeliveryDirector.getId() <= 0) {
//            return ResponseEntity.badRequest().body("Invalid Delivery Director ID. Please provide a valid ID.");
//        }
//
//        // Check if a delivery director with the same name already exists
//        Optional<MD_DeliveryDirector> existingDeliveryDirector = Optional.ofNullable(deliveryDirectorRepo.findByDeliveryDirector(mdDeliveryDirector.getDeliveryDirector()));
//        if (existingDeliveryDirector.isPresent() && !existingDeliveryDirector.get().getId().equals(mdDeliveryDirector.getId())) {
//            return ResponseEntity.status(HttpStatus.CONFLICT)
//                    .body("Duplicate entry: Delivery Director '" + mdDeliveryDirector.getDeliveryDirector() + "' already exists.");
//        }
//
//        // Check if the delivery director with the provided ID exists
//        Optional<MD_DeliveryDirector> optionalDeliveryDirector = deliveryDirectorRepo.findById(mdDeliveryDirector.getId());
//        if (!optionalDeliveryDirector.isPresent()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body("Delivery Director with ID '" + mdDeliveryDirector.getId() + "' not found.");
//        }
//
//        // Update the delivery director
//        MD_DeliveryDirector updatedDeliveryDirector = deliveryDirectorRepo.save(mdDeliveryDirector);
//        if (updatedDeliveryDirector == null) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Failed to update delivery director. Please try again later.");
//        }
//
//        String responseMessage = "Data updated: Delivery Director '" + updatedDeliveryDirector.getDeliveryDirector() + "' updated successfully.";
//        return ResponseEntity.ok(responseMessage);
//    }


    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteDeliveryDirectorsByIds(@RequestBody List<Integer> ids) {
        List<Integer> notFoundIds = new ArrayList<>();
        List<String> deletedDirectorNames = new ArrayList<>();

        for (Integer id : ids) {
            Optional<MD_DeliveryDirector> directorOptional = deliveryDirectorRepo.findById(id);
            if (directorOptional.isEmpty()) {
                notFoundIds.add(id);
            } else {
                MD_DeliveryDirector director = directorOptional.get();
                deletedDirectorNames.add(director.getDeliveryDirector());
                deliveryDirectorRepo.deleteById(id);
            }
        }

        if (!notFoundIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No delivery directors found with IDs: " + notFoundIds.toString());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body("Delivery Directors: " + deletedDirectorNames + " deleted successfully");
    }
}