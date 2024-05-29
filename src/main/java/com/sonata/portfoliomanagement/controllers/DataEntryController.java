package com.sonata.portfoliomanagement.controllers;

import java.util.*;
import java.util.stream.Collectors;
import java.util.logging.Logger;

import com.sonata.portfoliomanagement.interfaces.PursuitTrackerRepository;
import com.sonata.portfoliomanagement.interfaces.RevenueBudgetSummaryRepository;
import com.sonata.portfoliomanagement.interfaces.RevenueGrowthSummaryRepository;
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
        List<DataEntry> dataentry = dataEntryRepo.findAll();
        return ResponseEntity.ok(dataentry);
    }

    @GetMapping("/dataentry/blue/{financialYear}")
    public ResponseEntity<List<DataEntryDTO>> getDataEntriesByFinancialYear(@PathVariable int financialYear) {
        List<DataEntry> dataEntries = dataEntryRepo.findAllByFinancialYear(financialYear);

        List<DataEntryDTO> dataEntryDTOs = dataEntries.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dataEntryDTOs);
    }

    //use smaller 'o' in 'annuityorNonAnnuity' because O was being considered as OR-->
    private DataEntryDTO convertToDTO(DataEntry dataEntry) {
        DataEntryDTO dto = new DataEntryDTO();
        dto.setId(dataEntry.getId());
        dto.setMonth(dataEntry.getMonth());
        dto.setVertical(dataEntry.getVertical());
        dto.setClassification(dataEntry.getClassification());
        dto.setDeliveryManager(dataEntry.getDeliveryManager());
        dto.setDeliveryDirector(dataEntry.getDeliveryDirector());
        dto.setAccount(dataEntry.getAccount());
        dto.setProjectManager(dataEntry.getProjectManager());
        dto.setProjectName(dataEntry.getProjectName());
        dto.setCategory(dataEntry.getCategory());
        dto.setAnnuityorNonAnnuity(dataEntry.getAnnuityorNonAnnuity());
        dto.setValue(dataEntry.getValue());
        dto.setFinancialYear(dataEntry.getFinancialYear());
        dto.setQuarter(dataEntry.getQuarter());
        dto.setBudget(dataEntry.getBudget());

        return dto;
    }

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

                DataEntry updatedDataEntry = dataEntryService.saveDataEntry(existingDataEntry);
                updatedDataEntries.add(updatedDataEntry);
            } else {
                // if data entry with the given ID is not found, add it to the response
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

        // Update the existing data entry with new values-->
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
            // update the fields from DTO
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

        // Extract data fields from the retrieved data-->
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

//    @PostMapping("/populate")
//    private String fetchData() {
//        List<DataEntry> dataEntryList = dataEntryRepo.findAll();
//        for (DataEntry dataEntry : dataEntryList) {
//            RevenueBudgetSummary revenue = getRevenueBudgetSummary(dataEntry);
//            // check if a duplicate entry exists
//            RevenueBudgetSummary existingRevenue = revenueBudgetSummaryRepository.findByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarterAndBudget(
//                    dataEntry.getVertical(),
//                    dataEntry.getClassification(),
//                    dataEntry.getDeliveryDirector(),
//                    dataEntry.getDeliveryManager(),
//                    dataEntry.getAccount(),
//                    dataEntry.getProjectManager(),
//                    dataEntry.getProjectName(),
//                    dataEntry.getFinancialYear(),
//                    dataEntry.getQuarter(),
//                    dataEntry.getBudget()
//            );
//
//            if (existingRevenue != null) {
//                continue;
//            }
//
//            // Fetch data from the repository based on the provided criteria
//            List<DataEntry> data = dataEntryRepo.findAllByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarterAndBudget(
//                    dataEntry.getVertical(),
//                    dataEntry.getClassification(),
//                    dataEntry.getDeliveryDirector(),
//                    dataEntry.getDeliveryManager(),
//                    dataEntry.getAccount(),
//                    dataEntry.getProjectManager(),
//                    dataEntry.getProjectName(),
//                    dataEntry.getFinancialYear(),
//                    dataEntry.getQuarter(),
//                    dataEntry.getBudget()
//            );
//
//            // Calculate total forecast
//            float total = (float) data.stream().mapToDouble(DataEntry::getLikely).sum() / 1000000;
//            revenue.setForecast(total);
//
//            // Calculate gap
//            float temp = dataEntry.getBudget() - revenue.getForecast();
//            revenue.setGap(temp);
//
//            // Save RevenueBudgetSummary
//            revenueBudgetSummaryRepository.save(revenue);
//        }
//
//        return "Data populated successfully to RevenueBudgetSummary";
//    }
//
//    private static RevenueBudgetSummary getRevenueBudgetSummary(DataEntry dataEntry) {
//        RevenueBudgetSummary revenueBudgetSummary = new RevenueBudgetSummary();
//        revenueBudgetSummary.setVertical(dataEntry.getVertical());
//        revenueBudgetSummary.setClassification(dataEntry.getClassification());
//        revenueBudgetSummary.setDeliveryDirector(dataEntry.getDeliveryDirector());
//        revenueBudgetSummary.setDeliveryManager(dataEntry.getDeliveryManager());
//        revenueBudgetSummary.setAccount(dataEntry.getAccount());
//        revenueBudgetSummary.setProjectManager(dataEntry.getProjectManager());
//        revenueBudgetSummary.setProjectName(dataEntry.getProjectName());
//        revenueBudgetSummary.setFinancialYear(dataEntry.getFinancialYear());
//        revenueBudgetSummary.setQuarter(dataEntry.getQuarter());
//        revenueBudgetSummary.setMonth(dataEntry.getMonth());
//        revenueBudgetSummary.setBudget(dataEntry.getBudget()); // Populate the budget field
//        return revenueBudgetSummary;
//    }

    //method to populate revenueGrowthSummary table-->
//    @PostMapping("/populateGrowth")
//    private String fetchDataforRevenueGrowth() {
//        List<DataEntry> dataEntryList = dataEntryRepo.findAll();
//
//        for (DataEntry dataEntry : dataEntryList) {
//
//            // Check if a duplicate entry exists-->
//            RevenueGrowthSummary existingRevenue = revenueGrowthSummaryRepository.findByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarter(
//                    dataEntry.getVertical(),
//                    dataEntry.getClassification(),
//                    dataEntry.getDeliveryDirector(),
//                    dataEntry.getDeliveryManager(),
//                    dataEntry.getAccount(),
//                    dataEntry.getProjectManager(),
//                    dataEntry.getProjectName(),
//                    dataEntry.getFinancialYear(),
//                    dataEntry.getQuarter()
//            );
//
//            //checking for duplicate values-->
//            if (existingRevenue != null) {
//
//                continue;
//            }
//            // Fetch data from the repository based on the provided criteria
//            List<DataEntry> data = dataEntryRepo.findAllByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarter(
//                    dataEntry.getVertical(),
//                    dataEntry.getClassification(),
//                    dataEntry.getDeliveryDirector(),
//                    dataEntry.getDeliveryManager(),
//                    dataEntry.getAccount(),
//                    dataEntry.getProjectManager(),
//                    dataEntry.getProjectName(),
//                    dataEntry.getFinancialYear(),
//                    dataEntry.getQuarter()
//            );
//
//            // Get previous quarter data
//            String previousQuarter = getPreviousQuarter(dataEntry.getQuarter());
//            List<DataEntry> previousQuarterData = dataEntryRepo.findAllByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarter(
//                    dataEntry.getVertical(),
//                    dataEntry.getClassification(),
//                    dataEntry.getDeliveryDirector(),
//                    dataEntry.getDeliveryManager(),
//                    dataEntry.getAccount(),
//                    dataEntry.getProjectManager(),
//                    dataEntry.getProjectName(),
//                    dataEntry.getFinancialYear(),
//                    previousQuarter
//            );
//
//            // Get or create RevenueGrowthSummary and calculate forecast, accountExpected, gap-->
//            RevenueGrowthSummary revenue = getRevenueGrowthSummary(dataEntry, data, previousQuarterData);
//
//            revenueGrowthSummaryRepository.save(revenue);
//        }
//
//        return "Data populated successfully to RevenueGrowthSummary";
//    }
//    private static RevenueGrowthSummary getRevenueGrowthSummary(DataEntry accountBudgets, List<DataEntry> data, List<DataEntry> previousQuarterData) {
//        RevenueGrowthSummary revenueGrowthSummary = new RevenueGrowthSummary();
//        // Set properties of revenueGrowthSummary based on accountBudgets
//        revenueGrowthSummary.setVertical(accountBudgets.getVertical());
//        revenueGrowthSummary.setClassification(accountBudgets.getClassification());
//        revenueGrowthSummary.setDeliveryDirector(accountBudgets.getDeliveryDirector());
//        revenueGrowthSummary.setDeliveryManager(accountBudgets.getDeliveryManager());
//        revenueGrowthSummary.setAccount(accountBudgets.getAccount());
//        revenueGrowthSummary.setProjectManager(accountBudgets.getProjectManager());
//        revenueGrowthSummary.setProjectName(accountBudgets.getProjectName());
//        revenueGrowthSummary.setFinancialYear(accountBudgets.getFinancialYear());
//        revenueGrowthSummary.setQuarter(accountBudgets.getQuarter());
//        revenueGrowthSummary.setMonth(accountBudgets.getMonth());
//        // Calculate forecast from current quarter data
//        float currentQuarterForecast = (float) data.stream().mapToDouble(DataEntry::getLikely).sum() / 1000000;
//        revenueGrowthSummary.setForecast(currentQuarterForecast);
//
//        // Calculate accountExpected based on previous quarter's forecast
//        float previousQuarterForecast = calculatePreviousQuarterForecast(accountBudgets.getQuarter(), previousQuarterData);
//
//        float accountExpected = Math.max(accountBudgets.getBudget(), 1.07f * previousQuarterForecast);
//        revenueGrowthSummary.setAccountExpected(accountExpected);
//
//        // Calculate gap
//        float gap = revenueGrowthSummary.getAccountExpected() - revenueGrowthSummary.getForecast();
//        revenueGrowthSummary.setGap(gap);
//
//        return revenueGrowthSummary;
//    }
//
//    // Helper method to calculate forecast of the previous quarter for a specific account
//    private static float calculatePreviousQuarterForecast(String currentQuarter, List<DataEntry> previousQuarterData) {
//        String previousQuarter = getPreviousQuarter(currentQuarter);
//        return (float) previousQuarterData.stream()
//                .filter(entry -> entry.getQuarter().equals(previousQuarter))
//                .mapToDouble(DataEntry::getLikely)
//                .sum() / 1000000;
//    }
//
//    //method to get the previous quarter based on the current quarter-->
//    private static String getPreviousQuarter(String currentQuarter) {
//        switch (currentQuarter) {
//            case "Q1":
//                return "Q4";
//            case "Q2":
//                return "Q1";
//            case "Q3":
//                return "Q2";
//            case "Q4":
//                return "Q3";
//            default:
//                return "Enter valid data";
//        }
//    }

//    @PostMapping("/updateBudget")
//    public ResponseEntity<String> updateBudgetFromRevenueBudgetSummary() {
//        List<DataEntry> dataEntries = dataEntryRepo.findAll();
//
//        for (DataEntry dataEntry : dataEntries) {
//            RevenueBudgetSummary matchingRevenueBudget = revenueBudgetSummaryRepository.findByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarter(
//                    dataEntry.getVertical(),
//                    dataEntry.getClassification(),
//                    dataEntry.getDeliveryDirector(),
//                    dataEntry.getDeliveryManager(),
//                    dataEntry.getAccount(),
//                    dataEntry.getProjectManager(),
//                    dataEntry.getProjectName(),
//                    dataEntry.getFinancialYear(),
//                    dataEntry.getQuarter()
//            );
//
//            if (matchingRevenueBudget != null) {
//                dataEntry.setBudget(matchingRevenueBudget.getBudget());
//                dataEntryRepo.save(dataEntry);
//            }
//        }
//
//        return ResponseEntity.ok("Budget updated for matching DataEntries.");
//    }



    @PostMapping("/updateBudget")
    public ResponseEntity<String> updateBudgetFromRevenueBudgetSummary() {
        List<DataEntry> dataEntries = dataEntryRepo.findAll();

        for (DataEntry dataEntry : dataEntries) {
            // Find matching RevenueBudgetSummary
            RevenueBudgetSummary matchingRevenueBudget = revenueBudgetSummaryRepository.findByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarter(
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

            // If a match is found, update the budget
            if (matchingRevenueBudget != null) {
                dataEntry.setBudget(matchingRevenueBudget.getBudget());
                // Ensure the ID is not null and corresponds to an existing entry
                if (dataEntry.getId() != null && dataEntryRepo.existsById(dataEntry.getId())) {
                    dataEntryRepo.save(dataEntry);
                }
            }
        }

        return ResponseEntity.ok("Budget updated for matching DataEntries.");
    }









































//    @PostMapping("/populateGrowth")
//    public ResponseEntity<String> fetchDataforRevenueGrowth() {
//        List<DataEntry> dataEntryList = dataEntryRepo.findAll();
//
//        for (DataEntry dataEntry : dataEntryList) {
//            // Check if a duplicate entry exists
//            RevenueGrowthSummary existingRevenue = revenueGrowthSummaryRepository.findByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarter(
//                    dataEntry.getVertical(),
//                    dataEntry.getClassification(),
//                    dataEntry.getDeliveryDirector(),
//                    dataEntry.getDeliveryManager(),
//                    dataEntry.getAccount(),
//                    dataEntry.getProjectManager(),
//                    dataEntry.getProjectName(),
//                    dataEntry.getFinancialYear(),
//                    dataEntry.getQuarter()
//            );
//
//            // Checking for duplicate values
//            if (existingRevenue != null) {
//                continue;
//            }
//
//            // Fetch data from the repository based on the provided criteria
//            List<DataEntry> data = dataEntryRepo.findAllByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarter(
//                    dataEntry.getVertical(),
//                    dataEntry.getClassification(),
//                    dataEntry.getDeliveryDirector(),
//                    dataEntry.getDeliveryManager(),
//                    dataEntry.getAccount(),
//                    dataEntry.getProjectManager(),
//                    dataEntry.getProjectName(),
//                    dataEntry.getFinancialYear(),
//                    dataEntry.getQuarter()
//            );
//
//            // Validate the current quarter
//            if (!isValidQuarter(dataEntry.getQuarter())) {
//                throw new IllegalArgumentException("Invalid quarter: " + dataEntry.getQuarter());
//            }
//
//            // Get previous quarter data
//            String previousQuarterKey = getPreviousQuarter(dataEntry.getFinancialYear(), dataEntry.getQuarter());
//            int previousFinancialYear = extractFinancialYear(previousQuarterKey);
//            String previousQuarter = extractQuarter(previousQuarterKey);
//
//            List<DataEntry> previousQuarterData = dataEntryRepo.findAllByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarter(
//                    dataEntry.getVertical(),
//                    dataEntry.getClassification(),
//                    dataEntry.getDeliveryDirector(),
//                    dataEntry.getDeliveryManager(),
//                    dataEntry.getAccount(),
//                    dataEntry.getProjectManager(),
//                    dataEntry.getProjectName(),
//                    previousFinancialYear,
//                    previousQuarter
//            );
//
//            // Get or create RevenueGrowthSummary and calculate forecast, accountExpected, gap
//            RevenueGrowthSummary revenue = getRevenueGrowthSummary(dataEntry, data, previousQuarterData);
//
//            revenueGrowthSummaryRepository.save(revenue);
//        }
//
//        return ResponseEntity.ok("Data populated successfully to RevenueGrowthSummary");
//    }
//
//    private static RevenueGrowthSummary getRevenueGrowthSummary(DataEntry accountBudgets, List<DataEntry> data, List<DataEntry> previousQuarterData) {
//        RevenueGrowthSummary revenueGrowthSummary = new RevenueGrowthSummary();
//        // Set properties of revenueGrowthSummary based on accountBudgets
//        revenueGrowthSummary.setVertical(accountBudgets.getVertical());
//        revenueGrowthSummary.setClassification(accountBudgets.getClassification());
//        revenueGrowthSummary.setDeliveryDirector(accountBudgets.getDeliveryDirector());
//        revenueGrowthSummary.setDeliveryManager(accountBudgets.getDeliveryManager());
//        revenueGrowthSummary.setAccount(accountBudgets.getAccount());
//        revenueGrowthSummary.setProjectManager(accountBudgets.getProjectManager());
//        revenueGrowthSummary.setProjectName(accountBudgets.getProjectName());
//        revenueGrowthSummary.setFinancialYear(accountBudgets.getFinancialYear());
//        revenueGrowthSummary.setQuarter(accountBudgets.getQuarter());
//        revenueGrowthSummary.setMonth(accountBudgets.getMonth());
//        // Calculate forecast from current quarter data
//        float currentQuarterForecast = (float) data.stream().mapToDouble(DataEntry::getLikely).sum() / 1000000;
//        revenueGrowthSummary.setForecast(currentQuarterForecast);
//
//        // Calculate accountExpected based on previous quarter's forecast
//        float previousQuarterForecast = calculatePreviousQuarterForecast(previousQuarterData);
//
//        float accountExpected = Math.max(accountBudgets.getBudget(), 1.07f * previousQuarterForecast);
//        revenueGrowthSummary.setAccountExpected(accountExpected);
//
//        // Calculate gap
//        float gap = revenueGrowthSummary.getAccountExpected() - revenueGrowthSummary.getForecast();
//        revenueGrowthSummary.setGap(gap);
//
//        return revenueGrowthSummary;
//    }
//
//    // Helper method to calculate forecast of the previous quarter for a specific account
//    private static float calculatePreviousQuarterForecast(List<DataEntry> previousQuarterData) {
//        return (float) previousQuarterData.stream()
//                .mapToDouble(DataEntry::getLikely)
//                .sum() / 1000000;
//    }
//
//    // Method to get the previous quarter based on the current quarter
//    private static String getPreviousQuarter(int financialYear, String currentQuarter) {
//        switch (currentQuarter) {
//            case "Q1":
//                return (financialYear - 1) + "Q4";
//            case "Q2":
//                return financialYear + "Q1";
//            case "Q3":
//                return financialYear + "Q2";
//            case "Q4":
//                return financialYear + "Q3";
//            default:
//                throw new IllegalArgumentException("Invalid quarter: " + currentQuarter);
//        }
//    }
//
//    // Method to extract financial year from the previous quarter key
//    private static int extractFinancialYear(String previousQuarterKey) {
//        try {
//            return Integer.parseInt(previousQuarterKey.substring(0, 4));
//        } catch (NumberFormatException e) {
//            throw new IllegalArgumentException("Invalid financial year in previous quarter key: " + previousQuarterKey, e);
//        }
//    }
//
//    // Method to extract quarter from the previous quarter key
//    private static String extractQuarter(String previousQuarterKey) {
//        return previousQuarterKey.substring(4);
//    }
//
//    // Validate the quarter format
//    private static boolean isValidQuarter(String quarter) {
//        return quarter.equals("Q1") || quarter.equals("Q2") || quarter.equals("Q3") || quarter.equals("Q4");
//    }





















    @PostMapping("/populate")
    public ResponseEntity<String> fetchDataAndPopulateTables() {
        List<DataEntry> dataEntryList = dataEntryRepo.findAll();

        for (DataEntry dataEntry : dataEntryList) {
            populateRevenueBudgetSummary(dataEntry);
            populateRevenueGrowthSummary(dataEntry);
        }

        return ResponseEntity.ok("Data populated successfully to both RevenueBudgetSummary and RevenueGrowthSummary");
    }

    private void populateRevenueBudgetSummary(DataEntry dataEntry) {
        // Check if the entry already exists in RevenueBudgetSummary
        if (isEntryExistsInRevenueBudgetSummary(dataEntry)) {
            // Log and skip existing entries
            System.out.println("Entry already exists in RevenueBudgetSummary. Skipping population for: " + dataEntry);
            return;
        }

        // Populate the entry in RevenueBudgetSummary
        RevenueBudgetSummary revenue = getRevenueBudgetSummary(dataEntry);

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

        float total = (float) data.stream().mapToDouble(DataEntry::getLikely).sum() / 1000000;
        revenue.setForecast(total);

        float temp = dataEntry.getBudget() - revenue.getForecast();
        revenue.setGap(temp);

        revenueBudgetSummaryRepository.save(revenue);
    }

    private boolean isEntryExistsInRevenueBudgetSummary(DataEntry dataEntry) {
        List<RevenueBudgetSummary> existingEntries = revenueBudgetSummaryRepository
                .findByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarterAndBudget(
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

        return !existingEntries.isEmpty();
    }

    private void populateRevenueGrowthSummary(DataEntry dataEntry) {
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

        if (existingRevenue != null) {
            return;
        }

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

        if (!isValidQuarter(dataEntry.getQuarter())) {
            throw new IllegalArgumentException("Invalid quarter: " + dataEntry.getQuarter());
        }

        String previousQuarterKey = getPreviousQuarter(dataEntry.getFinancialYear(), dataEntry.getQuarter());
        int previousFinancialYear = extractFinancialYear(previousQuarterKey);
        String previousQuarter = extractQuarter(previousQuarterKey);

        List<DataEntry> previousQuarterData = dataEntryRepo.findAllByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarter(
                dataEntry.getVertical(),
                dataEntry.getClassification(),
                dataEntry.getDeliveryDirector(),
                dataEntry.getDeliveryManager(),
                dataEntry.getAccount(),
                dataEntry.getProjectManager(),
                dataEntry.getProjectName(),
                previousFinancialYear,
                previousQuarter
        );

        RevenueGrowthSummary revenue = getRevenueGrowthSummary(dataEntry, data, previousQuarterData);

        revenueGrowthSummaryRepository.save(revenue);
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
        revenueBudgetSummary.setBudget(dataEntry.getBudget());
        return revenueBudgetSummary;
    }

    private static RevenueGrowthSummary getRevenueGrowthSummary(DataEntry accountBudgets, List<DataEntry> data, List<DataEntry> previousQuarterData) {
        RevenueGrowthSummary revenueGrowthSummary = new RevenueGrowthSummary();
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

        float currentQuarterForecast = (float) data.stream().mapToDouble(DataEntry::getLikely).sum() / 1000000;
        revenueGrowthSummary.setForecast(currentQuarterForecast);

        float previousQuarterForecast = calculatePreviousQuarterForecast(previousQuarterData);

        float accountExpected = Math.max(accountBudgets.getBudget(), 1.07f * previousQuarterForecast);
        revenueGrowthSummary.setAccountExpected(accountExpected);

        float gap = revenueGrowthSummary.getAccountExpected() - revenueGrowthSummary.getForecast();
        revenueGrowthSummary.setGap(gap);

        return revenueGrowthSummary;
    }

    private static float calculatePreviousQuarterForecast(List<DataEntry> previousQuarterData) {
        return (float) previousQuarterData.stream().mapToDouble(DataEntry::getLikely).sum() / 1000000;
    }

    private static String getPreviousQuarter(int financialYear, String currentQuarter) {
        switch (currentQuarter) {
            case "Q1":
                return (financialYear - 1) + "Q4";
            case "Q2":
                return financialYear + "Q1";
            case "Q3":
                return financialYear + "Q2";
            case "Q4":
                return financialYear + "Q3";
            default:
                throw new IllegalArgumentException("Invalid quarter: " + currentQuarter);
        }
    }

    private static int extractFinancialYear(String previousQuarterKey) {
        try {
            return Integer.parseInt(previousQuarterKey.substring(0, 4));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid financial year in previous quarter key: " + previousQuarterKey, e);
        }
    }

    private static String extractQuarter(String previousQuarterKey) {
        return previousQuarterKey.substring(4);
    }

    private static boolean isValidQuarter(String quarter) {
        return quarter.equals("Q1") || quarter.equals("Q2") || quarter.equals("Q3") || quarter.equals("Q4");
    }
}