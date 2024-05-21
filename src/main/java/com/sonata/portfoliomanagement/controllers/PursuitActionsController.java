package com.sonata.portfoliomanagement.controllers;

import com.sonata.portfoliomanagement.interfaces.PursuitActionsRepository;
import com.sonata.portfoliomanagement.model.PursuitActions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pursuitactions")
public class PursuitActionsController {
    @Autowired
    PursuitActionsRepository pursuitActionRepo;

    @GetMapping("/get")
    public ResponseEntity<List<PursuitActions>> getAllData() {
        List<PursuitActions> pursuitAction = pursuitActionRepo.findAll();
        return ResponseEntity.ok(pursuitAction);
    }
    @PostMapping("/save")
    public ResponseEntity<PursuitActions> createPursuitAction(@RequestBody PursuitActions pursuitAction) {
        PursuitActions createdPursuit = pursuitActionRepo.save(pursuitAction);
        return new ResponseEntity<>(createdPursuit, HttpStatus.CREATED);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<PursuitActions> updatePursuitActions(@PathVariable int id, @RequestBody PursuitActions updatedPursuitActions) {
        // Check if PursuitAction with given id exists
        if (!pursuitActionRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // Set the id of updatedPursuitAction
        updatedPursuitActions.setId(id);

        // Save the updated PursuitAction
        PursuitActions updatedActions = pursuitActionRepo.save(updatedPursuitActions);

        return ResponseEntity.ok(updatedActions);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePursuitAction(@PathVariable int id) {
        if (pursuitActionRepo.existsById(id)) {
            pursuitActionRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
