package com.sonata.portfoliomanagement.controllers;

import java.util.List;

import com.sonata.portfoliomanagement.interfaces.MD_DeliveryManagerRepository;
import com.sonata.portfoliomanagement.model.MD_DeliveryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "http://localhost:5173" )
@RestController
@RequestMapping("/mddm")
public class MD_DeliveryManagerController {

    @Autowired
    MD_DeliveryManagerRepository dmRepo;

    @GetMapping("/get")
    public ResponseEntity<List<MD_DeliveryManager>> getAllData() {
        List<MD_DeliveryManager> mdDm = dmRepo.findAll();
        return ResponseEntity.ok(mdDm);
    }
    @PostMapping("/save")
    public ResponseEntity<MD_DeliveryManager> createMdDm(@RequestBody MD_DeliveryManager mdDms) {
        MD_DeliveryManager createdDms = dmRepo.save(mdDms);
        return new ResponseEntity<>(createdDms, HttpStatus.CREATED);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<MD_DeliveryManager> updateMdDm(@PathVariable int id, @RequestBody MD_DeliveryManager updatedMdDm) {
        // Check if PursuitAction with given id exists
        if (!dmRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // Set the id of updatedPursuitAction
        updatedMdDm.setId(id);

        // Save the updated PursuitAction
        MD_DeliveryManager updateMdDms = dmRepo.save(updatedMdDm);

        return ResponseEntity.ok(updateMdDms);
    }


}
