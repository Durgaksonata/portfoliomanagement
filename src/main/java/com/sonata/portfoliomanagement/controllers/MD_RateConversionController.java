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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173" )
@RestController
@RequestMapping("mdrateconversion")
public class MD_RateConversionController {

    private static final Logger logger = LoggerFactory.getLogger(MD_RateConversionController.class);

    @Autowired
    private MD_RateConversionRepository rateConversionRepository;

    @GetMapping("/get")
    public ResponseEntity<List<MD_RateConversion>> getAllRateConversions() {
        List<MD_RateConversion> rateConversions = rateConversionRepository.findAll();
        return ResponseEntity.ok(rateConversions);
    }


    @PostMapping("/save")
    public ResponseEntity<Object> createRateConversion(@RequestBody MD_RateConversion newRateConversion) {
        // Check if a record with the same financial year, month, and quarter already exists
        List<MD_RateConversion> existingRates = rateConversionRepository.findByFinancialYearAndMonthAndQuarter(
                newRateConversion.getFinancialYear(), newRateConversion.getMonth(), newRateConversion.getQuarter());

        if (!existingRates.isEmpty()) {
            // If a duplicate record exists, return a conflict response
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Collections.singletonMap("message", "Rate conversion for financial year '" +
                            newRateConversion.getFinancialYear() + "', month '" + newRateConversion.getMonth() +
                            "', and quarter '" + newRateConversion.getQuarter() + "' already exists."));
        }

        // Save the new rate conversion
        MD_RateConversion createdRateConversion = rateConversionRepository.save(newRateConversion);

        // Prepare the response with a message
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Rate conversion created successfully.");
        response.put("createdRateConversion", createdRateConversion);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }




    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateRateConversion(@RequestBody MD_RateConversion updatedRateConversion) {
        // Fetch the existing rate conversion by ID
        Optional<MD_RateConversion> rateConversionOptional = rateConversionRepository.findById(updatedRateConversion.getId());

        if (!rateConversionOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        MD_RateConversion existingRateConversion = rateConversionOptional.get();

        // Check if another record with the same financial year, month, and quarter exists
        List<MD_RateConversion> rateWithSameDetails = rateConversionRepository.findByFinancialYearAndMonthAndQuarter(
                updatedRateConversion.getFinancialYear(), updatedRateConversion.getMonth(), updatedRateConversion.getQuarter());

        // Remove the current record being updated from the list of results
        rateWithSameDetails.removeIf(rate -> rate.getId() == updatedRateConversion.getId());

        // If any records are left in the list, it means there are duplicates
        if (!rateWithSameDetails.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Collections.singletonMap("message", "Rate conversion for financial year '" +
                            updatedRateConversion.getFinancialYear() + "', month '" + updatedRateConversion.getMonth() +
                            "', and quarter '" + updatedRateConversion.getQuarter() + "' already exists."));
        }

        // Check and update values only if they differ
        boolean isUpdated = false;
        StringBuilder updateMessage = new StringBuilder();


        if (!existingRateConversion.getFinancialYear().equals(updatedRateConversion.getFinancialYear())) {
            updateMessage.append("Financial year updated from '")
                    .append(existingRateConversion.getFinancialYear())
                    .append("' to '")
                    .append(updatedRateConversion.getFinancialYear())
                    .append("'. ");
            existingRateConversion.setFinancialYear(updatedRateConversion.getFinancialYear());
            isUpdated = true;
        }

        if (!existingRateConversion.getMonth().equals(updatedRateConversion.getMonth())) {
            updateMessage.append("Month changed from '")
                    .append(existingRateConversion.getMonth())
                    .append("' to '")
                    .append(updatedRateConversion.getMonth())
                    .append("'. ");
            existingRateConversion.setMonth(updatedRateConversion.getMonth());
            isUpdated = true;
        }

        if (!existingRateConversion.getQuarter().equals(updatedRateConversion.getQuarter())) {
            updateMessage.append("Quarter changed from '")
                    .append(existingRateConversion.getQuarter())
                    .append("' to '")
                    .append(updatedRateConversion.getQuarter())
                    .append("'. ");
            existingRateConversion.setQuarter(updatedRateConversion.getQuarter());
            isUpdated = true;
        }

        if (existingRateConversion.getUsdToInr() != updatedRateConversion.getUsdToInr()) {
            updateMessage.append("USD to INR rate changed from '")
                    .append(existingRateConversion.getUsdToInr())
                    .append("' to '")
                    .append(updatedRateConversion.getUsdToInr())
                    .append("'. ");
            existingRateConversion.setUsdToInr(updatedRateConversion.getUsdToInr());
            isUpdated = true;
        }

        if (existingRateConversion.getGbpToUsd() != updatedRateConversion.getGbpToUsd()) {
            updateMessage.append("GBP to USD rate changed from '")
                    .append(existingRateConversion.getGbpToUsd())
                    .append("' to '")
                    .append(updatedRateConversion.getGbpToUsd())
                    .append("'. ");
            existingRateConversion.setGbpToUsd(updatedRateConversion.getGbpToUsd());
            isUpdated = true;
        }

        // If no changes are detected, return a custom message
        if (!isUpdated) {
            return ResponseEntity.ok(Collections.singletonMap("message", "No changes detected."));
        }

        // Save the updated rate conversion
        MD_RateConversion updatedRateConversionEntity = rateConversionRepository.save(existingRateConversion);

        Map<String, Object> response = new HashMap<>();
        response.put("message", updateMessage.toString());
        response.put("updatedRateConversion", updatedRateConversionEntity);

        return ResponseEntity.ok(response);
    }



    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteRateConversionsByIds(@RequestBody List<Integer> ids) {
        List<Integer> notFoundIds = new ArrayList<>();
        List<String> deletedRateConversionDetails = new ArrayList<>();

        for (Integer id : ids) {
            Optional<MD_RateConversion> rateConversionOptional = rateConversionRepository.findById(id);
            if (rateConversionOptional.isEmpty()) {
                notFoundIds.add(id);
            } else {
                MD_RateConversion rateConversion = rateConversionOptional.get();
                deletedRateConversionDetails.add("ID: " + id + ", Financial Year: " + rateConversion.getFinancialYear() +
                        ", Month: " + rateConversion.getMonth() + ", Quarter: " + rateConversion.getQuarter());
                rateConversionRepository.deleteById(id);
            }
        }

        if (!notFoundIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No rate conversions found with IDs: " + notFoundIds.toString());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body("Rate conversions: " + deletedRateConversionDetails + " deleted successfully");
    }

}