package com.sonata.portfoliomanagement.controllers;

import com.sonata.portfoliomanagement.interfaces.MD_PursuitProbabilityRepository;
import com.sonata.portfoliomanagement.model.DataEntry;
import com.sonata.portfoliomanagement.model.MD_PursuitProbability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/MDPursuitProbability")
public class MD_PursuitProbabilityController {

    @Autowired
    private MD_PursuitProbabilityRepository MD_PursuitProbabilityrepo;

    // GET method to retrieve all MD_PursuitProbability entries
    @GetMapping("/get")
    public ResponseEntity<List<MD_PursuitProbability>> getAllPursuitProbabilities() {
        List<MD_PursuitProbability> pursuitProbabilities = MD_PursuitProbabilityrepo.findAll();
        return ResponseEntity.ok(pursuitProbabilities);
    }

    // POST method to add a new MD_PursuitProbability entry
    @PostMapping("/save")
    public ResponseEntity<MD_PursuitProbability> createPursuitProbability(@RequestBody MD_PursuitProbability pursuitProbability) {
        MD_PursuitProbability savedPursuitProbability = MD_PursuitProbabilityrepo.save(pursuitProbability);
        return ResponseEntity.ok(savedPursuitProbability);
    }


}
