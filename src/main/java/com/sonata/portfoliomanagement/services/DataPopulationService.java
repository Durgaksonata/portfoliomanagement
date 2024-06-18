package com.sonata.portfoliomanagement.services;

import com.sonata.portfoliomanagement.interfaces.DataEntryRepository;
import com.sonata.portfoliomanagement.interfaces.RevenueBudgetSummaryRepository;
import com.sonata.portfoliomanagement.interfaces.RevenueGrowthSummaryRepository;
import com.sonata.portfoliomanagement.model.DataEntry;
import com.sonata.portfoliomanagement.model.RevenueBudgetSummary;
import com.sonata.portfoliomanagement.model.RevenueGrowthSummary;
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
            // Check if data entry already exists in both tables
            boolean existsInBudget = revenueBudgetSummaryRepository.existsByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarterAndMonth(
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

            if (!existsInBudget && !existsInGrowth) {
                // Data entry does not exist in either table, populate both summaries
                populateRevenueBudgetSummary(dataEntry);
                populateRevenueGrowthSummary(dataEntry);
            } else {
                logger.info("Data entry already exists in both tables, skipping: {}", dataEntry.getId());
            }
        }

        return "Data populated successfully to RevenueBudgetSummary and RevenueGrowthSummary";
    }

    private void populateRevenueBudgetSummary(DataEntry dataEntry) {
        // Populate RevenueBudgetSummary
        List<DataEntry> budgetData = dataEntryRepository.findAllByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarterAndMonth(
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

        float budgetForecast = (float) budgetData.stream().mapToDouble(DataEntry::getLikely).sum() / 1000000;
        RevenueBudgetSummary revenueBudgetSummary = getRevenueBudgetSummary(dataEntry, budgetForecast);

        revenueBudgetSummaryRepository.save(revenueBudgetSummary);
        logger.info("Saved new RevenueBudgetSummary for DataEntry ID: {}", dataEntry.getId());
    }

    private static RevenueBudgetSummary getRevenueBudgetSummary(DataEntry dataEntry, float budgetForecast) {
        float budgetGap = dataEntry.getBudget() - budgetForecast;

        RevenueBudgetSummary revenueBudgetSummary = new RevenueBudgetSummary();
        // Set properties for revenueBudgetSummary
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
        revenueBudgetSummary.setForecast(budgetForecast);
        revenueBudgetSummary.setGap(budgetGap);
        return revenueBudgetSummary;
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

        float growthForecast = (float) growthData.stream().mapToDouble(DataEntry::getLikely).sum() / 1000000;

        float previousQuarterForecast = calculatePreviousQuarterForecast(previousQuarterData);


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