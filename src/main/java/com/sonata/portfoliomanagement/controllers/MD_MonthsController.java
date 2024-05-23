package com.sonata.portfoliomanagement.controllers;

import com.sonata.portfoliomanagement.interfaces.MD_MonthsRepository;
import com.sonata.portfoliomanagement.model.MD_Months;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mdmonth")
public class MD_MonthsController {

    @Autowired
    MD_MonthsRepository monthsRepo;

    @GetMapping("/get")
    public ResponseEntity<List<MD_Months>> getAllData() {
        List<MD_Months> mdmonths = monthsRepo.findAll();
        return ResponseEntity.ok(mdmonths);
    }

    @PostMapping("/save")
    public ResponseEntity<MD_Months> createMdMonths(@RequestBody MD_Months mdMonths) {
        MD_Months createdmonths = monthsRepo.save(mdMonths);
        return new ResponseEntity<>(createdmonths, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<MD_Months> updateMdcategory(@PathVariable int id, @RequestBody MD_Months updatedMdmonths) {
        // Check if PursuitAction with given id exists
        if (!monthsRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        // Set the id of updatedPursuitAction
        updatedMdmonths.setId(id);
        // Save the updated PursuitAction
        MD_Months updateMdmonths = monthsRepo.save(updatedMdmonths);
        return ResponseEntity.ok(updateMdmonths);
    }
}
