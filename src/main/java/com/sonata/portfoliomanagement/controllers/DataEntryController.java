package com.sonata.portfoliomanagement.controllers;

import java.util.List;

import com.sonata.portfoliomanagement.interfaces.RevenueBudgetSummaryRepository;
import com.sonata.portfoliomanagement.model.AccountBudgets;
import com.sonata.portfoliomanagement.model.DataEntryDTO;
import com.sonata.portfoliomanagement.model.RevenueBudgetSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sonata.portfoliomanagement.interfaces.DataEntryRepository;
import com.sonata.portfoliomanagement.model.DataEntry;


@RestController
@RequestMapping("/dataentry")
public class DataEntryController {

    @Autowired
    DataEntryRepository dataEntryRepo;
    @Autowired
    private RevenueBudgetSummaryRepository revenueBudgetSummaryRepository;

    @GetMapping("/get")
    public ResponseEntity<List<DataEntry>> getAllData() {
        List<DataEntry> dataentry = dataEntryRepo.findAll();
        return ResponseEntity.ok(dataentry);
    }

    @PostMapping("/save")
    public ResponseEntity<DataEntry> createDataEntry(@RequestBody DataEntryDTO dataEntryDTO) {
        // Create a new DataEntry object
        DataEntry dataEntry = new DataEntry();

        // Set the values from the DTO
        dataEntry.setMonth(dataEntryDTO.getMonth());
        dataEntry.setVertical(dataEntryDTO.getVertical());
        dataEntry.setClassification(dataEntryDTO.getClassification());
        dataEntry.setDeliveryDirector(dataEntryDTO.getDeliveryDirector());
        dataEntry.setDeliveryManager(dataEntryDTO.getDeliveryManager());
        dataEntry.setAccount(dataEntryDTO.getAccount());
        dataEntry.setProjectManager(dataEntryDTO.getProjectManager());
        dataEntry.setProjectName(dataEntryDTO.getProjectName());
        dataEntry.setCategory(dataEntryDTO.getCategory());
        dataEntry.setAnnuityOrNonAnnuity(dataEntryDTO.getAnnuityOrNonAnnuity());
        dataEntry.setValue(dataEntryDTO.getValue());

        // Calculate OffshoreCost
        calculateOffshoreCost(dataEntry);
        // Calculate OnsiteCost
        calculateOnsiteCost(dataEntry);
        // Calculate Type
        calculateType(dataEntry);
        // Calculate TotalCost
        calculateTotalCost(dataEntry);
        // Calculate BillablePM
        calculateBillablePM(dataEntry);
        // Calculate Confirmed
        calculateConfirmed(dataEntry);
        // Calculate Upside
        calculateUpside(dataEntry);
        // Calculate Likely
        calculateLikely(dataEntry);
        // Logic to calculate remaining fields

        dataEntry.setFinancialYear(dataEntryDTO.getFinancialYear());
        dataEntry.setQuarter(dataEntryDTO.getQuarter());
        // Calculate and set other fields...

        // Save the DataEntry entity
        DataEntry createdDataEntry = dataEntryRepo.save(dataEntry);
        return new ResponseEntity<>(createdDataEntry, HttpStatus.CREATED);
    }

    // Calculate OffshoreCost
    private void calculateOffshoreCost(DataEntry dataEntry) {
        if (dataEntry.getCategory().equals("Offshore Cost")) {
            dataEntry.setOffshoreCost(dataEntry.getValue() * dataEntry.getProbability());
        } else {
            dataEntry.setOffshoreCost(0);
        }
    }

    // Calculate OnsiteCost
    private void calculateOnsiteCost(DataEntry dataEntry) {
        if (dataEntry.getCategory().equals("Onsite Cost")) {
            dataEntry.setOnsiteCost(dataEntry.getValue() * dataEntry.getProbability());
        } else {
            dataEntry.setOnsiteCost(0);
        }
    }

    // Calculate Type
    private void calculateType(DataEntry dataEntry) {
        String category = dataEntry.getCategory().toLowerCase();
        String type;
        if (category.contains("confirmed") || category.contains("upside")) {
            type = "Revenue";
        } else if (category.contains("cost")) {
            type = "Cost";
        } else {
            type = "Operations";
        }
        dataEntry.setType(type);
    }

    // Calculate TotalCost
    private void calculateTotalCost(DataEntry dataEntry) {
        float offshoreCost = dataEntry.getOffshoreCost();
        float onsiteCost = dataEntry.getOnsiteCost();
        float totalCost = offshoreCost + onsiteCost;
        dataEntry.setTotalCost(totalCost);
    }

    // Calculate BillablePM
    private void calculateBillablePM(DataEntry dataEntry) {
        if (dataEntry.getCategory().equals("Billable PM")) {
            dataEntry.setBillableProjectManager(dataEntry.getValue() * dataEntry.getProbability());
        } else {
            dataEntry.setBillableProjectManager(0);
        }
    }

    // Calculate Confirmed
    private void calculateConfirmed(DataEntry dataEntry) {
        String category = dataEntry.getCategory();
        if (category.equals("Offshore Confirmed") || category.equals("Onsite Confirmed")) {
            dataEntry.setConfirmed(dataEntry.getValue() * dataEntry.getProbability());
        } else {
            dataEntry.setConfirmed(0);
        }
    }

    // Calculate Upside
    private void calculateUpside(DataEntry dataEntry) {
        String category = dataEntry.getCategory();
        if (category.equals("Offshore Upside") || category.equals("Onsite Upside")) {
            dataEntry.setUpside(dataEntry.getValue());
        } else {
            dataEntry.setUpside(0);
        }
    }

    // Calculate Likely
    private void calculateLikely(DataEntry dataEntry) {
        float confirmed = dataEntry.getConfirmed();
        float probability = dataEntry.getProbability() / 100.0f; // Ensure probability is treated as a percentage
        float upside = dataEntry.getUpside();
        float likely = confirmed + (probability * upside);
        dataEntry.setLikely(likely);
    }



}