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
    private MD_RateConversionRepository rateConversionRepository;

    @GetMapping("/get")
    public ResponseEntity<List<MD_RateConversion>> getAllData() {
        List<MD_RateConversion> mdrateconvo = rateConversionRepository.findAll();
        return ResponseEntity.ok(mdrateconvo);
    }

    @PostMapping("/save")
    public ResponseEntity<MD_RateConversion> createMDRateConversion(@Valid @RequestBody MD_RateConversion mdrateconvo) {
        logger.info("Received MDRateConversion: {}", mdrateconvo);
        try {
            MD_RateConversion createdMDRateConvo = rateConversionRepository.save(mdrateconvo);
            logger.info("Saved MDRateConversion: {}", createdMDRateConvo);
            return new ResponseEntity<>(createdMDRateConvo, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error saving MDRateConversion: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/update")
    public ResponseEntity<MD_RateConversion> updateRateConversion(@RequestBody MD_RateConversion updatedRateConversion) {
        Integer id = updatedRateConversion.getId();

        // Check if the entity with the given ID exists
        if (id == null || !rateConversionRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // Save the updated entity
        MD_RateConversion updatedEntity = rateConversionRepository.save(updatedRateConversion);
        return ResponseEntity.ok(updatedEntity);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteRateConversions(@RequestBody List<Integer> ids) {
        StringBuilder responseMessage = new StringBuilder();

        for (Integer id : ids) {
            if (rateConversionRepository.existsById(id)) {
                rateConversionRepository.deleteById(id);
                responseMessage.append("Entity with ID ").append(id).append(" deleted successfully. ");
            } else {
                responseMessage.append("Entity with ID ").append(id).append(" does not exist. Skipping deletion. ");
            }
        }

        return ResponseEntity.ok(responseMessage.toString());
    }
}