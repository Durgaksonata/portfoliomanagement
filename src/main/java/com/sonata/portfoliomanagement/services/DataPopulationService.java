package com.sonata.portfoliomanagement.services;


import com.sonata.portfoliomanagement.interfaces.DataEntryRepository;
import com.sonata.portfoliomanagement.interfaces.RevenueBudgetSummaryRepository;
import com.sonata.portfoliomanagement.interfaces.RevenueGrowthSummaryRepository;
import com.sonata.portfoliomanagement.model.DataEntry;
import com.sonata.portfoliomanagement.model.RevenueBudgetSummary;
import com.sonata.portfoliomanagement.model.RevenueGrowthSummary;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class DataPopulationService {

    @Autowired
    private DataEntryRepository dataEntryRepository;

    @Autowired
    private RevenueBudgetSummaryRepository revenueBudgetSummaryRepository;

    @Autowired
    private RevenueGrowthSummaryRepository revenueGrowthSummaryRepository;

    private static final Logger logger = LoggerFactory.getLogger(DataPopulationService.class);

    public String populateData() {
        List<DataEntry> dataEntryList = dataEntryRepository.findAll();
        logger.info("Found {} data entries to process.", dataEntryList.size());

        for (DataEntry dataEntry : dataEntryList) {
            // Check if data entry already exists in RevenueBudgetSummary table
            boolean existsInBudget = revenueBudgetSummaryRepository.existsByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarterAndBudgetAndMonth(
                    dataEntry.getVertical(),
                    dataEntry.getClassification(),
                    dataEntry.getDeliveryDirector(),
                    dataEntry.getDeliveryManager(),
                    dataEntry.getAccount(),
                    dataEntry.getProjectManager(),
                    dataEntry.getProjectName(),
                    dataEntry.getFinancialYear(),
                    dataEntry.getQuarter(),
                    dataEntry.getBudget(),
                    0.0001f,
                    dataEntry.getMonth()
            );

            // Check if data entry already exists in RevenueGrowthSummary table
            boolean existsInGrowth = revenueGrowthSummaryRepository.existsByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarterAndMonth(
                    dataEntry.getVertical(),
                    dataEntry.getClassification(),
                    dataEntry.getDeliveryDirector(),
                    dataEntry.getDeliveryManager(),
                    dataEntry.getAccount(),
                    dataEntry.getProjectManager(),
                    dataEntry.getProjectName(),
                    dataEntry.getFinancialYear(),
                    dataEntry.getQuarter(),
                    dataEntry.getMonth()
            );

            // Populate missing summaries
            if (!existsInBudget) {
                populateRevenueBudgetSummary(dataEntry);
            } else {
                logger.info("Data entry already exists in RevenueBudgetSummary, skipping: {}", dataEntry.getId());
            }

            if (!existsInGrowth) {
                populateRevenueGrowthSummary(dataEntry);
            } else {
                logger.info("Data entry already exists in RevenueGrowthSummary, skipping: {}", dataEntry.getId());
            }
        }

        return "Data populated successfully to RevenueBudgetSummary and RevenueGrowthSummary";
    }

    private void populateRevenueBudgetSummary(DataEntry dataEntry) {
        // Retrieve all budget data entries that match the given criteria
        List<DataEntry> budgetData = dataEntryRepository.findAllByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarterAndBudgetAndMonth(
                dataEntry.getVertical(),
                dataEntry.getClassification(),
                dataEntry.getDeliveryDirector(),
                dataEntry.getDeliveryManager(),
                dataEntry.getAccount(),
                dataEntry.getProjectManager(),
                dataEntry.getProjectName(),
                dataEntry.getFinancialYear(),
                dataEntry.getQuarter(),
                dataEntry.getBudget(),
                dataEntry.getMonth()
        );

        logger.info("Found {} budget data entries for DataEntry ID: {}", budgetData.size(), dataEntry.getId());

        budgetData.forEach(entry -> {
            logger.info("DataEntry ID: {}, Likely: {}", entry.getId(), entry.getLikely());
        });

        // Calculate forecast with the updated method
        float forecast = calculateForecast(budgetData, dataEntry.getAccount(), String.valueOf(dataEntry.getFinancialYear()), dataEntry.getQuarter());

        logger.info("Calculated forecast for DataEntry ID: {} is {}", dataEntry.getId(), forecast);

        // Calculate gap
        float gap = dataEntry.getBudget() - forecast;

        // Create RevenueBudgetSummary object with the correct parameters
        RevenueBudgetSummary revenueBudgetSummary = new RevenueBudgetSummary(
                dataEntry.getId(),
                dataEntry.getVertical(),
                dataEntry.getClassification(),
                dataEntry.getDeliveryDirector(),
                dataEntry.getDeliveryManager(),
                dataEntry.getAccount(),
                dataEntry.getProjectManager(),
                dataEntry.getProjectName(),
                dataEntry.getFinancialYear(),
                dataEntry.getQuarter(),
                dataEntry.getMonth(),
                dataEntry.getBudget(),
                forecast,
                gap
        );

        // Save the RevenueBudgetSummary object to the repository
        revenueBudgetSummaryRepository.save(revenueBudgetSummary);
        logger.info("Saved RevenueBudgetSummary for DataEntry ID: {}", dataEntry.getId());
    }

    private void populateRevenueGrowthSummary(DataEntry dataEntry) {
        // Populate RevenueGrowthSummary
        List<DataEntry> growthData = dataEntryRepository.findAllByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarterAndMonth(
                dataEntry.getVertical(),
                dataEntry.getClassification(),
                dataEntry.getDeliveryDirector(),
                dataEntry.getDeliveryManager(),
                dataEntry.getAccount(),
                dataEntry.getProjectManager(),
                dataEntry.getProjectName(),
                dataEntry.getFinancialYear(),
                dataEntry.getQuarter(),
                dataEntry.getMonth()
        );

        if (!isValidQuarter(dataEntry.getQuarter())) {
            throw new IllegalArgumentException("Invalid quarter: " + dataEntry.getQuarter());
        }

        String previousQuarterKey = getPreviousQuarter(dataEntry.getFinancialYear(), dataEntry.getQuarter());
        int previousFinancialYear = extractFinancialYear(previousQuarterKey);
        String previousQuarter = extractQuarter(previousQuarterKey);

        List<DataEntry> previousQuarterData = dataEntryRepository.findAllByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarter(
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

        float growthForecast = calculateForecast(growthData, dataEntry.getAccount(), String.valueOf(dataEntry.getFinancialYear()), dataEntry.getQuarter());

        float previousQuarterForecast = calculateForecast(previousQuarterData, dataEntry.getAccount(), String.valueOf(previousFinancialYear), previousQuarter);

        // Calculate account expected and gap
        float accountExpected = Math.max(dataEntry.getBudget(), 1.07f * previousQuarterForecast);

        // Calculate gap
        float gap = accountExpected - growthForecast;

        RevenueGrowthSummary revenueGrowthSummary = new RevenueGrowthSummary();
        // Set properties for revenueGrowthSummary
        revenueGrowthSummary.setVertical(dataEntry.getVertical());
        revenueGrowthSummary.setClassification(dataEntry.getClassification());
        revenueGrowthSummary.setDeliveryDirector(dataEntry.getDeliveryDirector());
        revenueGrowthSummary.setDeliveryManager(dataEntry.getDeliveryManager());
        revenueGrowthSummary.setAccount(dataEntry.getAccount());
        revenueGrowthSummary.setProjectManager(dataEntry.getProjectManager());
        revenueGrowthSummary.setProjectName(dataEntry.getProjectName());
        revenueGrowthSummary.setFinancialYear(dataEntry.getFinancialYear());
        revenueGrowthSummary.setQuarter(dataEntry.getQuarter());
        revenueGrowthSummary.setMonth(dataEntry.getMonth());
        revenueGrowthSummary.setAccountExpected(accountExpected);
        revenueGrowthSummary.setForecast(growthForecast);
        revenueGrowthSummary.setGap(gap);
        // Set other calculated fields

        revenueGrowthSummaryRepository.save(revenueGrowthSummary);
        logger.info("Saved new RevenueGrowthSummary for DataEntry ID: {}", dataEntry.getId());
    }

    private float calculateForecast(List<DataEntry> dataEntries, String account, String financialYear, String quarter) {
        double sum = dataEntries.stream()
                .filter(entry -> account.equals(entry.getAccount()))
                .filter(entry -> financialYear.equals(entry.getFinancialYear()))
                .filter(entry -> quarter.equals(entry.getQuarter()))
                .mapToDouble(DataEntry::getLikely)
                .sum();
        return (float) (sum / 1000000);
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
