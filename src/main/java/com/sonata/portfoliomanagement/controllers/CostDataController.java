package com.sonata.portfoliomanagement.controllers;





import com.sonata.portfoliomanagement.interfaces.CostDataRepository;
import com.sonata.portfoliomanagement.model.CostData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/costdata")
public class CostDataController {

    private final CostDataRepository costDataRepository;

    @Autowired
    public CostDataController(CostDataRepository costDataRepository) {
        this.costDataRepository = costDataRepository;
    }

    @GetMapping("/getall")
    public ResponseEntity<List<CostData>> getAllCostData() {
        List<CostData> costDataList = costDataRepository.findAll();
        if (costDataList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(costDataList);
        }
    }
}