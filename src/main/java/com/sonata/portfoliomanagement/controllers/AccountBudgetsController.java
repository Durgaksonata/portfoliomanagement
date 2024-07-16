package com.sonata.portfoliomanagement.controllers;

import com.sonata.portfoliomanagement.interfaces.AccountBudgetsRepository;
import com.sonata.portfoliomanagement.model.AccountBudgets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:5173" )
@RestController
@RequestMapping("/accbudgets")
public class AccountBudgetsController {

    @Autowired
    private AccountBudgetsRepository accountBudgetsRepository;


    // Get all AccountBudgets
    @GetMapping("/getall")
    public ResponseEntity<List<AccountBudgets>> getAllAccountBudgets() {
        List<AccountBudgets> accountBudgetsList = accountBudgetsRepository.findAll();
        return ResponseEntity.ok(accountBudgetsList);
    }

    // Create a new AccountBudgets
    @PostMapping("/save")
    public ResponseEntity<String> createAccountBudgets(@RequestBody AccountBudgets accountBudgets) {
        accountBudgetsRepository.save(accountBudgets);
        return ResponseEntity.ok("AccountBudgets saved successfully");
    }

    // Update multiple AccountBudgets
    @PutMapping("/update")
    public ResponseEntity<String> updateAccountBudgets(@RequestBody List<AccountBudgets> accountBudgetsList) {
        StringBuilder updateMessage = new StringBuilder("Updated fields: ");
        boolean notFound = false;

        for (AccountBudgets accountBudgetsDetails : accountBudgetsList) {
            Optional<AccountBudgets> accountBudgetsOptional = accountBudgetsRepository.findById(accountBudgetsDetails.getId());

            if (accountBudgetsOptional.isPresent()) {
                AccountBudgets accountBudgets = accountBudgetsOptional.get();
                if (!accountBudgets.getVertical().equals(accountBudgetsDetails.getVertical())) {
                    accountBudgets.setVertical(accountBudgetsDetails.getVertical());
                    updateMessage.append("vertical, ");
                }
                if (!accountBudgets.getClassification().equals(accountBudgetsDetails.getClassification())) {
                    accountBudgets.setClassification(accountBudgetsDetails.getClassification());
                    updateMessage.append("classification, ");
                }
                if (!accountBudgets.getDeliveryDirector().equals(accountBudgetsDetails.getDeliveryDirector())) {
                    accountBudgets.setDeliveryDirector(accountBudgetsDetails.getDeliveryDirector());
                    updateMessage.append("deliveryDirector, ");
                }
                if (!accountBudgets.getDeliveryManager().equals(accountBudgetsDetails.getDeliveryManager())) {
                    accountBudgets.setDeliveryManager(accountBudgetsDetails.getDeliveryManager());
                    updateMessage.append("deliveryManager, ");
                }
                if (!accountBudgets.getAccount().equals(accountBudgetsDetails.getAccount())) {
                    accountBudgets.setAccount(accountBudgetsDetails.getAccount());
                    updateMessage.append("account, ");
                }
                if (!accountBudgets.getProjectManager().equals(accountBudgetsDetails.getProjectManager())) {
                    accountBudgets.setProjectManager(accountBudgetsDetails.getProjectManager());
                    updateMessage.append("projectManager, ");
                }
                if (!accountBudgets.getProjectName().equals(accountBudgetsDetails.getProjectName())) {
                    accountBudgets.setProjectName(accountBudgetsDetails.getProjectName());
                    updateMessage.append("projectName, ");
                }
                if (accountBudgets.getFinancialYear() != accountBudgetsDetails.getFinancialYear()) {
                    accountBudgets.setFinancialYear(accountBudgetsDetails.getFinancialYear());
                    updateMessage.append("financialYear, ");
                }
                if (!accountBudgets.getQuarter().equals(accountBudgetsDetails.getQuarter())) {
                    accountBudgets.setQuarter(accountBudgetsDetails.getQuarter());
                    updateMessage.append("quarter, ");
                }
                if (accountBudgets.getBudget() != accountBudgetsDetails.getBudget()) {
                    accountBudgets.setBudget(accountBudgetsDetails.getBudget());
                    updateMessage.append("budget, ");
                }
                accountBudgetsRepository.save(accountBudgets);
            } else {
                notFound = true;
            }
        }
        if (notFound) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updateMessage.toString());
    }

    // Delete multiple AccountBudgets by IDs
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAccountBudgets(@RequestBody List<Integer> ids) {
        boolean notFound = false;

        for (int id : ids) {
            Optional<AccountBudgets> accountBudgetsOptional = accountBudgetsRepository.findById(id);
            if (accountBudgetsOptional.isPresent()) {
                accountBudgetsRepository.deleteById(id);
            } else {
                notFound = true;
            }
        }
        if (notFound) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("AccountBudgets deleted successfully");
    }


}
