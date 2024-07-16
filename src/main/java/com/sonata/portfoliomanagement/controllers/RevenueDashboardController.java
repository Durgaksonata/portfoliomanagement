package com.sonata.portfoliomanagement.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sonata.portfoliomanagement.interfaces.*;
import com.sonata.portfoliomanagement.model.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        int currentFinancialYear = getCurrentFinancialYear();

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




    @PostMapping("/getRevenueDashboardByDirectors")
    public ResponseEntity<List<RevDashboardDTO>> getRevenueDashboardByDirectors(@RequestBody List<String> deliveryDirectors) {
        // Iterate through the list of delivery directors
        List<RevDashboardDTO> result = new ArrayList<>();

        for (String deliveryDirector : deliveryDirectors) {
            logger.info("Fetching data for Delivery Director: {}", deliveryDirector);

            List<RevenueBudgetSummary> revenueBudgetSummaries = revenueBudgetSummaryRepository.findByDeliveryDirector(deliveryDirector);
            List<RevenueGrowthSummary> revenueGrowthSummaries = revenueGrowthSummaryRepository.findByDeliveryDirector(deliveryDirector);
            List<PipelineState> pipelineStates = pipelineStateRepository.findByDeliveryDirector(deliveryDirector);

            logger.info("Revenue Budget Summaries for {}: {}", deliveryDirector, revenueBudgetSummaries);
            logger.info("Revenue Growth Summaries for {}: {}", deliveryDirector, revenueGrowthSummaries);
            logger.info("Pipeline States for {}: {}", deliveryDirector, pipelineStates);

            // Map to store aggregated data
            Map<String, RevDashboardDTO> directorManagerMap = new HashMap<>();

            // Aggregate RevenueBudgetSummary data
            for (RevenueBudgetSummary revenueBudget : revenueBudgetSummaries) {
                String directorManagerKey = generateDirectorManagerKey(revenueBudget.getDeliveryDirector(), revenueBudget.getDeliveryManager());
                logger.info("Processing RevenueBudgetSummary for key: {}", directorManagerKey);

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
                logger.info("Processing RevenueGrowthSummary for key: {}", directorManagerKey);

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
                logger.info("Processing PipelineState for key: {}", directorManagerKey);

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

            // Add aggregated data to result list
            result.addAll(directorManagerMap.values());
        }

        return ResponseEntity.ok(result);
    }

    private String generateDirectorManagerKey11(String deliveryDirector, String deliveryManager) {
        return deliveryDirector + "_" + deliveryManager;
    }

    private RevDashboardDTO.AccountData findOrCreateAccountData11(List<RevDashboardDTO.AccountData> accountDataList, String account, int financialYear, String quarter) {
        for (RevDashboardDTO.AccountData accountData : accountDataList) {
            if (accountData.getAccount().equals(account) && accountData.getFinancialYear() == financialYear && accountData.getQuarter().equals(quarter)) {
                return accountData;
            }
        }

        RevDashboardDTO.AccountData newAccountData = new RevDashboardDTO.AccountData(account, financialYear, quarter, null, null, null);
        accountDataList.add(newAccountData);
        return newAccountData;
    }


    @PostMapping("/getRevenueDashboardManager")
    public ResponseEntity<List<RevDashboardDTO>> getRevenueDashboardByManager(@RequestBody List<String> deliveryManagers) {
        List<RevDashboardDTO> result = new ArrayList<>();

        for (String deliveryManager : deliveryManagers) {
            logger.info("Fetching data for Delivery Manager: {}", deliveryManager);

            List<RevenueBudgetSummary> revenueBudgetSummaries = revenueBudgetSummaryRepository.findByDeliveryManager(deliveryManager);
            List<RevenueGrowthSummary> revenueGrowthSummaries = revenueGrowthSummaryRepository.findByDeliveryManager(deliveryManager);
            List<PipelineState> pipelineStates = pipelineStateRepository.findByDeliveryManager(deliveryManager);

            // Aggregate data for delivery manager
            Map<String, RevDashboardDTO> directorManagerMap = aggregateData(revenueBudgetSummaries, revenueGrowthSummaries, pipelineStates);

            // Add aggregated data to result list
            result.addAll(directorManagerMap.values());
        }

        return ResponseEntity.ok(result);
    }

    private Map<String, RevDashboardDTO> aggregateData(List<RevenueBudgetSummary> revenueBudgetSummaries,
                                                       List<RevenueGrowthSummary> revenueGrowthSummaries,
                                                       List<PipelineState> pipelineStates) {
        Map<String, RevDashboardDTO> directorManagerMap = new HashMap<>();

        // Aggregate RevenueBudgetSummary data
        for (RevenueBudgetSummary revenueBudget : revenueBudgetSummaries) {
            String directorManagerKey = generateDirectorManagerKey(revenueBudget.getDeliveryDirector(), revenueBudget.getDeliveryManager());
            logger.info("Processing RevenueBudgetSummary for key: {}", directorManagerKey);

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
            logger.info("Processing RevenueGrowthSummary for key: {}", directorManagerKey);

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
            logger.info("Processing PipelineState for key: {}", directorManagerKey);

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

        return directorManagerMap;
    }

    private String generateDirectorManagerKey2(String deliveryDirector, String deliveryManager) {
        return deliveryDirector + "_" + deliveryManager;
    }

    private RevDashboardDTO.AccountData findOrCreateAccountData2(List<RevDashboardDTO.AccountData> accountDataList, String account, int financialYear, String quarter) {
        for (RevDashboardDTO.AccountData accountData : accountDataList) {
            if (accountData.getAccount().equals(account) && accountData.getFinancialYear() == financialYear && accountData.getQuarter().equals(quarter)) {
                return accountData;
            }
        }

        RevDashboardDTO.AccountData newAccountData = new RevDashboardDTO.AccountData(account, financialYear, quarter, null, null, null);
        accountDataList.add(newAccountData);
        return newAccountData;
    }

    @PostMapping("/getRevenueDashboardByaccount")
    public ResponseEntity<List<RevDashboardDTO>> getRevenueDashboardByAccount(@RequestBody List<String> accounts) {
        // Iterate through the list of accounts
        List<RevDashboardDTO> result = new ArrayList<>();

        for (String account : accounts) {
            logger.info("Fetching data for Account: {}", account);

            List<RevenueBudgetSummary> revenueBudgetSummaries = revenueBudgetSummaryRepository.findByAccount(account);
            List<RevenueGrowthSummary> revenueGrowthSummaries = revenueGrowthSummaryRepository.findByAccount(account);
            List<PipelineState> pipelineStates = pipelineStateRepository.findByAccount(account);

            logger.info("Revenue Budget Summaries: {}", revenueBudgetSummaries);
            logger.info("Revenue Growth Summaries: {}", revenueGrowthSummaries);
            logger.info("Pipeline States: {}", pipelineStates);

            // Map to store aggregated data
            Map<String, RevDashboardDTO> directorManagerMap = new HashMap<>();

            // Aggregate RevenueBudgetSummary data
            for (RevenueBudgetSummary revenueBudget : revenueBudgetSummaries) {
                String directorManagerKey = generateDirectorManagerKey11(revenueBudget.getDeliveryDirector(), revenueBudget.getDeliveryManager());
                logger.info("Processing RevenueBudgetSummary for key: {}", directorManagerKey);

                if (!directorManagerMap.containsKey(directorManagerKey)) {
                    directorManagerMap.put(directorManagerKey, new RevDashboardDTO(
                            revenueBudget.getDeliveryDirector(),
                            revenueBudget.getDeliveryManager(),
                            new ArrayList<>()
                    ));
                }

                RevDashboardDTO dto = directorManagerMap.get(directorManagerKey);
                RevDashboardDTO.AccountData accountData = findOrCreateAccountData1(dto.getAccounts(), revenueBudget.getAccount(),
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
                String directorManagerKey = generateDirectorManagerKey11(revenueGrowth.getDeliveryDirector(), revenueGrowth.getDeliveryManager());
                logger.info("Processing RevenueGrowthSummary for key: {}", directorManagerKey);

                if (!directorManagerMap.containsKey(directorManagerKey)) {
                    directorManagerMap.put(directorManagerKey, new RevDashboardDTO(
                            revenueGrowth.getDeliveryDirector(),
                            revenueGrowth.getDeliveryManager(),
                            new ArrayList<>()
                    ));
                }

                RevDashboardDTO dto = directorManagerMap.get(directorManagerKey);
                RevDashboardDTO.AccountData accountData = findOrCreateAccountData1(dto.getAccounts(), revenueGrowth.getAccount(),
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
                String directorManagerKey = generateDirectorManagerKey11(pipelineState.getDeliveryDirector(), pipelineState.getDeliveryManager());
                logger.info("Processing PipelineState for key: {}", directorManagerKey);

                if (!directorManagerMap.containsKey(directorManagerKey)) {
                    directorManagerMap.put(directorManagerKey, new RevDashboardDTO(
                            pipelineState.getDeliveryDirector(),
                            pipelineState.getDeliveryManager(),
                            new ArrayList<>()
                    ));
                }

                RevDashboardDTO dto = directorManagerMap.get(directorManagerKey);
                RevDashboardDTO.AccountData accountData = findOrCreateAccountData1(dto.getAccounts(), pipelineState.getAccount(),
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

            // Add aggregated data to result list
            result.addAll(directorManagerMap.values());
        }

        return ResponseEntity.ok(result);
    }

    private String generateDirectorManagerKey1(String deliveryDirector, String deliveryManager) {
        return deliveryDirector + "_" + deliveryManager;
    }

    private RevDashboardDTO.AccountData findOrCreateAccountData1(List<RevDashboardDTO.AccountData> accountDataList, String account, int financialYear, String quarter) {
        for (RevDashboardDTO.AccountData accountData : accountDataList) {
            if (accountData.getAccount().equals(account) && accountData.getFinancialYear() == financialYear && accountData.getQuarter().equals(quarter)) {
                return accountData;
            }
        }

        RevDashboardDTO.AccountData newAccountData = new RevDashboardDTO.AccountData(account, financialYear, quarter, null, null, null);
        accountDataList.add(newAccountData);
        return newAccountData;
    }





    @PostMapping("/getByRoleAndName")
    public ResponseEntity<String> getByRoleAndName(@RequestBody RoleAndNameRequest request) {
        String role = request.getRole();
        String name = request.getName();

        // Fetch matching role
        MD_Role mdRole = mdRolesRepository.findFirstByRole(role);
        if (mdRole == null) {
            return ResponseEntity.badRequest().body("Invalid role");
        }

        // Fetch matching user
        String[] nameParts = name.split(" ");
        if (nameParts.length != 2) {
            return ResponseEntity.badRequest().body("Invalid name format");
        }
        String firstName = nameParts[0];
        String lastName = nameParts[1];
        MD_Users mdUser = mdUsersRepository.findFirstByFirstNameAndLastName(firstName, lastName);
        if (mdUser == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        String deliveryDirector = firstName + " " + lastName;

        // Fetch matching records from RevenueBudgetSummary
        List<RevenueBudgetSummary> revenueBudgetSummaries = revenueBudgetSummaryRepository.findByDeliveryDirector(deliveryDirector);

        // Fetch matching records from RevenueGrowthSummary
        List<RevenueGrowthSummary> revenueGrowthSummaries = revenueGrowthSummaryRepository.findByDeliveryDirector(deliveryDirector);

        // Fetch matching records from PipelineState
        List<PipelineState> pipelineStates = pipelineStateRepository.findByDeliveryDirector(deliveryDirector);

        // Construct response
        RevDashboardData response = new RevDashboardData();
        response.setDeliveryDirector(deliveryDirector);

        List<RevDashboardData.AccountData> accounts = new ArrayList<>();
        for (RevenueBudgetSummary budgetSummary : revenueBudgetSummaries) {
            RevDashboardData.AccountData accountData = new RevDashboardData.AccountData();
            accountData.setAccount(budgetSummary.getAccount());
            accountData.setFinancialYear(budgetSummary.getFinancialYear());
            accountData.setQuarter(budgetSummary.getQuarter());

            RevDashboardData.RevenueBudget revenueBudget = new RevDashboardData.RevenueBudget();
            revenueBudget.setBudget(budgetSummary.getBudget());
            revenueBudget.setForecast(budgetSummary.getForecast());
            revenueBudget.setGap(budgetSummary.getGap());
            accountData.setRevenueBudget(revenueBudget);

            accounts.add(accountData);

            // Set delivery manager from RevenueBudgetSummary if not already set
            if (response.getDeliveryManager() == null || response.getDeliveryManager().isEmpty()) {
                response.setDeliveryManager(budgetSummary.getDeliveryManager());
            }
        }

        for (RevenueGrowthSummary growthSummary : revenueGrowthSummaries) {
            for (RevDashboardData.AccountData accountData : accounts) {
                if (accountData.getAccount().equals(growthSummary.getAccount())) {
                    RevDashboardData.RevenueGrowth revenueGrowth = new RevDashboardData.RevenueGrowth();
                    revenueGrowth.setAccountExpected(growthSummary.getAccountExpected());
                    revenueGrowth.setForecast(growthSummary.getForecast());
                    revenueGrowth.setGap(growthSummary.getGap());
                    accountData.setRevenueGrowth(revenueGrowth);

                    // Set delivery manager from RevenueGrowthSummary if not already set
                    if (response.getDeliveryManager() == null || response.getDeliveryManager().isEmpty()) {
                        response.setDeliveryManager(growthSummary.getDeliveryManager());
                    }
                }
            }
        }

        for (PipelineState pipelineState : pipelineStates) {
            for (RevDashboardData.AccountData accountData : accounts) {
                if (accountData.getAccount().equals(pipelineState.getAccount())) {
                    RevDashboardData.PipelineState pipelineStatus = new RevDashboardData.PipelineState();
                    pipelineStatus.setSumOfPipeline_pitch(pipelineState.getSumOfPipeline_pitch());
                    pipelineStatus.setSumOfPipeline_opportunity(pipelineState.getSumOfPipeline_opportunity());
                    pipelineStatus.setSumOfPipeline_shaping(pipelineState.getSumOfPipeline_shaping());
                    pipelineStatus.setSumOfPipeline_total(pipelineState.getSumOfPipeline_total());

                    accountData.setPipelineState(pipelineStatus);
                }
            }
        }

        response.setAccounts(accounts);

        // Serialize the response to pretty-printed JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse;
        try {
            jsonResponse = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing response");
        }

        // Prepend the type declaration
        String fullResponse = "type RevDashboardData = " + jsonResponse;
        return ResponseEntity.ok(fullResponse);
    }








    @GetMapping("/getAllData")
    public ResponseEntity<Map<String, RevDashboardData>> getDashboardData() {
        Integer currentYear = revenueBudgetSummaryRepository.findMaxFinancialYear();
        if (currentYear == null) {
            return ResponseEntity.noContent().build();
        }
        Integer previousYear = currentYear - 1;

        List<RevenueBudgetSummary> revenueBudgets = revenueBudgetSummaryRepository.findByFinancialYearIn(List.of(currentYear, previousYear));
        List<RevenueGrowthSummary> revenueGrowths = revenueGrowthSummaryRepository.findByFinancialYearIn(List.of(currentYear, previousYear));
        List<PipelineState> pipelineStates = pipelineStateRepository.findByFinancialYearIn(List.of(currentYear, previousYear));

        Map<String, RevDashboardData.AccountData> accountDataMap = new HashMap<>();

        revenueBudgets.forEach(revenueBudget -> {
            String key = revenueBudget.getFinancialYear() + "-" + revenueBudget.getQuarter();

            RevDashboardData.AccountData accountData = accountDataMap.getOrDefault(key, new RevDashboardData.AccountData());
            accountData.setAccount("all");
            accountData.setFinancialYear(revenueBudget.getFinancialYear());
            accountData.setQuarter(revenueBudget.getQuarter());

            RevDashboardData.RevenueBudget revenueBudgetData = accountData.getRevenueBudget();
            if (revenueBudgetData == null) {
                revenueBudgetData = new RevDashboardData.RevenueBudget();
            }
            revenueBudgetData.setBudget(revenueBudgetData.getBudget() + revenueBudget.getBudget());
            revenueBudgetData.setForecast(revenueBudgetData.getForecast() + revenueBudget.getForecast());
            revenueBudgetData.setGap(revenueBudgetData.getGap() + revenueBudget.getGap());
            accountData.setRevenueBudget(revenueBudgetData);

            accountDataMap.put(key, accountData);
        });

        revenueGrowths.forEach(revenueGrowth -> {
            String key = revenueGrowth.getFinancialYear() + "-" + revenueGrowth.getQuarter();

            RevDashboardData.AccountData accountData = accountDataMap.getOrDefault(key, new RevDashboardData.AccountData());
            accountData.setAccount("all");
            accountData.setFinancialYear(revenueGrowth.getFinancialYear());
            accountData.setQuarter(revenueGrowth.getQuarter());

            RevDashboardData.RevenueGrowth revenueGrowthData = accountData.getRevenueGrowth();
            if (revenueGrowthData == null) {
                revenueGrowthData = new RevDashboardData.RevenueGrowth();
            }
            revenueGrowthData.setAccountExpected(revenueGrowthData.getAccountExpected() + revenueGrowth.getAccountExpected());
            revenueGrowthData.setForecast(revenueGrowthData.getForecast() + revenueGrowth.getForecast());
            revenueGrowthData.setGap(revenueGrowthData.getGap() + revenueGrowth.getGap());
            accountData.setRevenueGrowth(revenueGrowthData);

            accountDataMap.put(key, accountData);
        });

        pipelineStates.forEach(pipelineState -> {
            String key = pipelineState.getFinancialYear() + "-" + pipelineState.getQuarter();

            RevDashboardData.AccountData accountData = accountDataMap.getOrDefault(key, new RevDashboardData.AccountData());
            accountData.setAccount("all");
            accountData.setFinancialYear(pipelineState.getFinancialYear());
            accountData.setQuarter(pipelineState.getQuarter());

            RevDashboardData.PipelineState pipelineStateData = accountData.getPipelineState();
            if (pipelineStateData == null) {
                pipelineStateData = new RevDashboardData.PipelineState();
            }
            pipelineStateData.setSumOfPipeline_pitch(pipelineStateData.getSumOfPipeline_pitch() + pipelineState.getSumOfPipeline_pitch());
            pipelineStateData.setSumOfPipeline_opportunity(pipelineStateData.getSumOfPipeline_opportunity() + pipelineState.getSumOfPipeline_opportunity());
            pipelineStateData.setSumOfPipeline_shaping(pipelineStateData.getSumOfPipeline_shaping() + pipelineState.getSumOfPipeline_shaping());
            pipelineStateData.setSumOfPipeline_total(pipelineStateData.getSumOfPipeline_total() + pipelineState.getSumOfPipeline_total());
            accountData.setPipelineState(pipelineStateData);

            accountDataMap.put(key, accountData);
        });

        // Convert map values to list and sort it
        List<RevDashboardData.AccountData> accounts = new ArrayList<>(accountDataMap.values());
        accounts.sort((a, b) -> {
            // Prioritize current year over previous year
            int yearComparison = b.getFinancialYear() - a.getFinancialYear();
            if (yearComparison != 0) {
                return yearComparison;
            }
            // If years are the same, compare quarters
            return a.getQuarter().compareTo(b.getQuarter());
        });

        Map<String, RevDashboardData> response = new HashMap<>();
        RevDashboardData dashboardData = new RevDashboardData();
        dashboardData.setDeliveryDirector("all");
        dashboardData.setDeliveryManager("all");
        dashboardData.setAccounts(accounts);
        response.put("type RevDashboardData", dashboardData);

        return ResponseEntity.ok(response);
    }



}
