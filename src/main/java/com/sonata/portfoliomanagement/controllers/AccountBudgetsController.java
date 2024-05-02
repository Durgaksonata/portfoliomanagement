package com.sonata.portfoliomanagement.controllers;

import com.sonata.portfoliomanagement.interfaces.DataEntryRepository;
import com.sonata.portfoliomanagement.interfaces.RevenueBudgetSummaryRepository;
import com.sonata.portfoliomanagement.interfaces.RevenueGrowthSummaryRepository;
import com.sonata.portfoliomanagement.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sonata.portfoliomanagement.interfaces.AccountBudgetsRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/acbudgets")
public class AccountBudgetsController {
	
	@Autowired
	AccountBudgetsRepository acBudgetsRepo;
    @Autowired
    RevenueGrowthSummaryRepository revenueGrowthRepository;
	
    @PostMapping("/save")
    public ResponseEntity<AccountBudgets> createAcBudgets(@RequestBody AccountBudgets acBudgets) {
    	AccountBudgets createAcccountBudgets = acBudgetsRepo.save(acBudgets);
        return new ResponseEntity<>(createAcccountBudgets, HttpStatus.CREATED);
    }

    @GetMapping("/get")
    public ResponseEntity<List<AccountBudgets>> getAllAccountBudgets() {
        List<AccountBudgets> allAccountBudgets = acBudgetsRepo.findAll();
        return new ResponseEntity<>(allAccountBudgets, HttpStatus.OK);
    }

    @Autowired
    private AccountBudgetsRepository accountBudgetsRepository;

    @Autowired
    private RevenueBudgetSummaryRepository revenueBudgetSummaryRepository;

    @Autowired
    private DataEntryRepository dataEntryRepository;

    //method to populate revenuebudgetsummary table from acbudgets-->
//    @PostMapping("/abcd")
//    public String populateRevenueBudgetSummaryFromAccountBudgets() {
//        try {
//            // Retrieve all AccountBudgets data
//            List<AccountBudgets> accountBudgetsList = accountBudgetsRepository.findAll();
//
//            for (AccountBudgets accountBudgets : accountBudgetsList) {
//                RevenueBudgetSummary revenueBudgetSummary = new RevenueBudgetSummary();
//                revenueBudgetSummary.setVertical(accountBudgets.getVertical());
//                revenueBudgetSummary.setClassification(accountBudgets.getClassification());
//                revenueBudgetSummary.setDeliveryManager(accountBudgets.getDeliveryManager());
//                revenueBudgetSummary.setAccount(accountBudgets.getAccount());
//                revenueBudgetSummary.setProjectManager(accountBudgets.getProjectManager());
//                revenueBudgetSummary.setProjectName(accountBudgets.getProjectName());
//                revenueBudgetSummary.setFinancialYear(accountBudgets.getFinancialYear());
//                revenueBudgetSummary.setQuarter(accountBudgets.getQuarter());
//                revenueBudgetSummary.setMonth(accountBudgets.getMonth());
//                revenueBudgetSummary.setBudget(accountBudgets.getBudget()); // Populate the budget field
//
//                revenueBudgetSummaryRepository.save(revenueBudgetSummary); // Save the populated RevenueBudgetSummary object
//            }
//
//            return "Data populated successfully in RevenueBudgetSummary";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Failed to populate data in RevenueBudgetSummary";
//        }
//    }

    @PostMapping("/populate")
    private String fetchData() {
        List<AccountBudgets> accountBudgetsList = accountBudgetsRepository.findAll();
        for (AccountBudgets accountBudgets : accountBudgetsList) {
            // Get or create RevenueBudgetSummary
            RevenueBudgetSummary revenue = getRevenueBudgetSummary(accountBudgets);

            // Check if a duplicate entry exists
            RevenueBudgetSummary existingRevenue = revenueBudgetSummaryRepository.findByVerticalAndClassificationAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarter(
                    accountBudgets.getVertical(),
                    accountBudgets.getClassification(),
                    accountBudgets.getDeliveryManager(),
                    accountBudgets.getAccount(),
                    accountBudgets.getProjectManager(),
                    accountBudgets.getProjectName(),
                    accountBudgets.getFinancialYear(),
                    accountBudgets.getQuarter()
            );

            if (existingRevenue != null) {

                continue;
            }

            // Fetch data from the repository based on the provided criteria
            List<DataEntry> data = dataEntryRepository.findAllByVerticalAndClassificationAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarter(
                    accountBudgets.getVertical(),
                    accountBudgets.getClassification(),
                    accountBudgets.getDeliveryManager(),
                    accountBudgets.getAccount(),
                    accountBudgets.getProjectManager(),
                    accountBudgets.getProjectName(),
                    accountBudgets.getFinancialYear(),
                    accountBudgets.getQuarter()
            );

            // Calculate total forecast
            float total = (float) data.stream().mapToDouble(DataEntry::getLikely).sum() / 1000000;
            revenue.setForecast(total);

            // Calculate gap
            float temp = accountBudgets.getBudget() - revenue.getForecast();
            revenue.setGap(temp);

            // Save RevenueBudgetSummary
            revenueBudgetSummaryRepository.save(revenue);
        }

        return "Data populated successfully to RevenueBudgetSummary";
    }


    private static RevenueBudgetSummary getRevenueBudgetSummary(AccountBudgets accountBudgets) {
        RevenueBudgetSummary revenueBudgetSummary = new RevenueBudgetSummary();
        revenueBudgetSummary.setVertical(accountBudgets.getVertical());
        revenueBudgetSummary.setClassification(accountBudgets.getClassification());
        revenueBudgetSummary.setDeliveryManager(accountBudgets.getDeliveryManager());
        revenueBudgetSummary.setAccount(accountBudgets.getAccount());
        revenueBudgetSummary.setProjectManager(accountBudgets.getProjectManager());
        revenueBudgetSummary.setProjectName(accountBudgets.getProjectName());
        revenueBudgetSummary.setFinancialYear(accountBudgets.getFinancialYear());
        revenueBudgetSummary.setQuarter(accountBudgets.getQuarter());
        revenueBudgetSummary.setMonth(accountBudgets.getMonth());
        revenueBudgetSummary.setBudget(accountBudgets.getBudget()); // Populate the budget field
        return revenueBudgetSummary;
    }


    //method to populate revenueGrowthSummary table-->
    @PostMapping("/populateGrowth")
    private String fetchDataforRevenueGrowth() {
        List<AccountBudgets> accountBudgetsList = accountBudgetsRepository.findAll();

        for (AccountBudgets accountBudgets : accountBudgetsList) {

            // Check if a duplicate entry exists-->
            RevenueGrowthSummary existingRevenue = revenueGrowthRepository.findByVerticalAndClassificationAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarter(
                    accountBudgets.getVertical(),
                    accountBudgets.getClassification(),
                    accountBudgets.getDeliveryManager(),
                    accountBudgets.getAccount(),
                    accountBudgets.getProjectManager(),
                    accountBudgets.getProjectName(),
                    accountBudgets.getFinancialYear(),
                    accountBudgets.getQuarter()
            );

            //checking for duplicate values-->
            if (existingRevenue != null) {

                continue;
            }
            // Fetch data from the repository based on the provided criteria
            List<DataEntry> data = dataEntryRepository.findAllByVerticalAndClassificationAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarter(
                    accountBudgets.getVertical(),
                    accountBudgets.getClassification(),
                    accountBudgets.getDeliveryManager(),
                    accountBudgets.getAccount(),
                    accountBudgets.getProjectManager(),
                    accountBudgets.getProjectName(),
                    accountBudgets.getFinancialYear(),
                    accountBudgets.getQuarter()
            );


            // Get previous quarter data
            String previousQuarter = getPreviousQuarter(accountBudgets.getQuarter());
            List<DataEntry> previousQuarterData = dataEntryRepository.findAllByVerticalAndClassificationAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarter(
                    accountBudgets.getVertical(),
                    accountBudgets.getClassification(),
                    accountBudgets.getDeliveryManager(),
                    accountBudgets.getAccount(),
                    accountBudgets.getProjectManager(),
                    accountBudgets.getProjectName(),
                    accountBudgets.getFinancialYear(),
                    previousQuarter
            );

            // Get or create RevenueGrowthSummary and calculate forecast, accountExpected, gap-->
            RevenueGrowthSummary revenue = getRevenueGrowthSummary(accountBudgets, data, previousQuarterData);

            revenueGrowthRepository.save(revenue);
        }

        return "Data populated successfully to RevenueGrowthSummary";
    }

    private static RevenueGrowthSummary getRevenueGrowthSummary(AccountBudgets accountBudgets, List<DataEntry> data, List<DataEntry> previousQuarterData) {
        RevenueGrowthSummary revenueGrowthSummary = new RevenueGrowthSummary();
        // Set properties of revenueGrowthSummary based on accountBudgets
        revenueGrowthSummary.setVertical(accountBudgets.getVertical());
        revenueGrowthSummary.setClassification(accountBudgets.getClassification());
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
        if (previousQuarterForecast > 0) {
            float accountExpected = Math.max(accountBudgets.getBudget(), 1.07f * previousQuarterForecast);
            revenueGrowthSummary.setAccountExpected(accountExpected);
        }

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
}