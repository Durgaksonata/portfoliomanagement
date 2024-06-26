package com.sonata.portfoliomanagement.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sonata.portfoliomanagement.interfaces.CurrentResourceDataRepository;
import com.sonata.portfoliomanagement.model.CurrentResourceData;

@RestController
@RequestMapping("/currentresourcedata")
public class CurrentResourceDataController {

    @Autowired
    private CurrentResourceDataRepository currentresourceDataRepository;

    @GetMapping("/getall")
    public ResponseEntity<List<CurrentResourceData>> getAllCurrentResourceData() {
        List<CurrentResourceData> currentresources = currentresourceDataRepository.findAll();
        if (currentresources.isEmpty()) {
            return ResponseEntity.noContent().build(); // Returns 204 No Content if no data found
        } else {
            return ResponseEntity.ok(currentresources); // Returns 200 OK with the list of ResourceData
        }
    }



}