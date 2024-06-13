package com.sonata.portfoliomanagement.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.sonata.portfoliomanagement.interfaces.MD_DeliveryDirectorRepository;
import com.sonata.portfoliomanagement.model.MD_DeliveryDirector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mddd")
public class MD_DeliveryDirectorController {

    @Autowired
    private MD_DeliveryDirectorRepository deliveryDirectorRepository;

    @GetMapping("/get")
    public ResponseEntity<List<MD_DeliveryDirector>> getAllDeliveryDirectors() {
        List<MD_DeliveryDirector> deliveryDirectors = deliveryDirectorRepository.findAll();
        return ResponseEntity.ok(deliveryDirectors);
    }

    @PostMapping("/save")
    public ResponseEntity<List<MD_DeliveryDirector>> createDeliveryDirectors(@RequestBody List<MD_DeliveryDirector> deliveryDirectorsList) {
        List<MD_DeliveryDirector> createdDeliveryDirectors = new ArrayList<>();
        for (MD_DeliveryDirector deliveryDirector : deliveryDirectorsList) {
            MD_DeliveryDirector createdDeliveryDirector = deliveryDirectorRepository.save(deliveryDirector);
            createdDeliveryDirectors.add(createdDeliveryDirector);
        }
        return new ResponseEntity<>(createdDeliveryDirectors, HttpStatus.CREATED);
    }

    @PutMapping("/update/{ids}")
    public ResponseEntity<List<MD_DeliveryDirector>> updateDeliveryDirectors(@PathVariable List<Integer> ids, @RequestBody List<MD_DeliveryDirector> updatedDeliveryDirectors) {
        List<MD_DeliveryDirector> updatedEntities = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            int id = ids.get(i);
            MD_DeliveryDirector updatedDeliveryDirector = updatedDeliveryDirectors.get(i);
            Optional<MD_DeliveryDirector> existingDeliveryDirectorOptional = deliveryDirectorRepository.findById(id);
            if (existingDeliveryDirectorOptional.isPresent()) {
                updatedDeliveryDirector.setId(id);
                updatedEntities.add(deliveryDirectorRepository.save(updatedDeliveryDirector));
            }
        }
        return ResponseEntity.ok(updatedEntities);
    }

    @DeleteMapping("/delete/{ids}")
    public ResponseEntity<Void> deleteDeliveryDirectors(@PathVariable List<Integer> ids) {
        for (Integer id : ids) {
            if (deliveryDirectorRepository.existsById(id)) {
                deliveryDirectorRepository.deleteById(id);
            }
        }
        return ResponseEntity.noContent().build();
    }
}