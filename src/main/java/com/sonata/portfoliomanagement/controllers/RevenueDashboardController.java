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

            if (ps.getAccount().equals(baseline.getAccount())) {
                baseline.setSumOfPipeline_opportunity(baseline.getSumOfPipeline_opportunity() + ps.getSumOfPipeline_opportunity());
                baseline.setSumOfPipeline_shaping(baseline.getSumOfPipeline_shaping() + ps.getSumOfPipeline_shaping());
                baseline.setSumOfPipeline_pitch(baseline.getSumOfPipeline_pitch() + ps.getSumOfPipeline_pitch());
            }
//            baseline.setSumOfPipeline_opportunity(baseline.getSumOfPipeline_opportunity() + ps.getSumOfPipeline_opportunity());
//            baseline.setSumOfPipeline_shaping(baseline.getSumOfPipeline_shaping() + ps.getSumOfPipeline_shaping());
//            baseline.setSumOfPipeline_pitch(baseline.getSumOfPipeline_pitch() + ps.getSumOfPipeline_pitch());
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

            RevDashboardDTO.PipelineState pipelineSummary = accountData.getpipelineState();
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
    public ResponseEntity<RevDashboardDTO> getRevenueDashboardByDirector(@RequestBody List<String> deliveryDirector) {
        Map<String, RevDashboardDTO> directorMap = new HashMap<>();

        // Fetch and aggregate data for each delivery director
        for (String deliveryDirectors : deliveryDirector) {
            aggregateDataForDirector(deliveryDirectors, directorMap);
        }

        // Prepare result list from directorMap values
        List<RevDashboardDTO> result = new ArrayList<>(directorMap.values());

        // Filter financial years to the latest two for each RevDashboardDTO
        result.forEach(dto -> {
            Set<Integer> allFinancialYears = new TreeSet<>(Comparator.reverseOrder()); // Sorted in descending order
            dto.getAccounts().forEach(account -> allFinancialYears.add(account.getFinancialYear()));

            List<Integer> latestTwoFinancialYears = allFinancialYears.stream()
                    .limit(2)
                    .collect(Collectors.toList());

            // Sort accounts by financial year (highest to lowest)
            dto.getAccounts().sort(Comparator.comparingInt(RevDashboardDTO.AccountData::getFinancialYear).reversed());

            // Filter out accounts with financial years not in the latest two
            List<RevDashboardDTO.AccountData> filteredAccounts = dto.getAccounts().stream()
                    .filter(account -> latestTwoFinancialYears.contains(account.getFinancialYear()))
                    .collect(Collectors.toList());

            dto.setAccounts(filteredAccounts);
            dto.setFinancialYears(latestTwoFinancialYears);
        });

        if (!result.isEmpty()) {
            return ResponseEntity.ok(result.get(0));
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    // Aggregate data (budget summaries, growth summaries, pipeline states) for a delivery director
    private void aggregateDataForDirector(String deliveryDirector, Map<String, RevDashboardDTO> directorMap) {
        List<RevenueBudgetSummary> revenueBudgetSummaries = revenueBudgetSummaryRepository.findByDeliveryDirector(deliveryDirector);
        List<RevenueGrowthSummary> revenueGrowthSummaries = revenueGrowthSummaryRepository.findByDeliveryDirector(deliveryDirector);
        List<PipelineState> pipelineStates = pipelineStateRepository.findByDeliveryDirector(deliveryDirector);

        // Aggregate data into directorMap
        aggregateBudgetSummariesByDirector(revenueBudgetSummaries, directorMap, deliveryDirector);
        aggregateGrowthSummariesByDirector(revenueGrowthSummaries, directorMap, deliveryDirector);
        aggregatePipelineStatesByDirector(pipelineStates, directorMap, deliveryDirector);
    }

    // Aggregate budget summaries by director
    private void aggregateBudgetSummariesByDirector(List<RevenueBudgetSummary> revenueBudgetSummaries,
                                                    Map<String, RevDashboardDTO> directorMap,
                                                    String deliveryDirector) {
        for (RevenueBudgetSummary budgetSummary : revenueBudgetSummaries) {
            int financialYear = budgetSummary.getFinancialYear();

            // Wrap the deliveryDirector in a List<String>
            List<String> deliveryDirectorList = new ArrayList<>();
            deliveryDirectorList.add(deliveryDirector);

            directorMap.putIfAbsent(deliveryDirector, new RevDashboardDTO(deliveryDirectorList));
            RevDashboardDTO revDashboardDTO = directorMap.get(deliveryDirector);

            // Add account names to the list if not already present
            if (!revDashboardDTO.getAccountsNames().contains(budgetSummary.getAccount())) {
                revDashboardDTO.getAccountsNames().add(budgetSummary.getAccount());
            }

            // Aggregate under 'all' account
            Optional<RevDashboardDTO.AccountData> optionalAccountData = revDashboardDTO.getAccounts().stream()
                    .filter(a -> a.getFinancialYear() == financialYear && a.getQuarter().equals(budgetSummary.getQuarter()) && a.getAccount().equals("all"))
                    .findFirst();

            RevDashboardDTO.AccountData accountData;
            if (optionalAccountData.isPresent()) {
                accountData = optionalAccountData.get();
                accountData.getRevenueBudget().setBudget(accountData.getRevenueBudget().getBudget() + budgetSummary.getBudget());
                accountData.getRevenueBudget().setForecast(accountData.getRevenueBudget().getForecast() + budgetSummary.getForecast());
                accountData.getRevenueBudget().setGap(accountData.getRevenueBudget().getGap() + budgetSummary.getGap());
            } else {
                accountData = new RevDashboardDTO.AccountData("all", financialYear, budgetSummary.getQuarter(),
                        new RevDashboardDTO.RevenueBudgetSummary(budgetSummary.getBudget(), budgetSummary.getForecast(), budgetSummary.getGap()),
                        new RevDashboardDTO.RevenueGrowthSummary(0, 0, 0),
                        new RevDashboardDTO.PipelineState(0, 0, 0, 0));
                revDashboardDTO.getAccounts().add(accountData);
            }

            // Update financial years and delivery managers
            if (!revDashboardDTO.getFinancialYears().contains(financialYear)) {
                revDashboardDTO.getFinancialYears().add(financialYear);
            }
            if (!revDashboardDTO.getDeliveryManager().contains(budgetSummary.getDeliveryManager())) {
                revDashboardDTO.getDeliveryManager().add(budgetSummary.getDeliveryManager());
            }
        }
    }

    // Aggregate growth summaries by director
    private void aggregateGrowthSummariesByDirector(List<RevenueGrowthSummary> revenueGrowthSummaries,
                                                    Map<String, RevDashboardDTO> directorMap,
                                                    String deliveryDirector) {
        for (RevenueGrowthSummary growthSummary : revenueGrowthSummaries) {
            int financialYear = growthSummary.getFinancialYear();
            // Wrap the deliveryDirector in a List<String>
            List<String> deliveryDirectorList = new ArrayList<>();
            deliveryDirectorList.add(deliveryDirector);

            directorMap.putIfAbsent(deliveryDirector, new RevDashboardDTO(deliveryDirectorList));
            RevDashboardDTO revDashboardDTO = directorMap.get(deliveryDirector);

            // Add account names to the list if not already present
            if (!revDashboardDTO.getAccountsNames().contains(growthSummary.getAccount())) {
                revDashboardDTO.getAccountsNames().add(growthSummary.getAccount());
            }

            // Aggregate under 'all' account
            Optional<RevDashboardDTO.AccountData> optionalAccountData = revDashboardDTO.getAccounts().stream()
                    .filter(a -> a.getFinancialYear() == financialYear && a.getQuarter().equals(growthSummary.getQuarter()) && a.getAccount().equals("all"))
                    .findFirst();

            RevDashboardDTO.AccountData accountData;
            if (optionalAccountData.isPresent()) {
                accountData = optionalAccountData.get();
                accountData.getRevenueGrowth().setAccountExpected(accountData.getRevenueGrowth().getAccountExpected() + growthSummary.getAccountExpected());
                accountData.getRevenueGrowth().setForecast(accountData.getRevenueGrowth().getForecast() + growthSummary.getForecast());
                accountData.getRevenueGrowth().setGap(accountData.getRevenueGrowth().getGap() + growthSummary.getGap());
            } else {
                accountData = new RevDashboardDTO.AccountData("all", financialYear, growthSummary.getQuarter(),
                        new RevDashboardDTO.RevenueBudgetSummary(0, 0, 0),
                        new RevDashboardDTO.RevenueGrowthSummary(growthSummary.getAccountExpected(), growthSummary.getForecast(), growthSummary.getGap()),
                        new RevDashboardDTO.PipelineState(0, 0, 0, 0));
                revDashboardDTO.getAccounts().add(accountData);
            }

            // Update financial years and delivery managers
            if (!revDashboardDTO.getFinancialYears().contains(financialYear)) {
                revDashboardDTO.getFinancialYears().add(financialYear);
            }
            if (!revDashboardDTO.getDeliveryManager().contains(growthSummary.getDeliveryManager())) {
                revDashboardDTO.getDeliveryManager().add(growthSummary.getDeliveryManager());
            }
        }
    }

    // Aggregate pipeline states by director
    private void aggregatePipelineStatesByDirector(List<PipelineState> pipelineStates,
                                                   Map<String, RevDashboardDTO> directorMap,
                                                   String deliveryDirector) {
        for (PipelineState pipelineState : pipelineStates) {
            int financialYear = pipelineState.getFinancialYear();

            // Wrap the deliveryDirector in a List<String>
            List<String> deliveryDirectorList = new ArrayList<>();
            deliveryDirectorList.add(deliveryDirector);

            directorMap.putIfAbsent(deliveryDirector, new RevDashboardDTO(deliveryDirectorList));
            RevDashboardDTO revDashboardDTO = directorMap.get(deliveryDirector);

            // Add account names to the list if not already present
            if (!revDashboardDTO.getAccountsNames().contains(pipelineState.getAccount())) {
                revDashboardDTO.getAccountsNames().add(pipelineState.getAccount());
            }

            // Aggregate under 'all' account
            Optional<RevDashboardDTO.AccountData> optionalAccountData = revDashboardDTO.getAccounts().stream()
                    .filter(a -> a.getFinancialYear() == financialYear && a.getQuarter().equals(pipelineState.getQuarter()) && a.getAccount().equals("all"))
                    .findFirst();

            RevDashboardDTO.AccountData accountData;
            if (optionalAccountData.isPresent()) {
                accountData = optionalAccountData.get();
                accountData.getpipelineState().setSumOfPipeline_pitch(accountData.getpipelineState().getSumOfPipeline_pitch() + pipelineState.getSumOfPipeline_pitch());
                accountData.getpipelineState().setSumOfPipeline_opportunity(accountData.getpipelineState().getSumOfPipeline_opportunity() + pipelineState.getSumOfPipeline_opportunity());
                accountData.getpipelineState().setSumOfPipeline_total(accountData.getpipelineState().getSumOfPipeline_total() + pipelineState.getSumOfPipeline_total());
                accountData.getpipelineState().setSumOfPipeline_shaping(accountData.getpipelineState().getSumOfPipeline_shaping() + pipelineState.getSumOfPipeline_shaping());
            } else {
                accountData = new RevDashboardDTO.AccountData("all", financialYear, pipelineState.getQuarter(),
                        new RevDashboardDTO.RevenueBudgetSummary(0, 0, 0),
                        new RevDashboardDTO.RevenueGrowthSummary(0, 0, 0),
                        new RevDashboardDTO.PipelineState(pipelineState.getSumOfPipeline_pitch(), pipelineState.getSumOfPipeline_opportunity(),
                                pipelineState.getSumOfPipeline_total(), pipelineState.getSumOfPipeline_shaping()));
                revDashboardDTO.getAccounts().add(accountData);
            }

            // Update financial years and delivery managers
            if (!revDashboardDTO.getFinancialYears().contains(financialYear)) {
                revDashboardDTO.getFinancialYears().add(financialYear);
            }
            if (!revDashboardDTO.getDeliveryManager().contains(pipelineState.getDeliveryManager())) {
                revDashboardDTO.getDeliveryManager().add(pipelineState.getDeliveryManager());
            }
        }
    }

    @PostMapping("/getRevenueDashboardByManager")
    public ResponseEntity<RevDashboardDTO1> getRevenueDashboardByManager(@RequestBody List<String> deliveryManager) {
        Map<String, RevDashboardDTO1> directorMap = new HashMap<>();

        // Determine the highest and previous year
        List<Integer> allYears = getAllYearsFromDatabase();
        int highestYear = Collections.max(allYears);
        int previousYear = highestYear - 1;
        List<Integer> financialYears = Arrays.asList(highestYear, previousYear);

        for (String deliveryManagers : deliveryManager) {
            logger.info("Fetching data for Delivery Manager: {}", deliveryManager);

            List<RevenueBudgetSummary> revenueBudgetSummaries = revenueBudgetSummaryRepository.findByDeliveryManagerAndFinancialYears(deliveryManagers, financialYears);
            List<RevenueGrowthSummary> revenueGrowthSummaries = revenueGrowthSummaryRepository.findByDeliveryManagerAndFinancialYears(deliveryManagers, financialYears);
            List<PipelineState> pipelineStates = pipelineStateRepository.findByDeliveryManagerAndFinancialYears(deliveryManagers, financialYears);

            aggregateData(revenueBudgetSummaries, directorMap, deliveryManagers, true);
            aggregateData(revenueGrowthSummaries, directorMap, deliveryManagers, false);
            aggregateData(pipelineStates, directorMap, deliveryManagers, false);
        }

        // Aggregate data into a single RevDashboardDTO1
        RevDashboardDTO1 consolidatedDTO = new RevDashboardDTO1(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        List<RevDashboardDTO1> result = new ArrayList<>(directorMap.values());

        Set<String> allDeliveryDirectors = new HashSet<>();
        Set<String> allDeliveryManagers = new HashSet<>();
        Set<String> allAccountsNames = new HashSet<>();
        Set<Integer> allFinancialYears = new HashSet<>();

        result.forEach(dto -> {
            allDeliveryDirectors.addAll(dto.getDeliveryDirector());
            allDeliveryManagers.addAll(dto.getDeliveryManager());
            allFinancialYears.addAll(dto.getFinancialYears());

            // Add account names to the list
            addAccountNames(allAccountsNames, dto.getAccounts());

            // Combine accounts data
            consolidatedDTO.getAccounts().addAll(dto.getAccounts());
        });

        // Debugging logs
        logger.debug("Delivery Director: {}", allDeliveryDirectors);
        logger.debug("Delivery Manager: {}", allDeliveryManagers);
        logger.debug("Financial Years: {}", allFinancialYears);
        logger.debug("Accounts Names: {}", allAccountsNames);

        consolidatedDTO.setDeliveryDirector(new ArrayList<>(allDeliveryDirectors));
        consolidatedDTO.setDeliveryManager(new ArrayList<>(allDeliveryManagers));
        consolidatedDTO.setAccountsNames(new ArrayList<>(allAccountsNames));


// Sort financial years in descending order
        List<Integer> sortedFinancialYears = new ArrayList<>(allFinancialYears);
        sortedFinancialYears.sort(Collections.reverseOrder());  // Sorting in descending order
        consolidatedDTO.setFinancialYears(sortedFinancialYears);

        // Combine accounts under "all" and consolidate data
        consolidateAccounts(consolidatedDTO);

        // Sort accounts in the desired order
        consolidatedDTO.getAccounts().sort(Comparator.comparingInt(RevDashboardDTO1.AccountData::getFinancialYear).reversed()
                .thenComparing(RevDashboardDTO1.AccountData::getQuarter));

        return ResponseEntity.ok(consolidatedDTO);
    }

    private List<Integer> getAllYearsFromDatabase() {
        return revenueBudgetSummaryRepository.findDistinctFinancialYears();
    }

    private void aggregateData(List<?> dataList, Map<String, RevDashboardDTO1> directorMap, String deliveryManager, boolean isBudget) {
        for (Object data : dataList) {
            String director;
            int financialYear;
            String quarter;
            String account;

            if (data instanceof RevenueBudgetSummary) {
                RevenueBudgetSummary budgetSummary = (RevenueBudgetSummary) data;
                director = budgetSummary.getDeliveryDirector();
                financialYear = budgetSummary.getFinancialYear();
                quarter = budgetSummary.getQuarter();
                account = budgetSummary.getAccount(); // Set the actual account name
            } else if (data instanceof RevenueGrowthSummary) {
                RevenueGrowthSummary growthSummary = (RevenueGrowthSummary) data;
                director = growthSummary.getDeliveryDirector();
                financialYear = growthSummary.getFinancialYear();
                quarter = growthSummary.getQuarter();
                account = growthSummary.getAccount(); // Set the actual account name
            } else { // PipelineState
                PipelineState pipelineState = (PipelineState) data;
                director = pipelineState.getDeliveryDirector();
                financialYear = pipelineState.getFinancialYear();
                quarter = pipelineState.getQuarter();
                account = pipelineState.getAccount(); // Set the actual account name
            }

            directorMap.putIfAbsent(director, new RevDashboardDTO1(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
            RevDashboardDTO1 revDashboardDTO1 = directorMap.get(director);

            // Add delivery manager and director to their lists
            if (!revDashboardDTO1.getDeliveryDirector().contains(director)) {
                revDashboardDTO1.getDeliveryDirector().add(director);
            }
            if (!revDashboardDTO1.getDeliveryManager().contains(deliveryManager)) {
                revDashboardDTO1.getDeliveryManager().add(deliveryManager);
            }

            // Add account names and financial years to the lists
            if (!revDashboardDTO1.getAccountsNames().contains(account)) {
                revDashboardDTO1.getAccountsNames().add(account);
            }
            if (!revDashboardDTO1.getFinancialYears().contains(financialYear)) {
                revDashboardDTO1.getFinancialYears().add(financialYear);
            }

            Optional<RevDashboardDTO1.AccountData> optionalAccountData = revDashboardDTO1.getAccounts().stream()
                    .filter(a -> a.getFinancialYear() == financialYear && a.getQuarter().equals(quarter))
                    .findFirst();

            if (optionalAccountData.isPresent()) {
                RevDashboardDTO1.AccountData accountData = optionalAccountData.get();
                if (isBudget) {
                    accountData.getRevenueBudget().setBudget(roundToTwoDecimalPlaces(accountData.getRevenueBudget().getBudget() + ((RevenueBudgetSummary) data).getBudget()));
                    accountData.getRevenueBudget().setForecast(roundToTwoDecimalPlaces(accountData.getRevenueBudget().getForecast() + ((RevenueBudgetSummary) data).getForecast()));
                    accountData.getRevenueBudget().setGap(roundToTwoDecimalPlaces(accountData.getRevenueBudget().getGap() + ((RevenueBudgetSummary) data).getGap()));
                } else {
                    if (data instanceof RevenueGrowthSummary) {
                        accountData.getRevenueGrowth().setAccountExpected(roundToTwoDecimalPlaces(accountData.getRevenueGrowth().getAccountExpected() + ((RevenueGrowthSummary) data).getAccountExpected()));
                        accountData.getRevenueGrowth().setForecast(roundToTwoDecimalPlaces(accountData.getRevenueGrowth().getForecast() + ((RevenueGrowthSummary) data).getForecast()));
                        accountData.getRevenueGrowth().setGap(roundToTwoDecimalPlaces(accountData.getRevenueGrowth().getGap() + ((RevenueGrowthSummary) data).getGap()));
                    } else { // PipelineState
                        accountData.getPipelineState().setSumOfPipeline_pitch(roundToTwoDecimalPlaces(accountData.getPipelineState().getSumOfPipeline_pitch() + ((PipelineState) data).getSumOfPipeline_pitch()));
                        accountData.getPipelineState().setSumOfPipeline_opportunity(roundToTwoDecimalPlaces(accountData.getPipelineState().getSumOfPipeline_opportunity() + ((PipelineState) data).getSumOfPipeline_opportunity()));
                        accountData.getPipelineState().setSumOfPipeline_total(roundToTwoDecimalPlaces(accountData.getPipelineState().getSumOfPipeline_total() + ((PipelineState) data).getSumOfPipeline_total()));
                        accountData.getPipelineState().setSumOfPipeline_shaping(roundToTwoDecimalPlaces(accountData.getPipelineState().getSumOfPipeline_shaping() + ((PipelineState) data).getSumOfPipeline_shaping()));
                    }
                }
            } else {
                RevDashboardDTO1.AccountData accountData;
                if (isBudget) {
                    accountData = new RevDashboardDTO1.AccountData(account, financialYear, quarter,
                            new RevDashboardDTO1.RevenueBudgetSummary(
                                    roundToTwoDecimalPlaces(((RevenueBudgetSummary) data).getBudget()),
                                    roundToTwoDecimalPlaces(((RevenueBudgetSummary) data).getForecast()),
                                    roundToTwoDecimalPlaces(((RevenueBudgetSummary) data).getGap())),
                            new RevDashboardDTO1.RevenueGrowthSummary(0, 0, 0),
                            new RevDashboardDTO1.PipelineState(0, 0, 0, 0));
                } else {
                    if (data instanceof RevenueGrowthSummary) {
                        accountData = new RevDashboardDTO1.AccountData(account, financialYear, quarter,
                                new RevDashboardDTO1.RevenueBudgetSummary(0, 0, 0),
                                new RevDashboardDTO1.RevenueGrowthSummary(
                                        roundToTwoDecimalPlaces(((RevenueGrowthSummary) data).getAccountExpected()),
                                        roundToTwoDecimalPlaces(((RevenueGrowthSummary) data).getForecast()),
                                        roundToTwoDecimalPlaces(((RevenueGrowthSummary) data).getGap())),
                                new RevDashboardDTO1.PipelineState(0, 0, 0, 0));
                    } else { // PipelineState
                        accountData = new RevDashboardDTO1.AccountData(account, financialYear, quarter,
                                new RevDashboardDTO1.RevenueBudgetSummary(0, 0, 0),
                                new RevDashboardDTO1.RevenueGrowthSummary(0, 0, 0),
                                new RevDashboardDTO1.PipelineState(
                                        roundToTwoDecimalPlaces(((PipelineState) data).getSumOfPipeline_pitch()),
                                        roundToTwoDecimalPlaces(((PipelineState) data).getSumOfPipeline_opportunity()),
                                        roundToTwoDecimalPlaces(((PipelineState) data).getSumOfPipeline_total()),
                                        roundToTwoDecimalPlaces(((PipelineState) data).getSumOfPipeline_shaping())));
                    }
                }
                revDashboardDTO1.getAccounts().add(accountData);
            }
        }
    }

    private void addAccountNames(Set<String> accountNames, List<RevDashboardDTO1.AccountData> accountDataList) {
        for (RevDashboardDTO1.AccountData accountData : accountDataList) {
            accountNames.add(accountData.getAccount());
        }
    }

    private void consolidateAccounts(RevDashboardDTO1 revDashboardDTO1) {
        Map<Integer, Map<String, RevDashboardDTO1.AccountData>> accountDataMap = new HashMap<>();

        for (RevDashboardDTO1.AccountData accountsData : revDashboardDTO1.getAccounts()) {
            accountDataMap.putIfAbsent(accountsData.getFinancialYear(), new HashMap<>());
            Map<String, RevDashboardDTO1.AccountData> quarterMap = accountDataMap.get(accountsData.getFinancialYear());

            if (quarterMap.containsKey(accountsData.getQuarter())) {
                RevDashboardDTO1.AccountData existingData = quarterMap.get(accountsData.getQuarter());
                existingData.getRevenueBudget().setBudget(roundToTwoDecimalPlaces(existingData.getRevenueBudget().getBudget() + accountsData.getRevenueBudget().getBudget()));
                existingData.getRevenueBudget().setForecast(roundToTwoDecimalPlaces(existingData.getRevenueBudget().getForecast() + accountsData.getRevenueBudget().getForecast()));
                existingData.getRevenueBudget().setGap(roundToTwoDecimalPlaces(existingData.getRevenueBudget().getGap() + accountsData.getRevenueBudget().getGap()));
                existingData.getRevenueGrowth().setAccountExpected(roundToTwoDecimalPlaces(existingData.getRevenueGrowth().getAccountExpected() + accountsData.getRevenueGrowth().getAccountExpected()));
                existingData.getRevenueGrowth().setForecast(roundToTwoDecimalPlaces(existingData.getRevenueGrowth().getForecast() + accountsData.getRevenueGrowth().getForecast()));
                existingData.getRevenueGrowth().setGap(roundToTwoDecimalPlaces(existingData.getRevenueGrowth().getGap() + accountsData.getRevenueGrowth().getGap()));
                existingData.getPipelineState().setSumOfPipeline_pitch(roundToTwoDecimalPlaces(existingData.getPipelineState().getSumOfPipeline_pitch() + accountsData.getPipelineState().getSumOfPipeline_pitch()));
                existingData.getPipelineState().setSumOfPipeline_opportunity(roundToTwoDecimalPlaces(existingData.getPipelineState().getSumOfPipeline_opportunity() + accountsData.getPipelineState().getSumOfPipeline_opportunity()));
                existingData.getPipelineState().setSumOfPipeline_total(roundToTwoDecimalPlaces(existingData.getPipelineState().getSumOfPipeline_total() + accountsData.getPipelineState().getSumOfPipeline_total()));
                existingData.getPipelineState().setSumOfPipeline_shaping(roundToTwoDecimalPlaces(existingData.getPipelineState().getSumOfPipeline_shaping() + accountsData.getPipelineState().getSumOfPipeline_shaping()));
            } else {
                RevDashboardDTO1.AccountData newData = new RevDashboardDTO1.AccountData("all", accountsData.getFinancialYear(), accountsData.getQuarter(),
                        new RevDashboardDTO1.RevenueBudgetSummary(
                                roundToTwoDecimalPlaces(accountsData.getRevenueBudget().getBudget()),
                                roundToTwoDecimalPlaces(accountsData.getRevenueBudget().getForecast()),
                                roundToTwoDecimalPlaces(accountsData.getRevenueBudget().getGap())),
                        new RevDashboardDTO1.RevenueGrowthSummary(
                                roundToTwoDecimalPlaces(accountsData.getRevenueGrowth().getAccountExpected()),
                                roundToTwoDecimalPlaces(accountsData.getRevenueGrowth().getForecast()),
                                roundToTwoDecimalPlaces(accountsData.getRevenueGrowth().getGap())),
                        new RevDashboardDTO1.PipelineState(
                                roundToTwoDecimalPlaces(accountsData.getPipelineState().getSumOfPipeline_pitch()),
                                roundToTwoDecimalPlaces(accountsData.getPipelineState().getSumOfPipeline_opportunity()),
                                roundToTwoDecimalPlaces(accountsData.getPipelineState().getSumOfPipeline_total()),
                                roundToTwoDecimalPlaces(accountsData.getPipelineState().getSumOfPipeline_shaping())));
                quarterMap.put(accountsData.getQuarter(), newData);
            }
        }

        List<RevDashboardDTO1.AccountData> consolidatedAccountDataList = new ArrayList<>();
        for (Map<String, RevDashboardDTO1.AccountData> quarterMap : accountDataMap.values()) {
            consolidatedAccountDataList.addAll(quarterMap.values());
        }
        revDashboardDTO1.setAccounts(consolidatedAccountDataList);
    }

    private float roundToTwoDecimalPlaces(float value) {
        return Math.round(value * 100.0f) / 100.0f;
    }


    @PostMapping("/getRevenueDashboardAccount")
    public ResponseEntity<RevDashboardDTO> getRevenueDashboardByAccount(@RequestBody List<String> accounts) {
        Map<String, RevDashboardDTO> accountMap = new HashMap<>();

        for (String account : accounts) {
            logger.info("Fetching data for Account: {}", account);

            List<RevenueBudgetSummary> revenueBudgetSummaries = revenueBudgetSummaryRepository.findByAccount(account);
            List<RevenueGrowthSummary> revenueGrowthSummaries = revenueGrowthSummaryRepository.findByAccount(account);
            List<PipelineState> pipelineStates = pipelineStateRepository.findByAccount(account);

            aggregateBudgetSummariesByAccount(revenueBudgetSummaries, accountMap, account);
            aggregateGrowthSummariesByAccount(revenueGrowthSummaries, accountMap, account);
            aggregatePipelineStatesByAccount(pipelineStates, accountMap, account);
        }

        List<RevDashboardDTO> result = new ArrayList<>(accountMap.values());

        // Consolidate financial years into a single list across all RevDashboardDTOs
        Set<Integer> allFinancialYears = new HashSet<>();
        result.forEach(dto -> allFinancialYears.addAll(dto.getFinancialYears()));
        result.forEach(dto -> dto.setFinancialYears(new ArrayList<>(allFinancialYears)));

        // Filter to keep only the 2 most recent financial years and their 8 quarters
        List<Integer> recentFinancialYears = allFinancialYears.stream()
                .sorted(Comparator.reverseOrder())
                .limit(2)
                .collect(Collectors.toList());

        result.forEach(dto -> {
            dto.setFinancialYears(recentFinancialYears);
            dto.getAccounts().removeIf(accountData -> !recentFinancialYears.contains(accountData.getFinancialYear()));
        });

        // Sort financial years in descending order
        result.forEach(dto -> dto.getFinancialYears().sort(Collections.reverseOrder()));

        // Sort accounts by financial year (descending) and quarter (ascending)
        result.forEach(dto -> dto.getAccounts().sort(Comparator
                .comparing(RevDashboardDTO.AccountData::getFinancialYear, Comparator.reverseOrder())
                .thenComparing(RevDashboardDTO.AccountData::getQuarter)));

        if (!result.isEmpty()) {
            return ResponseEntity.ok(result.get(0));
        } else {
            return ResponseEntity.notFound().build();
        }

    }


    private void aggregateBudgetSummariesByAccount(List<RevenueBudgetSummary> revenueBudgetSummaries,
                                                   Map<String, RevDashboardDTO> accountMap,
                                                   String account) {
        for (RevenueBudgetSummary budgetSummary : revenueBudgetSummaries) {
            int financialYear = budgetSummary.getFinancialYear();
            String quarter = budgetSummary.getQuarter();
            String deliveryDirector = budgetSummary.getDeliveryDirector();
            String deliveryManager = budgetSummary.getDeliveryManager();

            // Wrap the deliveryDirector in a List<String>
            List<String> deliveryDirectorList = new ArrayList<>();
            deliveryDirectorList.add(deliveryDirector);

            RevDashboardDTO revDashboardDTO = accountMap.computeIfAbsent(account, k -> new RevDashboardDTO(deliveryDirectorList));

            if (!revDashboardDTO.getAccountsNames().contains(account)) {
                revDashboardDTO.getAccountsNames().add(account);
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

            if (!revDashboardDTO.getFinancialYears().contains(financialYear)) {
                revDashboardDTO.getFinancialYears().add(financialYear);
            }
            if (!revDashboardDTO.getDeliveryManager().contains(deliveryManager)) {
                revDashboardDTO.getDeliveryManager().add(deliveryManager);
            }
        }
    }

    private void aggregateGrowthSummariesByAccount(List<RevenueGrowthSummary> revenueGrowthSummaries,
                                                   Map<String, RevDashboardDTO> accountMap,
                                                   String account) {
        for (RevenueGrowthSummary growthSummary : revenueGrowthSummaries) {
            int financialYear = growthSummary.getFinancialYear();
            String quarter = growthSummary.getQuarter();
            String deliveryDirector = growthSummary.getDeliveryDirector();
            String deliveryManager = growthSummary.getDeliveryManager();
            // Wrap the deliveryDirector in a List<String>
            List<String> deliveryDirectorList = new ArrayList<>();
            deliveryDirectorList.add(deliveryDirector);

            RevDashboardDTO revDashboardDTO = accountMap.computeIfAbsent(account, k -> new RevDashboardDTO(deliveryDirectorList));

            if (!revDashboardDTO.getAccountsNames().contains(account)) {
                revDashboardDTO.getAccountsNames().add(account);
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

            if (!revDashboardDTO.getFinancialYears().contains(financialYear)) {
                revDashboardDTO.getFinancialYears().add(financialYear);
            }
            if (!revDashboardDTO.getDeliveryManager().contains(deliveryManager)) {
                revDashboardDTO.getDeliveryManager().add(deliveryManager);
            }
        }
    }

    private void aggregatePipelineStatesByAccount(List<PipelineState> pipelineStates,
                                                  Map<String, RevDashboardDTO> accountMap,
                                                  String account) {
        for (PipelineState pipelineState : pipelineStates) {
            int financialYear = pipelineState.getFinancialYear();
            String quarter = pipelineState.getQuarter();
            String deliveryDirector = pipelineState.getDeliveryDirector();
            String deliveryManager = pipelineState.getDeliveryManager();


            // Wrap the deliveryDirector in a List<String>
            List<String> deliveryDirectorList = new ArrayList<>();
            deliveryDirectorList.add(deliveryDirector);

            RevDashboardDTO revDashboardDTO = accountMap.computeIfAbsent(account, k -> new RevDashboardDTO(deliveryDirectorList));

            if (!revDashboardDTO.getAccountsNames().contains(account)) {
                revDashboardDTO.getAccountsNames().add(account);
            }

            Optional<RevDashboardDTO.AccountData> optionalAccountData = revDashboardDTO.getAccounts().stream()
                    .filter(a -> a.getFinancialYear() == financialYear && a.getQuarter().equals(quarter) && a.getAccount().equals(account))
                    .findFirst();

            RevDashboardDTO.AccountData accountData;
            if (optionalAccountData.isPresent()) {
                accountData = optionalAccountData.get();
                accountData.getpipelineState().setSumOfPipeline_pitch(accountData.getpipelineState().getSumOfPipeline_pitch() + pipelineState.getSumOfPipeline_pitch());
                accountData.getpipelineState().setSumOfPipeline_opportunity(accountData.getpipelineState().getSumOfPipeline_opportunity() + pipelineState.getSumOfPipeline_opportunity());
                accountData.getpipelineState().setSumOfPipeline_total(accountData.getpipelineState().getSumOfPipeline_total() + pipelineState.getSumOfPipeline_total());
                accountData.getpipelineState().setSumOfPipeline_shaping(accountData.getpipelineState().getSumOfPipeline_shaping() + pipelineState.getSumOfPipeline_shaping());
            } else {
                accountData = new RevDashboardDTO.AccountData(account, financialYear, quarter,
                        new RevDashboardDTO.RevenueBudgetSummary(0, 0, 0),
                        new RevDashboardDTO.RevenueGrowthSummary(0, 0, 0),
                        new RevDashboardDTO.PipelineState(pipelineState.getSumOfPipeline_pitch(), pipelineState.getSumOfPipeline_opportunity(), pipelineState.getSumOfPipeline_total(), pipelineState.getSumOfPipeline_shaping()));
                revDashboardDTO.getAccounts().add(accountData);
            }

            if (!revDashboardDTO.getFinancialYears().contains(financialYear)) {
                revDashboardDTO.getFinancialYears().add(financialYear);
            }
            if (!revDashboardDTO.getDeliveryManager().contains(deliveryManager)) {
                revDashboardDTO.getDeliveryManager().add(deliveryManager);
            }
        }
    }


    @PostMapping("/getByRoleAndName")
    public ResponseEntity<RevDashboardData> getByRoleAndName(@RequestBody RoleAndNameRequest request) {
        List<String> roles = request.getRole();
        String name = request.getName();

        // Split the name into first and last parts
        String[] nameParts = name.split(" ");
        if (nameParts.length != 2) {
            return ResponseEntity.badRequest().build();
        }
        String firstName = nameParts[0];
        String lastName = nameParts[1];
        String fullName = firstName + " " + lastName;

        // Construct response object
        RevDashboardData response = new RevDashboardData();

        // Initialize collections
        Set<String> deliveryManagers = new HashSet<>();
        Set<String> deliveryDirectors = new HashSet<>();
        Set<String> accountsNames = new HashSet<>();
        Set<Integer> financialYears = new TreeSet<>(Collections.reverseOrder());
        Map<String, RevDashboardData.AccountData> aggregatedAccounts = new LinkedHashMap<>();

        // Process each role
        for (String role : roles) {
            if (role.equals("Delivery Director")) {
                List<RevenueBudgetSummary> revenueBudgetSummaries = revenueBudgetSummaryRepository.findByDeliveryDirector(fullName);
                List<RevenueGrowthSummary> revenueGrowthSummaries = revenueGrowthSummaryRepository.findByDeliveryDirector(fullName);
                List<PipelineState> pipelineStates = pipelineStateRepository.findByDeliveryDirector(fullName);

                deliveryManagers.addAll(revenueBudgetSummaries.stream().map(RevenueBudgetSummary::getDeliveryManager).collect(Collectors.toSet()));
                deliveryDirectors.add(fullName); // Add the current Delivery Director to the set
                accountsNames.addAll(revenueBudgetSummaries.stream().map(RevenueBudgetSummary::getAccount).collect(Collectors.toSet()));
                financialYears.addAll(revenueBudgetSummaries.stream().map(RevenueBudgetSummary::getFinancialYear).collect(Collectors.toSet()));
            } else if (role.equals("Delivery Manager")) {
                List<RevenueBudgetSummary> revenueBudgetSummaries = revenueBudgetSummaryRepository.findByDeliveryManager(fullName);
                List<RevenueGrowthSummary> revenueGrowthSummaries = revenueGrowthSummaryRepository.findByDeliveryManager(fullName);
                List<PipelineState> pipelineStates = pipelineStateRepository.findByDeliveryManager(fullName);

                // Ensure this is a Delivery Manager role
                deliveryDirectors.addAll(revenueBudgetSummaries.stream().map(RevenueBudgetSummary::getDeliveryDirector).collect(Collectors.toSet()));
                deliveryManagers.add(fullName); // Ensure the current Delivery Manager is added
                accountsNames.addAll(revenueBudgetSummaries.stream().map(RevenueBudgetSummary::getAccount).collect(Collectors.toSet()));
                financialYears.addAll(revenueBudgetSummaries.stream().map(RevenueBudgetSummary::getFinancialYear).collect(Collectors.toSet()));
            } else if (role.equals("Project Manager")) {
                List<RevenueBudgetSummary> revenueBudgetSummaries = revenueBudgetSummaryRepository.findByProjectManager(fullName);
                List<RevenueGrowthSummary> revenueGrowthSummaries = revenueGrowthSummaryRepository.findByProjectManager(fullName);

                deliveryManagers.addAll(revenueBudgetSummaries.stream().map(RevenueBudgetSummary::getDeliveryManager).collect(Collectors.toSet()));
                accountsNames.addAll(revenueBudgetSummaries.stream().map(RevenueBudgetSummary::getAccount).collect(Collectors.toSet()));
                financialYears.addAll(revenueBudgetSummaries.stream().map(RevenueBudgetSummary::getFinancialYear).collect(Collectors.toSet()));
            }
        }

        // Debug logs
        System.out.println("Delivery Managers: " + deliveryManagers);
        System.out.println("Delivery Directors: " + deliveryDirectors);

        // Restrict to the latest two financial years
        List<Integer> latestTwoFinancialYears = financialYears.stream().limit(2).collect(Collectors.toList());

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

        // Process records for aggregation
        for (String role : roles) {
            if (role.equals("Delivery Director")) {
                processAggregationForRole(revenueBudgetSummaryRepository.findByDeliveryDirector(fullName),
                        revenueGrowthSummaryRepository.findByDeliveryDirector(fullName),
                        pipelineStateRepository.findByDeliveryDirector(fullName),
                        aggregatedAccounts, latestTwoFinancialYears);
            } else if (role.equals("Delivery Manager")) {
                processAggregationForRole(revenueBudgetSummaryRepository.findByDeliveryManager(fullName),
                        revenueGrowthSummaryRepository.findByDeliveryManager(fullName),
                        pipelineStateRepository.findByDeliveryManager(fullName),
                        aggregatedAccounts, latestTwoFinancialYears);
            } else if (role.equals("Project Manager")) {
                processAggregationForRole(revenueBudgetSummaryRepository.findByProjectManager(fullName),
                        revenueGrowthSummaryRepository.findByProjectManager(fullName),
                        new ArrayList<>(), // Assuming no PipelineState data for Project Manager
                        aggregatedAccounts, latestTwoFinancialYears);
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

        // Set collected data in response
        response.setDeliveryManager(new ArrayList<>(deliveryManagers)); // Correctly set Delivery Managers
        response.setDeliveryDirector(new ArrayList<>(deliveryDirectors));
        response.setAccountsNames(new ArrayList<>(accountsNames));
        response.setFinancialYears(latestTwoFinancialYears);
        response.setAccounts(responseAccounts);

        // Return the RevDashboardData directly
        return ResponseEntity.ok(response);
    }

    // Method to process aggregation for a given role
    private void processAggregationForRole(List<RevenueBudgetSummary> revenueBudgetSummaries,
                                           List<RevenueGrowthSummary> revenueGrowthSummaries,
                                           List<PipelineState> pipelineStates,
                                           Map<String, RevDashboardData.AccountData> aggregatedAccounts,
                                           List<Integer> latestTwoFinancialYears) {
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


