package com.sonata.portfoliomanagement.controllers;

import com.sonata.portfoliomanagement.interfaces.MD_VerticalRepository;
import com.sonata.portfoliomanagement.model.MD_Vertical;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/mdvertical")
public class MD_VerticalController {

    @Autowired
    private MD_VerticalRepository verticalRepo;

    // Handles GET requests to retrieve all MD_Vertical records
    @GetMapping("/getall")
    public ResponseEntity<?> getAllData() {
        List<MD_Vertical> mdVerticals = verticalRepo.findAll();
        if (!mdVerticals.isEmpty()) {
            return ResponseEntity.ok(mdVerticals);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No verticals found.");
        }
    }


    // New GET method to retrieve unique vertical names without repetition
    @GetMapping("/get")
    public ResponseEntity<?> getUniqueData() {
        List<MD_Vertical> mdVerticals = verticalRepo.findAll();
        Set<String> uniqueverticalsSet = new HashSet<>();
        List<String> uniqueMdVerticals = new ArrayList<>();

        for (MD_Vertical vertical : mdVerticals) {
            if (uniqueverticalsSet.add(vertical.getVertical())) {
                uniqueMdVerticals.add(vertical.getVertical());
            }
        }

        if (!uniqueMdVerticals.isEmpty()) {
            return ResponseEntity.ok(uniqueMdVerticals);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No unique Vertical found.");
        }
    }


    @PostMapping("/save")
    public ResponseEntity<Object> createMdVertical( @RequestBody MD_Vertical mdVertical) {
        if (mdVertical == null) {
            return ResponseEntity.badRequest().body("MD Vertical object cannot be null. Please provide valid data.");
        }

        // Check if vertical name is null or empty
        if (mdVertical.getVertical() == null || mdVertical.getVertical().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Vertical name cannot be null or empty. Please provide a valid vertical name.");
        }

        // Check if a vertical with the same name already exists
        Optional<MD_Vertical> existingVertical = verticalRepo.findByVertical(mdVertical.getVertical());
        if (existingVertical.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Duplicate entry: Vertical '" + mdVertical.getVertical() + "' already exists.");
        }

        // Save the new vertical
        MD_Vertical createdVertical = verticalRepo.save(mdVertical);
        if (createdVertical == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to save vertical. Please try again later.");
        }

        String responseMessage = "Data added: Vertical '" + createdVertical.getVertical() + "' added successfully.";
        return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
    }

    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateMdVertical(@RequestBody MD_Vertical updatedVertical) {
        if (updatedVertical.getVertical()=="") {
            return ResponseEntity.badRequest().body(Map.of("message", "Updated vertical or its ID cannot be null. Please provide valid data."));
        }

        // Check if the vertical with the given ID exists
        Optional<MD_Vertical> existingVerticalOptional = verticalRepo.findById(updatedVertical.getId());
        if (!existingVerticalOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // Check for duplicate vertical names if the updated vertical name is not null
        if (updatedVertical.getVertical() != null && !updatedVertical.getVertical().isEmpty()) {
            Optional<MD_Vertical> duplicateVertical = verticalRepo.findByVertical(updatedVertical.getVertical());
//            if (duplicateVertical.isPresent() && !duplicateVertical.get().getId().equals(updatedVertical.getId())) {
            if (duplicateVertical.isPresent() && duplicateVertical.get().getId() != (updatedVertical.getId())) {

                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("message", "Duplicate entry: Vertical '" + updatedVertical.getVertical() + "' already exists."));
            }
        } else {
            return ResponseEntity.badRequest().body(Map.of("message", "Vertical name cannot be null or empty. Please provide a valid vertical name."));
        }

        // Update the existing vertical with the new values
        MD_Vertical existingVertical = existingVerticalOptional.get();
        StringBuilder updateMessage = new StringBuilder();
        boolean isUpdated = false;

        // Example: Check and update the vertical name
        if (!existingVertical.getVertical().equals(updatedVertical.getVertical())) {
            updateMessage.append("Vertical name updated from '")
                    .append(existingVertical.getVertical())
                    .append("' to '")
                    .append(updatedVertical.getVertical())
                    .append("'. ");
            existingVertical.setVertical(updatedVertical.getVertical());
            isUpdated = true;

        }
        if (!isUpdated) {
            return ResponseEntity.ok(Collections.singletonMap("message", "No changes detected"));
        }

        // Save the updated vertical
        MD_Vertical savedVertical = verticalRepo.save(existingVertical);
        if (savedVertical == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to update vertical. Please try again later."));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("message", updateMessage.toString());
        response.put("updatedVertical", savedVertical);

        // Return the updated vertical with 200 OK status
        return ResponseEntity.ok(response);
    }

    // Handles DELETE requests to delete multiple MD_Vertical records by IDs
    @DeleteMapping("/deleteMultiple")
    public ResponseEntity<String> deleteMultipleMdVerticals(@RequestBody List<Integer> verticalIds) {
        List<Integer> notFoundIds = new ArrayList<>();
        List<String> deletedVerticalNames = new ArrayList<>();

        for (Integer id : verticalIds) {
            Optional<MD_Vertical> verticalOptional = verticalRepo.findById(id);
            if (!verticalOptional.isPresent()) {
                notFoundIds.add(id);
            } else {
                MD_Vertical vertical = verticalOptional.get();
                deletedVerticalNames.add(vertical.getVertical());
                verticalRepo.deleteById(id);
            }
        }

        if (!notFoundIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No verticals found with IDs: " + notFoundIds.toString());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body("Verticals '" + deletedVerticalNames + "' deleted successfully.");
    }
}