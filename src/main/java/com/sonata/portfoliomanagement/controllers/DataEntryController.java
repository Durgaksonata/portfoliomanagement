package com.sonata.portfoliomanagement.controllers;

import java.util.*;
import java.util.stream.Collectors;
import java.util.logging.Logger;

import com.sonata.portfoliomanagement.interfaces.PursuitTrackerRepository;
import com.sonata.portfoliomanagement.interfaces.RevenueBudgetSummaryRepository;
import com.sonata.portfoliomanagement.interfaces.RevenueGrowthSummaryRepository;
import com.sonata.portfoliomanagement.model.*;
import com.sonata.portfoliomanagement.services.DataEntryService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sonata.portfoliomanagement.interfaces.DataEntryRepository;


@RestController
@RequestMapping("/dataentry")
public class DataEntryController {
    private static final Logger logger = Logger.getLogger(DataEntryController.class.getName());

    @Autowired
    private DataEntryRepository dataEntryRepo;
    @Autowired
    private DataEntryService dataEntryService;
    @Autowired
    private PursuitTrackerRepository pursuitTrackerRepository;
    @Autowired
    private RevenueBudgetSummaryRepository revenueBudgetSummaryRepository;
    @Autowired
    private RevenueGrowthSummaryRepository revenueGrowthSummaryRepository;

    @GetMapping("/get")
    public ResponseEntity<List<DataEntry>> getAllData() {
        List<DataEntry> dataEntries = dataEntryRepo.findAllByOrderByIdDesc();
        return ResponseEntity.ok(dataEntries);
    }

//    @PostMapping("/save")
//    public ResponseEntity<DataEntry> createDataEntry(@RequestBody DataEntryDTO dataEntryDTO) {
//        if (dataEntryService.isDuplicateEntry(dataEntryDTO)) {
//            return new ResponseEntity<>(HttpStatus.CONFLICT);
//        }
//
//        DataEntry dataEntry = dataEntryService.createDataEntryFromDTO(dataEntryDTO);
//        DataEntry savedDataEntry = dataEntryService.saveDataEntry(dataEntry);
//        return new ResponseEntity<>(savedDataEntry, HttpStatus.CREATED);
//    }

    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> createDataEntries(@RequestBody List<DataEntryDTO> dataEntryDTOList) {
        Map<String, Object> response = new HashMap<>();
        List<DataEntry> savedDataEntries = new ArrayList<>();

        for (DataEntryDTO dataEntryDTO : dataEntryDTOList) {
            // Check for duplicate entries for each DTO
            if (dataEntryService.isDuplicateEntry(dataEntryDTO)) {
                response.put("message", "Data already exists for one or more entries.");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }

            // Create DataEntry entity from DTO
            DataEntry dataEntry = dataEntryService.createDataEntryFromDTO(dataEntryDTO);
            // Save DataEntry
            DataEntry savedDataEntry = dataEntryService.saveDataEntry(dataEntry);
            savedDataEntries.add(savedDataEntry);
        }

        response.put("message", "Data successfully created.");
        response.put("data", savedDataEntries);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //It's a helper method for the below one-->
    private DataEntry getExistingDataEntry(Optional<DataEntry> optionalDataEntry) {
        return optionalDataEntry.orElse(null);
    }

    //Method for updating multiple rows of data at once-->
    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateDataEntries(@RequestBody List<DataEntryDTO> dataEntryDTOList) {
        Map<String, Object> response = new HashMap<>();
        List<DataEntry> updatedDataEntries = new ArrayList<>();

        for (DataEntryDTO dataEntryDTO : dataEntryDTOList) {
            // Find the data entry by ID
            Optional<DataEntry> optionalDataEntry = dataEntryRepo.findById(dataEntryDTO.getId());

            // Get the existing data entry
            DataEntry existingDataEntry = getExistingDataEntry(optionalDataEntry);

            // Check if data entry exists
            if (existingDataEntry != null) {
                // Update the existing data entry with new values
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

                // Save the updated data entry
                DataEntry updatedDataEntry = dataEntryService.saveDataEntry(existingDataEntry);
                updatedDataEntries.add(updatedDataEntry);
            } else {
                // If data entry with the given ID is not found, add it to the response
                response.put("message", "Data entry not found for one or more entries.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        }

        response.put("message", "Data successfully updated.");
        response.put("data", updatedDataEntries);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private static DataEntry getDataEntry(DataEntryDTO dataEntryDTO, Optional<DataEntry> optionalDataEntry) {
        DataEntry existingDataEntry = optionalDataEntry.get();

        // Update the existing data entry with new values
        existingDataEntry.setMonth(dataEntryDTO.getMonth());
        existingDataEntry.setVertical(dataEntryDTO.getVertical());
        existingDataEntry.setClassification(dataEntryDTO.getClassification());
        existingDataEntry.setDeliveryDirector(dataEntryDTO.getDeliveryDirector());
        existingDataEntry.setDeliveryManager(dataEntryDTO.getDeliveryManager());
        existingDataEntry.setCategory(dataEntryDTO.getCategory());
        existingDataEntry.setAnnuityorNonAnnuity(dataEntryDTO.getAnnuityorNonAnnuity());
        existingDataEntry.setValue(dataEntryDTO.getValue());
        return existingDataEntry;
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





    @PostMapping("/populate")
    private String fetchData() {
        List<DataEntry> dataEntryList = dataEntryRepo.findAll();
        for (DataEntry dataEntry : dataEntryList) {
            // Get or create RevenueBudgetSummary
            RevenueBudgetSummary revenue = getRevenueBudgetSummary(dataEntry);
            // Check if a duplicate entry exists
            RevenueBudgetSummary existingRevenue = revenueBudgetSummaryRepository.findByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarterAndBudget(
                    dataEntry.getVertical(),
                    dataEntry.getClassification(),
                    dataEntry.getDeliveryDirector(),
                    dataEntry.getDeliveryManager(),
                    dataEntry.getAccount(),
                    dataEntry.getProjectManager(),
                    dataEntry.getProjectName(),
                    dataEntry.getFinancialYear(),
                    dataEntry.getQuarter(),
                    dataEntry.getBudget()
            );

            if (existingRevenue != null) {
                continue;
            }

            // Fetch data from the repository based on the provided criteria
            List<DataEntry> data = dataEntryRepo.findAllByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarterAndBudget(
                    dataEntry.getVertical(),
                    dataEntry.getClassification(),
                    dataEntry.getDeliveryDirector(),
                    dataEntry.getDeliveryManager(),
                    dataEntry.getAccount(),
                    dataEntry.getProjectManager(),
                    dataEntry.getProjectName(),
                    dataEntry.getFinancialYear(),
                    dataEntry.getQuarter(),
                    dataEntry.getBudget()
            );

            // Calculate total forecast
            float total = (float) data.stream().mapToDouble(DataEntry::getLikely).sum() / 1000000;
            revenue.setForecast(total);

            // Calculate gap
            float temp = dataEntry.getBudget() - revenue.getForecast();
            revenue.setGap(temp);

            // Save RevenueBudgetSummary
            revenueBudgetSummaryRepository.save(revenue);
        }

        return "Data populated successfully to RevenueBudgetSummary";
    }

    private static RevenueBudgetSummary getRevenueBudgetSummary(DataEntry dataEntry) {
        RevenueBudgetSummary revenueBudgetSummary = new RevenueBudgetSummary();
        revenueBudgetSummary.setVertical(dataEntry.getVertical());
        revenueBudgetSummary.setClassification(dataEntry.getClassification());
        revenueBudgetSummary.setDeliveryDirector(dataEntry.getDeliveryDirector());
        revenueBudgetSummary.setDeliveryManager(dataEntry.getDeliveryManager());
        revenueBudgetSummary.setAccount(dataEntry.getAccount());
        revenueBudgetSummary.setProjectManager(dataEntry.getProjectManager());
        revenueBudgetSummary.setProjectName(dataEntry.getProjectName());
        revenueBudgetSummary.setFinancialYear(dataEntry.getFinancialYear());
        revenueBudgetSummary.setQuarter(dataEntry.getQuarter());
        revenueBudgetSummary.setMonth(dataEntry.getMonth());
        revenueBudgetSummary.setBudget(dataEntry.getBudget()); // Populate the budget field
        return revenueBudgetSummary;
    }




    //method to populate revenueGrowthSummary table-->
    @PostMapping("/populateGrowth")
    private String fetchDataforRevenueGrowth() {
        List<DataEntry> dataEntryList = dataEntryRepo.findAll();

        for (DataEntry dataEntry : dataEntryList) {

            // Check if a duplicate entry exists-->
            RevenueGrowthSummary existingRevenue = revenueGrowthSummaryRepository.findByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarter(
                    dataEntry.getVertical(),
                    dataEntry.getClassification(),
                    dataEntry.getDeliveryDirector(),
                    dataEntry.getDeliveryManager(),
                    dataEntry.getAccount(),
                    dataEntry.getProjectManager(),
                    dataEntry.getProjectName(),
                    dataEntry.getFinancialYear(),
                    dataEntry.getQuarter()
            );

            //checking for duplicate values-->
            if (existingRevenue != null) {

                continue;
            }
            // Fetch data from the repository based on the provided criteria
            List<DataEntry> data = dataEntryRepo.findAllByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarter(
                    dataEntry.getVertical(),
                    dataEntry.getClassification(),
                    dataEntry.getDeliveryDirector(),
                    dataEntry.getDeliveryManager(),
                    dataEntry.getAccount(),
                    dataEntry.getProjectManager(),
                    dataEntry.getProjectName(),
                    dataEntry.getFinancialYear(),
                    dataEntry.getQuarter()
            );


            // Get previous quarter data
            String previousQuarter = getPreviousQuarter(dataEntry.getQuarter());
            List<DataEntry> previousQuarterData = dataEntryRepo.findAllByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarter(
                    dataEntry.getVertical(),
                    dataEntry.getClassification(),
                    dataEntry.getDeliveryDirector(),
                    dataEntry.getDeliveryManager(),
                    dataEntry.getAccount(),
                    dataEntry.getProjectManager(),
                    dataEntry.getProjectName(),
                    dataEntry.getFinancialYear(),
                    previousQuarter
            );

            // Get or create RevenueGrowthSummary and calculate forecast, accountExpected, gap-->
            RevenueGrowthSummary revenue = getRevenueGrowthSummary(dataEntry, data, previousQuarterData);

            revenueGrowthSummaryRepository.save(revenue);
        }

        return "Data populated successfully to RevenueGrowthSummary";
    }
    private static RevenueGrowthSummary getRevenueGrowthSummary(DataEntry accountBudgets, List<DataEntry> data, List<DataEntry> previousQuarterData) {
        RevenueGrowthSummary revenueGrowthSummary = new RevenueGrowthSummary();
        // Set properties of revenueGrowthSummary based on accountBudgets
        revenueGrowthSummary.setVertical(accountBudgets.getVertical());
        revenueGrowthSummary.setClassification(accountBudgets.getClassification());
        revenueGrowthSummary.setDeliveryDirector(accountBudgets.getDeliveryDirector());
        revenueGrowthSummary.setDeliveryManager(accountBudgets.getDeliveryManager());
        revenueGrowthSummary.setAccount(accountBudgets.getAccount());
        revenueGrowthSummary.setProjectManager(accountBudgets.getProjectManager());
        revenueGrowthSummary.setProjectName(accountBudgets.getProjectName());
        revenueGrowthSummary.setFinancialYear(accountBudgets.getFinancialYear());
        revenueGrowthSummary.setQuarter(accountBudgets.getQuarter());
        revenueGrowthSummary.setMonth(accountBudgets.getMonth());
        // Calculate forecast from current quarter data
        float currentQuarterForecast = (float) data.stream().mapToDouble(DataEntry::getLikely).sum() / 1000000;
        revenueGrowthSummary.setForecast(currentQuarterForecast);

        // Calculate accountExpected based on previous quarter's forecast
        float previousQuarterForecast = calculatePreviousQuarterForecast(accountBudgets.getQuarter(), previousQuarterData);

        float accountExpected = Math.max(accountBudgets.getBudget(), 1.07f * previousQuarterForecast);
        revenueGrowthSummary.setAccountExpected(accountExpected);

        // Calculate gap
        float gap = revenueGrowthSummary.getAccountExpected() - revenueGrowthSummary.getForecast();
        revenueGrowthSummary.setGap(gap);

        return revenueGrowthSummary;
    }

    // Helper method to calculate forecast of the previous quarter for a specific account
    private static float calculatePreviousQuarterForecast(String currentQuarter, List<DataEntry> previousQuarterData) {
        String previousQuarter = getPreviousQuarter(currentQuarter);
        return (float) previousQuarterData.stream()
                .filter(entry -> entry.getQuarter().equals(previousQuarter))
                .mapToDouble(DataEntry::getLikely)
                .sum() / 1000000;
    }

    //method to get the previous quarter based on the current quarter-->
    private static String getPreviousQuarter(String currentQuarter) {
        switch (currentQuarter) {
            case "Q1":
                return "Q4";
            case "Q2":
                return "Q1";
            case "Q3":
                return "Q2";
            case "Q4":
                return "Q3";
            default:
                return "Enter valid data";
        }
    }


    @Transactional
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteDataEntry(@PathVariable Integer id) {
        Optional<DataEntry> dataEntryOptional = dataEntryRepo.findById(id);

        if (!dataEntryOptional.isPresent()) {
            return new ResponseEntity<>("Data entry not found", HttpStatus.NOT_FOUND);
        }

        DataEntry dataEntry = dataEntryOptional.get();

        // Delete related entries in RevenueBudgetSummary
        revenueBudgetSummaryRepository.deleteByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarter(
                dataEntry.getVertical(),
                dataEntry.getClassification(),
                dataEntry.getDeliveryDirector(),
                dataEntry.getDeliveryManager(),
                dataEntry.getAccount(),
                dataEntry.getProjectManager(),
                dataEntry.getProjectName(),
                dataEntry.getFinancialYear(),
                dataEntry.getQuarter()
        );

        // Delete related entries in RevenueGrowthSummary
        revenueGrowthSummaryRepository.deleteByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarter(
                dataEntry.getVertical(),
                dataEntry.getClassification(),
                dataEntry.getDeliveryDirector(),
                dataEntry.getDeliveryManager(),
                dataEntry.getAccount(),
                dataEntry.getProjectManager(),
                dataEntry.getProjectName(),
                dataEntry.getFinancialYear(),
                dataEntry.getQuarter()
        );

        // Delete the data entry itself
        dataEntryRepo.deleteById(id);

        return new ResponseEntity<>("Data entry and related summaries deleted successfully", HttpStatus.OK);
    }



}