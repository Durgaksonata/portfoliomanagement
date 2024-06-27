package com.sonata.portfoliomanagement.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.sonata.portfoliomanagement.interfaces.MD_AccountsRepository;
import com.sonata.portfoliomanagement.model.MD_Accounts;
import com.sonata.portfoliomanagement.model.MD_AccountsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "http://localhost:5173" )
@RestController
@RequestMapping("/mdaccount")
public class MD_AccountsController {

    @Autowired
    MD_AccountsRepository accountsRepo;

    @GetMapping("/getall")
    public ResponseEntity<List<MD_Accounts>> getAllAccounts() {
        List<MD_Accounts> accounts = accountsRepo.findAll();
        return ResponseEntity.ok(accounts);
    }

    // New GET method to retrieve unique account names without repetition
    @GetMapping("/get")
    public ResponseEntity<?> getUniqueData() {
        List<MD_Accounts> mdAccounts = accountsRepo.findAll();
        Set<String> uniqueAccountsSet = new HashSet<>();
        List<String> uniqueMdAccounts = new ArrayList<>();

        for (MD_Accounts account : mdAccounts) {
            if (uniqueAccountsSet.add(account.getAccounts())) {
                uniqueMdAccounts.add(account.getAccounts());
            }
        }

        if (!uniqueMdAccounts.isEmpty()) {
            return ResponseEntity.ok(uniqueMdAccounts);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No unique accounts found.");
        }
    }

    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> createAccount(@RequestBody MD_Accounts newAccount) {
        // Check for existing accounts with the same name
        List<MD_Accounts> existingAccounts = accountsRepo.findByAccounts(newAccount.getAccounts());

        if (!existingAccounts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Collections.singletonMap("message", "Account with name '" + newAccount.getAccounts() + "' already exists."));
        }

        // Save the new account
        MD_Accounts createdAccount = accountsRepo.save(newAccount);

        // Prepare response
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Account created successfully with name '" + createdAccount.getAccounts() + "'.");
        response.put("createdAccount", createdAccount);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateAccount(@RequestBody MD_Accounts updatedAccount) {
        Optional<MD_Accounts> accountOptional = accountsRepo.findById(updatedAccount.getId());

        if (!accountOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "Account with ID '" + updatedAccount.getId() + "' not found."));
        }

        MD_Accounts existingAccount = accountOptional.get();

        // Check for duplicates excluding the current record
        List<MD_Accounts> accountsWithSameName = accountsRepo.findByAccounts(updatedAccount.getAccounts());
        accountsWithSameName.removeIf(account -> account.getId().equals(updatedAccount.getId()));

        if (!accountsWithSameName.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Collections.singletonMap("message", "Account with name '" + updatedAccount.getAccounts() + "' already exists."));
        }

        StringBuilder updateMessage = new StringBuilder();
        boolean isUpdated = false;

        // Update fields if they differ
        if (!existingAccount.getAccounts().equals(updatedAccount.getAccounts())) {
            updateMessage.append("Account name updated from '")
                    .append(existingAccount.getAccounts())
                    .append("' to '")
                    .append(updatedAccount.getAccounts())
                    .append("'. ");
            existingAccount.setAccounts(updatedAccount.getAccounts());
            isUpdated = true;
        }

        if (!isUpdated) {
            return ResponseEntity.ok(Collections.singletonMap("message", "No changes detected."));
        }

        MD_Accounts updatedAccountEntity = accountsRepo.save(existingAccount);
        Map<String, Object> response = new HashMap<>();
        response.put("message", updateMessage.toString());
        response.put("updatedAccount", updatedAccountEntity);

        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAccountsByIds(@RequestBody List<Integer> ids) {
        List<Integer> notFoundIds = new ArrayList<>();
        List<String> deletedAccountNames = new ArrayList<>();

        for (Integer id : ids) {
            Optional<MD_Accounts> accountOptional = accountsRepo.findById(id);
            if (accountOptional.isEmpty()) {
                notFoundIds.add(id);
            } else {
                MD_Accounts account = accountOptional.get();
                deletedAccountNames.add(account.getAccounts());
                accountsRepo.deleteById(id);
            }
        }

        if (!notFoundIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No accounts found with IDs: " + notFoundIds.toString());
        }

        return ResponseEntity.ok("Accounts: " + deletedAccountNames + " deleted successfully.");
    }




}