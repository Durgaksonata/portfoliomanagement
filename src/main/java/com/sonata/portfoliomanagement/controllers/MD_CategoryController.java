package com.sonata.portfoliomanagement.controllers;

import java.util.List;
import java.util.Optional;
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
    public ResponseEntity<MD_Category> createMdCategory(@RequestBody MD_Category mdcategory) {
        MD_Category createdcategory = categoryRepo.save(mdcategory);
        return new ResponseEntity<>(createdcategory, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<MD_Category> updateMdCategory(@RequestBody MD_Category updatedCategory) {
        // Check if the category with the given ID exists
        Optional<MD_Category> categoryOptional = categoryRepo.findById(updatedCategory.getId());

        if (!categoryOptional.isPresent()) {
            // If category with the given ID is not found, return 404 Not Found
            return ResponseEntity.notFound().build();
        }

        // Update the existing category with the new values
        MD_Category existingCategory = categoryOptional.get();
        existingCategory.setCategory(updatedCategory.getCategory());  // Example: Update other fields similarly

        // Save the updated category
        MD_Category updatedCategoryEntity = categoryRepo.save(existingCategory);

        // Return the updated category with 200 OK status
        return ResponseEntity.ok(updatedCategoryEntity);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteCategoriesByIds(@RequestBody List<Integer> ids) {
        List<Integer> notFoundIds = ids.stream()
                .filter(id -> {
                    Optional<MD_Category> category = categoryRepo.findById(id);
                    if (category.isEmpty()) {
                        return true;
                    }
                    categoryRepo.deleteById(id);
                    return false;
                })
                .collect(Collectors.toList());

        if (!notFoundIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No categories found with IDs: " + notFoundIds.toString());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body("Categories have been deleted succesfully.");
    }


}