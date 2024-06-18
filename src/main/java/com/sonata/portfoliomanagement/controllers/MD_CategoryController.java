package com.sonata.portfoliomanagement.controllers;

import java.util.*;
import java.util.stream.Collectors;

import com.sonata.portfoliomanagement.interfaces.MD_CategoryRepository;
import com.sonata.portfoliomanagement.model.MD_Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173" )
@RestController
@RequestMapping("/mdcategory")
public class MD_CategoryController {

    @Autowired
    MD_CategoryRepository categoryRepo;

    @GetMapping("/get")
    public ResponseEntity<List<MD_Category>> getAllData() {
        List<MD_Category> mdcategory = categoryRepo.findAll();
        return ResponseEntity.ok(mdcategory);
    }

    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> createMdCategory(@RequestBody MD_Category mdCategory) {
        // Check if a category with the same name already exists
        Optional<MD_Category> existingCategory = categoryRepo.findByCategory(mdCategory.getCategory());
        if (existingCategory.isPresent()) {
            // If a duplicate category exists, return a conflict response
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Duplicate entry: Category '" + mdCategory.getCategory() + "' already exists."));
        }

        // Save the new category
        MD_Category createdCategory = categoryRepo.save(mdCategory);

        // Prepare the response with a success message and the created category
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Category '" + createdCategory.getCategory() + "' saved successfully.");
        response.put("createdCategory", createdCategory);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }



    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateMdCategory(@RequestBody MD_Category updatedCategory) {
        // Check if the category with the given ID exists
        Optional<MD_Category> categoryOptional = categoryRepo.findById(updatedCategory.getId());

        if (!categoryOptional.isPresent()) {
            // If category with the given ID is not found, return 404 Not Found
            return ResponseEntity.notFound().build();
        }

        // Check for duplicate category names
        Optional<MD_Category> duplicateCategory = categoryRepo.findByCategory(updatedCategory.getCategory());
        if (duplicateCategory.isPresent() && duplicateCategory.get().getId() != (updatedCategory.getId())) {
            // If a duplicate category exists and it's not the current category, return a conflict response
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Duplicate entry: Category: '" + updatedCategory.getCategory() + "' already exists."));
        }

        // Update the existing category with the new values
        MD_Category existingCategory = categoryOptional.get();
        StringBuilder updateMessage = new StringBuilder();
        boolean isUpdated = false;

        // Example: Check and update the category name
        if (!existingCategory.getCategory().equals(updatedCategory.getCategory())) {
            updateMessage.append("Category name updated from '")
                    .append(existingCategory.getCategory())
                    .append("' to '")
                    .append(updatedCategory.getCategory())
                    .append("'. ");
            existingCategory.setCategory(updatedCategory.getCategory());
            isUpdated = true;
        }
        if (!isUpdated) {
            return ResponseEntity.ok(Collections.singletonMap("message", "No changes detected."));
        }

        // Save the updated category
        MD_Category updatedCategoryEntity = categoryRepo.save(existingCategory);

        Map<String, Object> response = new HashMap<>();
        response.put("message", updateMessage.toString());
        response.put("updatedCategory", updatedCategoryEntity);

        // Return the updated category with 200 OK status
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteCategoriesByIds(@RequestBody List<Integer> ids) {
        List<Integer> notFoundIds = new ArrayList<>();
        List<String> deletedCategoryNames = new ArrayList<>();

        for (Integer id : ids) {
            Optional<MD_Category> categoryOptional = categoryRepo.findById(id);
            if (categoryOptional.isEmpty()) {
                notFoundIds.add(id);
            } else {
                MD_Category category = categoryOptional.get();
                deletedCategoryNames.add(category.getCategory());
                categoryRepo.deleteById(id);
            }
        }

        if (!notFoundIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No categories found with IDs: " + notFoundIds.toString());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body("Categories:" + " " +  deletedCategoryNames +" " +"deleted successfully");
    }
}