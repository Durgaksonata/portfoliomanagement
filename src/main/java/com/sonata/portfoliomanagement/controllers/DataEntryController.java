package com.sonata.portfoliomanagement.controllers;

import java.util.*;
import java.util.stream.Collectors;

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
    public ResponseEntity<String> createDataEntry(@RequestBody DataEntryDTO dataEntryDTO) {
        List<DataEntry> existingEntries = dataEntryRepo.findAllByVerticalAndClassificationAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarterAndMonthAndDeliveryDirectorAndCategoryAndAnnuityorNonAnnuityAndValue(
                dataEntryDTO.getVertical(), dataEntryDTO.getClassification(), dataEntryDTO.getDeliveryManager(), dataEntryDTO.getAccount(), dataEntryDTO.getProjectManager(), dataEntryDTO.getProjectName(), dataEntryDTO.getFinancialYear(), dataEntryDTO.getQuarter(), dataEntryDTO.getMonth(), dataEntryDTO.getDeliveryDirector(), dataEntryDTO.getCategory(), dataEntryDTO.getAnnuityorNonAnnuity(), dataEntryDTO.getValue());

        if (!existingEntries.isEmpty()) {
            return new ResponseEntity<>("Duplicate entry exists", HttpStatus.CONFLICT);
        }

        DataEntry dataEntry = dataEntryService.createDataEntryFromDTO(dataEntryDTO);
        DataEntry createdDataEntry = dataEntryService.saveDataEntry(dataEntry);
        return new ResponseEntity<>("Data entry created successfully", HttpStatus.CREATED);
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
            existingDataEntry.setAnnuityorNonAnnuity(dataEntryDTO.getAnnuityorNonAnnuity());
            existingDataEntry.setValue(dataEntryDTO.getValue());
            existingDataEntry.setFinancialYear(dataEntryDTO.getFinancialYear());
            existingDataEntry.setQuarter(dataEntryDTO.getQuarter());

            // Recalculate fields and save the updated entry
            DataEntry updatedDataEntry = dataEntryService.saveDataEntry(existingDataEntry);
            return new ResponseEntity<>(updatedDataEntry, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/getfinancialyearlist")
    public List<Integer> getFinancialYearList() {
        List<Integer> fyList = new ArrayList<>();
        List<DataEntry> dataentryData = dataEntryRepo.findAll();

        for (DataEntry request : dataentryData) {
            fyList.add(request.getFinancialYear());
        }
        return fyList.stream().distinct().collect(Collectors.toList());
    }
    @PostMapping("/databyfy")
    public ResponseEntity<List<Map<String, Object>>> getDataByFy(@RequestBody DataEntryDTO requestDTO) {
        int financialYear = requestDTO.getFinancialYear();
        List<Map<String, Object>> data = new ArrayList<>();
        // Retrieve data based on financial year
        List<DataEntry> dataList = dataEntryRepo.findByFinancialYear(financialYear);

        // Extract data fields from the retrieved data
        for (DataEntry dataItem : dataList) {
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("id", dataItem.getId());
            dataMap.put("vertical", dataItem.getVertical());
            dataMap.put("classification", dataItem.getClassification());
            dataMap.put("deliveryDirector", dataItem.getDeliveryDirector());
            dataMap.put("deliveryManager", dataItem.getDeliveryManager());
            dataMap.put("account", dataItem.getAccount());
            dataMap.put("projectManager", dataItem.getProjectManager());
            dataMap.put("projectName", dataItem.getProjectName());
            dataMap.put("financialYear", dataItem.getFinancialYear());
            dataMap.put("quarter", dataItem.getQuarter());
            dataMap.put("month", dataItem.getMonth());
            dataMap.put("category", dataItem.getCategory());
            dataMap.put("annuityorNonAnnuity", dataItem.getAnnuityorNonAnnuity());
            dataMap.put("value", dataItem.getValue());
            dataMap.put("type", dataItem.getType());
            dataMap.put("probability", dataItem.getProbability());
            dataMap.put("projectsOrPursuitStage", dataItem.getProjectsOrPursuitStage());
            dataMap.put("confirmed", dataItem.getConfirmed());
            dataMap.put("upside", dataItem.getUpside());
            dataMap.put("likely", dataItem.getLikely());
            dataMap.put("annuityRevenue", dataItem.getAnnuityRevenue());
            dataMap.put("nonAnnuityRevenue", dataItem.getNonAnnuityRevenue());
            dataMap.put("offshoreCost", dataItem.getOffshoreCost());
            dataMap.put("onsiteCost", dataItem.getOnsiteCost());
            dataMap.put("totalCost", dataItem.getTotalCost());
            dataMap.put("offshoreProjectManager", dataItem.getOffshoreProjectManager());
            dataMap.put("onsiteProjectManager", dataItem.getOnsiteProjectManager());
            dataMap.put("billableProjectManager", dataItem.getBillableProjectManager());
            data.add(dataMap);
        }

        return ResponseEntity.ok(data);
    }

}