package com.sonata.portfoliomanagement.controllers;

import java.util.List;

import com.sonata.portfoliomanagement.interfaces.MD_AccountsRepository;
import com.sonata.portfoliomanagement.model.MD_Accounts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "http://localhost:5173" )

@RestController
@RequestMapping("/mdaccount")
public class MD_AccountsController {

    @Autowired
    MD_AccountsRepository acntRepo;

    @GetMapping("/get")
    public ResponseEntity<List<MD_Accounts>> getAllData() {
        List<MD_Accounts> mdAcnts = acntRepo.findAll();
        return ResponseEntity.ok(mdAcnts);
    }
    @PostMapping("/save")
    public ResponseEntity<MD_Accounts> createMdAcnts(@RequestBody MD_Accounts mdAcnts) {
        MD_Accounts createdAcnts = acntRepo.save(mdAcnts);
        return new ResponseEntity<>(createdAcnts, HttpStatus.CREATED);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<MD_Accounts> updateMdAcnts(@PathVariable int id, @RequestBody MD_Accounts updatedMdAcnts) {
        // Check if PursuitAction with given id exists
        if (!acntRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // Set the id of updatedPursuitAction
        updatedMdAcnts.setId(id);

        // Save the updated PursuitAction
        MD_Accounts updateMdAcnts = acntRepo.save(updatedMdAcnts);

        return ResponseEntity.ok(updateMdAcnts);
    }



}