package com.sonata.portfoliomanagement.controllers;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.logging.Logger;

import com.sonata.portfoliomanagement.interfaces.DataEntryRepository;
import com.sonata.portfoliomanagement.interfaces.PursuitTrackerRepository;
import com.sonata.portfoliomanagement.interfaces.RevenueBudgetSummaryRepository;
import com.sonata.portfoliomanagement.interfaces.RevenueGrowthSummaryRepository;
import com.sonata.portfoliomanagement.model.*;
import com.sonata.portfoliomanagement.services.DataEntryService;
import com.sonata.portfoliomanagement.services.DataPopulationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173" )
@RestController
@RequestMapping("/dataentry")
public class DataEntryController {
    private static final Logger logger = Logger.getLogger(DataEntryController.class.getName());

    @Autowired
    private DataEntryService dataEntryService;
    @Autowired
    private PursuitTrackerRepository pursuitTrackerRepository;
    @Autowired
    private DataEntryRepository dataEntryRepo;

    @Autowired
    private DataPopulationService dataPopulationService;

    @Autowired
    private RevenueBudgetSummaryRepository revenueBudgetSummaryRepository;
    @Autowired
    private RevenueGrowthSummaryRepository revenueGrowthSummaryRepository;

    @GetMapping("/get")
    public ResponseEntity<List<DataEntry>> getAllData() {
        List<DataEntry> dataEntries = dataEntryRepo.findAllByOrderByIdDesc();
        return ResponseEntity.ok(dataEntries);
    }


    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateDataEntries(@RequestBody List<DataEntryDTO> dataEntryDTOList) {
        Map<String, Object> response = new HashMap<>();
        List<DataEntry> updatedDataEntries = new ArrayList<>();

        for (DataEntryDTO dataEntryDTO : dataEntryDTOList) {
            // Find the data entry by ID
            Optional<DataEntry> optionalDataEntry = dataEntryRepo.findById(dataEntryDTO.getId());

            // Check if data entry exists
            if (optionalDataEntry.isPresent()) {
                DataEntry existingDataEntry = optionalDataEntry.get();

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
                existingDataEntry.setBudget(dataEntryDTO.getBudget());
                existingDataEntry.setLastUpdated(LocalDateTime.now()); // Set the last updated timestamp

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



    @PostMapping("/databyfy")
    public ResponseEntity<List<Map<String, Object>>> getDataByFy(@RequestBody DataEntryDTO requestDTO) {
        int financialYear = requestDTO.getFinancialYear();
        List<Map<String, Object>> data = new ArrayList<>();

        // Retrieve data based on financial year and order by lastUpdated and id in descending order
        Sort sort = Sort.by(Sort.Order.desc("lastUpdated"));
        List<DataEntry> dataList = dataEntryRepo.findByFinancialYear(financialYear, sort);

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
            dataMap.put("budget", dataItem.getBudget());
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

    @GetMapping("/getfinancialyearlist")
    public List<Integer> getFinancialYearList() {
        List<Integer> fyList = new ArrayList<>();
        List<DataEntry> dataentryData = dataEntryRepo.findAll();

        for (DataEntry request : dataentryData) {
            fyList.add(request.getFinancialYear());
        }
        return fyList.stream().distinct().collect(Collectors.toList());
    }


    @Transactional
@DeleteMapping("/delete/multiple")
public ResponseEntity<String> deleteMultipleDataEntries(@RequestBody List<Integer> ids) {
    for (Integer id : ids) {
        Optional<DataEntry> dataEntryOptional = dataEntryRepo.findById(id);
        if (dataEntryOptional.isPresent()) {
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
        }
    }

    return new ResponseEntity<>("Data entries and related summaries deleted successfully", HttpStatus.OK);
}

    @GetMapping("/blue/{financialYear}")
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


    @GetMapping("blue/all")
    public List<DataEntryDTO> getAllDataEntries() {
        return dataEntryService.getAllDataEntries();
    }









    @GetMapping("/populateData")
    public String populateData() {
        dataPopulationService.populateData();
        return "Data population completed to both revenuebudget and revenuegrowth tables.";
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
            DataEntry dataEntry = new DataEntry(
                    dataEntryDTO.getId(),
                    dataEntryDTO.getMonth(),
                    dataEntryDTO.getVertical(),
                    dataEntryDTO.getClassification(),
                    dataEntryDTO.getDeliveryDirector(),
                    dataEntryDTO.getDeliveryManager(),
                    dataEntryDTO.getAccount(),
                    dataEntryDTO.getProjectManager(),
                    dataEntryDTO.getProjectName(),
                    dataEntryDTO.getCategory(),
                    dataEntryDTO.getAnnuityorNonAnnuity(),
                    dataEntryDTO.getValue(),
                    null, // Type will be calculated
                    dataEntryDTO.getFinancialYear(),
                    dataEntryDTO.getQuarter(),
                    0, // Probability will be calculated
                    null, // Projects or Pursuit Stage will be calculated
                    0, // Confirmed will be calculated
                    0, // Upside will be calculated
                    0, // Likely will be calculated
                    0, // Annuity Revenue will be calculated
                    0, // Non-Annuity Revenue will be calculated
                    0, // Offshore Cost will be calculated
                    0, // Onsite Cost will be calculated
                    0, // Total Cost will be calculated
                    0, // Offshore Project Manager will be calculated
                    0, // Onsite Project Manager will be calculated
                    0, // Billable Project Manager will be calculated
                    dataEntryDTO.getBudget()
            );

            // Perform calculations
            calculateType(dataEntry);
            calculateOffshoreCost(dataEntry);
            calculateOnsiteCost(dataEntry);
            calculateTotalCost(dataEntry);
            calculateBillablePM(dataEntry);
            calculateConfirmed(dataEntry);
            calculateUpside(dataEntry);
            calculateLikely(dataEntry);
            calculateProjectsOrPursuitStage(dataEntry);
            calculateProbability(dataEntry);
            calculateAnnuityRevenue(dataEntry);
            calculateNonAnnuityRevenue(dataEntry);
            calculateOffshorePM(dataEntry);
            calculateOnsitePM(dataEntry);

            // Save DataEntry
            DataEntry savedDataEntry = dataEntryService.saveDataEntry(dataEntry);
            savedDataEntries.add(savedDataEntry);
        }

        response.put("message", "Data successfully created.");
        response.put("data", savedDataEntries);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Calculation methods here (same as before)
    private void calculateOffshoreCost(DataEntry dataEntry) {
        if (dataEntry.getCategory().equals("Offshore Cost")) {
            dataEntry.setOffshoreCost(dataEntry.getValue() * dataEntry.getProbability() / 100);
        } else {
            dataEntry.setOffshoreCost(0);
        }
    }

    private void calculateOnsiteCost(DataEntry dataEntry) {
        if (dataEntry.getCategory().equals("Onsite Cost")) {
            dataEntry.setOnsiteCost(dataEntry.getValue() * dataEntry.getProbability() / 100);
        } else {
            dataEntry.setOnsiteCost(0);
        }
    }

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

    private void calculateTotalCost(DataEntry dataEntry) {
        float offshoreCost = dataEntry.getOffshoreCost();
        float onsiteCost = dataEntry.getOnsiteCost();
        float totalCost = offshoreCost + onsiteCost;
        dataEntry.setTotalCost(totalCost);
    }

    private void calculateBillablePM(DataEntry dataEntry) {
        if (dataEntry.getCategory().equals("Billable PM")) {
            dataEntry.setBillableProjectManager(dataEntry.getValue() * dataEntry.getProbability() / 100);
        } else {
            dataEntry.setBillableProjectManager(0);
        }
    }

    private void calculateConfirmed(DataEntry dataEntry) {
        String category = dataEntry.getCategory();
        if (category.equals("Offshore Confirmed") || category.equals("Onsite Confirmed")) {
            dataEntry.setConfirmed(dataEntry.getValue() * dataEntry.getProbability() / 100);
        } else {
            dataEntry.setConfirmed(0);
        }
    }

    private void calculateUpside(DataEntry dataEntry) {
        String category = dataEntry.getCategory();
        if (category.equals("Offshore Upside") || category.equals("Onsite Upside")) {
            dataEntry.setUpside(dataEntry.getValue());
        } else {
            dataEntry.setUpside(0);
        }
    }

    private void calculateLikely(DataEntry dataEntry) {
        float confirmed = dataEntry.getConfirmed();
        float probability = dataEntry.getProbability() / 100.0f; // Ensure probability is treated as a percentage
        float upside = dataEntry.getUpside();
        float likely = confirmed + (probability * upside);
        dataEntry.setLikely(likely);
    }

    private void calculateOffshorePM(DataEntry dataEntry) {
        if (dataEntry.getCategory().equals("Offshore PM")) {
            dataEntry.setOffshoreProjectManager(dataEntry.getValue() * dataEntry.getProbability() / 100);
        } else {
            dataEntry.setOffshoreProjectManager(0);
        }
    }

    private void calculateOnsitePM(DataEntry dataEntry) {
        if (dataEntry.getCategory().equals("Onsite PM")) {
            dataEntry.setOnsiteProjectManager(dataEntry.getValue() * dataEntry.getProbability() / 100);
        } else {
            dataEntry.setOnsiteProjectManager(0);
        }
    }

    private void calculateAnnuityRevenue(DataEntry dataEntry) {
        String category = dataEntry.getCategory();
        String annuityOrNonAnnuity = dataEntry.getAnnuityorNonAnnuity();
        if ((category.equals("Offshore Confirmed") || category.equals("Offshore Upside") ||
                category.equals("Onsite Confirmed") || category.equals("Onsite Upside")) &&
                annuityOrNonAnnuity.equals("Annuity")) {
            dataEntry.setAnnuityRevenue(dataEntry.getLikely());
        } else {
            dataEntry.setAnnuityRevenue(0);
        }
    }

    private void calculateNonAnnuityRevenue(DataEntry dataEntry) {
        String category = dataEntry.getCategory();
        String annuityOrNonAnnuity = dataEntry.getAnnuityorNonAnnuity();
        if ((category.equals("Offshore Confirmed") || category.equals("Offshore Upside") ||
                category.equals("Onsite Confirmed") || category.equals("Onsite Upside")) &&
                annuityOrNonAnnuity.equals("Annuity")) {
            dataEntry.setNonAnnuityRevenue(0);
        } else {
            dataEntry.setNonAnnuityRevenue(dataEntry.getLikely());
        }
    }

    private void calculateProjectsOrPursuitStage(DataEntry dataEntry) {
        // Concatenate Account and Project Name
        String concatenatedName = dataEntry.getAccount() + dataEntry.getProjectName();

        // Check if the concatenated name exists in PursuitTracker
        PursuitTracker pursuitTracker = pursuitTrackerRepository.findByConcatenatedName(concatenatedName);

        // If pursuitTracker is null, set Projects or Pursuit Stage to "Confirmed"
        if (pursuitTracker == null) {
            dataEntry.setProjectsOrPursuitStage("Confirmed");
        } else {
            // Set Projects or Pursuit Stage to the respective stage value
            dataEntry.setProjectsOrPursuitStage(pursuitTracker.getStage());
        }
    }

    private void calculateProbability(DataEntry dataEntry) {
        // Get the Projects or Pursuit Stage value
        String projectsOrPursuitStage = dataEntry.getProjectsOrPursuitStage();

        // If Projects or Pursuit Stage is "Confirmed", set Probability to 100%
        if (projectsOrPursuitStage.equals("Confirmed")) {
            dataEntry.setProbability(100);
        } else {
            // Concatenate Account and Project Name
            String concatenatedName = dataEntry.getAccount() + dataEntry.getProjectName();

            // Retrieve PursuitTracker by concatenated name
            PursuitTracker pursuitTracker = pursuitTrackerRepository.findByConcatenatedName(concatenatedName);

            // If PursuitTracker exists, set Probability to Pursuit Probability
            if (pursuitTracker != null) {
                dataEntry.setProbability(pursuitTracker.getPursuitProbability());
            } else {
                // If PursuitTracker does not exist, set Probability to 0
                dataEntry.setProbability(0);
            }
        }
    }

}


