package com.sonata.portfoliomanagement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sonata.portfoliomanagement.interfaces.AccountBudgetsRepository;
import com.sonata.portfoliomanagement.model.AccountBudgets;

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


}
