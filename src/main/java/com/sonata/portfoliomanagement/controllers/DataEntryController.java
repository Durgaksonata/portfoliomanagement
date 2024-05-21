package com.sonata.portfoliomanagement.controllers;

import java.util.List;

import com.sonata.portfoliomanagement.interfaces.PursuitTrackerRepository;
import com.sonata.portfoliomanagement.interfaces.RevenueBudgetSummaryRepository;
import com.sonata.portfoliomanagement.model.*;
import com.sonata.portfoliomanagement.services.DataEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sonata.portfoliomanagement.interfaces.DataEntryRepository;


@RestController
@RequestMapping("/dataentry")
public class DataEntryController {

    @Autowired
    private DataEntryRepository dataEntryRepo;
    @Autowired
    private DataEntryService dataEntryService;
    @Autowired
    private PursuitTrackerRepository pursuitTrackerRepository;
    @Autowired
    private RevenueBudgetSummaryRepository revenueBudgetSummaryRepository;

    @GetMapping("/get")
    public ResponseEntity<List<DataEntry>> getAllData() {
        List<DataEntry> dataentry = dataEntryRepo.findAll();
        return ResponseEntity.ok(dataentry);
    }


    @PostMapping("/save")
    public ResponseEntity<DataEntry> createDataEntry(@RequestBody DataEntryDTO dataEntryDTO) {
        DataEntry dataEntry = dataEntryService.createDataEntryFromDTO(dataEntryDTO);
        DataEntry createdDataEntry = dataEntryService.saveDataEntry(dataEntry);
        return new ResponseEntity<>(createdDataEntry, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DataEntry> updateDataEntry(@PathVariable Integer id, @RequestBody DataEntryDTO dataEntryDTO) {
        return dataEntryRepo.findById(id).map(existingDataEntry -> {
            // Update the fields from DTO
            existingDataEntry.setMonth(dataEntryDTO.getMonth());
            existingDataEntry.setVertical(dataEntryDTO.getVertical());
            existingDataEntry.setClassification(dataEntryDTO.getClassification());
            existingDataEntry.setDeliveryDirector(dataEntryDTO.getDeliveryDirector());
            existingDataEntry.setDeliveryManager(dataEntryDTO.getDeliveryManager());
            existingDataEntry.setAccount(dataEntryDTO.getAccount());
            existingDataEntry.setProjectManager(dataEntryDTO.getProjectManager());
            existingDataEntry.setProjectName(dataEntryDTO.getProjectName());
            existingDataEntry.setCategory(dataEntryDTO.getCategory());
            existingDataEntry.setAnnuityOrNonAnnuity(dataEntryDTO.getAnnuityOrNonAnnuity());
            existingDataEntry.setValue(dataEntryDTO.getValue());
            existingDataEntry.setFinancialYear(dataEntryDTO.getFinancialYear());
            existingDataEntry.setQuarter(dataEntryDTO.getQuarter());

            // Recalculate fields and save the updated entry
            DataEntry updatedDataEntry = dataEntryService.saveDataEntry(existingDataEntry);
            return new ResponseEntity<>(updatedDataEntry, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }




}