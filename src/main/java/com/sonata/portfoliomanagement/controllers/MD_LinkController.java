package com.sonata.portfoliomanagement.controllers;

import com.sonata.portfoliomanagement.model.*;
import com.sonata.portfoliomanagement.services.MD_LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class MD_LinkController {

    @Autowired
    private MD_LinkService linkService;

    @GetMapping("/getAllUniqueData")
    public ResponseEntity<Map<String, List<String>>> getAllUniqueData() {
        Map<String, List<String>> uniqueData = linkService.getAllUniqueData();
        return ResponseEntity.ok(uniqueData);
    }
}