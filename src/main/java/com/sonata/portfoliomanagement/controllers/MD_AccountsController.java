package com.sonata.portfoliomanagement.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    MD_AccountsRepository acntRepo;

    @GetMapping("/get")
    public ResponseEntity<List<MD_Accounts>> getAllData() {
        List<MD_Accounts> mdAcnts = acntRepo.findAll();
        return ResponseEntity.ok(mdAcnts);
    }
    @PostMapping("/save")
    public ResponseEntity<MD_Accounts> createMdAcnts(@RequestBody MD_Accounts mdAcnts) {
        MD_Accounts createdAcnts = acntRepo.save(mdAcnts);
        return new ResponseEntity<>(createdAcnts, HttpStatus.CREATED);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<MD_Accounts> updateMdAcnts(@PathVariable int id, @RequestBody MD_Accounts updatedMdAcnts) {
        // Check if PursuitAction with given id exists
        if (!acntRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // Set the id of updatedPursuitAction
        updatedMdAcnts.setId(id);

        // Save the updated PursuitAction
        MD_Accounts updateMdAcnts = acntRepo.save(updatedMdAcnts);

        return ResponseEntity.ok(updateMdAcnts);
    }

    @PostMapping("/accountbydm")
    public ResponseEntity<Set<String>> getAccountByDM(@RequestBody MD_AccountsDTO dmList) {
        List<String> dmNames = dmList.getDmList();
        Set<String> distinctAccounts = new HashSet<>(); // Use a Set to collect distinct account names
        List<MD_Accounts> acnt = acntRepo.findByDeliveryManagerIn(dmNames);

        for (MD_Accounts acnts : acnt) {
            // Add the account name to the Set
            distinctAccounts.add(acnts.getAccounts());
        }

        return ResponseEntity.ok(distinctAccounts);
    }

    @PostMapping("/dmbyacnt")
    public ResponseEntity<Set<String>> getDMByAccount(@RequestBody MD_AccountsDTO accountList) {
        List<String> accountNames = accountList.getAccountList();
        Set<String> distinctDMs = new HashSet<>(); // Use a Set to collect distinct account names

        List<MD_Accounts> dm = acntRepo.findByAccountsIn(accountNames);

        for (MD_Accounts dms : dm) {
            // Add the account name to the Set
            distinctDMs.add(dms.getDeliveryManager());
        }

        return ResponseEntity.ok(distinctDMs);
    }


}