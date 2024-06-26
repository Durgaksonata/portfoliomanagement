package com.sonata.portfoliomanagement.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sonata.portfoliomanagement.interfaces.ResourceDataRepository;
import com.sonata.portfoliomanagement.model.ResourceData;

@RestController
@RequestMapping("/resourcedata")
public class ResourceDataController {

    @Autowired
    private ResourceDataRepository resourceDataRepository;

    @GetMapping("/getall")
    public ResponseEntity<List<ResourceData>> getAllResourceData() {
        List<ResourceData> resources = resourceDataRepository.findAll();
        if (resources.isEmpty()) {
            return ResponseEntity.noContent().build(); // Returns 204 No Content if no data found
        } else {
            return ResponseEntity.ok(resources); // Returns 200 OK with the list of ResourceData
        }
    }

}