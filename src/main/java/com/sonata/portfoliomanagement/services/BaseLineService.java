package com.sonata.portfoliomanagement.services;

import com.sonata.portfoliomanagement.interfaces.*;
import com.sonata.portfoliomanagement.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class BaseLineService {

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


//
//    public Map<String, Object> getDeliveryManagerData(List<String> deliveryManagerNames) {
//        Set<String> deliveryDirectors = new HashSet<>();
//        Set<String> accounts = new HashSet<>();
//        Map<String, DataDTO> previousDataMap = new HashMap<>();
//        Map<String, DataDTO> currentDataMap = new HashMap<>();
//
//        // Temporary set to find the maximum year
//        Set<Integer> allYears = new HashSet<>();
//
//        for (String deliveryManagerName : deliveryManagerNames) {
//            // Fetch all unique accounts for the DM
//            Set<String> accountSet = new HashSet<>();
//            accountSet.addAll(revenueBudgetSummaryRepository.findAccountsByDeliveryManager(deliveryManagerName));
//            accountSet.addAll(revenueGrowthSummaryRepository.findAccountsByDeliveryManager(deliveryManagerName));
//            accountSet.addAll(pipelineStateRepository.findAccountsByDeliveryManager(deliveryManagerName));
//            accounts.addAll(accountSet);
//
//            for (String account : accountSet) {
//                // Fetch data for the current account and DM
//                List<RevenueBudgetSummary> currentRevenueBudget = revenueBudgetSummaryRepository.findByDeliveryManagerAndAccount(deliveryManagerName, account);
//                List<RevenueGrowthSummary> currentRevenueGrowth = revenueGrowthSummaryRepository.findByDeliveryManagerAndAccount(deliveryManagerName, account);
//                List<PipelineState> currentPipelineState = pipelineStateRepository.findByDeliveryManagerAndAccount(deliveryManagerName, account);
//                List<BaseLine_RevenueBudgetSummary> previousRevenueBudget = baseLineRevenueBudgetSummaryRepository.findByDeliveryManagerAndAccount(deliveryManagerName, account);
//                List<BaseLine_RevenueGrowthSummary> previousRevenueGrowth = baseLineRevenueGrowthSummaryRepository.findByDeliveryManagerAndAccount(deliveryManagerName, account);
//                List<BaseLine_PipelineState> previousPipelineState = baseLinePipelineStateRepository.findByDeliveryManagerAndAccount(deliveryManagerName, account);
//
//                // Add years to the set
//                currentRevenueBudget.forEach(rbs -> allYears.add(rbs.getFinancialYear()));
//                currentRevenueGrowth.forEach(rgs -> allYears.add(rgs.getFinancialYear()));
//                currentPipelineState.forEach(ps -> allYears.add(ps.getFinancialYear()));
//                previousRevenueBudget.forEach(rbs -> allYears.add(rbs.getFinancialYear()));
//                previousRevenueGrowth.forEach(rgs -> allYears.add(rgs.getFinancialYear()));
//                previousPipelineState.forEach(ps -> allYears.add(ps.getFinancialYear()));
//            }
//        }
//
//        // Find the maximum year and the previous year
//        int currentYear = allYears.stream().max(Integer::compareTo).orElse(Year.now().getValue());
//        int previousYear = currentYear - 1;
//
//        for (String deliveryManagerName : deliveryManagerNames) {
//            Set<String> accountSet = new HashSet<>();
//            accountSet.addAll(revenueBudgetSummaryRepository.findAccountsByDeliveryManager(deliveryManagerName));
//            accountSet.addAll(revenueGrowthSummaryRepository.findAccountsByDeliveryManager(deliveryManagerName));
//            accountSet.addAll(pipelineStateRepository.findAccountsByDeliveryManager(deliveryManagerName));
//            accounts.addAll(accountSet);
//
//            for (String account : accountSet) {
//                List<RevenueBudgetSummary> currentRevenueBudget = revenueBudgetSummaryRepository.findByDeliveryManagerAndAccount(deliveryManagerName, account);
//                List<RevenueGrowthSummary> currentRevenueGrowth = revenueGrowthSummaryRepository.findByDeliveryManagerAndAccount(deliveryManagerName, account);
//                List<PipelineState> currentPipelineState = pipelineStateRepository.findByDeliveryManagerAndAccount(deliveryManagerName, account);
//                List<BaseLine_RevenueBudgetSummary> previousRevenueBudget = baseLineRevenueBudgetSummaryRepository.findByDeliveryManagerAndAccount(deliveryManagerName, account);
//                List<BaseLine_RevenueGrowthSummary> previousRevenueGrowth = baseLineRevenueGrowthSummaryRepository.findByDeliveryManagerAndAccount(deliveryManagerName, account);
//                List<BaseLine_PipelineState> previousPipelineState = baseLinePipelineStateRepository.findByDeliveryManagerAndAccount(deliveryManagerName, account);
//
//                String dd = "";
//                if (!currentRevenueBudget.isEmpty()) {
//                    dd = currentRevenueBudget.get(0).getDeliveryDirector();
//                } else if (!currentRevenueGrowth.isEmpty()) {
//                    dd = currentRevenueGrowth.get(0).getDeliveryDirector();
//                } else if (!currentPipelineState.isEmpty()) {
//                    dd = currentPipelineState.get(0).getDeliveryDirector();
//                }
//                if (!dd.isEmpty()) {
//                    deliveryDirectors.add(dd);
//                }
//
//                for (RevenueBudgetSummary rbs : currentRevenueBudget) {
//                    if (rbs.getFinancialYear() == currentYear || rbs.getFinancialYear() == previousYear) {
//                        String key = rbs.getFinancialYear() + "-" + rbs.getQuarter();
//                        DataDTO data = currentDataMap.getOrDefault(key, new DataDTO());
//                        data.setFinancialYear(rbs.getFinancialYear());
//                        data.setQuarter(rbs.getQuarter());
//                        data.setRvb_Forecast(data.getRvb_Forecast() + rbs.getForecast());
//                        currentDataMap.put(key, data);
//                    }
//                }
//
//                for (RevenueGrowthSummary rgs : currentRevenueGrowth) {
//                    if (rgs.getFinancialYear() == currentYear || rgs.getFinancialYear() == previousYear) {
//                        String key = rgs.getFinancialYear() + "-" + rgs.getQuarter();
//                        DataDTO data = currentDataMap.getOrDefault(key, new DataDTO());
//                        data.setFinancialYear(rgs.getFinancialYear());
//                        data.setQuarter(rgs.getQuarter());
//                        data.setRvg_Forecast(data.getRvg_Forecast() + rgs.getForecast());
//                        currentDataMap.put(key, data);
//                    }
//                }
//
//                for (PipelineState ps : currentPipelineState) {
//                    if (ps.getFinancialYear() == currentYear || ps.getFinancialYear() == previousYear) {
//                        String key = ps.getFinancialYear() + "-" + ps.getQuarter();
//                        DataDTO data = currentDataMap.getOrDefault(key, new DataDTO());
//                        data.setFinancialYear(ps.getFinancialYear());
//                        data.setQuarter(ps.getQuarter());
//                        data.setTotalPipelineSum(data.getTotalPipelineSum() + ps.getSumOfPipeline_total());
//                        currentDataMap.put(key, data);
//                    }
//                }
//
//                for (BaseLine_RevenueBudgetSummary rbs : previousRevenueBudget) {
//                    if (rbs.getFinancialYear() == currentYear || rbs.getFinancialYear() == previousYear) {
//                        String key = rbs.getFinancialYear() + "-" + rbs.getQuarter();
//                        DataDTO data = previousDataMap.getOrDefault(key, new DataDTO());
//                        data.setFinancialYear(rbs.getFinancialYear());
//                        data.setQuarter(rbs.getQuarter());
//                        data.setRvb_Forecast(data.getRvb_Forecast() + rbs.getForecast());
//                        previousDataMap.put(key, data);
//                    }
//                }
//
//                for (BaseLine_RevenueGrowthSummary rgs : previousRevenueGrowth) {
//                    if (rgs.getFinancialYear() == currentYear || rgs.getFinancialYear() == previousYear) {
//                        String key = rgs.getFinancialYear() + "-" + rgs.getQuarter();
//                        DataDTO data = previousDataMap.getOrDefault(key, new DataDTO());
//                        data.setFinancialYear(rgs.getFinancialYear());
//                        data.setQuarter(rgs.getQuarter());
//                        data.setRvg_Forecast(data.getRvg_Forecast() + rgs.getForecast());
//                        previousDataMap.put(key, data);
//                    }
//                }
//
//                for (BaseLine_PipelineState ps : previousPipelineState) {
//                    if (ps.getFinancialYear() == currentYear || ps.getFinancialYear() == previousYear) {
//                        String key = ps.getFinancialYear() + "-" + ps.getQuarter();
//                        DataDTO data = previousDataMap.getOrDefault(key, new DataDTO());
//                        data.setFinancialYear(ps.getFinancialYear());
//                        data.setQuarter(ps.getQuarter());
//                        data.setTotalPipelineSum(data.getTotalPipelineSum() + ps.getSumOfPipeline_total());
//                        previousDataMap.put(key, data);
//                    }
//                }
//            }
//        }
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("deliveryDirector", new ArrayList<>(deliveryDirectors));
//        response.put("deliveryManager", deliveryManagerNames);
//        response.put("account", new ArrayList<>(accounts));
//        response.put("previousData", new ArrayList<>(previousDataMap.values()));
//        response.put("currentData", new ArrayList<>(currentDataMap.values()));
//
//        return response;
//    }



    //new method to reduce the time taken to fetch data-->

    public Map<String, Object> getDeliveryManagerData(List<String> deliveryManagerNames) {
        // Define sets to hold unique delivery directors and accounts
        Set<String> deliveryDirectors = ConcurrentHashMap.newKeySet();
        Set<String> accounts = ConcurrentHashMap.newKeySet();

        // Maps to hold previous and current data
        Map<String, DataDTO> previousDataMap = new ConcurrentHashMap<>();
        Map<String, DataDTO> currentDataMap = new ConcurrentHashMap<>();

        // Fetch all necessary data in batch
        List<RevenueBudgetSummary> currentRevenueBudgets = revenueBudgetSummaryRepository.findByDeliveryManagerIn(deliveryManagerNames);
        List<RevenueGrowthSummary> currentRevenueGrowths = revenueGrowthSummaryRepository.findByDeliveryManagerIn(deliveryManagerNames);
        List<PipelineState> currentPipelineStates = pipelineStateRepository.findByDeliveryManagerIn(deliveryManagerNames);
        List<BaseLine_RevenueBudgetSummary> previousRevenueBudgets = baseLineRevenueBudgetSummaryRepository.findByDeliveryManagerIn(deliveryManagerNames);
        List<BaseLine_RevenueGrowthSummary> previousRevenueGrowths = baseLineRevenueGrowthSummaryRepository.findByDeliveryManagerIn(deliveryManagerNames);
        List<BaseLine_PipelineState> previousPipelineStates = baseLinePipelineStateRepository.findByDeliveryManagerIn(deliveryManagerNames);

        // Collect all years
        Set<Integer> allYears = new HashSet<>();
        allYears.addAll(currentRevenueBudgets.stream().map(RevenueBudgetSummary::getFinancialYear).collect(Collectors.toSet()));
        allYears.addAll(currentRevenueGrowths.stream().map(RevenueGrowthSummary::getFinancialYear).collect(Collectors.toSet()));
        allYears.addAll(currentPipelineStates.stream().map(PipelineState::getFinancialYear).collect(Collectors.toSet()));
        allYears.addAll(previousRevenueBudgets.stream().map(BaseLine_RevenueBudgetSummary::getFinancialYear).collect(Collectors.toSet()));
        allYears.addAll(previousRevenueGrowths.stream().map(BaseLine_RevenueGrowthSummary::getFinancialYear).collect(Collectors.toSet()));
        allYears.addAll(previousPipelineStates.stream().map(BaseLine_PipelineState::getFinancialYear).collect(Collectors.toSet()));

        // Determine current and previous year
        int currentYear = allYears.stream().max(Integer::compare).orElse(Year.now().getValue());
        int previousYear = currentYear - 1;

        // Process all current data in parallel
        currentRevenueBudgets.parallelStream().forEach(rbs -> processCurrentData(rbs, currentYear, previousYear, currentDataMap));
        currentRevenueGrowths.parallelStream().forEach(rgs -> processCurrentData(rgs, currentYear, previousYear, currentDataMap));
        currentPipelineStates.parallelStream().forEach(ps -> processCurrentData(ps, currentYear, previousYear, currentDataMap));

        // Process all previous data in parallel
        previousRevenueBudgets.parallelStream().forEach(rbs -> processPreviousData(rbs, currentYear, previousYear, previousDataMap));
        previousRevenueGrowths.parallelStream().forEach(rgs -> processPreviousData(rgs, currentYear, previousYear, previousDataMap));
        previousPipelineStates.parallelStream().forEach(ps -> processPreviousData(ps, currentYear, previousYear, previousDataMap));

        // Collect unique accounts and delivery directors
        currentRevenueBudgets.forEach(rbs -> collectAccountsAndDirectors(rbs, accounts, deliveryDirectors));
        currentRevenueGrowths.forEach(rgs -> collectAccountsAndDirectors(rgs, accounts, deliveryDirectors));
        currentPipelineStates.forEach(ps -> collectAccountsAndDirectors(ps, accounts, deliveryDirectors));

        // Prepare response
        Map<String, Object> response = new HashMap<>();
        response.put("deliveryDirector", new ArrayList<>(deliveryDirectors));
        response.put("deliveryManager", deliveryManagerNames);
        response.put("account", new ArrayList<>(accounts));
        response.put("previousData", new ArrayList<>(previousDataMap.values()));
        response.put("currentData", new ArrayList<>(currentDataMap.values()));

        return response;
    }

    private void collectAccountsAndDirectors(Object summary, Set<String> accounts, Set<String> deliveryDirectors) {
        if (summary instanceof RevenueBudgetSummary) {
            RevenueBudgetSummary rbs = (RevenueBudgetSummary) summary;
            accounts.add(rbs.getAccount());
            if (rbs.getDeliveryDirector() != null) {
                deliveryDirectors.add(rbs.getDeliveryDirector());
            }
        } else if (summary instanceof RevenueGrowthSummary) {
            RevenueGrowthSummary rgs = (RevenueGrowthSummary) summary;
            accounts.add(rgs.getAccount());
            if (rgs.getDeliveryDirector() != null) {
                deliveryDirectors.add(rgs.getDeliveryDirector());
            }
        } else if (summary instanceof PipelineState) {
            PipelineState ps = (PipelineState) summary;
            accounts.add(ps.getAccount());
            if (ps.getDeliveryDirector() != null) {
                deliveryDirectors.add(ps.getDeliveryDirector());
            }
        }
    }


//    public Map<String, Object> getDeliveryDirectorData(List<String> deliveryDirectorNames) {
//        Set<String> deliveryManagers = new HashSet<>();
//        Set<String> accounts = new HashSet<>();
//        Map<String, DataDTO> previousDataMap = new HashMap<>();
//        Map<String, DataDTO> currentDataMap = new HashMap<>();
//
//        // Temporary set to find the maximum year
//        Set<Integer> allYears = new HashSet<>();
//
//        for (String deliveryDirectorName : deliveryDirectorNames) {
//            // Fetch all unique accounts for the DD
//            Set<String> accountSet = new HashSet<>();
//            accountSet.addAll(revenueBudgetSummaryRepository.findAccountsByDeliveryDirector(deliveryDirectorName));
//            accountSet.addAll(revenueGrowthSummaryRepository.findAccountsByDeliveryDirector(deliveryDirectorName));
//            accountSet.addAll(pipelineStateRepository.findAccountsByDeliveryDirector(deliveryDirectorName));
//            accounts.addAll(accountSet);
//
//            for (String account : accountSet) {
//                // Fetch data for the current account and DD
//                List<RevenueBudgetSummary> currentRevenueBudget = revenueBudgetSummaryRepository.findByDeliveryDirectorAndAccount(deliveryDirectorName, account);
//                List<RevenueGrowthSummary> currentRevenueGrowth = revenueGrowthSummaryRepository.findByDeliveryDirectorAndAccount(deliveryDirectorName, account);
//                List<PipelineState> currentPipelineState = pipelineStateRepository.findByDeliveryDirectorAndAccount(deliveryDirectorName, account);
//                List<BaseLine_RevenueBudgetSummary> previousRevenueBudget = baseLineRevenueBudgetSummaryRepository.findByDeliveryDirectorAndAccount(deliveryDirectorName, account);
//                List<BaseLine_RevenueGrowthSummary> previousRevenueGrowth = baseLineRevenueGrowthSummaryRepository.findByDeliveryDirectorAndAccount(deliveryDirectorName, account);
//                List<BaseLine_PipelineState> previousPipelineState = baseLinePipelineStateRepository.findByDeliveryDirectorAndAccount(deliveryDirectorName, account);
//
//                // Add years to the set
//                currentRevenueBudget.forEach(rbs -> allYears.add(rbs.getFinancialYear()));
//                currentRevenueGrowth.forEach(rgs -> allYears.add(rgs.getFinancialYear()));
//                currentPipelineState.forEach(ps -> allYears.add(ps.getFinancialYear()));
//                previousRevenueBudget.forEach(rbs -> allYears.add(rbs.getFinancialYear()));
//                previousRevenueGrowth.forEach(rgs -> allYears.add(rgs.getFinancialYear()));
//                previousPipelineState.forEach(ps -> allYears.add(ps.getFinancialYear()));
//            }
//        }
//
//        // Find the maximum year and the previous year
//        int currentYear = allYears.stream().max(Integer::compareTo).orElse(Year.now().getValue());
//        int previousYear = currentYear - 1;
//
//        for (String deliveryDirectorName : deliveryDirectorNames) {
//            Set<String> accountSet = new HashSet<>();
//            accountSet.addAll(revenueBudgetSummaryRepository.findAccountsByDeliveryDirector(deliveryDirectorName));
//            accountSet.addAll(revenueGrowthSummaryRepository.findAccountsByDeliveryDirector(deliveryDirectorName));
//            accountSet.addAll(pipelineStateRepository.findAccountsByDeliveryDirector(deliveryDirectorName));
//            accounts.addAll(accountSet);
//
//            for (String account : accountSet) {
//                List<RevenueBudgetSummary> currentRevenueBudget = revenueBudgetSummaryRepository.findByDeliveryDirectorAndAccount(deliveryDirectorName, account);
//                List<RevenueGrowthSummary> currentRevenueGrowth = revenueGrowthSummaryRepository.findByDeliveryDirectorAndAccount(deliveryDirectorName, account);
//                List<PipelineState> currentPipelineState = pipelineStateRepository.findByDeliveryDirectorAndAccount(deliveryDirectorName, account);
//                List<BaseLine_RevenueBudgetSummary> previousRevenueBudget = baseLineRevenueBudgetSummaryRepository.findByDeliveryDirectorAndAccount(deliveryDirectorName, account);
//                List<BaseLine_RevenueGrowthSummary> previousRevenueGrowth = baseLineRevenueGrowthSummaryRepository.findByDeliveryDirectorAndAccount(deliveryDirectorName, account);
//                List<BaseLine_PipelineState> previousPipelineState = baseLinePipelineStateRepository.findByDeliveryDirectorAndAccount(deliveryDirectorName, account);
//
//                String dm = "";
//                if (!currentRevenueBudget.isEmpty()) {
//                    dm = currentRevenueBudget.get(0).getDeliveryManager();
//                } else if (!currentRevenueGrowth.isEmpty()) {
//                    dm = currentRevenueGrowth.get(0).getDeliveryManager();
//                } else if (!currentPipelineState.isEmpty()) {
//                    dm = currentPipelineState.get(0).getDeliveryManager();
//                }
//                if (!dm.isEmpty()) {
//                    deliveryManagers.add(dm);
//                }
//
//                for (RevenueBudgetSummary rbs : currentRevenueBudget) {
//                    if (rbs.getFinancialYear() == currentYear || rbs.getFinancialYear() == previousYear) {
//                        String key = rbs.getFinancialYear() + "-" + rbs.getQuarter();
//                        DataDTO data = currentDataMap.getOrDefault(key, new DataDTO());
//                        data.setFinancialYear(rbs.getFinancialYear());
//                        data.setQuarter(rbs.getQuarter());
//                        data.setRvb_Forecast(data.getRvb_Forecast() + rbs.getForecast());
//                        currentDataMap.put(key, data);
//                    }
//                }
//
//                for (RevenueGrowthSummary rgs : currentRevenueGrowth) {
//                    if (rgs.getFinancialYear() == currentYear || rgs.getFinancialYear() == previousYear) {
//                        String key = rgs.getFinancialYear() + "-" + rgs.getQuarter();
//                        DataDTO data = currentDataMap.getOrDefault(key, new DataDTO());
//                        data.setFinancialYear(rgs.getFinancialYear());
//                        data.setQuarter(rgs.getQuarter());
//                        data.setRvg_Forecast(data.getRvg_Forecast() + rgs.getForecast());
//                        currentDataMap.put(key, data);
//                    }
//                }
//
//                for (PipelineState ps : currentPipelineState) {
//                    if (ps.getFinancialYear() == currentYear || ps.getFinancialYear() == previousYear) {
//                        String key = ps.getFinancialYear() + "-" + ps.getQuarter();
//                        DataDTO data = currentDataMap.getOrDefault(key, new DataDTO());
//                        data.setFinancialYear(ps.getFinancialYear());
//                        data.setQuarter(ps.getQuarter());
//                        data.setTotalPipelineSum(data.getTotalPipelineSum() + ps.getSumOfPipeline_total());
//                        currentDataMap.put(key, data);
//                    }
//                }
//
//                for (BaseLine_RevenueBudgetSummary rbs : previousRevenueBudget) {
//                    if (rbs.getFinancialYear() == currentYear || rbs.getFinancialYear() == previousYear) {
//                        String key = rbs.getFinancialYear() + "-" + rbs.getQuarter();
//                        DataDTO data = previousDataMap.getOrDefault(key, new DataDTO());
//                        data.setFinancialYear(rbs.getFinancialYear());
//                        data.setQuarter(rbs.getQuarter());
//                        data.setRvb_Forecast(data.getRvb_Forecast() + rbs.getForecast());
//                        previousDataMap.put(key, data);
//                    }
//                }
//
//                for (BaseLine_RevenueGrowthSummary rgs : previousRevenueGrowth) {
//                    if (rgs.getFinancialYear() == currentYear || rgs.getFinancialYear() == previousYear) {
//                        String key = rgs.getFinancialYear() + "-" + rgs.getQuarter();
//                        DataDTO data = previousDataMap.getOrDefault(key, new DataDTO());
//                        data.setFinancialYear(rgs.getFinancialYear());
//                        data.setQuarter(rgs.getQuarter());
//                        data.setRvg_Forecast(data.getRvg_Forecast() + rgs.getForecast());
//                        previousDataMap.put(key, data);
//                    }
//                }
//
//                for (BaseLine_PipelineState ps : previousPipelineState) {
//                    if (ps.getFinancialYear() == currentYear || ps.getFinancialYear() == previousYear) {
//                        String key = ps.getFinancialYear() + "-" + ps.getQuarter();
//                        DataDTO data = previousDataMap.getOrDefault(key, new DataDTO());
//                        data.setFinancialYear(ps.getFinancialYear());
//                        data.setQuarter(ps.getQuarter());
//                        data.setTotalPipelineSum(data.getTotalPipelineSum() + ps.getSumOfPipeline_total());
//                        previousDataMap.put(key, data);
//                    }
//                }
//            }
//        }
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("deliveryDirector", deliveryDirectorNames);
//        response.put("deliveryManager", new ArrayList<>(deliveryManagers));
//        response.put("account", new ArrayList<>(accounts));
//        response.put("previousData", new ArrayList<>(previousDataMap.values()));
//        response.put("currentData", new ArrayList<>(currentDataMap.values()));
//
//        return response;
//    }


    public Map<String, Object> getDeliveryDirectorData(List<String> deliveryDirectorNames) {
        // Define sets to hold unique delivery managers and accounts
        Set<String> deliveryManagers = ConcurrentHashMap.newKeySet();
        Set<String> accounts = ConcurrentHashMap.newKeySet();

        // Maps to hold previous and current data
        Map<String, DataDTO> previousDataMap = new ConcurrentHashMap<>();
        Map<String, DataDTO> currentDataMap = new ConcurrentHashMap<>();

        // Fetch all necessary data in batch
        List<RevenueBudgetSummary> currentRevenueBudgets = revenueBudgetSummaryRepository.findByDeliveryDirectorIn(deliveryDirectorNames);
        List<RevenueGrowthSummary> currentRevenueGrowths = revenueGrowthSummaryRepository.findByDeliveryDirectorIn(deliveryDirectorNames);
        List<PipelineState> currentPipelineStates = pipelineStateRepository.findByDeliveryDirectorIn(deliveryDirectorNames);
        List<BaseLine_RevenueBudgetSummary> previousRevenueBudgets = baseLineRevenueBudgetSummaryRepository.findByDeliveryDirectorIn(deliveryDirectorNames);
        List<BaseLine_RevenueGrowthSummary> previousRevenueGrowths = baseLineRevenueGrowthSummaryRepository.findByDeliveryDirectorIn(deliveryDirectorNames);
        List<BaseLine_PipelineState> previousPipelineStates = baseLinePipelineStateRepository.findByDeliveryDirectorIn(deliveryDirectorNames);

        // Collect all years
        Set<Integer> allYears = new HashSet<>();
        allYears.addAll(currentRevenueBudgets.stream().map(RevenueBudgetSummary::getFinancialYear).collect(Collectors.toSet()));
        allYears.addAll(currentRevenueGrowths.stream().map(RevenueGrowthSummary::getFinancialYear).collect(Collectors.toSet()));
        allYears.addAll(currentPipelineStates.stream().map(PipelineState::getFinancialYear).collect(Collectors.toSet()));
        allYears.addAll(previousRevenueBudgets.stream().map(BaseLine_RevenueBudgetSummary::getFinancialYear).collect(Collectors.toSet()));
        allYears.addAll(previousRevenueGrowths.stream().map(BaseLine_RevenueGrowthSummary::getFinancialYear).collect(Collectors.toSet()));
        allYears.addAll(previousPipelineStates.stream().map(BaseLine_PipelineState::getFinancialYear).collect(Collectors.toSet()));

        // Determine current and previous year
        int currentYear = allYears.stream().max(Integer::compare).orElse(Year.now().getValue());
        int previousYear = currentYear - 1;

        // Process all current data in parallel
        currentRevenueBudgets.parallelStream().forEach(rbs -> processCurrentData(rbs, currentYear, previousYear, currentDataMap));
        currentRevenueGrowths.parallelStream().forEach(rgs -> processCurrentData(rgs, currentYear, previousYear, currentDataMap));
        currentPipelineStates.parallelStream().forEach(ps -> processCurrentData(ps, currentYear, previousYear, currentDataMap));

        // Process all previous data in parallel
        previousRevenueBudgets.parallelStream().forEach(rbs -> processPreviousData(rbs, currentYear, previousYear, previousDataMap));
        previousRevenueGrowths.parallelStream().forEach(rgs -> processPreviousData(rgs, currentYear, previousYear, previousDataMap));
        previousPipelineStates.parallelStream().forEach(ps -> processPreviousData(ps, currentYear, previousYear, previousDataMap));

        // Collect unique accounts and delivery managers
        currentRevenueBudgets.forEach(rbs -> collectAccountsAndManagers(rbs, accounts, deliveryManagers));
        currentRevenueGrowths.forEach(rgs -> collectAccountsAndManagers(rgs, accounts, deliveryManagers));
        currentPipelineStates.forEach(ps -> collectAccountsAndManagers(ps, accounts, deliveryManagers));

        // Prepare response
        Map<String, Object> response = new HashMap<>();
        response.put("deliveryDirector", deliveryDirectorNames);
        response.put("deliveryManager", new ArrayList<>(deliveryManagers));
        response.put("account", new ArrayList<>(accounts));
        response.put("previousData", new ArrayList<>(previousDataMap.values()));
        response.put("currentData", new ArrayList<>(currentDataMap.values()));

        return response;
    }

    private void processCurrentData(Object summary, int currentYear, int previousYear, Map<String, DataDTO> dataMap) {
        int financialYear;
        String quarter;
        String key;
        DataDTO data;

        if (summary instanceof RevenueBudgetSummary) {
            RevenueBudgetSummary rbs = (RevenueBudgetSummary) summary;
            financialYear = rbs.getFinancialYear();
            quarter = rbs.getQuarter();
            if (financialYear == currentYear || financialYear == previousYear) {
                key = financialYear + "-" + quarter;
                data = dataMap.getOrDefault(key, new DataDTO());
                data.setFinancialYear(financialYear);
                data.setQuarter(quarter);
                data.setRvb_Forecast(data.getRvb_Forecast() + rbs.getForecast());
                dataMap.put(key, data);
            }
        } else if (summary instanceof RevenueGrowthSummary) {
            RevenueGrowthSummary rgs = (RevenueGrowthSummary) summary;
            financialYear = rgs.getFinancialYear();
            quarter = rgs.getQuarter();
            if (financialYear == currentYear || financialYear == previousYear) {
                key = financialYear + "-" + quarter;
                data = dataMap.getOrDefault(key, new DataDTO());
                data.setFinancialYear(financialYear);
                data.setQuarter(quarter);
                data.setRvg_Forecast(data.getRvg_Forecast() + rgs.getForecast());
                dataMap.put(key, data);
            }
        } else if (summary instanceof PipelineState) {
            PipelineState ps = (PipelineState) summary;
            financialYear = ps.getFinancialYear();
            quarter = ps.getQuarter();
            if (financialYear == currentYear || financialYear == previousYear) {
                key = financialYear + "-" + quarter;
                data = dataMap.getOrDefault(key, new DataDTO());
                data.setFinancialYear(financialYear);
                data.setQuarter(quarter);
                data.setTotalPipelineSum(data.getTotalPipelineSum() + ps.getSumOfPipeline_total());
                dataMap.put(key, data);
            }
        }
    }

    private void processPreviousData(Object summary, int currentYear, int previousYear, Map<String, DataDTO> dataMap) {
        int financialYear;
        String quarter;
        String key;
        DataDTO data;

        if (summary instanceof BaseLine_RevenueBudgetSummary) {
            BaseLine_RevenueBudgetSummary rbs = (BaseLine_RevenueBudgetSummary) summary;
            financialYear = rbs.getFinancialYear();
            quarter = rbs.getQuarter();
            if (financialYear == currentYear || financialYear == previousYear) {
                key = financialYear + "-" + quarter;
                data = dataMap.getOrDefault(key, new DataDTO());
                data.setFinancialYear(financialYear);
                data.setQuarter(quarter);
                data.setRvb_Forecast(data.getRvb_Forecast() + rbs.getForecast());
                dataMap.put(key, data);
            }
        } else if (summary instanceof BaseLine_RevenueGrowthSummary) {
            BaseLine_RevenueGrowthSummary rgs = (BaseLine_RevenueGrowthSummary) summary;
            financialYear = rgs.getFinancialYear();
            quarter = rgs.getQuarter();
            if (financialYear == currentYear || financialYear == previousYear) {
                key = financialYear + "-" + quarter;
                data = dataMap.getOrDefault(key, new DataDTO());
                data.setFinancialYear(financialYear);
                data.setQuarter(quarter);
                data.setRvg_Forecast(data.getRvg_Forecast() + rgs.getForecast());
                dataMap.put(key, data);
            }
        } else if (summary instanceof BaseLine_PipelineState) {
            BaseLine_PipelineState ps = (BaseLine_PipelineState) summary;
            financialYear = ps.getFinancialYear();
            quarter = ps.getQuarter();
            if (financialYear == currentYear || financialYear == previousYear) {
                key = financialYear + "-" + quarter;
                data = dataMap.getOrDefault(key, new DataDTO());
                data.setFinancialYear(financialYear);
                data.setQuarter(quarter);
                data.setTotalPipelineSum(data.getTotalPipelineSum() + ps.getSumOfPipeline_total());
                dataMap.put(key, data);
            }
        }
    }

    private void collectAccountsAndManagers(Object summary, Set<String> accounts, Set<String> deliveryManagers) {
        if (summary instanceof RevenueBudgetSummary) {
            RevenueBudgetSummary rbs = (RevenueBudgetSummary) summary;
            accounts.add(rbs.getAccount());
            if (rbs.getDeliveryManager() != null) {
                deliveryManagers.add(rbs.getDeliveryManager());
            }
        } else if (summary instanceof RevenueGrowthSummary) {
            RevenueGrowthSummary rgs = (RevenueGrowthSummary) summary;
            accounts.add(rgs.getAccount());
            if (rgs.getDeliveryManager() != null) {
                deliveryManagers.add(rgs.getDeliveryManager());
            }
        } else if (summary instanceof PipelineState) {
            PipelineState ps = (PipelineState) summary;
            accounts.add(ps.getAccount());
            if (ps.getDeliveryManager() != null) {
                deliveryManagers.add(ps.getDeliveryManager());
            }
        }
    }




//
//
//    //service method for getByAccount gets data by account for 2 years-->
//
//    public Map<String, Object> getAccountData(List<String> accountNames) {
//        Map<String, Object> response = new HashMap<>();
//
//        if (accountNames == null || accountNames.isEmpty()) {
//            throw new IllegalArgumentException("Account list cannot be empty");
//        }
//
//        String account = accountNames.get(0); // Assuming only one account in the list for simplicity
//
//        Set<String> deliveryDirectors = new HashSet<>();
//        Set<String> deliveryManagers = new HashSet<>();
//
//        List<String> currentDDs = revenueBudgetSummaryRepository.findDeliveryDirectorsByAccount(account);
//        currentDDs.addAll(revenueGrowthSummaryRepository.findDeliveryDirectorsByAccount(account));
//        currentDDs.addAll(pipelineStateRepository.findDeliveryDirectorsByAccount(account));
//
//        List<String> baselineDDs = baseLineRevenueBudgetSummaryRepository.findDeliveryDirectorsByAccount(account);
//        baselineDDs.addAll(baseLineRevenueGrowthSummaryRepository.findDeliveryDirectorsByAccount(account));
//        baselineDDs.addAll(baseLinePipelineStateRepository.findDeliveryDirectorsByAccount(account));
//
//        deliveryDirectors.addAll(currentDDs);
//        deliveryDirectors.addAll(baselineDDs);
//
//        List<String> currentDMs = revenueBudgetSummaryRepository.findDeliveryManagersByAccount(account);
//        currentDMs.addAll(revenueGrowthSummaryRepository.findDeliveryManagersByAccount(account));
//        currentDMs.addAll(pipelineStateRepository.findDeliveryManagersByAccount(account));
//
//        List<String> baselineDMs = baseLineRevenueBudgetSummaryRepository.findDeliveryManagersByAccount(account);
//        baselineDMs.addAll(baseLineRevenueGrowthSummaryRepository.findDeliveryManagersByAccount(account));
//        baselineDMs.addAll(baseLinePipelineStateRepository.findDeliveryManagersByAccount(account));
//
//        deliveryManagers.addAll(currentDMs);
//        deliveryManagers.addAll(baselineDMs);
//
//        List<DataDTO> currentData = getCurrentDataForAccount(account);
//        List<DataDTO> previousData = getPreviousDataForAccount(account);
//
//        // Implementing logic for fetching only data for currentYear and previousYear
//        Set<Integer> allYears = new HashSet<>();
//        currentData.forEach(data -> allYears.add(data.getFinancialYear()));
//        previousData.forEach(data -> allYears.add(data.getFinancialYear()));
//
//        int currentYear = allYears.stream().max(Integer::compareTo).orElse(Year.now().getValue());
//        int previousYear = currentYear - 1;
//
//        List<DataDTO> filteredCurrentData = currentData.stream()
//                .filter(data -> data.getFinancialYear() == currentYear || data.getFinancialYear() == previousYear)
//                .collect(Collectors.toList());
//
//        List<DataDTO> filteredPreviousData = previousData.stream()
//                .filter(data -> data.getFinancialYear() == currentYear || data.getFinancialYear() == previousYear)
//                .collect(Collectors.toList());
//
//        response.put("deliveryDirector", new ArrayList<>(deliveryDirectors));
//        response.put("deliveryManager", new ArrayList<>(deliveryManagers));
//        response.put("currentData", filteredCurrentData);
//        response.put("previousData", filteredPreviousData);
//        response.put("account", accountNames);
//
//        return response;
//    }
//
//
//    private List<DataDTO> getCurrentDataForAccount(String account) {
//        List<DataDTO> currentDataList = new ArrayList<>();
//
//        List<RevenueBudgetSummary> revenueBudgetSummaries = revenueBudgetSummaryRepository.findByAccount(account);
//        List<RevenueGrowthSummary> revenueGrowthSummaries = revenueGrowthSummaryRepository.findByAccount(account);
//        List<PipelineState> pipelineStates = pipelineStateRepository.findByAccount(account);
//
//        Map<String, DataDTO> dataMap = new HashMap<>();
//
//        for (RevenueBudgetSummary rbs : revenueBudgetSummaries) {
//            String key = rbs.getQuarter() + rbs.getFinancialYear();
//            DataDTO data = dataMap.getOrDefault(key, new DataDTO(rbs.getQuarter(), rbs.getFinancialYear(), 0.0f, 0.0f, 0.0f));
//            data.setRvb_Forecast(rbs.getForecast());
//            dataMap.put(key, data);
//        }
//
//        for (RevenueGrowthSummary rgs : revenueGrowthSummaries) {
//            String key = rgs.getQuarter() + rgs.getFinancialYear();
//            DataDTO data = dataMap.getOrDefault(key, new DataDTO(rgs.getQuarter(), rgs.getFinancialYear(), 0.0f, 0.0f, 0.0f));
//            data.setRvg_Forecast(rgs.getForecast());
//            dataMap.put(key, data);
//        }
//
//        for (PipelineState ps : pipelineStates) {
//            String key = ps.getQuarter() + ps.getFinancialYear();
//            DataDTO data = dataMap.getOrDefault(key, new DataDTO(ps.getQuarter(), ps.getFinancialYear(), 0.0f, 0.0f, 0.0f));
//            data.setTotalPipelineSum(ps.getSumOfPipeline_total());
//            dataMap.put(key, data);
//        }
//
//        currentDataList.addAll(dataMap.values());
//        return currentDataList;
//    }
//
//    private List<DataDTO> getPreviousDataForAccount(String account) {
//        List<DataDTO> previousDataList = new ArrayList<>();
//
//        List<BaseLine_RevenueBudgetSummary> baselineRevenueBudgetSummaries = baseLineRevenueBudgetSummaryRepository.findByAccount(account);
//        List<BaseLine_RevenueGrowthSummary> baselineRevenueGrowthSummaries = baseLineRevenueGrowthSummaryRepository.findByAccount(account);
//        List<BaseLine_PipelineState> baselinePipelineStates = baseLinePipelineStateRepository.findByAccount(account);
//
//        Map<String, DataDTO> dataMap = new HashMap<>();
//
//        for (BaseLine_RevenueBudgetSummary brbs : baselineRevenueBudgetSummaries) {
//            String key = brbs.getQuarter() + brbs.getFinancialYear();
//            DataDTO data = dataMap.getOrDefault(key, new DataDTO(brbs.getQuarter(), brbs.getFinancialYear(), 0.0f, 0.0f, 0.0f));
//            data.setRvb_Forecast(brbs.getForecast());
//            dataMap.put(key, data);
//        }
//
//        for (BaseLine_RevenueGrowthSummary brgs : baselineRevenueGrowthSummaries) {
//            String key = brgs.getQuarter() + brgs.getFinancialYear();
//            DataDTO data = dataMap.getOrDefault(key, new DataDTO(brgs.getQuarter(), brgs.getFinancialYear(), 0.0f, 0.0f, 0.0f));
//            data.setRvg_Forecast(brgs.getForecast());
//            dataMap.put(key, data);
//        }
//
//        for (BaseLine_PipelineState bps : baselinePipelineStates) {
//            String key = bps.getQuarter() + bps.getFinancialYear();
//            DataDTO data = dataMap.getOrDefault(key, new DataDTO(bps.getQuarter(), bps.getFinancialYear(), 0.0f, 0.0f, 0.0f));
//            data.setTotalPipelineSum(bps.getSumOfPipeline_total());
//            dataMap.put(key, data);
//        }
//
//        previousDataList.addAll(dataMap.values());
//        return previousDataList;
//    }




    public Map<String, Object> getAccountData(List<String> accountNames) {
        if (accountNames == null || accountNames.isEmpty()) {
            throw new IllegalArgumentException("Account list cannot be empty");
        }

        // Assuming only one account in the list for simplicity
        String account = accountNames.get(0);

        // Sets to hold unique values
        Set<String> deliveryDirectors = ConcurrentHashMap.newKeySet();
        Set<String> deliveryManagers = ConcurrentHashMap.newKeySet();

        // Maps to hold previous and current data
        Map<String, DataDTO> currentDataMap = new ConcurrentHashMap<>();
        Map<String, DataDTO> previousDataMap = new ConcurrentHashMap<>();

        // Fetch all necessary data in batch
        List<RevenueBudgetSummary> revenueBudgetSummaries = revenueBudgetSummaryRepository.findByAccount(account);
        List<RevenueGrowthSummary> revenueGrowthSummaries = revenueGrowthSummaryRepository.findByAccount(account);
        List<PipelineState> pipelineStates = pipelineStateRepository.findByAccount(account);
        List<BaseLine_RevenueBudgetSummary> baselineRevenueBudgetSummaries = baseLineRevenueBudgetSummaryRepository.findByAccount(account);
        List<BaseLine_RevenueGrowthSummary> baselineRevenueGrowthSummaries = baseLineRevenueGrowthSummaryRepository.findByAccount(account);
        List<BaseLine_PipelineState> baselinePipelineStates = baseLinePipelineStateRepository.findByAccount(account);

        // Collect delivery directors and managers
        collectDeliveryDirectorsAndManagers(
                revenueBudgetSummaries, revenueGrowthSummaries, pipelineStates,
                baselineRevenueBudgetSummaries, baselineRevenueGrowthSummaries, baselinePipelineStates,
                deliveryDirectors, deliveryManagers
        );

        // Process current and previous data
        processData(
                revenueBudgetSummaries, revenueGrowthSummaries, pipelineStates,
                baselineRevenueBudgetSummaries, baselineRevenueGrowthSummaries, baselinePipelineStates,
                currentDataMap, previousDataMap
        );

        // Determine current and previous year
        Set<Integer> allYears = new HashSet<>();
        allYears.addAll(currentDataMap.values().stream().map(DataDTO::getFinancialYear).collect(Collectors.toSet()));
        allYears.addAll(previousDataMap.values().stream().map(DataDTO::getFinancialYear).collect(Collectors.toSet()));

        int currentYear = allYears.stream().max(Integer::compare).orElse(Year.now().getValue());
        int previousYear = currentYear - 1;

        // Filter data for current and previous years
        List<DataDTO> filteredCurrentData = currentDataMap.values().stream()
                .filter(data -> data.getFinancialYear() == currentYear || data.getFinancialYear() == previousYear)
                .collect(Collectors.toList());

        List<DataDTO> filteredPreviousData = previousDataMap.values().stream()
                .filter(data -> data.getFinancialYear() == currentYear || data.getFinancialYear() == previousYear)
                .collect(Collectors.toList());

        // Prepare response
        Map<String, Object> response = new HashMap<>();
        response.put("deliveryDirector", new ArrayList<>(deliveryDirectors));
        response.put("deliveryManager", new ArrayList<>(deliveryManagers));
        response.put("currentData", filteredCurrentData);
        response.put("previousData", filteredPreviousData);
        response.put("account", accountNames);

        return response;
    }

    private void collectDeliveryDirectorsAndManagers(
            List<RevenueBudgetSummary> revenueBudgetSummaries, List<RevenueGrowthSummary> revenueGrowthSummaries, List<PipelineState> pipelineStates,
            List<BaseLine_RevenueBudgetSummary> baselineRevenueBudgetSummaries, List<BaseLine_RevenueGrowthSummary> baselineRevenueGrowthSummaries, List<BaseLine_PipelineState> baselinePipelineStates,
            Set<String> deliveryDirectors, Set<String> deliveryManagers
    ) {
        // Collect delivery directors and managers
        processDeliveryDirectorsAndManagers(revenueBudgetSummaries, deliveryDirectors, deliveryManagers);
        processDeliveryDirectorsAndManagers(revenueGrowthSummaries, deliveryDirectors, deliveryManagers);
        processDeliveryDirectorsAndManagers(pipelineStates, deliveryDirectors, deliveryManagers);
        processDeliveryDirectorsAndManagers(baselineRevenueBudgetSummaries, deliveryDirectors, deliveryManagers);
        processDeliveryDirectorsAndManagers(baselineRevenueGrowthSummaries, deliveryDirectors, deliveryManagers);
        processDeliveryDirectorsAndManagers(baselinePipelineStates, deliveryDirectors, deliveryManagers);
    }

    private void processDeliveryDirectorsAndManagers(
            List<?> summaries, Set<String> deliveryDirectors, Set<String> deliveryManagers
    ) {
        for (Object summary : summaries) {
            if (summary instanceof RevenueBudgetSummary) {
                RevenueBudgetSummary rbs = (RevenueBudgetSummary) summary;
                deliveryDirectors.add(rbs.getDeliveryDirector());
                deliveryManagers.add(rbs.getDeliveryManager());
            } else if (summary instanceof RevenueGrowthSummary) {
                RevenueGrowthSummary rgs = (RevenueGrowthSummary) summary;
                deliveryDirectors.add(rgs.getDeliveryDirector());
                deliveryManagers.add(rgs.getDeliveryManager());
            } else if (summary instanceof PipelineState) {
                PipelineState ps = (PipelineState) summary;
                deliveryDirectors.add(ps.getDeliveryDirector());
                deliveryManagers.add(ps.getDeliveryManager());
            } else if (summary instanceof BaseLine_RevenueBudgetSummary) {
                BaseLine_RevenueBudgetSummary brbs = (BaseLine_RevenueBudgetSummary) summary;
                deliveryDirectors.add(brbs.getDeliveryDirector());
                deliveryManagers.add(brbs.getDeliveryManager());
            } else if (summary instanceof BaseLine_RevenueGrowthSummary) {
                BaseLine_RevenueGrowthSummary brgs = (BaseLine_RevenueGrowthSummary) summary;
                deliveryDirectors.add(brgs.getDeliveryDirector());
                deliveryManagers.add(brgs.getDeliveryManager());
            } else if (summary instanceof BaseLine_PipelineState) {
                BaseLine_PipelineState bps = (BaseLine_PipelineState) summary;
                deliveryDirectors.add(bps.getDeliveryDirector());
                deliveryManagers.add(bps.getDeliveryManager());
            }
        }
    }

    private void processData(
            List<RevenueBudgetSummary> revenueBudgetSummaries, List<RevenueGrowthSummary> revenueGrowthSummaries, List<PipelineState> pipelineStates,
            List<BaseLine_RevenueBudgetSummary> baselineRevenueBudgetSummaries, List<BaseLine_RevenueGrowthSummary> baselineRevenueGrowthSummaries, List<BaseLine_PipelineState> baselinePipelineStates,
            Map<String, DataDTO> currentDataMap, Map<String, DataDTO> previousDataMap
    ) {
        // Process current data
        processSummaryData(revenueBudgetSummaries, currentDataMap);
        processSummaryData(revenueGrowthSummaries, currentDataMap);
        processSummaryData(pipelineStates, currentDataMap);

        // Process previous data
        processSummaryData(baselineRevenueBudgetSummaries, previousDataMap);
        processSummaryData(baselineRevenueGrowthSummaries, previousDataMap);
        processSummaryData(baselinePipelineStates, previousDataMap);
    }

    private void processSummaryData(List<?> summaries, Map<String, DataDTO> dataMap) {
        for (Object summary : summaries) {
            DataDTO data;
            String key;
            int financialYear;
            String quarter;

            if (summary instanceof RevenueBudgetSummary) {
                RevenueBudgetSummary rbs = (RevenueBudgetSummary) summary;
                financialYear = rbs.getFinancialYear();
                quarter = rbs.getQuarter();
                key = quarter + financialYear;
                data = dataMap.getOrDefault(key, new DataDTO(quarter, financialYear, 0.0f, 0.0f, 0.0f));
                data.setRvb_Forecast(rbs.getForecast());
                dataMap.put(key, data);
            } else if (summary instanceof RevenueGrowthSummary) {
                RevenueGrowthSummary rgs = (RevenueGrowthSummary) summary;
                financialYear = rgs.getFinancialYear();
                quarter = rgs.getQuarter();
                key = quarter + financialYear;
                data = dataMap.getOrDefault(key, new DataDTO(quarter, financialYear, 0.0f, 0.0f, 0.0f));
                data.setRvg_Forecast(rgs.getForecast());
                dataMap.put(key, data);
            } else if (summary instanceof PipelineState) {
                PipelineState ps = (PipelineState) summary;
                financialYear = ps.getFinancialYear();
                quarter = ps.getQuarter();
                key = quarter + financialYear;
                data = dataMap.getOrDefault(key, new DataDTO(quarter, financialYear, 0.0f, 0.0f, 0.0f));
                data.setTotalPipelineSum(ps.getSumOfPipeline_total());
                dataMap.put(key, data);
            } else if (summary instanceof BaseLine_RevenueBudgetSummary) {
                BaseLine_RevenueBudgetSummary brbs = (BaseLine_RevenueBudgetSummary) summary;
                financialYear = brbs.getFinancialYear();
                quarter = brbs.getQuarter();
                key = quarter + financialYear;
                data = dataMap.getOrDefault(key, new DataDTO(quarter, financialYear, 0.0f, 0.0f, 0.0f));
                data.setRvb_Forecast(brbs.getForecast());
                dataMap.put(key, data);
            } else if (summary instanceof BaseLine_RevenueGrowthSummary) {
                BaseLine_RevenueGrowthSummary brgs = (BaseLine_RevenueGrowthSummary) summary;
                financialYear = brgs.getFinancialYear();
                quarter = brgs.getQuarter();
                key = quarter + financialYear;
                data = dataMap.getOrDefault(key, new DataDTO(quarter, financialYear, 0.0f, 0.0f, 0.0f));
                data.setRvg_Forecast(brgs.getForecast());
                dataMap.put(key, data);
            } else if (summary instanceof BaseLine_PipelineState) {
                BaseLine_PipelineState bps = (BaseLine_PipelineState) summary;
                financialYear = bps.getFinancialYear();
                quarter = bps.getQuarter();
                key = quarter + financialYear;
                data = dataMap.getOrDefault(key, new DataDTO(quarter, financialYear, 0.0f, 0.0f, 0.0f));
                data.setTotalPipelineSum(bps.getSumOfPipeline_total());
                dataMap.put(key, data);
            }
        }
    }
















}