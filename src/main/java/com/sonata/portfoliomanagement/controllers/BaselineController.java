package com.sonata.portfoliomanagement.controllers;


import com.sonata.portfoliomanagement.interfaces.*;
import com.sonata.portfoliomanagement.model.*;

import com.sonata.portfoliomanagement.services.BaseLineService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
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
    private final BaseLineService baseLineService;
@Autowired
    private MD_RolesRepository md_RolesRepository;

    @Autowired
    public BaselineController(BaseLineService baseLineService) {
        this.baseLineService = baseLineService;
    }


    @PostMapping("/deliveryManagersData")
    public ResponseEntity<Map<String, Object>> getDeliveryManagersData(@RequestBody List<String> deliveryManagerNames) {
        Map<String, Object> data = baseLineService.getDeliveryManagerData(deliveryManagerNames);
        return new ResponseEntity<>(data, HttpStatus.OK);
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

        // Find the highest financial year and the previous year
        Set<Integer> allYears = new HashSet<>();
        baselineBudgetSummaries.forEach(budgetSummary -> allYears.add(budgetSummary.getFinancialYear()));
        currentBudgetSummaries.forEach(budgetSummary -> allYears.add(budgetSummary.getFinancialYear()));

        List<Integer> sortedYears = allYears.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        int highestYear = sortedYears.get(0);
        int previousYear = sortedYears.size() > 1 ? sortedYears.get(1) : highestYear - 1;

        // Populate deliveryDirector, deliveryManager, and account lists from baseline and current data
        Set<String> deliveryDirector = new HashSet<>();
        Set<String> deliveryManager = new HashSet<>();
        Set<String> account = new HashSet<>();

        baselineBudgetSummaries.forEach(budgetSummary -> {
            if (budgetSummary.getFinancialYear() == highestYear || budgetSummary.getFinancialYear() == previousYear) {
                deliveryDirector.add(budgetSummary.getDeliveryDirector());
                deliveryManager.add(budgetSummary.getDeliveryManager());
                account.add(budgetSummary.getAccount());
            }
        });

        currentBudgetSummaries.forEach(budgetSummary -> {
            if (budgetSummary.getFinancialYear() == highestYear || budgetSummary.getFinancialYear() == previousYear) {
                deliveryDirector.add(budgetSummary.getDeliveryDirector());
                deliveryManager.add(budgetSummary.getDeliveryManager());
                account.add(budgetSummary.getAccount());
            }
        });

        // Populate previousData from baseline summaries
        Map<String, SummaryData> previousDataMap = new HashMap<>();
        populateSummaryDataMapForBaseline(
                baselineBudgetSummaries,
                baselineGrowthSummaries,
                baselinePipelineSummaries,
                previousDataMap,
                highestYear,
                previousYear
        );

        previousData.addAll(previousDataMap.values());
        previousData.sort(this::compareSummaryData);

        // Populate currentData from current summaries
        Map<String, SummaryData> currentDataMap = new HashMap<>();
        populateSummaryDataMap(
                currentBudgetSummaries,
                currentGrowthSummaries,
                currentPipelineSummaries,
                currentDataMap,
                highestYear,
                previousYear
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
            Map<String, SummaryData> dataMap,
            int highestYear,
            int previousYear) {

        budgetSummaries.forEach(budgetSummary -> {
            if (budgetSummary.getFinancialYear() == highestYear || budgetSummary.getFinancialYear() == previousYear) {
                String key = budgetSummary.getFinancialYear() + budgetSummary.getQuarter();
                SummaryData summaryData = dataMap.getOrDefault(key, new SummaryData(
                        budgetSummary.getFinancialYear(),
                        budgetSummary.getQuarter()
                ));
                summaryData.setRVB_Forecast(summaryData.getRVB_Forecast() + budgetSummary.getForecast());
                dataMap.put(key, summaryData);
            }
        });

        growthSummaries.forEach(growthSummary -> {
            if (growthSummary.getFinancialYear() == highestYear || growthSummary.getFinancialYear() == previousYear) {
                String key = growthSummary.getFinancialYear() + growthSummary.getQuarter();
                SummaryData summaryData = dataMap.getOrDefault(key, new SummaryData(
                        growthSummary.getFinancialYear(),
                        growthSummary.getQuarter()
                ));
                summaryData.setRVG_Forecast(summaryData.getRVG_Forecast() + growthSummary.getForecast());
                dataMap.put(key, summaryData);
            }
        });

        pipelineSummaries.forEach(pipelineState -> {
            if (pipelineState.getFinancialYear() == highestYear || pipelineState.getFinancialYear() == previousYear) {
                String key = pipelineState.getFinancialYear() + pipelineState.getQuarter();
                SummaryData summaryData = dataMap.getOrDefault(key, new SummaryData(
                        pipelineState.getFinancialYear(),
                        pipelineState.getQuarter()
                ));
                double pipelineSum = pipelineState.getSumOfPipeline_total();
                summaryData.setTotalPipelineSum((float) (summaryData.getTotalPipelineSum() + pipelineSum));
                dataMap.put(key, summaryData);
            }
        });
    }

    private void populateSummaryDataMap(
            List<RevenueBudgetSummary> budgetSummaries,
            List<RevenueGrowthSummary> growthSummaries,
            List<PipelineState> pipelineSummaries,
            Map<String, SummaryData> dataMap,
            int highestYear,
            int previousYear) {

        budgetSummaries.forEach(budgetSummary -> {
            if (budgetSummary.getFinancialYear() == highestYear || budgetSummary.getFinancialYear() == previousYear) {
                String key = budgetSummary.getFinancialYear() + budgetSummary.getQuarter();
                SummaryData summaryData = dataMap.getOrDefault(key, new SummaryData(
                        budgetSummary.getFinancialYear(),
                        budgetSummary.getQuarter()
                ));
                summaryData.setRVB_Forecast(summaryData.getRVB_Forecast() + budgetSummary.getForecast());
                dataMap.put(key, summaryData);
            }
        });

        growthSummaries.forEach(growthSummary -> {
            if (growthSummary.getFinancialYear() == highestYear || growthSummary.getFinancialYear() == previousYear) {
                String key = growthSummary.getFinancialYear() + growthSummary.getQuarter();
                SummaryData summaryData = dataMap.getOrDefault(key, new SummaryData(
                        growthSummary.getFinancialYear(),
                        growthSummary.getQuarter()
                ));
                summaryData.setRVG_Forecast(summaryData.getRVG_Forecast() + growthSummary.getForecast());
                dataMap.put(key, summaryData);
            }
        });

        pipelineSummaries.forEach(pipelineState -> {
            if (pipelineState.getFinancialYear() == highestYear || pipelineState.getFinancialYear() == previousYear) {
                String key = pipelineState.getFinancialYear() + pipelineState.getQuarter();
                SummaryData summaryData = dataMap.getOrDefault(key, new SummaryData(
                        pipelineState.getFinancialYear(),
                        pipelineState.getQuarter()
                ));
                double pipelineSum = pipelineState.getSumOfPipeline_total();
                summaryData.setTotalPipelineSum((float) (summaryData.getTotalPipelineSum() + pipelineSum));
                dataMap.put(key, summaryData);
            }
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


    @PostMapping("/accountData")
    public Map<String, Object> getAccountData(@RequestBody List<String> accountNames) {
        return baseLineService.getAccountData(accountNames);
    }





//
//    @PostMapping("/baselineByRoleAndName")
//    public ResponseEntity<Map<String, Object>> getBaselineByRoleAndName(@RequestBody RoleAndNameRequest request) {
//        List<String> roles = request.getRole();
//        String name = request.getName();
//
//        // Split name into first and last names
//        String[] nameParts = name.split(" ");
//        if (nameParts.length != 2) {
//            return ResponseEntity.badRequest().build();
//        }
//        String firstName = nameParts[0];
//        String lastName = nameParts[1];
//        String deliveryDirector = firstName + " " + lastName;
//
//        // Construct response object
//        Map<String, Object> response = new HashMap<>();
//        response.put("deliveryDirector", Collections.singletonList(deliveryDirector));
//
//        // Fetch baseline delivery managers, account names, and financial years
//        Set<String> deliveryManagers = new HashSet<>();
//        Set<String> accountNames = new HashSet<>();
//        Set<Integer> financialYears = new TreeSet<>(Collections.reverseOrder()); // Sorted in descending order
//
//        // Aggregate data
//        Map<String, Double> totalPipelineSums = new LinkedHashMap<>();
//        Map<String, Double> rvbForecasts = new LinkedHashMap<>();
//        Map<String, Double> rvgForecasts = new LinkedHashMap<>();
//
//        // Process each role separately
//        for (String role : roles) {
//            // Fetch matching role
//            MD_Role mdRole = md_RolesRepository.findFirstByRole(role);
//            if (mdRole == null) {
//                return ResponseEntity.badRequest().build();
//            }
//
//            // Fetch matching records from BaseLine_RevenueBudgetSummary
//            List<BaseLine_RevenueBudgetSummary> baselineBudgetSummaries = baseLineRevenueBudgetSummaryRepository.findByDeliveryDirector(deliveryDirector);
//            List<BaseLine_RevenueGrowthSummary> baselineGrowthSummaries = baseLineRevenueGrowthSummaryRepository.findByDeliveryDirector(deliveryDirector);
//            List<BaseLine_PipelineState> baselinePipelineStates = baseLinePipelineStateRepository.findByDeliveryDirector(deliveryDirector);
//
//            // Collect delivery managers, account names, and financial years
//            deliveryManagers.addAll(baselineBudgetSummaries.stream()
//                    .map(BaseLine_RevenueBudgetSummary::getDeliveryManager)
//                    .collect(Collectors.toSet()));
//
//            accountNames.addAll(baselineBudgetSummaries.stream()
//                    .map(BaseLine_RevenueBudgetSummary::getAccount)
//                    .collect(Collectors.toSet()));
//
//            financialYears.addAll(baselineBudgetSummaries.stream()
//                    .map(BaseLine_RevenueBudgetSummary::getFinancialYear)
//                    .collect(Collectors.toSet()));
//
//            // Aggregate data by financial year and quarter
//            for (BaseLine_RevenueBudgetSummary budgetSummary : baselineBudgetSummaries) {
//                String key = budgetSummary.getFinancialYear() + "-" + budgetSummary.getQuarter();
//                rvbForecasts.putIfAbsent(key, 0.0);
//                rvbForecasts.put(key, rvbForecasts.get(key) + budgetSummary.getForecast());
//            }
//
//            for (BaseLine_RevenueGrowthSummary growthSummary : baselineGrowthSummaries) {
//                String key = growthSummary.getFinancialYear() + "-" + growthSummary.getQuarter();
//                rvgForecasts.putIfAbsent(key, 0.0);
//                rvgForecasts.put(key, rvgForecasts.get(key) + growthSummary.getForecast());
//            }
//
//            for (BaseLine_PipelineState pipelineState : baselinePipelineStates) {
//                String key = pipelineState.getFinancialYear() + "-" + pipelineState.getQuarter();
//                totalPipelineSums.putIfAbsent(key, 0.0);
//                totalPipelineSums.put(key, totalPipelineSums.get(key) + pipelineState.getSumOfPipeline_total());
//            }
//        }
//
//        // Restrict to the latest two financial years
//        List<Integer> latestTwoFinancialYears = financialYears.stream()
//                .limit(2)
//                .collect(Collectors.toList());
//
//        // Prepare previous data list
//        List<Map<String, Object>> previousData = new ArrayList<>();
//        for (Integer year : latestTwoFinancialYears) {
//            for (String quarter : Arrays.asList("Q1", "Q2", "Q3", "Q4")) {
//                String key = year + "-" + quarter;
//                Map<String, Object> quarterData = new HashMap<>();
//                quarterData.put("quarter", quarter);
//                quarterData.put("financialYear", year);
//                quarterData.put("totalPipelineSum", totalPipelineSums.getOrDefault(key, 0.0));
//                quarterData.put("rvb_Forecast", rvbForecasts.getOrDefault(key, 0.0));
//                quarterData.put("rvg_Forecast", rvgForecasts.getOrDefault(key, 0.0));
//                previousData.add(quarterData);
//            }
//        }
//
//        // Set final response fields
//        response.put("deliveryManager", new ArrayList<>(deliveryManagers));
//        response.put("account", new ArrayList<>(accountNames));
//        response.put("previous", previousData);
//
//        // Return the response
//        return ResponseEntity.ok(response);
//    }


    @PostMapping("/combinedByRoleAndName")
    public ResponseEntity<Map<String, Object>> getCombinedByRoleAndName(@RequestBody RoleAndNameRequest request) {
        List<String> roles = request.getRole();
        String name = request.getName();

        // Split name into first and last names
        String[] nameParts = name.split(" ");
        if (nameParts.length != 2) {
            return ResponseEntity.badRequest().build();
        }
        String firstName = nameParts[0];
        String lastName = nameParts[1];
        String fullName = firstName + " " + lastName;

        // Initialize response object
        Map<String, Object> response = new HashMap<>();
        Set<String> deliveryManagers = new HashSet<>();
        Set<String> deliveryDirectors = new HashSet<>();
        Set<String> accountNames = new HashSet<>();
        Set<Integer> financialYears = new TreeSet<>(Collections.reverseOrder()); // Sorted in descending order

        // Aggregated data maps for current and previous data
        Map<String, Double> currentPipelineSums = new LinkedHashMap<>();
        Map<String, Double> currentRvbForecasts = new LinkedHashMap<>();
        Map<String, Double> currentRvgForecasts = new LinkedHashMap<>();

        Map<String, Double> previousPipelineSums = new LinkedHashMap<>();
        Map<String, Double> previousRvbForecasts = new LinkedHashMap<>();
        Map<String, Double> previousRvgForecasts = new LinkedHashMap<>();

        // Process each role
        for (String role : roles) {
            // Fetch matching role
            MD_Role mdRole = md_RolesRepository.findFirstByRole(role);
            if (mdRole == null) {
                return ResponseEntity.badRequest().build();
            }

            if (role.equals("Delivery Director")) {
                // Fetch current and previous data for Delivery Director
                List<RevenueBudgetSummary> currentBudgetSummaries = revenueBudgetSummaryRepository.findByDeliveryDirector(fullName);
                List<RevenueGrowthSummary> currentGrowthSummaries = revenueGrowthSummaryRepository.findByDeliveryDirector(fullName);
                List<PipelineState> currentPipelineStates = pipelineStateRepository.findByDeliveryDirector(fullName);

                List<BaseLine_RevenueBudgetSummary> previousBudgetSummaries = baseLineRevenueBudgetSummaryRepository.findByDeliveryDirector(fullName);
                List<BaseLine_RevenueGrowthSummary> previousGrowthSummaries = baseLineRevenueGrowthSummaryRepository.findByDeliveryDirector(fullName);
                List<BaseLine_PipelineState> previousPipelineStates = baseLinePipelineStateRepository.findByDeliveryDirector(fullName);

                // Collect delivery managers, account names, and financial years
                deliveryManagers.addAll(currentBudgetSummaries.stream()
                        .map(RevenueBudgetSummary::getDeliveryManager)
                        .collect(Collectors.toSet()));
                deliveryManagers.addAll(previousBudgetSummaries.stream()
                        .map(BaseLine_RevenueBudgetSummary::getDeliveryManager)
                        .collect(Collectors.toSet()));

                deliveryDirectors.add(fullName);

                accountNames.addAll(currentBudgetSummaries.stream()
                        .map(RevenueBudgetSummary::getAccount)
                        .collect(Collectors.toSet()));
                accountNames.addAll(previousBudgetSummaries.stream()
                        .map(BaseLine_RevenueBudgetSummary::getAccount)
                        .collect(Collectors.toSet()));

                financialYears.addAll(currentBudgetSummaries.stream()
                        .map(RevenueBudgetSummary::getFinancialYear)
                        .collect(Collectors.toSet()));
                financialYears.addAll(previousBudgetSummaries.stream()
                        .map(BaseLine_RevenueBudgetSummary::getFinancialYear)
                        .collect(Collectors.toSet()));

                // Aggregate current data
                for (RevenueBudgetSummary budgetSummary : currentBudgetSummaries) {
                    String key = budgetSummary.getFinancialYear() + "-" + budgetSummary.getQuarter();
                    currentRvbForecasts.putIfAbsent(key, 0.0);
                    currentRvbForecasts.put(key, currentRvbForecasts.get(key) + budgetSummary.getForecast());
                }

                for (RevenueGrowthSummary growthSummary : currentGrowthSummaries) {
                    String key = growthSummary.getFinancialYear() + "-" + growthSummary.getQuarter();
                    currentRvgForecasts.putIfAbsent(key, 0.0);
                    currentRvgForecasts.put(key, currentRvgForecasts.get(key) + growthSummary.getForecast());
                }

                for (PipelineState pipelineState : currentPipelineStates) {
                    String key = pipelineState.getFinancialYear() + "-" + pipelineState.getQuarter();
                    currentPipelineSums.putIfAbsent(key, 0.0);
                    currentPipelineSums.put(key, currentPipelineSums.get(key) + pipelineState.getSumOfPipeline_total());
                }

                // Aggregate previous data
                for (BaseLine_RevenueBudgetSummary budgetSummary : previousBudgetSummaries) {
                    String key = budgetSummary.getFinancialYear() + "-" + budgetSummary.getQuarter();
                    previousRvbForecasts.putIfAbsent(key, 0.0);
                    previousRvbForecasts.put(key, previousRvbForecasts.get(key) + budgetSummary.getForecast());
                }

                for (BaseLine_RevenueGrowthSummary growthSummary : previousGrowthSummaries) {
                    String key = growthSummary.getFinancialYear() + "-" + growthSummary.getQuarter();
                    previousRvgForecasts.putIfAbsent(key, 0.0);
                    previousRvgForecasts.put(key, previousRvgForecasts.get(key) + growthSummary.getForecast());
                }

                for (BaseLine_PipelineState pipelineState : previousPipelineStates) {
                    String key = pipelineState.getFinancialYear() + "-" + pipelineState.getQuarter();
                    previousPipelineSums.putIfAbsent(key, 0.0);
                    previousPipelineSums.put(key, previousPipelineSums.get(key) + pipelineState.getSumOfPipeline_total());
                }
            } else if (role.equals("Delivery Manager")) {
                // Fetch current and previous data for Delivery Manager
                List<RevenueBudgetSummary> currentBudgetSummaries = revenueBudgetSummaryRepository.findByDeliveryManager(fullName);
                List<RevenueGrowthSummary> currentGrowthSummaries = revenueGrowthSummaryRepository.findByDeliveryManager(fullName);
                List<PipelineState> currentPipelineStates = pipelineStateRepository.findByDeliveryManager(fullName);

                List<BaseLine_RevenueBudgetSummary> previousBudgetSummaries = baseLineRevenueBudgetSummaryRepository.findByDeliveryManager(fullName);
                List<BaseLine_RevenueGrowthSummary> previousGrowthSummaries = baseLineRevenueGrowthSummaryRepository.findByDeliveryManager(fullName);
                List<BaseLine_PipelineState> previousPipelineStates = baseLinePipelineStateRepository.findByDeliveryManager(fullName);

                // Collect delivery managers, account names, and financial years
                deliveryManagers.add(fullName);

                deliveryDirectors.addAll(currentBudgetSummaries.stream()
                        .map(RevenueBudgetSummary::getDeliveryDirector)
                        .collect(Collectors.toSet()));
                deliveryDirectors.addAll(previousBudgetSummaries.stream()
                        .map(BaseLine_RevenueBudgetSummary::getDeliveryDirector)
                        .collect(Collectors.toSet()));

                accountNames.addAll(currentBudgetSummaries.stream()
                        .map(RevenueBudgetSummary::getAccount)
                        .collect(Collectors.toSet()));
                accountNames.addAll(previousBudgetSummaries.stream()
                        .map(BaseLine_RevenueBudgetSummary::getAccount)
                        .collect(Collectors.toSet()));

                financialYears.addAll(currentBudgetSummaries.stream()
                        .map(RevenueBudgetSummary::getFinancialYear)
                        .collect(Collectors.toSet()));
                financialYears.addAll(previousBudgetSummaries.stream()
                        .map(BaseLine_RevenueBudgetSummary::getFinancialYear)
                        .collect(Collectors.toSet()));

                // Aggregate current data
                for (RevenueBudgetSummary budgetSummary : currentBudgetSummaries) {
                    String key = budgetSummary.getFinancialYear() + "-" + budgetSummary.getQuarter();
                    currentRvbForecasts.putIfAbsent(key, 0.0);
                    currentRvbForecasts.put(key, currentRvbForecasts.get(key) + budgetSummary.getForecast());
                }

                for (RevenueGrowthSummary growthSummary : currentGrowthSummaries) {
                    String key = growthSummary.getFinancialYear() + "-" + growthSummary.getQuarter();
                    currentRvgForecasts.putIfAbsent(key, 0.0);
                    currentRvgForecasts.put(key, currentRvgForecasts.get(key) + growthSummary.getForecast());
                }

                for (PipelineState pipelineState : currentPipelineStates) {
                    String key = pipelineState.getFinancialYear() + "-" + pipelineState.getQuarter();
                    currentPipelineSums.putIfAbsent(key, 0.0);
                    currentPipelineSums.put(key, currentPipelineSums.get(key) + pipelineState.getSumOfPipeline_total());
                }

                // Aggregate previous data
                for (BaseLine_RevenueBudgetSummary budgetSummary : previousBudgetSummaries) {
                    String key = budgetSummary.getFinancialYear() + "-" + budgetSummary.getQuarter();
                    previousRvbForecasts.putIfAbsent(key, 0.0);
                    previousRvbForecasts.put(key, previousRvbForecasts.get(key) + budgetSummary.getForecast());
                }

                for (BaseLine_RevenueGrowthSummary growthSummary : previousGrowthSummaries) {
                    String key = growthSummary.getFinancialYear() + "-" + growthSummary.getQuarter();
                    previousRvgForecasts.putIfAbsent(key, 0.0);
                    previousRvgForecasts.put(key, previousRvgForecasts.get(key) + growthSummary.getForecast());
                }

                for (BaseLine_PipelineState pipelineState : previousPipelineStates) {
                    String key = pipelineState.getFinancialYear() + "-" + pipelineState.getQuarter();
                    previousPipelineSums.putIfAbsent(key, 0.0);
                    previousPipelineSums.put(key, previousPipelineSums.get(key) + pipelineState.getSumOfPipeline_total());
                }
            }
        }

        // Restrict to the latest two financial years
        List<Integer> latestTwoFinancialYears = financialYears.stream()
                .limit(2)
                .collect(Collectors.toList());

        // Prepare current data list
        List<Map<String, Object>> currentData = new ArrayList<>();
        for (Integer year : latestTwoFinancialYears) {
            for (String quarter : Arrays.asList("Q1", "Q2", "Q3", "Q4")) {
                String key = year + "-" + quarter;
                Map<String, Object> quarterData = new HashMap<>();
                quarterData.put("quarter", quarter);
                quarterData.put("financialYear", year);
                quarterData.put("totalPipelineSum", currentPipelineSums.getOrDefault(key, 0.0));
                quarterData.put("rvb_Forecast", currentRvbForecasts.getOrDefault(key, 0.0));
                quarterData.put("rvg_Forecast", currentRvgForecasts.getOrDefault(key, 0.0));
                currentData.add(quarterData);
            }
        }

        // Prepare previous data list
        List<Map<String, Object>> previousData = new ArrayList<>();
        for (Integer year : latestTwoFinancialYears) {
            for (String quarter : Arrays.asList("Q1", "Q2", "Q3", "Q4")) {
                String key = year + "-" + quarter;
                Map<String, Object> quarterData = new HashMap<>();
                quarterData.put("quarter", quarter);
                quarterData.put("financialYear", year);
                quarterData.put("totalPipelineSum", previousPipelineSums.getOrDefault(key, 0.0));
                quarterData.put("rvb_Forecast", previousRvbForecasts.getOrDefault(key, 0.0));
                quarterData.put("rvg_Forecast", previousRvgForecasts.getOrDefault(key, 0.0));
                previousData.add(quarterData);
            }
        }

        // Set final response fields
        response.put("deliveryManager", new ArrayList<>(deliveryManagers));
        response.put("deliveryDirector", new ArrayList<>(deliveryDirectors));
        response.put("account", new ArrayList<>(accountNames));
        response.put("currentData", currentData);
        response.put("previousData", previousData);

        // Return the response
        return ResponseEntity.ok(response);
    }








}