package com.sonata.portfoliomanagement.controllers;

import com.sonata.portfoliomanagement.interfaces.DataEntryRepository;
import com.sonata.portfoliomanagement.interfaces.RevenueBudgetSummaryRepository;
import com.sonata.portfoliomanagement.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sonata.portfoliomanagement.interfaces.AccountBudgetsRepository;

import java.util.List;

@RestController
@RequestMapping("/acbudgets")
public class AccountBudgetsController {
	
	@Autowired
	AccountBudgetsRepository acBudgetsRepo;
	
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
}