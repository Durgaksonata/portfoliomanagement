package com.sonata.portfoliomanagement.controllers;


import com.sonata.portfoliomanagement.interfaces.*;
import com.sonata.portfoliomanagement.model.*;

import com.sonata.portfoliomanagement.services.BaseLineService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

@CrossOrigin(origins = "http://localhost:5173" )
@RestController
@RequestMapping("baseline")
public class BaselineController {

    private static final Logger logger = LoggerFactory.getLogger(BaselineController.class);


    @Autowired
    private RevenueBudgetSummaryRepository revenueBudgetSummaryRepository;

    @Autowired
    private RevenueGrowthSummaryRepository revenueGrowthSummaryRepository;

    @Autowired
    private PipelineStateRepository pipelineStateRepository;

    @Autowired
    private BaseLine_RevenueBudgetSummaryRepository baseLineRevenueBudgetSummaryRepository;

    @Autowired
    private BaseLine_RevenueGrowthSummaryRepository baseLineRevenueGrowthSummaryRepository;
    @Autowired
    private BaseLine_PipelineStateRepository baseLinePipelineStateRepository;

    @Autowired
    private BaseLineService baseLineService;




    @PostMapping("/deliveryManagersData")
    public ResponseEntity<Map<String, Object>> getDeliveryManagersData(@RequestBody List<String> deliveryManagerNames) {
        Map<String, Object> data = baseLineService.getDeliveryManagerData(deliveryManagerNames);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }


    @PostMapping("/accountData")
    public List<DeliveryManagerDataDTO> getAccountData(@RequestBody List<String> accountNames) {
        return baseLineService.getAccountData(accountNames);
    }
    @PostMapping("/deliveryDirectorData")
    public ResponseEntity<Map<String, Object>> getDeliveryDirectorsData(@RequestBody List<String> deliveryDirectorNames) {
        Map<String, Object> data = baseLineService.getDeliveryDirectorData(deliveryDirectorNames);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }



    @GetMapping("/baselineSummary")
    public ResponseEntity<BaselineData> getBaselineSummary() {
        BaselineData baselineData = new BaselineData();
        List<SummaryData> previousData = new ArrayList<>();
        List<SummaryData> currentData = new ArrayList<>();

        // Fetch baseline data from repositories
        List<BaseLine_RevenueBudgetSummary> baselineBudgetSummaries = baseLineRevenueBudgetSummaryRepository.findAll();
        List<BaseLine_RevenueGrowthSummary> baselineGrowthSummaries = baseLineRevenueGrowthSummaryRepository.findAll();
        List<BaseLine_PipelineState> baselinePipelineSummaries = baseLinePipelineStateRepository.findAll();

        // Fetch current data from repositories
        List<RevenueBudgetSummary> currentBudgetSummaries = revenueBudgetSummaryRepository.findAll();
        List<RevenueGrowthSummary> currentGrowthSummaries = revenueGrowthSummaryRepository.findAll();
        List<PipelineState> currentPipelineSummaries = pipelineStateRepository.findAll();

        // Populate deliveryDirector, deliveryManager, and account lists from baseline and current data
        Set<String> deliveryDirector = new HashSet<>();
        Set<String> deliveryManager = new HashSet<>();
        Set<String> account = new HashSet<>();

        baselineBudgetSummaries.forEach(budgetSummary -> {
            deliveryDirector.add(budgetSummary.getDeliveryDirector());
            deliveryManager.add(budgetSummary.getDeliveryManager());
            account.add(budgetSummary.getAccount());
        });

        currentBudgetSummaries.forEach(budgetSummary -> {
            deliveryDirector.add(budgetSummary.getDeliveryDirector());
            deliveryManager.add(budgetSummary.getDeliveryManager());
            account.add(budgetSummary.getAccount());
        });

        // Populate previousData from baseline summaries
        Map<String, SummaryData> previousDataMap = new HashMap<>();
        populateSummaryDataMapForBaseline(
                baselineBudgetSummaries,
                baselineGrowthSummaries,
                baselinePipelineSummaries,
                previousDataMap
        );

        previousData.addAll(previousDataMap.values());
        previousData.sort(this::compareSummaryData);

        // Populate currentData from current summaries
        Map<String, SummaryData> currentDataMap = new HashMap<>();
        populateSummaryDataMap(
                currentBudgetSummaries,
                currentGrowthSummaries,
                currentPipelineSummaries,
                currentDataMap
        );

        currentData.addAll(currentDataMap.values());
        currentData.sort(this::compareSummaryData);

        baselineData.setDeliveryDirector(new ArrayList<>(deliveryDirector));
        baselineData.setDeliveryManager(new ArrayList<>(deliveryManager));
        baselineData.setAccount(new ArrayList<>(account));
        baselineData.setPreviousData(previousData);
        baselineData.setCurrentData(currentData);

        return ResponseEntity.ok(baselineData);
    }

    private void populateSummaryDataMapForBaseline(
            List<BaseLine_RevenueBudgetSummary> budgetSummaries,
            List<BaseLine_RevenueGrowthSummary> growthSummaries,
            List<BaseLine_PipelineState> pipelineSummaries,
            Map<String, SummaryData> dataMap) {

        budgetSummaries.forEach(budgetSummary -> {
            String key = budgetSummary.getFinancialYear() + budgetSummary.getQuarter();
            SummaryData summaryData = dataMap.getOrDefault(key, new SummaryData(
                    budgetSummary.getFinancialYear(),
                    budgetSummary.getQuarter()
            ));
            summaryData.setRVB_Forecast(summaryData.getRVB_Forecast() + budgetSummary.getForecast());
            dataMap.put(key, summaryData);
        });

        growthSummaries.forEach(growthSummary -> {
            String key = growthSummary.getFinancialYear() + growthSummary.getQuarter();
            SummaryData summaryData = dataMap.getOrDefault(key, new SummaryData(
                    growthSummary.getFinancialYear(),
                    growthSummary.getQuarter()
            ));
            summaryData.setRVG_Forecast(summaryData.getRVG_Forecast() + growthSummary.getForecast());
            dataMap.put(key, summaryData);
        });

        pipelineSummaries.forEach(pipelineState -> {
            String key = pipelineState.getFinancialYear() + pipelineState.getQuarter();
            SummaryData summaryData = dataMap.getOrDefault(key, new SummaryData(
                    pipelineState.getFinancialYear(),
                    pipelineState.getQuarter()
            ));
            double pipelineSum = pipelineState.getSumOfPipeline_total();
            summaryData.setTotalPipelineSum((float) (summaryData.getTotalPipelineSum() + pipelineSum));
            dataMap.put(key, summaryData);
        });
    }

    private void populateSummaryDataMap(
            List<RevenueBudgetSummary> budgetSummaries,
            List<RevenueGrowthSummary> growthSummaries,
            List<PipelineState> pipelineSummaries,
            Map<String, SummaryData> dataMap) {

        budgetSummaries.forEach(budgetSummary -> {
            String key = budgetSummary.getFinancialYear() + budgetSummary.getQuarter();
            SummaryData summaryData = dataMap.getOrDefault(key, new SummaryData(
                    budgetSummary.getFinancialYear(),
                    budgetSummary.getQuarter()
            ));
            summaryData.setRVB_Forecast(summaryData.getRVB_Forecast() + budgetSummary.getForecast());
            dataMap.put(key, summaryData);
        });

        growthSummaries.forEach(growthSummary -> {
            String key = growthSummary.getFinancialYear() + growthSummary.getQuarter();
            SummaryData summaryData = dataMap.getOrDefault(key, new SummaryData(
                    growthSummary.getFinancialYear(),
                    growthSummary.getQuarter()
            ));
            summaryData.setRVG_Forecast(summaryData.getRVG_Forecast() + growthSummary.getForecast());
            dataMap.put(key, summaryData);
        });

        pipelineSummaries.forEach(pipelineState -> {
            String key = pipelineState.getFinancialYear() + pipelineState.getQuarter();
            SummaryData summaryData = dataMap.getOrDefault(key, new SummaryData(
                    pipelineState.getFinancialYear(),
                    pipelineState.getQuarter()
            ));
            double pipelineSum = pipelineState.getSumOfPipeline_total();
            summaryData.setTotalPipelineSum((float) (summaryData.getTotalPipelineSum() + pipelineSum));
            dataMap.put(key, summaryData);
        });
    }

    private int compareSummaryData(SummaryData s1, SummaryData s2) {
        int yearComparison = Integer.compare(s2.getFinancialYear(), s1.getFinancialYear());
        if (yearComparison != 0) {
            return yearComparison;
        }
        return compareQuarters(s1.getQuarter(), s2.getQuarter());
    }

    private int compareQuarters(String q1, String q2) {
        return Integer.compare(
                Integer.parseInt(q1.substring(1)), // Assuming quarters are in format "Q1", "Q2", etc.
                Integer.parseInt(q2.substring(1))
        );
    }



}