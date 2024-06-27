package com.sonata.portfoliomanagement.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sonata.portfoliomanagement.interfaces.MD_DeliveryManagerRepository;
import com.sonata.portfoliomanagement.model.MD_DeliveryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "http://localhost:5173" )
@RestController
@RequestMapping("/mddm")
public class MD_DeliveryManagerController {

    @Autowired
    MD_DeliveryManagerRepository dmRepo;

    @GetMapping("/getall")
    public ResponseEntity<List<MD_DeliveryManager>> getAllData() {
        List<MD_DeliveryManager> mdDm = dmRepo.findAll();
        return ResponseEntity.ok(mdDm);
    }


    // New GET method to retrieve unique deliveryManager names without repetition
    @GetMapping("/get")
    public ResponseEntity<?> getUniqueData() {
        List<MD_DeliveryManager> mdDeliveryManagers = dmRepo.findAll();
        Set<String> uniquedeliveryManagersSet = new HashSet<>();
        List<String> uniqueMdDeliveryManagers = new ArrayList<>();

        for (MD_DeliveryManager deliveryManager : mdDeliveryManagers) {
            if (uniquedeliveryManagersSet.add(deliveryManager.getDelivery_Managers())) {
                uniqueMdDeliveryManagers.add(deliveryManager.getDelivery_Managers());
            }
        }

        if (!uniqueMdDeliveryManagers.isEmpty()) {
            return ResponseEntity.ok(uniqueMdDeliveryManagers);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No unique deliveryManager found.");
        }
    }
    @PostMapping("/save")
    public ResponseEntity<MD_DeliveryManager> createMdDm(@RequestBody MD_DeliveryManager mdDms) {
        MD_DeliveryManager createdDms = dmRepo.save(mdDms);
        return new ResponseEntity<>(createdDms, HttpStatus.CREATED);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<MD_DeliveryManager> updateMdDm(@PathVariable int id, @RequestBody MD_DeliveryManager updatedMdDm) {
        // Check if PursuitAction with given id exists
        if (!dmRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // Set the id of updatedPursuitAction
        updatedMdDm.setId(id);

        // Save the updated PursuitAction
        MD_DeliveryManager updateMdDms = dmRepo.save(updatedMdDm);

        return ResponseEntity.ok(updateMdDms);
    }


}
