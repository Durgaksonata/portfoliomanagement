package com.sonata.portfoliomanagement.controllers;

import java.util.List;

import com.sonata.portfoliomanagement.interfaces.RevenueBudgetSummaryRepository;
import com.sonata.portfoliomanagement.model.AccountBudgets;
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
    public ResponseEntity<DataEntry> createAcBudgets(@RequestBody DataEntry dataEntry) {
        DataEntry createdDataEntry = dataEntryRepo.save(dataEntry);
        return new ResponseEntity<>(createdDataEntry, HttpStatus.CREATED);
    }
}