package com.sonata.portfoliomanagement.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sonata.portfoliomanagement.interfaces.*;
import com.sonata.portfoliomanagement.model.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173" )
@RestController
@RequestMapping("revenuedashboard")
public class RevenueDashboardController {

    @Autowired
    private MD_RolesRepository mdRolesRepository;

    @Autowired
    private MD_UsersRepository mdUsersRepository;


    @Autowired
    private BaseLine_RevenueBudgetSummaryRepository baseLineRevenueBudgetSummaryRepository;

    @Autowired
    private BaseLine_RevenueGrowthSummaryRepository baseLineRevenueGrowthSummaryRepository;
    @Autowired
    private RevenueBudgetSummaryRepository revenueBudgetSummaryRepository;

    @Autowired
    private RevenueGrowthSummaryRepository revenueGrowthSummaryRepository;
    @Autowired
    private PipelineStateRepository pipelineStateRepository;


    private static final Logger logger = LoggerFactory.getLogger(RevenueDashboardController.class);


    @Autowired
    private BaseLine_PipelineStateRepository baseLine_PipelineStateRepository;


    @GetMapping("/baselinedata")
    public String createBaselineData() {
        LocalDate currentTimestamp = LocalDate.now();

        // Get the maximum financial year from the data
        Set<Integer> allYears = new HashSet<>();
        allYears.addAll(revenueBudgetSummaryRepository.findAll().stream().map(RevenueBudgetSummary::getFinancialYear).collect(Collectors.toSet()));
        allYears.addAll(revenueGrowthSummaryRepository.findAll().stream().map(RevenueGrowthSummary::getFinancialYear).collect(Collectors.toSet()));
        allYears.addAll(pipelineStateRepository.findAll().stream().map(PipelineState::getFinancialYear).collect(Collectors.toSet()));

        int currentFinancialYear = allYears.stream().max(Integer::compareTo).orElse(getCurrentFinancialYear());
        int previousFinancialYear = currentFinancialYear - 1;

        // Remove existing data in baseline tables for the current timestamp
        baseLineRevenueBudgetSummaryRepository.deleteAll();
        baseLineRevenueGrowthSummaryRepository.deleteAll();
        baseLine_PipelineStateRepository.deleteAll();

        // Copy and sum Revenue Budget data
        List<RevenueBudgetSummary> revenueBudgetSummaries = revenueBudgetSummaryRepository.findAll().stream()
                .filter(rbs -> rbs.getFinancialYear() == currentFinancialYear || rbs.getFinancialYear() == currentFinancialYear - 1)
                .collect(Collectors.toList());

        Map<String, BaseLine_RevenueBudgetSummary> baselineRevenueBudgetMap = new HashMap<>();
        for (RevenueBudgetSummary rbs : revenueBudgetSummaries) {
            String key = rbs.getAccount() + rbs.getFinancialYear() + rbs.getQuarter();
            baselineRevenueBudgetMap.computeIfAbsent(key, k -> {
                BaseLine_RevenueBudgetSummary baseline = new BaseLine_RevenueBudgetSummary();
                baseline.setDeliveryDirector(rbs.getDeliveryDirector());
                baseline.setDeliveryManager(rbs.getDeliveryManager());
                baseline.setAccount(rbs.getAccount());
                baseline.setFinancialYear(rbs.getFinancialYear());
                baseline.setQuarter(rbs.getQuarter());
                baseline.setBaselineTimestamp(currentTimestamp);
                return baseline;
            });

            BaseLine_RevenueBudgetSummary baseline = baselineRevenueBudgetMap.get(key);
            baseline.setBudget(baseline.getBudget() + rbs.getBudget());
            baseline.setForecast(baseline.getForecast() + rbs.getForecast());
            baseline.setGap(baseline.getGap() + rbs.getGap());
        }
        baseLineRevenueBudgetSummaryRepository.saveAll(baselineRevenueBudgetMap.values());

        // Copy and sum Revenue Growth data
        List<RevenueGrowthSummary> revenueGrowthSummaries = revenueGrowthSummaryRepository.findAll().stream()
                .filter(rgs -> rgs.getFinancialYear() == currentFinancialYear || rgs.getFinancialYear() == currentFinancialYear - 1)
                .collect(Collectors.toList());

        Map<String, BaseLine_RevenueGrowthSummary> baselineRevenueGrowthMap = new HashMap<>();
        for (RevenueGrowthSummary rgs : revenueGrowthSummaries) {
            String key = rgs.getAccount() + rgs.getFinancialYear() + rgs.getQuarter();
            baselineRevenueGrowthMap.computeIfAbsent(key, k -> {
                BaseLine_RevenueGrowthSummary baseline = new BaseLine_RevenueGrowthSummary();
                baseline.setDeliveryDirector(rgs.getDeliveryDirector());
                baseline.setDeliveryManager(rgs.getDeliveryManager());
                baseline.setAccount(rgs.getAccount());
                baseline.setFinancialYear(rgs.getFinancialYear());
                baseline.setQuarter(rgs.getQuarter());
                baseline.setBaselineTimestamp(currentTimestamp);
                return baseline;
            });

            BaseLine_RevenueGrowthSummary baseline = baselineRevenueGrowthMap.get(key);
            baseline.setAccountExpected(baseline.getAccountExpected() + rgs.getAccountExpected());
            baseline.setForecast(baseline.getForecast() + rgs.getForecast());
            baseline.setGap(baseline.getGap() + rgs.getGap());
        }
        baseLineRevenueGrowthSummaryRepository.saveAll(baselineRevenueGrowthMap.values());

        // Copy and sum Pipeline data
        List<PipelineState> pipelineStates = pipelineStateRepository.findAll().stream()
                .filter(ps -> ps.getFinancialYear() == currentFinancialYear || ps.getFinancialYear() == currentFinancialYear - 1)
                .collect(Collectors.toList());

        Map<String, BaseLine_PipelineState> baselinePipelineMap = new HashMap<>();
        for (PipelineState ps : pipelineStates) {
            String key = ps.getAccount() + ps.getFinancialYear() + ps.getQuarter();
            baselinePipelineMap.computeIfAbsent(key, k -> {
                BaseLine_PipelineState baseline = new BaseLine_PipelineState();
                baseline.setDeliveryDirector(ps.getDeliveryDirector());
                baseline.setDeliveryManager(ps.getDeliveryManager());
                baseline.setAccount(ps.getAccount());
                baseline.setFinancialYear(ps.getFinancialYear());
                baseline.setQuarter(ps.getQuarter());
                baseline.setBaselineTimestamp(currentTimestamp);
                return baseline;
            });

            BaseLine_PipelineState baseline = baselinePipelineMap.get(key);
            baseline.setSumOfPipeline_opportunity(baseline.getSumOfPipeline_opportunity() + ps.getSumOfPipeline_opportunity());
            baseline.setSumOfPipeline_shaping(baseline.getSumOfPipeline_shaping() + ps.getSumOfPipeline_shaping());
            baseline.setSumOfPipeline_pitch(baseline.getSumOfPipeline_pitch() + ps.getSumOfPipeline_pitch());
            baseline.setSumOfPipeline_total(baseline.getSumOfPipeline_total() + ps.getSumOfPipeline_total());
        }
        baseLine_PipelineStateRepository.saveAll(baselinePipelineMap.values());

        return "Baseline data created successfully for date - " + currentTimestamp;
    }

    private int getCurrentFinancialYear() {
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        if (today.getMonthValue() >= Month.APRIL.getValue()) {
            return year;
        } else {
            return year - 1;
        }
    }


    //get data by datestamp
    @PostMapping("/databydatestamp")
    public Baseline_RevenueDataDTO getDataByBaselineTimestamp(@RequestBody Baseline_RevenueDataDTO request) {
        LocalDate baselineTimestamp = request.getTimestamp();

        // Retrieve data from Baseline Revenue Budget Summary table
        List<BaseLine_RevenueBudgetSummary> revenueBudgetSummaries = baseLineRevenueBudgetSummaryRepository.findAll().stream()
                .filter(rbs -> rbs.getBaselineTimestamp().equals(baselineTimestamp))
                .collect(Collectors.toList());

        // Retrieve data from Baseline Revenue Growth Summary table
        List<BaseLine_RevenueGrowthSummary> revenueGrowthSummaries = baseLineRevenueGrowthSummaryRepository.findAll().stream()
                .filter(rgs -> rgs.getBaselineTimestamp().equals(baselineTimestamp))
                .collect(Collectors.toList());

        // Retrieve data from Baseline Pipeline State table
        List<BaseLine_PipelineState> pipelineStates = baseLine_PipelineStateRepository.findAll().stream()
                .filter(ps -> ps.getBaselineTimestamp().equals(baselineTimestamp))
                .collect(Collectors.toList());

        // Set the data in the request DTO and return
        request.setRevenueBudget(revenueBudgetSummaries);
        request.setRevenueGrowth(revenueGrowthSummaries);
        request.setPipelineStates(pipelineStates);

        return request;
    }


//getting list data of baseline_tables for revenue dashboard

    @GetMapping("/all-baseline-summaries")
    public RevBaselineListDTO getAllBaselineSummaries() {
        RevBaselineListDTO responseDTO = new RevBaselineListDTO();

        // Collect data from BaseLine_RevenueBudgetSummary table
        List<BaseLine_RevenueBudgetSummary> budgetSummaries = baseLineRevenueBudgetSummaryRepository.findAll();
        Set<String> deliveryDirectors = budgetSummaries.stream()
                .map(BaseLine_RevenueBudgetSummary::getDeliveryDirector)
                .collect(Collectors.toSet());
        Set<String> deliveryManagers = budgetSummaries.stream()
                .map(BaseLine_RevenueBudgetSummary::getDeliveryManager)
                .collect(Collectors.toSet());
        Set<String> accounts = budgetSummaries.stream()
                .map(BaseLine_RevenueBudgetSummary::getAccount)
                .collect(Collectors.toSet());


        // Collect data from BaseLine_RevenueGrowthSummary table
        List<BaseLine_RevenueGrowthSummary> growthSummaries = baseLineRevenueGrowthSummaryRepository.findAll();
        deliveryDirectors.addAll(growthSummaries.stream()
                .map(BaseLine_RevenueGrowthSummary::getDeliveryDirector)
                .collect(Collectors.toSet()));
        deliveryManagers.addAll(growthSummaries.stream()
                .map(BaseLine_RevenueGrowthSummary::getDeliveryManager)
                .collect(Collectors.toSet()));
        accounts.addAll(growthSummaries.stream()
                .map(BaseLine_RevenueGrowthSummary::getAccount)
                .collect(Collectors.toSet()));

        // Collect data from BaseLine_PipelineState table
        List<BaseLine_PipelineState> pipelineStates = baseLine_PipelineStateRepository.findAll();
        deliveryDirectors.addAll(pipelineStates.stream()
                .map(BaseLine_PipelineState::getDeliveryDirector)
                .collect(Collectors.toSet()));
        deliveryManagers.addAll(pipelineStates.stream()
                .map(BaseLine_PipelineState::getDeliveryManager)
                .collect(Collectors.toSet()));
        accounts.addAll(pipelineStates.stream()
                .map(BaseLine_PipelineState::getAccount)
                .collect(Collectors.toSet()));


        responseDTO.setDeliveryDirectors(List.copyOf(deliveryDirectors));
        responseDTO.setDeliveryManagers(List.copyOf(deliveryManagers));
        responseDTO.setAccounts(List.copyOf(accounts));


        return responseDTO;
    }


    //getting list data of Revenue_tables for revenue dashboard
    @GetMapping("/all-revenue-summaries")
    public RevenueDashboardListDTO getAllRevenueSummaries() {
        RevenueDashboardListDTO responseDTO = new RevenueDashboardListDTO();

        // Collect data from RevenueBudgetSummary table
        List<RevenueBudgetSummary> budgetSummaries = revenueBudgetSummaryRepository.findAll();
        Set<String> deliveryDirectors = budgetSummaries.stream()
                .map(RevenueBudgetSummary::getDeliveryDirector)
                .collect(Collectors.toSet());
        Set<String> deliveryManagers = budgetSummaries.stream()
                .map(RevenueBudgetSummary::getDeliveryManager)
                .collect(Collectors.toSet());
        Set<String> accounts = budgetSummaries.stream()
                .map(RevenueBudgetSummary::getAccount)
                .collect(Collectors.toSet());
        Set<Integer> financialYears = budgetSummaries.stream()
                .map(RevenueBudgetSummary::getFinancialYear)
                .collect(Collectors.toSet());

        // Collect data from RevenueGrowthSummary table
        List<RevenueGrowthSummary> growthSummaries = revenueGrowthSummaryRepository.findAll();
        deliveryDirectors.addAll(growthSummaries.stream()
                .map(RevenueGrowthSummary::getDeliveryDirector)
                .collect(Collectors.toSet()));
        deliveryManagers.addAll(growthSummaries.stream()
                .map(RevenueGrowthSummary::getDeliveryManager)
                .collect(Collectors.toSet()));
        accounts.addAll(growthSummaries.stream()
                .map(RevenueGrowthSummary::getAccount)
                .collect(Collectors.toSet()));
        financialYears.addAll(growthSummaries.stream()
                .map(RevenueGrowthSummary::getFinancialYear)
                .collect(Collectors.toSet()));

        // Collect data from PipelineState table
        List<PipelineState> pipelineStates = pipelineStateRepository.findAll();
        deliveryDirectors.addAll(pipelineStates.stream()
                .map(PipelineState::getDeliveryDirector)
                .collect(Collectors.toSet()));
        deliveryManagers.addAll(pipelineStates.stream()
                .map(PipelineState::getDeliveryManager)
                .collect(Collectors.toSet()));
        accounts.addAll(pipelineStates.stream()
                .map(PipelineState::getAccount)
                .collect(Collectors.toSet()));
        financialYears.addAll(pipelineStates.stream()
                .map(PipelineState::getFinancialYear)
                .collect(Collectors.toSet()));

        responseDTO.setDeliveryDirectors(List.copyOf(deliveryDirectors));
        responseDTO.setDeliveryManagers(List.copyOf(deliveryManagers));
        responseDTO.setAccounts(List.copyOf(accounts));
        responseDTO.setFinancialYears(List.copyOf(financialYears));

        return responseDTO;
    }
//-----------------------------------------------------------


    public RevenueDashboardController(RevenueBudgetSummaryRepository revenueBudgetSummaryRepository,
                                      RevenueGrowthSummaryRepository revenueGrowthSummaryRepository,
                                      PipelineStateRepository pipelineStateRepository) {
        this.revenueBudgetSummaryRepository = revenueBudgetSummaryRepository;
        this.revenueGrowthSummaryRepository = revenueGrowthSummaryRepository;
        this.pipelineStateRepository = pipelineStateRepository;
    }

    @GetMapping("/revenue-dashboard")
    public ResponseEntity<List<RevDashboardDTO>> getRevenueDashboard() {
        List<RevenueBudgetSummary> revenueBudgetSummaries = revenueBudgetSummaryRepository.findAll();
        List<RevenueGrowthSummary> revenueGrowthSummaries = revenueGrowthSummaryRepository.findAll();
        List<PipelineState> pipelineStates = pipelineStateRepository.findAll();

        Map<String, RevDashboardDTO> directorManagerMap = new HashMap<>();

        // Aggregate RevenueBudgetSummary data
        for (RevenueBudgetSummary revenueBudget : revenueBudgetSummaries) {
            String directorManagerKey = generateDirectorManagerKey(revenueBudget.getDeliveryDirector(), revenueBudget.getDeliveryManager());

            if (!directorManagerMap.containsKey(directorManagerKey)) {
                directorManagerMap.put(directorManagerKey, new RevDashboardDTO(
                        revenueBudget.getDeliveryDirector(),
                        revenueBudget.getDeliveryManager(),
                        new ArrayList<>()
                ));
            }

            RevDashboardDTO dto = directorManagerMap.get(directorManagerKey);
            RevDashboardDTO.AccountData accountData = findOrCreateAccountData(dto.getAccounts(), revenueBudget.getAccount(),
                    revenueBudget.getFinancialYear(), revenueBudget.getQuarter());

            RevDashboardDTO.RevenueBudgetSummary budgetSummary = accountData.getRevenueBudget();
            if (budgetSummary == null) {
                budgetSummary = new RevDashboardDTO.RevenueBudgetSummary(0, 0, 0);
                accountData.setRevenueBudget(budgetSummary);
            }
            budgetSummary.setBudget(budgetSummary.getBudget() + revenueBudget.getBudget());
            budgetSummary.setForecast(budgetSummary.getForecast() + revenueBudget.getForecast());
            budgetSummary.setGap(budgetSummary.getGap() + revenueBudget.getGap());
        }

        // Aggregate RevenueGrowthSummary data
        for (RevenueGrowthSummary revenueGrowth : revenueGrowthSummaries) {
            String directorManagerKey = generateDirectorManagerKey(revenueGrowth.getDeliveryDirector(), revenueGrowth.getDeliveryManager());

            if (!directorManagerMap.containsKey(directorManagerKey)) {
                directorManagerMap.put(directorManagerKey, new RevDashboardDTO(
                        revenueGrowth.getDeliveryDirector(),
                        revenueGrowth.getDeliveryManager(),
                        new ArrayList<>()
                ));
            }

            RevDashboardDTO dto = directorManagerMap.get(directorManagerKey);
            RevDashboardDTO.AccountData accountData = findOrCreateAccountData(dto.getAccounts(), revenueGrowth.getAccount(),
                    revenueGrowth.getFinancialYear(), revenueGrowth.getQuarter());

            RevDashboardDTO.RevenueGrowthSummary growthSummary = accountData.getRevenueGrowth();
            if (growthSummary == null) {
                growthSummary = new RevDashboardDTO.RevenueGrowthSummary(0, 0, 0);
                accountData.setRevenueGrowth(growthSummary);
            }
            growthSummary.setAccountExpected(growthSummary.getAccountExpected() + revenueGrowth.getAccountExpected());
            growthSummary.setForecast(growthSummary.getForecast() + revenueGrowth.getForecast());
            growthSummary.setGap(growthSummary.getGap() + revenueGrowth.getGap());
        }

        // Aggregate PipelineState data based on quarter
        for (PipelineState pipelineState : pipelineStates) {
            String directorManagerKey = generateDirectorManagerKey(pipelineState.getDeliveryDirector(), pipelineState.getDeliveryManager());

            if (!directorManagerMap.containsKey(directorManagerKey)) {
                directorManagerMap.put(directorManagerKey, new RevDashboardDTO(
                        pipelineState.getDeliveryDirector(),
                        pipelineState.getDeliveryManager(),
                        new ArrayList<>()
                ));
            }

            RevDashboardDTO dto = directorManagerMap.get(directorManagerKey);
            RevDashboardDTO.AccountData accountData = findOrCreateAccountData(dto.getAccounts(), pipelineState.getAccount(),
                    pipelineState.getFinancialYear(), pipelineState.getQuarter());

            RevDashboardDTO.PipelineState pipelineSummary = accountData.getPipelineStatus();
            if (pipelineSummary == null) {
                pipelineSummary = new RevDashboardDTO.PipelineState(0, 0, 0, 0);
                accountData.setPipelineStatus(pipelineSummary);
            }
            pipelineSummary.setSumOfPipeline_pitch(pipelineSummary.getSumOfPipeline_pitch() + pipelineState.getSumOfPipeline_pitch());
            pipelineSummary.setSumOfPipeline_opportunity(pipelineSummary.getSumOfPipeline_opportunity() + pipelineState.getSumOfPipeline_opportunity());
            pipelineSummary.setSumOfPipeline_total(pipelineSummary.getSumOfPipeline_total() + pipelineState.getSumOfPipeline_total());
            pipelineSummary.setSumOfPipeline_shaping(pipelineSummary.getSumOfPipeline_shaping() + pipelineState.getSumOfPipeline_shaping());
        }

        return ResponseEntity.ok(new ArrayList<>(directorManagerMap.values()));
    }

    private String generateDirectorManagerKey(String deliveryDirector, String deliveryManager) {
        return deliveryDirector + "_" + deliveryManager;
    }

    private RevDashboardDTO.AccountData findOrCreateAccountData(List<RevDashboardDTO.AccountData> accountDataList, String account, int financialYear, String quarter) {
        for (RevDashboardDTO.AccountData accountData : accountDataList) {
            if (accountData.getAccount().equals(account) && accountData.getFinancialYear() == financialYear && accountData.getQuarter().equals(quarter)) {
                return accountData;
            }
        }

        RevDashboardDTO.AccountData newAccountData = new RevDashboardDTO.AccountData(account, financialYear, quarter, null, null, null);
        accountDataList.add(newAccountData);
        return newAccountData;
    }



    @PostMapping("/getRevenueDashboardDirector")
    public ResponseEntity<List<RevDashboardDTO>> getRevenueDashboardByDirector(@RequestBody List<String> deliveryDirectors) {
        Map<String, RevDashboardDTO> directorMap = new HashMap<>();

        for (String deliveryDirector : deliveryDirectors) {
            logger.info("Fetching data for Delivery Director: {}", deliveryDirector);

            List<RevenueBudgetSummary> revenueBudgetSummaries = revenueBudgetSummaryRepository.findByDeliveryDirector(deliveryDirector);
            List<RevenueGrowthSummary> revenueGrowthSummaries = revenueGrowthSummaryRepository.findByDeliveryDirector(deliveryDirector);
            List<PipelineState> pipelineStates = pipelineStateRepository.findByDeliveryDirector(deliveryDirector);

            aggregateBudgetSummariesByDirector(revenueBudgetSummaries, directorMap, deliveryDirector);
            aggregateGrowthSummariesByDirector(revenueGrowthSummaries, directorMap, deliveryDirector);
            aggregatePipelineStatesByDirector(pipelineStates, directorMap, deliveryDirector);
        }

        List<RevDashboardDTO> result = new ArrayList<>(directorMap.values());

        // Consolidate financial years into a single list across all RevDashboardDTOs
        Set<Integer> allFinancialYears = new HashSet<>();
        result.forEach(dto -> allFinancialYears.addAll(dto.getFinancialYears()));
        result.forEach(dto -> dto.setFinancialYears(new ArrayList<>(allFinancialYears)));

        return ResponseEntity.ok(result);
    }

    private void aggregateBudgetSummariesByDirector(List<RevenueBudgetSummary> revenueBudgetSummaries,
                                                    Map<String, RevDashboardDTO> directorMap,
                                                    String deliveryDirector) {
        for (RevenueBudgetSummary budgetSummary : revenueBudgetSummaries) {
            int financialYear = budgetSummary.getFinancialYear();
            String quarter = budgetSummary.getQuarter();
            String account = budgetSummary.getAccount();
            String deliveryManager = budgetSummary.getDeliveryManager();

            directorMap.putIfAbsent(deliveryDirector, new RevDashboardDTO(deliveryDirector));
            RevDashboardDTO revDashboardDTO = directorMap.get(deliveryDirector);

            // Add account names and financial years to the lists
            if (!revDashboardDTO.getAccountNames().contains(account)) {
                revDashboardDTO.getAccountNames().add(account);
            }
            if (!revDashboardDTO.getFinancialYears().contains(financialYear)) {
                revDashboardDTO.getFinancialYears().add(financialYear);
            }

            // Add delivery manager to the list
            if (!revDashboardDTO.getDeliveryManagers().contains(deliveryManager)) {
                revDashboardDTO.getDeliveryManagers().add(deliveryManager);
            }

            Optional<RevDashboardDTO.AccountData> optionalAccountData = revDashboardDTO.getAccounts().stream()
                    .filter(a -> a.getFinancialYear() == financialYear && a.getQuarter().equals(quarter) && a.getAccount().equals(account))
                    .findFirst();

            RevDashboardDTO.AccountData accountData;
            if (optionalAccountData.isPresent()) {
                accountData = optionalAccountData.get();
                accountData.getRevenueBudget().setBudget(accountData.getRevenueBudget().getBudget() + budgetSummary.getBudget());
                accountData.getRevenueBudget().setForecast(accountData.getRevenueBudget().getForecast() + budgetSummary.getForecast());
                accountData.getRevenueBudget().setGap(accountData.getRevenueBudget().getGap() + budgetSummary.getGap());
            } else {
                accountData = new RevDashboardDTO.AccountData(account, financialYear, quarter,
                        new RevDashboardDTO.RevenueBudgetSummary(budgetSummary.getBudget(), budgetSummary.getForecast(), budgetSummary.getGap()),
                        new RevDashboardDTO.RevenueGrowthSummary(0, 0, 0),
                        new RevDashboardDTO.PipelineState(0, 0, 0, 0));
                revDashboardDTO.getAccounts().add(accountData);
            }
        }
    }

    private void aggregateGrowthSummariesByDirector(List<RevenueGrowthSummary> revenueGrowthSummaries,
                                                    Map<String, RevDashboardDTO> directorMap,
                                                    String deliveryDirector) {
        for (RevenueGrowthSummary growthSummary : revenueGrowthSummaries) {
            int financialYear = growthSummary.getFinancialYear();
            String quarter = growthSummary.getQuarter();
            String account = growthSummary.getAccount();
            String deliveryManager = growthSummary.getDeliveryManager();

            RevDashboardDTO revDashboardDTO = directorMap.get(deliveryDirector);

// Add account names and financial years to the lists
            if (!revDashboardDTO.getAccountNames().contains(account)) {
                revDashboardDTO.getAccountNames().add(account);
            }
            if (!revDashboardDTO.getFinancialYears().contains(financialYear)) {
                revDashboardDTO.getFinancialYears().add(financialYear);
            }

// Add delivery manager to the list
            if (!revDashboardDTO.getDeliveryManagers().contains(deliveryManager)) {
                revDashboardDTO.getDeliveryManagers().add(deliveryManager);
            }

            Optional<RevDashboardDTO.AccountData> optionalAccountData = revDashboardDTO.getAccounts().stream()
                    .filter(a -> a.getFinancialYear() == financialYear && a.getQuarter().equals(quarter) && a.getAccount().equals(account))
                    .findFirst();

            RevDashboardDTO.AccountData accountData;
            if (optionalAccountData.isPresent()) {
                accountData = optionalAccountData.get();
                accountData.getRevenueGrowth().setAccountExpected(accountData.getRevenueGrowth().getAccountExpected() + growthSummary.getAccountExpected());
                accountData.getRevenueGrowth().setForecast(accountData.getRevenueGrowth().getForecast() + growthSummary.getForecast());
                accountData.getRevenueGrowth().setGap(accountData.getRevenueGrowth().getGap() + growthSummary.getGap());
            } else {
                accountData = new RevDashboardDTO.AccountData(account, financialYear, quarter,
                        new RevDashboardDTO.RevenueBudgetSummary(0, 0, 0),
                        new RevDashboardDTO.RevenueGrowthSummary(growthSummary.getAccountExpected(), growthSummary.getForecast(), growthSummary.getGap()),
                        new RevDashboardDTO.PipelineState(0, 0, 0, 0));
                revDashboardDTO.getAccounts().add(accountData);
            }
        }
    }

    private void aggregatePipelineStatesByDirector(List<PipelineState> pipelineStates,
                                                   Map<String, RevDashboardDTO> directorMap,
                                                   String deliveryDirector) {
        for (PipelineState pipelineState : pipelineStates) {
            int financialYear = pipelineState.getFinancialYear();
            String quarter = pipelineState.getQuarter();
            String account = pipelineState.getAccount();
            String deliveryManager = pipelineState.getDeliveryManager();

            RevDashboardDTO revDashboardDTO = directorMap.get(deliveryDirector);

// Add account names and financial years to the lists
            if (!revDashboardDTO.getAccountNames().contains(account)) {
                revDashboardDTO.getAccountNames().add(account);
            }
            if (!revDashboardDTO.getFinancialYears().contains(financialYear)) {
                revDashboardDTO.getFinancialYears().add(financialYear);
            }

// Add delivery manager to the list
            if (!revDashboardDTO.getDeliveryManagers().contains(deliveryManager)) {
                revDashboardDTO.getDeliveryManagers().add(deliveryManager);
            }

            Optional<RevDashboardDTO.AccountData> optionalAccountData = revDashboardDTO.getAccounts().stream()
                    .filter(a -> a.getFinancialYear() == financialYear && a.getQuarter().equals(quarter) && a.getAccount().equals(account))
                    .findFirst();

            RevDashboardDTO.AccountData accountData;
            if (optionalAccountData.isPresent()) {
                accountData = optionalAccountData.get();
                accountData.getPipelineStatus().setSumOfPipeline_pitch(accountData.getPipelineStatus().getSumOfPipeline_pitch() + pipelineState.getSumOfPipeline_pitch());
                accountData.getPipelineStatus().setSumOfPipeline_opportunity(accountData.getPipelineStatus().getSumOfPipeline_opportunity() + pipelineState.getSumOfPipeline_opportunity());
                accountData.getPipelineStatus().setSumOfPipeline_total(accountData.getPipelineStatus().getSumOfPipeline_total() + pipelineState.getSumOfPipeline_total());
                accountData.getPipelineStatus().setSumOfPipeline_shaping(accountData.getPipelineStatus().getSumOfPipeline_shaping() + pipelineState.getSumOfPipeline_shaping());
            } else {
                accountData = new RevDashboardDTO.AccountData(account, financialYear, quarter,
                        new RevDashboardDTO.RevenueBudgetSummary(0, 0, 0),
                        new RevDashboardDTO.RevenueGrowthSummary(0, 0, 0),
                        new RevDashboardDTO.PipelineState(pipelineState.getSumOfPipeline_pitch(), pipelineState.getSumOfPipeline_opportunity(), pipelineState.getSumOfPipeline_total(), pipelineState.getSumOfPipeline_shaping()));
                revDashboardDTO.getAccounts().add(accountData);
            }
        }
    }



    @PostMapping("/getRevenueDashboardManager")
    public ResponseEntity<List<RevDashboardDTO>> getRevenueDashboardByManager(@RequestBody List<String> deliveryManagers) {
        Map<String, Map<String, RevDashboardDTO>> directorMap = new HashMap<>();

        for (String deliveryManager : deliveryManagers) {
            logger.info("Fetching data for Delivery Manager: {}", deliveryManager);

            List<RevenueBudgetSummary> revenueBudgetSummaries = revenueBudgetSummaryRepository.findByDeliveryManager(deliveryManager);
            List<RevenueGrowthSummary> revenueGrowthSummaries = revenueGrowthSummaryRepository.findByDeliveryManager(deliveryManager);
            List<PipelineState> pipelineStates = pipelineStateRepository.findByDeliveryManager(deliveryManager);

            aggregateBudgetSummaries(revenueBudgetSummaries, directorMap, deliveryManager);
            aggregateGrowthSummaries(revenueGrowthSummaries, directorMap, deliveryManager);
            aggregatePipelineStates(pipelineStates, directorMap, deliveryManager);
        }

        List<RevDashboardDTO> result = new ArrayList<>();
        for (Map<String, RevDashboardDTO> managerMap : directorMap.values()) {
            result.addAll(managerMap.values());
        }

        // Consolidate financial years into a single list across all RevDashboardDTOs
        Set<Integer> allFinancialYears = new HashSet<>();
        result.forEach(dto -> allFinancialYears.addAll(dto.getFinancialYears()));
        result.forEach(dto -> dto.setFinancialYears(new ArrayList<>(allFinancialYears)));

        return ResponseEntity.ok(result);
    }

    private void aggregateBudgetSummaries(List<RevenueBudgetSummary> revenueBudgetSummaries,
                                          Map<String, Map<String, RevDashboardDTO>> directorMap,
                                          String deliveryManager) {
        for (RevenueBudgetSummary budgetSummary : revenueBudgetSummaries) {
            String director = budgetSummary.getDeliveryDirector();
            int financialYear = budgetSummary.getFinancialYear();
            String quarter = budgetSummary.getQuarter();
            String account = budgetSummary.getAccount();

            directorMap.putIfAbsent(director, new HashMap<>());
            Map<String, RevDashboardDTO> managerMap = directorMap.get(director);

            String managerKey = director + "_" + financialYear + "_" + quarter;
            managerMap.putIfAbsent(managerKey, new RevDashboardDTO(director, new ArrayList<>()));
            RevDashboardDTO revDashboardDTO = managerMap.get(managerKey);

            // Add account names and financial years to the lists
            if (!revDashboardDTO.getAccountNames().contains(account)) {
                revDashboardDTO.getAccountNames().add(account);
            }
            if (!revDashboardDTO.getFinancialYears().contains(financialYear)) {
                revDashboardDTO.getFinancialYears().add(financialYear);
            }

            Optional<RevDashboardDTO.AccountData> optionalAccountData = revDashboardDTO.getAccounts().stream()
                    .filter(a -> a.getFinancialYear() == financialYear && a.getQuarter().equals(quarter))
                    .findFirst();

            RevDashboardDTO.AccountData accountData;
            if (optionalAccountData.isPresent()) {
                accountData = optionalAccountData.get();
                accountData.getRevenueBudget().setBudget(accountData.getRevenueBudget().getBudget() + budgetSummary.getBudget());
                accountData.getRevenueBudget().setForecast(accountData.getRevenueBudget().getForecast() + budgetSummary.getForecast());
                accountData.getRevenueBudget().setGap(accountData.getRevenueBudget().getGap() + budgetSummary.getGap());
            } else {
                accountData = new RevDashboardDTO.AccountData("multiple", financialYear, quarter,
                        new RevDashboardDTO.RevenueBudgetSummary(budgetSummary.getBudget(), budgetSummary.getForecast(), budgetSummary.getGap()),
                        new RevDashboardDTO.RevenueGrowthSummary(0, 0, 0),
                        new RevDashboardDTO.PipelineState(0, 0, 0, 0));
                revDashboardDTO.getAccounts().add(accountData);
            }

            if (!revDashboardDTO.getDeliveryManagers().contains(deliveryManager)) {
                revDashboardDTO.getDeliveryManagers().add(deliveryManager);
            }
        }
    }

    private void aggregateGrowthSummaries(List<RevenueGrowthSummary> revenueGrowthSummaries,
                                          Map<String, Map<String, RevDashboardDTO>> directorMap,
                                          String deliveryManager) {
        for (RevenueGrowthSummary growthSummary : revenueGrowthSummaries) {
            String director = growthSummary.getDeliveryDirector();
            int financialYear = growthSummary.getFinancialYear();
            String quarter = growthSummary.getQuarter();
            String account = growthSummary.getAccount();

            Map<String, RevDashboardDTO> managerMap = directorMap.get(director);

            String managerKey = director + "_" + financialYear + "_" + quarter;
            managerMap.putIfAbsent(managerKey, new RevDashboardDTO(director, new ArrayList<>()));
            RevDashboardDTO revDashboardDTO = managerMap.get(managerKey);

            // Add account names and financial years to the lists
            if (!revDashboardDTO.getAccountNames().contains(account)) {
                revDashboardDTO.getAccountNames().add(account);
            }
            if (!revDashboardDTO.getFinancialYears().contains(financialYear)) {
                revDashboardDTO.getFinancialYears().add(financialYear);
            }

            Optional<RevDashboardDTO.AccountData> optionalAccountData = revDashboardDTO.getAccounts().stream()
                    .filter(a -> a.getFinancialYear() == financialYear && a.getQuarter().equals(quarter))
                    .findFirst();

            if (optionalAccountData.isPresent()) {
                RevDashboardDTO.AccountData accountData = optionalAccountData.get();
                accountData.getRevenueGrowth().setAccountExpected(accountData.getRevenueGrowth().getAccountExpected() + growthSummary.getAccountExpected());
                accountData.getRevenueGrowth().setForecast(accountData.getRevenueGrowth().getForecast() + growthSummary.getForecast());
                accountData.getRevenueGrowth().setGap(accountData.getRevenueGrowth().getGap() + growthSummary.getGap());
            }

            if (!revDashboardDTO.getDeliveryManagers().contains(deliveryManager)) {
                revDashboardDTO.getDeliveryManagers().add(deliveryManager);
            }
        }
    }

    private void aggregatePipelineStates(List<PipelineState> pipelineStates,
                                         Map<String, Map<String, RevDashboardDTO>> directorMap,
                                         String deliveryManager) {
        for (PipelineState pipelineState : pipelineStates) {
            String director = pipelineState.getDeliveryDirector();
            int financialYear = pipelineState.getFinancialYear();
            String quarter = pipelineState.getQuarter();
            String account = pipelineState.getAccount();

            Map<String, RevDashboardDTO> managerMap = directorMap.get(director);

            String managerKey = director + "_" + financialYear + "_" + quarter;
            managerMap.putIfAbsent(managerKey, new RevDashboardDTO(director, new ArrayList<>()));
            RevDashboardDTO revDashboardDTO = managerMap.get(managerKey);

            // Add account names and financial years to the lists
            if (!revDashboardDTO.getAccountNames().contains(account)) {
                revDashboardDTO.getAccountNames().add(account);
            }
            if (!revDashboardDTO.getFinancialYears().contains(financialYear)) {
                revDashboardDTO.getFinancialYears().add(financialYear);
            }

            Optional<RevDashboardDTO.AccountData> optionalAccountData = revDashboardDTO.getAccounts().stream()
                    .filter(a -> a.getFinancialYear() == financialYear && a.getQuarter().equals(quarter))
                    .findFirst();

            if (optionalAccountData.isPresent()) {
                RevDashboardDTO.AccountData accountData = optionalAccountData.get();
                accountData.getPipelineStatus().setSumOfPipeline_pitch(accountData.getPipelineStatus().getSumOfPipeline_pitch() + pipelineState.getSumOfPipeline_pitch());
                accountData.getPipelineStatus().setSumOfPipeline_opportunity(accountData.getPipelineStatus().getSumOfPipeline_opportunity() + pipelineState.getSumOfPipeline_opportunity());
                accountData.getPipelineStatus().setSumOfPipeline_total(accountData.getPipelineStatus().getSumOfPipeline_total() + pipelineState.getSumOfPipeline_total());
                accountData.getPipelineStatus().setSumOfPipeline_shaping(accountData.getPipelineStatus().getSumOfPipeline_shaping() + pipelineState.getSumOfPipeline_shaping());
            }

            if (!revDashboardDTO.getDeliveryManagers().contains(deliveryManager)) {
                revDashboardDTO.getDeliveryManagers().add(deliveryManager);
            }
        }
    }



    @PostMapping("/getRevenueDashboardByAccount")
    public ResponseEntity<List<RevDashboardDTO>> getRevenueDashboardByAccount(@RequestBody List<String> accounts) {
        Map<String, Map<String, RevDashboardDTO>> directorMap = new HashMap<>();

        for (String account : accounts) {
            logger.info("Fetching data for Account: {}", account);

            List<RevenueBudgetSummary> revenueBudgetSummaries = revenueBudgetSummaryRepository.findByAccount(account);
            List<RevenueGrowthSummary> revenueGrowthSummaries = revenueGrowthSummaryRepository.findByAccount(account);
            List<PipelineState> pipelineStates = pipelineStateRepository.findByAccount(account);

            aggregateBudgetSummariesByAccount(revenueBudgetSummaries, directorMap, account);
            aggregateGrowthSummariesByAccount(revenueGrowthSummaries, directorMap, account);
            aggregatePipelineStatesByAccount(pipelineStates, directorMap, account);
        }

        List<RevDashboardDTO> result = new ArrayList<>();
        for (Map<String, RevDashboardDTO> managerMap : directorMap.values()) {
            for (RevDashboardDTO dto : managerMap.values()) {
                // Check if there are accounts with data
                if (!dto.getAccounts().isEmpty()) {
                    // Consolidate financial years into a single list across all RevDashboardDTOs
                    Set<Integer> allFinancialYears = new HashSet<>();
                    dto.getAccounts().forEach(accountData -> allFinancialYears.add(accountData.getFinancialYear()));
                    dto.setFinancialYears(new ArrayList<>(allFinancialYears));

                    // Add to result only if accounts are not empty
                    result.add(dto);
                }
            }
        }

        return ResponseEntity.ok(result);
    }

    private void aggregateBudgetSummariesByAccount(List<RevenueBudgetSummary> revenueBudgetSummaries,
                                                   Map<String, Map<String, RevDashboardDTO>> directorMap,
                                                   String account) {
        for (RevenueBudgetSummary budgetSummary : revenueBudgetSummaries) {
            String director = budgetSummary.getDeliveryDirector();
            String deliveryManager = budgetSummary.getDeliveryManager();
            int financialYear = budgetSummary.getFinancialYear();
            String quarter = budgetSummary.getQuarter();

            directorMap.putIfAbsent(director, new HashMap<>());
            Map<String, RevDashboardDTO> managerMap = directorMap.get(director);

            String managerKey = director + "_" + financialYear + "_" + quarter;
            managerMap.putIfAbsent(managerKey, new RevDashboardDTO(director, new ArrayList<>()));
            RevDashboardDTO revDashboardDTO = managerMap.get(managerKey);

            // Add account names and financial years to the lists
            if (!revDashboardDTO.getAccountNames().contains(account)) {
                revDashboardDTO.getAccountNames().add(account);
            }
            if (!revDashboardDTO.getFinancialYears().contains(financialYear)) {
                revDashboardDTO.getFinancialYears().add(financialYear);
            }

            Optional<RevDashboardDTO.AccountData> optionalAccountData = revDashboardDTO.getAccounts().stream()
                    .filter(a -> a.getFinancialYear() == financialYear && a.getQuarter().equals(quarter))
                    .findFirst();

            RevDashboardDTO.AccountData accountData;
            if (optionalAccountData.isPresent()) {
                accountData = optionalAccountData.get();
                accountData.getRevenueBudget().setBudget(accountData.getRevenueBudget().getBudget() + budgetSummary.getBudget());
                accountData.getRevenueBudget().setForecast(accountData.getRevenueBudget().getForecast() + budgetSummary.getForecast());
                accountData.getRevenueBudget().setGap(accountData.getRevenueBudget().getGap() + budgetSummary.getGap());
            } else {
                accountData = new RevDashboardDTO.AccountData("multiple", financialYear, quarter,
                        new RevDashboardDTO.RevenueBudgetSummary(budgetSummary.getBudget(), budgetSummary.getForecast(), budgetSummary.getGap()),
                        new RevDashboardDTO.RevenueGrowthSummary(0, 0, 0),
                        new RevDashboardDTO.PipelineState(0, 0, 0, 0));
                revDashboardDTO.getAccounts().add(accountData);
            }

            if (!revDashboardDTO.getDeliveryManagers().contains(deliveryManager)) {
                revDashboardDTO.getDeliveryManagers().add(deliveryManager);
            }
        }
    }

    private void aggregateGrowthSummariesByAccount(List<RevenueGrowthSummary> revenueGrowthSummaries,
                                                   Map<String, Map<String, RevDashboardDTO>> directorMap,
                                                   String account) {
        for (RevenueGrowthSummary growthSummary : revenueGrowthSummaries) {
            String director = growthSummary.getDeliveryDirector();
            String deliveryManager = growthSummary.getDeliveryManager();
            int financialYear = growthSummary.getFinancialYear();
            String quarter = growthSummary.getQuarter();

            Map<String, RevDashboardDTO> managerMap = directorMap.get(director);

            String managerKey = director + "_" + financialYear + "_" + quarter;
            managerMap.putIfAbsent(managerKey, new RevDashboardDTO(director, new ArrayList<>()));
            RevDashboardDTO revDashboardDTO = managerMap.get(managerKey);

            // Add account names and financial years to the lists
            if (!revDashboardDTO.getAccountNames().contains(account)) {
                revDashboardDTO.getAccountNames().add(account);
            }
            if (!revDashboardDTO.getFinancialYears().contains(financialYear)) {
                revDashboardDTO.getFinancialYears().add(financialYear);
            }

            Optional<RevDashboardDTO.AccountData> optionalAccountData = revDashboardDTO.getAccounts().stream()
                    .filter(a -> a.getFinancialYear() == financialYear && a.getQuarter().equals(quarter))
                    .findFirst();

            if (optionalAccountData.isPresent()) {
                RevDashboardDTO.AccountData accountData = optionalAccountData.get();
                accountData.getRevenueGrowth().setAccountExpected(accountData.getRevenueGrowth().getAccountExpected() + growthSummary.getAccountExpected());
                accountData.getRevenueGrowth().setForecast(accountData.getRevenueGrowth().getForecast() + growthSummary.getForecast());
                accountData.getRevenueGrowth().setGap(accountData.getRevenueGrowth().getGap() + growthSummary.getGap());
            }

            if (!revDashboardDTO.getDeliveryManagers().contains(deliveryManager)) {
                revDashboardDTO.getDeliveryManagers().add(deliveryManager);
            }
        }
    }

    private void aggregatePipelineStatesByAccount(List<PipelineState> pipelineStates,
                                                  Map<String, Map<String, RevDashboardDTO>> directorMap,
                                                  String account) {
        for (PipelineState pipelineState : pipelineStates) {
            String director = pipelineState.getDeliveryDirector();
            String deliveryManager = pipelineState.getDeliveryManager();
            int financialYear = pipelineState.getFinancialYear();
            String quarter = pipelineState.getQuarter();

            Map<String, RevDashboardDTO> managerMap = directorMap.get(director);

            String managerKey = director + "_" + financialYear + "_" + quarter;
            managerMap.putIfAbsent(managerKey, new RevDashboardDTO(director, new ArrayList<>()));
            RevDashboardDTO revDashboardDTO = managerMap.get(managerKey);

            // Add account names and financial years to the lists
            if (!revDashboardDTO.getAccountNames().contains(account)) {
                revDashboardDTO.getAccountNames().add(account);
            }
            if (!revDashboardDTO.getFinancialYears().contains(financialYear)) {
                revDashboardDTO.getFinancialYears().add(financialYear);
            }

            Optional<RevDashboardDTO.AccountData> optionalAccountData = revDashboardDTO.getAccounts().stream()
                    .filter(a -> a.getFinancialYear() == financialYear && a.getQuarter().equals(quarter))
                    .findFirst();

            if (optionalAccountData.isPresent()) {
                RevDashboardDTO.AccountData accountData = optionalAccountData.get();
                accountData.getPipelineStatus().setSumOfPipeline_pitch(accountData.getPipelineStatus().getSumOfPipeline_pitch() + pipelineState.getSumOfPipeline_pitch());
                accountData.getPipelineStatus().setSumOfPipeline_opportunity(accountData.getPipelineStatus().getSumOfPipeline_opportunity() + pipelineState.getSumOfPipeline_opportunity());
                accountData.getPipelineStatus().setSumOfPipeline_total(accountData.getPipelineStatus().getSumOfPipeline_total() + pipelineState.getSumOfPipeline_total());
                accountData.getPipelineStatus().setSumOfPipeline_shaping(accountData.getPipelineStatus().getSumOfPipeline_shaping() + pipelineState.getSumOfPipeline_shaping());
            }

            if (!revDashboardDTO.getDeliveryManagers().contains(deliveryManager)) {
                revDashboardDTO.getDeliveryManagers().add(deliveryManager);
            }
        }
    }





    @PostMapping("/getByRoleAndName")
    public ResponseEntity<RevDashboardData> getByRoleAndName(@RequestBody RoleAndNameRequest request) {
        List<String> roles = request.getRole();
        String name = request.getName();

        // Fetch matching user
        String[] nameParts = name.split(" ");
        if (nameParts.length != 2) {
            return ResponseEntity.badRequest().build();
        }
        String firstName = nameParts[0];
        String lastName = nameParts[1];
        String deliveryDirector = firstName + " " + lastName;

        // Construct response object
        RevDashboardData response = new RevDashboardData();
        response.setDeliveryDirector(deliveryDirector);

        // Fetch delivery managers, account names, and financial years
        Set<String> deliveryManagers = new HashSet<>();
        Set<String> accountsNames = new HashSet<>();
        Set<Integer> financialYears = new TreeSet<>(Collections.reverseOrder()); // Sorted in descending order
        Map<String, RevDashboardData.AccountData> aggregatedAccounts = new LinkedHashMap<>(); // To keep insertion order

        // Process each role separately
        for (String role : roles) {
            // Fetch matching role
            MD_Role mdRole = mdRolesRepository.findFirstByRole(role);
            if (mdRole == null) {
                return ResponseEntity.badRequest().build();
            }

            // Fetch matching records from RevenueBudgetSummary
            List<RevenueBudgetSummary> revenueBudgetSummaries = revenueBudgetSummaryRepository.findByDeliveryDirector(deliveryDirector);

            // Fetch matching records from RevenueGrowthSummary
            List<RevenueGrowthSummary> revenueGrowthSummaries = revenueGrowthSummaryRepository.findByDeliveryDirector(deliveryDirector);

            // Fetch matching records from PipelineState
            List<PipelineState> pipelineStates = pipelineStateRepository.findByDeliveryDirector(deliveryDirector);

            // Collect delivery managers, account names, and financial years
            deliveryManagers.addAll(revenueBudgetSummaries.stream()
                    .map(RevenueBudgetSummary::getDeliveryManager)
                    .collect(Collectors.toSet()));

            accountsNames.addAll(revenueBudgetSummaries.stream()
                    .map(RevenueBudgetSummary::getAccount)
                    .collect(Collectors.toSet()));

            financialYears.addAll(revenueBudgetSummaries.stream()
                    .map(RevenueBudgetSummary::getFinancialYear)
                    .collect(Collectors.toSet()));
        }

        // Restrict to the latest two financial years
        List<Integer> latestTwoFinancialYears = financialYears.stream()
                .limit(2)
                .collect(Collectors.toList());

        // Initialize accounts map for aggregation
        for (Integer year : latestTwoFinancialYears) {
            for (String quarter : Arrays.asList("Q1", "Q2", "Q3", "Q4")) {
                String key = year + "-" + quarter;
                aggregatedAccounts.putIfAbsent(key, new RevDashboardData.AccountData());
                RevDashboardData.AccountData accountData = aggregatedAccounts.get(key);
                accountData.setAccount("all");
                accountData.setFinancialYear(year);
                accountData.setQuarter(quarter);
            }
        }

        // Process each record for aggregation
        for (String role : roles) {
            // Fetch matching role
            MD_Role mdRole = mdRolesRepository.findFirstByRole(role);
            if (mdRole == null) {
                continue;
            }

            // Fetch matching records from RevenueBudgetSummary
            List<RevenueBudgetSummary> revenueBudgetSummaries = revenueBudgetSummaryRepository.findByDeliveryDirector(deliveryDirector);

            // Fetch matching records from RevenueGrowthSummary
            List<RevenueGrowthSummary> revenueGrowthSummaries = revenueGrowthSummaryRepository.findByDeliveryDirector(deliveryDirector);

            // Fetch matching records from PipelineState
            List<PipelineState> pipelineStates = pipelineStateRepository.findByDeliveryDirector(deliveryDirector);

            // Process each record for aggregation
            for (RevenueBudgetSummary budgetSummary : revenueBudgetSummaries) {
                if (!latestTwoFinancialYears.contains(budgetSummary.getFinancialYear())) {
                    continue;
                }

                String key = budgetSummary.getFinancialYear() + "-" + budgetSummary.getQuarter();
                RevDashboardData.AccountData accountData = aggregatedAccounts.get(key);
                if (accountData == null) {
                    continue;
                }

                RevDashboardData.RevenueBudget revenueBudget = accountData.getRevenueBudget();
                if (revenueBudget == null) {
                    revenueBudget = new RevDashboardData.RevenueBudget();
                    accountData.setRevenueBudget(revenueBudget);
                }
                revenueBudget.setBudget(formatDouble(revenueBudget.getBudget() + budgetSummary.getBudget()));
                revenueBudget.setForecast(formatDouble(revenueBudget.getForecast() + budgetSummary.getForecast()));
                revenueBudget.setGap(formatDouble(revenueBudget.getGap() + budgetSummary.getGap()));

                // Find matching revenue growth summary
                RevenueGrowthSummary matchingGrowthSummary = revenueGrowthSummaries.stream()
                        .filter(g -> g.getAccount().equals(budgetSummary.getAccount()) &&
                                g.getQuarter().equals(budgetSummary.getQuarter()) &&
                                g.getFinancialYear() == budgetSummary.getFinancialYear())
                        .findFirst()
                        .orElse(null);
                if (matchingGrowthSummary != null) {
                    RevDashboardData.RevenueGrowth revenueGrowth = accountData.getRevenueGrowth();
                    if (revenueGrowth == null) {
                        revenueGrowth = new RevDashboardData.RevenueGrowth();
                        accountData.setRevenueGrowth(revenueGrowth);
                    }
                    revenueGrowth.setAccountExpected(formatDouble(revenueGrowth.getAccountExpected() + matchingGrowthSummary.getAccountExpected()));
                    revenueGrowth.setForecast(formatDouble(revenueGrowth.getForecast() + matchingGrowthSummary.getForecast()));
                    revenueGrowth.setGap(formatDouble(revenueGrowth.getGap() + matchingGrowthSummary.getGap()));
                }

                // Find matching pipeline state
                PipelineState matchingPipelineState = pipelineStates.stream()
                        .filter(p -> p.getAccount().equals(budgetSummary.getAccount()) &&
                                p.getQuarter().equals(budgetSummary.getQuarter()) &&
                                p.getFinancialYear() == budgetSummary.getFinancialYear())
                        .findFirst()
                        .orElse(null);
                if (matchingPipelineState != null) {
                    RevDashboardData.PipelineState pipelineState = accountData.getPipelineState();
                    if (pipelineState == null) {
                        pipelineState = new RevDashboardData.PipelineState();
                        accountData.setPipelineState(pipelineState);
                    }
                    pipelineState.setSumOfPipeline_pitch(formatDouble(pipelineState.getSumOfPipeline_pitch() + matchingPipelineState.getSumOfPipeline_pitch()));
                    pipelineState.setSumOfPipeline_opportunity(formatDouble(pipelineState.getSumOfPipeline_opportunity() + matchingPipelineState.getSumOfPipeline_opportunity()));
                    pipelineState.setSumOfPipeline_shaping(formatDouble(pipelineState.getSumOfPipeline_shaping() + matchingPipelineState.getSumOfPipeline_shaping()));
                    pipelineState.setSumOfPipeline_total(formatDouble(pipelineState.getSumOfPipeline_total() + matchingPipelineState.getSumOfPipeline_total()));
                }
            }
        }

        // Prepare response list
        List<RevDashboardData.AccountData> responseAccounts = new ArrayList<>(aggregatedAccounts.values());
        responseAccounts.sort(Comparator.comparing(RevDashboardData.AccountData::getFinancialYear)
                .thenComparing(RevDashboardData.AccountData::getQuarter));

        // Ensure only the latest 2 financial years with 4 quarters each
        if (responseAccounts.size() > 8) {
            responseAccounts = responseAccounts.subList(0, 8);
        }

        // Set collected delivery managers, account names, and financial years
        response.setDeliveryManager(new ArrayList<>(deliveryManagers));
        response.setAccountsNames(new ArrayList<>(accountsNames));
        response.setFinancialYears(latestTwoFinancialYears);
        response.setAccounts(responseAccounts);

        // Return the RevDashboardData directly
        return ResponseEntity.ok(response);
    }

    // Helper method to format double values to two decimal places
    private double formatDouble(double value) {
        return Math.round(value * 100.0) / 100.0;
    }









    @GetMapping("/getAllData")
    public ResponseEntity<RevenueDashboardData> getDashboardData() {
        Integer currentYear = revenueBudgetSummaryRepository.findMaxFinancialYear();
        if (currentYear == null) {
            return ResponseEntity.noContent().build();
        }
        Integer previousYear = currentYear - 1;

        List<RevenueBudgetSummary> revenueBudgets = revenueBudgetSummaryRepository.findByFinancialYearIn(List.of(currentYear, previousYear));
        List<RevenueGrowthSummary> revenueGrowths = revenueGrowthSummaryRepository.findByFinancialYearIn(List.of(currentYear, previousYear));
        List<PipelineState> pipelineStates = pipelineStateRepository.findByFinancialYearIn(List.of(currentYear, previousYear));

        Map<String, RevenueDashboardData.AccountData> accountDataMap = new HashMap<>();

        revenueBudgets.forEach(revenueBudget -> {
            String key = revenueBudget.getFinancialYear() + "-" + revenueBudget.getQuarter();

            RevenueDashboardData.AccountData accountData = accountDataMap.getOrDefault(key, new RevenueDashboardData.AccountData());
            accountData.setAccount("all");
            accountData.setFinancialYear(revenueBudget.getFinancialYear());
            accountData.setQuarter(revenueBudget.getQuarter());

            RevenueDashboardData.RevenueBudget revenueBudgetData = accountData.getRevenueBudget();
            if (revenueBudgetData == null) {
                revenueBudgetData = new RevenueDashboardData.RevenueBudget();
            }
            revenueBudgetData.setBudget(revenueBudgetData.getBudget() + revenueBudget.getBudget());
            revenueBudgetData.setForecast(revenueBudgetData.getForecast() + revenueBudget.getForecast());
            revenueBudgetData.setGap(revenueBudgetData.getGap() + revenueBudget.getGap());
            accountData.setRevenueBudget(revenueBudgetData);

            accountDataMap.put(key, accountData);
        });

        revenueGrowths.forEach(revenueGrowth -> {
            String key = revenueGrowth.getFinancialYear() + "-" + revenueGrowth.getQuarter();

            RevenueDashboardData.AccountData accountData = accountDataMap.getOrDefault(key, new RevenueDashboardData.AccountData());
            accountData.setAccount("all");
            accountData.setFinancialYear(revenueGrowth.getFinancialYear());
            accountData.setQuarter(revenueGrowth.getQuarter());

            RevenueDashboardData.RevenueGrowth revenueGrowthData = accountData.getRevenueGrowth();
            if (revenueGrowthData == null) {
                revenueGrowthData = new RevenueDashboardData.RevenueGrowth();
            }
            revenueGrowthData.setAccountExpected(revenueGrowthData.getAccountExpected() + revenueGrowth.getAccountExpected());
            revenueGrowthData.setForecast(revenueGrowthData.getForecast() + revenueGrowth.getForecast());
            revenueGrowthData.setGap(revenueGrowthData.getGap() + revenueGrowth.getGap());
            accountData.setRevenueGrowth(revenueGrowthData);

            accountDataMap.put(key, accountData);
        });

        pipelineStates.forEach(pipelineState -> {
            String key = pipelineState.getFinancialYear() + "-" + pipelineState.getQuarter();

            RevenueDashboardData.AccountData accountData = accountDataMap.getOrDefault(key, new RevenueDashboardData.AccountData());
            accountData.setAccount("all");
            accountData.setFinancialYear(pipelineState.getFinancialYear());
            accountData.setQuarter(pipelineState.getQuarter());

            RevenueDashboardData.PipelineState pipelineStateData = accountData.getPipelineState();
            if (pipelineStateData == null) {
                pipelineStateData = new RevenueDashboardData.PipelineState();
            }
            pipelineStateData.setSumOfPipeline_pitch(pipelineStateData.getSumOfPipeline_pitch() + pipelineState.getSumOfPipeline_pitch());
            pipelineStateData.setSumOfPipeline_opportunity(pipelineStateData.getSumOfPipeline_opportunity() + pipelineState.getSumOfPipeline_opportunity());
            pipelineStateData.setSumOfPipeline_shaping(pipelineStateData.getSumOfPipeline_shaping() + pipelineState.getSumOfPipeline_shaping());
            pipelineStateData.setSumOfPipeline_total(pipelineStateData.getSumOfPipeline_total() + pipelineState.getSumOfPipeline_total());
            accountData.setPipelineState(pipelineStateData);

            accountDataMap.put(key, accountData);
        });

        // Convert map values to list and sort it
        List<RevenueDashboardData.AccountData> accounts = new ArrayList<>(accountDataMap.values());
        accounts.sort((a, b) -> {
            // Prioritize current year over previous year
            int yearComparison = b.getFinancialYear() - a.getFinancialYear();
            if (yearComparison != 0) {
                return yearComparison;
            }
            // If years are the same, compare quarters
            return a.getQuarter().compareTo(b.getQuarter());
        });

        RevenueDashboardData dashboardData = new RevenueDashboardData();
        dashboardData.setDeliveryDirector("all");
        dashboardData.setDeliveryManager("all");
        dashboardData.setAccounts(accounts);

        return ResponseEntity.ok(dashboardData);
    }

}


