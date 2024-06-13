package com.sonata.portfoliomanagement.controllers;

import com.sonata.portfoliomanagement.interfaces.MD_RateConversionRepository;
import com.sonata.portfoliomanagement.model.MD_RateConversion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173" )
@RestController
@RequestMapping("mdrateconversion")
public class MD_RateConversionController {

    private static final Logger logger = LoggerFactory.getLogger(MD_RateConversionController.class);

    @Autowired
    private MD_RateConversionRepository repository;

    @GetMapping("/get")
    public ResponseEntity<List<MD_RateConversion>> getAllData() {
        List<MD_RateConversion> mdrateconvo = repository.findAll();
        return ResponseEntity.ok(mdrateconvo);
    }

    @PostMapping("/save")
    public ResponseEntity<MD_RateConversion> createMDRateConversion(@Valid @RequestBody MD_RateConversion mdrateconvo) {
        logger.info("Received MDRateConversion: {}", mdrateconvo);
        try {
            MD_RateConversion createdMDRateConvo = repository.save(mdrateconvo);
            logger.info("Saved MDRateConversion: {}", createdMDRateConvo);
            return new ResponseEntity<>(createdMDRateConvo, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error saving MDRateConversion: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<MD_RateConversion> updateMDRateConversion(@PathVariable int id, @Valid @RequestBody MD_RateConversion mdrateconvo) {
        logger.info("Received update request for MDRateConversion with id: {}", id);

        Optional<MD_RateConversion> optionalMDRateConvo = repository.findById(id);
        if (optionalMDRateConvo.isPresent()) {
            MD_RateConversion existingMDRateConvo = optionalMDRateConvo.get();
            existingMDRateConvo.setFinancialYear(mdrateconvo.getFinancialYear());
            existingMDRateConvo.setMonth(mdrateconvo.getMonth());
            existingMDRateConvo.setQuarter(mdrateconvo.getQuarter());
            existingMDRateConvo.setUsdToInr(mdrateconvo.getUsdToInr());
            existingMDRateConvo.setGbpToUsd(mdrateconvo.getGbpToUsd());

            MD_RateConversion updatedMDRateConvo = repository.save(existingMDRateConvo);
            logger.info("Updated MDRateConversion: {}", updatedMDRateConvo);
            return new ResponseEntity<>(updatedMDRateConvo, HttpStatus.OK);
        } else {
            logger.error("MDRateConversion with id {} not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
