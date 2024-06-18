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

    @PostMapping("/save")
    public ResponseEntity<Object> createDeliveryDirector(@RequestBody MD_DeliveryDirector deliveryDirector) {
        // Check if a delivery director with the same name already exists
        Optional<MD_DeliveryDirector> existingDirector = deliveryDirectorRepo.findByDeliveryDirector(deliveryDirector.getDeliveryDirector());
        if (existingDirector.isPresent()) {
            // If a duplicate delivery director exists, return a conflict response
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Duplicate entry: Delivery Director '" + deliveryDirector.getDeliveryDirector() + "' already exists.");
        }

        // Save the new delivery director
        MD_DeliveryDirector createdDirector = deliveryDirectorRepo.save(deliveryDirector);
        return new ResponseEntity<>(createdDirector, HttpStatus.CREATED);
    }



    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateDeliveryDirector(@RequestBody MD_DeliveryDirector updatedDirector) {
        // Check if the delivery director with the given ID exists
        Optional<MD_DeliveryDirector> directorOptional = deliveryDirectorRepo.findById(updatedDirector.getId());

        if (!directorOptional.isPresent()) {
            // If delivery director with the given ID is not found, return 404 Not Found
            return ResponseEntity.notFound().build();
        }

        // Check for duplicate delivery director names
        Optional<MD_DeliveryDirector> duplicateDirector = deliveryDirectorRepo.findByDeliveryDirector(updatedDirector.getDeliveryDirector());
        if (duplicateDirector.isPresent() && !duplicateDirector.get().getId().equals(updatedDirector.getId())) {
            // If a duplicate delivery director exists and it's not the current director, return a conflict response
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Duplicate entry: Delivery Director '" + updatedDirector.getDeliveryDirector() + "' already exists."));
        }

        // Update the existing delivery director with the new values
        MD_DeliveryDirector existingDirector = directorOptional.get();
        StringBuilder updateMessage = new StringBuilder("Updated successfully: ");


        if (!existingDirector.getDeliveryDirector().equals(updatedDirector.getDeliveryDirector())) {
            updateMessage.append("Delivery Director name changed from '")
                    .append(existingDirector.getDeliveryDirector())
                    .append("' to '")
                    .append(updatedDirector.getDeliveryDirector())
                    .append("'. ");
            existingDirector.setDeliveryDirector(updatedDirector.getDeliveryDirector());
        }

        // Save the updated delivery director
        MD_DeliveryDirector updatedDirectorEntity = deliveryDirectorRepo.save(existingDirector);

        Map<String, Object> response = new HashMap<>();
        response.put("message", updateMessage.toString());
        response.put("updatedDeliveryDirector", updatedDirectorEntity);


        return ResponseEntity.ok(response);
    }



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
                .body("Delivery Directors " + deletedDirectorNames + " deleted successfully");
    }
}