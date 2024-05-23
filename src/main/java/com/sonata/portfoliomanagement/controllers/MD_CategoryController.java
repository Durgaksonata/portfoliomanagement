package com.sonata.portfoliomanagement.controllers;

import java.util.List;

import com.sonata.portfoliomanagement.interfaces.MD_CategoryRepository;
import com.sonata.portfoliomanagement.model.MD_Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

    @PutMapping("/update/{id}")
    public ResponseEntity<MD_Category> updateMdcategory(@PathVariable int id, @RequestBody MD_Category updatedMdcategory) {
        // Check if PursuitAction with given id exists
        if (!categoryRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // Set the id of updatedPursuitAction
        updatedMdcategory.setId(id);

        // Save the updated PursuitAction
        MD_Category updateMdcategory = categoryRepo.save(updatedMdcategory);
        return ResponseEntity.ok(updateMdcategory);
    }
}
