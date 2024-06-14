package com.sonata.portfoliomanagement.services;

import ch.qos.logback.core.net.SyslogOutputStream;
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

    public void populateData() {
        List<DataEntry> dataEntries = dataEntryRepository.findAll();
        logger.info("Found {} data entries to process.", dataEntries.size());

        for (DataEntry dataEntry : dataEntries) {
            boolean existsInBudget = revenueBudgetSummaryRepository.existsByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarterAndBudget(
                    dataEntry.getVertical(), dataEntry.getClassification(), dataEntry.getDeliveryDirector(), dataEntry.getDeliveryManager(), dataEntry.getAccount(), dataEntry.getProjectManager(), dataEntry.getProjectName(), dataEntry.getFinancialYear(), dataEntry.getQuarter(), dataEntry.getBudget());

            boolean existsInGrowth = revenueGrowthSummaryRepository.existsByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarter(
                    dataEntry.getVertical(), dataEntry.getClassification(), dataEntry.getDeliveryDirector(), dataEntry.getDeliveryManager(), dataEntry.getAccount(), dataEntry.getProjectManager(), dataEntry.getProjectName(), dataEntry.getFinancialYear(), dataEntry.getQuarter());

            if (!existsInBudget) {
                RevenueBudgetSummary revenueBudgetSummary = createRevenueBudgetSummary(dataEntry);
                revenueBudgetSummaryRepository.save(revenueBudgetSummary);
                logger.info("Saved new RevenueBudgetSummary for DataEntry ID: {}", dataEntry.getId());
            }
            if (!existsInGrowth) {
                RevenueGrowthSummary revenueGrowthSummary = createRevenueGrowthSummary(dataEntry);
                revenueGrowthSummaryRepository.save(revenueGrowthSummary);
                logger.info("Saved new RevenueGrowthSummary for DataEntry ID: {}", dataEntry.getId());
            }
        }
    }



    private RevenueBudgetSummary createRevenueBudgetSummary(DataEntry dataEntry) {

        List<DataEntry> data = dataEntryRepository.findAllByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarterAndBudget(
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

        float budgetforecast = (float) data.stream().mapToDouble(DataEntry::getLikely).sum() / 1000000;
        logger.info("Budget Forecast for DataEntry ID {}: {}", dataEntry.getId(), budgetforecast);
        float gap = dataEntry.getBudget() - budgetforecast;


        return new RevenueBudgetSummary(
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
                budgetforecast, // forecast
                gap    // gap
        );
    }



    private RevenueGrowthSummary createRevenueGrowthSummary(DataEntry dataEntry) {
        List<DataEntry> data = dataEntryRepository.findAllByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarter(
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

        float forecast = (float) data.stream().mapToDouble(DataEntry::getLikely).sum() / 1000000;


        // Calculate previous quarter data
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

        float previousQuarterForecast = calculatePreviousQuarterForecast(previousQuarterData);

        // Calculate account expected and gap
        float accountExpected = Math.max(dataEntry.getBudget(), 1.07f * previousQuarterForecast);

        // Calculate gap
        float gap = accountExpected - forecast;




        return new RevenueGrowthSummary(
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
                accountExpected,
                forecast,
                gap
        );
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

    private void calculateLikely(DataEntry dataEntry) {
        float confirmed = dataEntry.getConfirmed();
        float probability = dataEntry.getProbability() / 100.0f; // Ensure probability is treated as a percentage
        float upside = dataEntry.getUpside();
        float likely = confirmed + (probability * upside);
        dataEntry.setLikely(likely);
    }



}
