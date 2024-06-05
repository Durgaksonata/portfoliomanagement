package com.sonata.portfoliomanagement.services;

import com.sonata.portfoliomanagement.interfaces.DataEntryRepository;
import com.sonata.portfoliomanagement.interfaces.PursuitTrackerRepository;
import com.sonata.portfoliomanagement.model.DataEntry;
import com.sonata.portfoliomanagement.model.DataEntryDTO;
import com.sonata.portfoliomanagement.model.PursuitTracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataEntryService {
    @Autowired
    private DataEntryRepository dataEntryRepo;

    @Autowired
    private PursuitTrackerRepository pursuitTrackerRepository;


    public DataEntry saveDataEntry(DataEntry dataEntry) {
        // Calculate fields
        calculateOffshoreCost(dataEntry);
        calculateOnsiteCost(dataEntry);
        calculateType(dataEntry);
        calculateTotalCost(dataEntry);
        calculateBillablePM(dataEntry);
        calculateConfirmed(dataEntry);
        calculateUpside(dataEntry);
        calculateLikely(dataEntry);
        calculateOffshorePM(dataEntry);
        calculateOnsitePM(dataEntry);
        calculateAnnuityRevenue(dataEntry);
        calculateNonAnnuityRevenue(dataEntry);
        calculateProjectsOrPursuitStage(dataEntry);
        calculateProbability(dataEntry);

        // Save the DataEntry entity
        return dataEntryRepo.save(dataEntry);
    }

    public boolean isDuplicateEntry(DataEntryDTO dataEntryDTO) {
        List<DataEntry> existingEntries = dataEntryRepo.findAllByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarterAndMonth(
                dataEntryDTO.getVertical(),
                dataEntryDTO.getClassification(),
                dataEntryDTO.getDeliveryDirector(),
                dataEntryDTO.getDeliveryManager(),
                dataEntryDTO.getAccount(),
                dataEntryDTO.getProjectManager(),
                dataEntryDTO.getProjectName(),
                dataEntryDTO.getFinancialYear(),
                dataEntryDTO.getQuarter(),
                dataEntryDTO.getMonth()
        );
        return !existingEntries.isEmpty();
    }

    public DataEntry createDataEntryFromDTO(DataEntryDTO dataEntryDTO) {
        DataEntry dataEntry = new DataEntry();
        dataEntry.setMonth(dataEntryDTO.getMonth());
        dataEntry.setVertical(dataEntryDTO.getVertical());
        dataEntry.setClassification(dataEntryDTO.getClassification());
        dataEntry.setDeliveryDirector(dataEntryDTO.getDeliveryDirector());
        dataEntry.setDeliveryManager(dataEntryDTO.getDeliveryManager());
        dataEntry.setAccount(dataEntryDTO.getAccount());
        dataEntry.setProjectManager(dataEntryDTO.getProjectManager());
        dataEntry.setProjectName(dataEntryDTO.getProjectName());
        dataEntry.setCategory(dataEntryDTO.getCategory());
        dataEntry.setAnnuityorNonAnnuity(dataEntryDTO.getAnnuityorNonAnnuity());
        dataEntry.setValue(dataEntryDTO.getValue());
        dataEntry.setFinancialYear(dataEntryDTO.getFinancialYear());
        dataEntry.setQuarter(dataEntryDTO.getQuarter());
        dataEntry.setBudget(dataEntryDTO.getBudget());
        return dataEntry;
    }

    // Calculation methods here (same as before)
    private void calculateOffshoreCost(DataEntry dataEntry) {
        if (dataEntry.getCategory().equals("Offshore Cost")) {
            dataEntry.setOffshoreCost(dataEntry.getValue() * dataEntry.getProbability());
        } else {
            dataEntry.setOffshoreCost(0);
        }

    }
    private void calculateOnsiteCost(DataEntry dataEntry) {
        if (dataEntry.getCategory().equals("Onsite Cost")) {
            dataEntry.setOnsiteCost(dataEntry.getValue() * dataEntry.getProbability());
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


    private void calculateTotalCost(DataEntry dataEntry) {{
            float offshoreCost = dataEntry.getOffshoreCost();
            float onsiteCost = dataEntry.getOnsiteCost();
            float totalCost = offshoreCost + onsiteCost;
            dataEntry.setTotalCost(totalCost);
        }
    }
    private void calculateBillablePM(DataEntry dataEntry) {
            if (dataEntry.getCategory().equals("Billable PM")) {
                dataEntry.setBillableProjectManager(dataEntry.getValue() * dataEntry.getProbability());
            } else {
                dataEntry.setBillableProjectManager(0);
            }
    }
    private void calculateConfirmed(DataEntry dataEntry) {
        String category = dataEntry.getCategory();
        if (category.equals("Offshore Confirmed")||category.equals("Onsite Confirmed")) {
            dataEntry.setConfirmed(dataEntry.getValue() * dataEntry.getProbability());
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


    public List<DataEntryDTO> getAllDataEntries() {
        List<DataEntry> dataEntries = dataEntryRepo.findAll();
        return dataEntries.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

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
}





