package com.sonata.portfoliomanagement.services;

import com.sonata.portfoliomanagement.interfaces.*;
import com.sonata.portfoliomanagement.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.*;
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



    public Map<String, Object> getDeliveryManagerData(List<String> deliveryManagerNames) {
        Set<String> deliveryDirectors = new HashSet<>();
        Set<String> accounts = new HashSet<>();
        Map<String, DataDTO> previousDataMap = new HashMap<>();
        Map<String, DataDTO> currentDataMap = new HashMap<>();

        // Temporary set to find the maximum year
        Set<Integer> allYears = new HashSet<>();

        for (String deliveryManagerName : deliveryManagerNames) {
            // Fetch all unique accounts for the DM
            Set<String> accountSet = new HashSet<>();
            accountSet.addAll(revenueBudgetSummaryRepository.findAccountsByDeliveryManager(deliveryManagerName));
            accountSet.addAll(revenueGrowthSummaryRepository.findAccountsByDeliveryManager(deliveryManagerName));
            accountSet.addAll(pipelineStateRepository.findAccountsByDeliveryManager(deliveryManagerName));
            accounts.addAll(accountSet);

            for (String account : accountSet) {
                // Fetch data for the current account and DM
                List<RevenueBudgetSummary> currentRevenueBudget = revenueBudgetSummaryRepository.findByDeliveryManagerAndAccount(deliveryManagerName, account);
                List<RevenueGrowthSummary> currentRevenueGrowth = revenueGrowthSummaryRepository.findByDeliveryManagerAndAccount(deliveryManagerName, account);
                List<PipelineState> currentPipelineState = pipelineStateRepository.findByDeliveryManagerAndAccount(deliveryManagerName, account);
                List<BaseLine_RevenueBudgetSummary> previousRevenueBudget = baseLineRevenueBudgetSummaryRepository.findByDeliveryManagerAndAccount(deliveryManagerName, account);
                List<BaseLine_RevenueGrowthSummary> previousRevenueGrowth = baseLineRevenueGrowthSummaryRepository.findByDeliveryManagerAndAccount(deliveryManagerName, account);
                List<BaseLine_PipelineState> previousPipelineState = baseLinePipelineStateRepository.findByDeliveryManagerAndAccount(deliveryManagerName, account);

                // Add years to the set
                currentRevenueBudget.forEach(rbs -> allYears.add(rbs.getFinancialYear()));
                currentRevenueGrowth.forEach(rgs -> allYears.add(rgs.getFinancialYear()));
                currentPipelineState.forEach(ps -> allYears.add(ps.getFinancialYear()));
                previousRevenueBudget.forEach(rbs -> allYears.add(rbs.getFinancialYear()));
                previousRevenueGrowth.forEach(rgs -> allYears.add(rgs.getFinancialYear()));
                previousPipelineState.forEach(ps -> allYears.add(ps.getFinancialYear()));
            }
        }

        // Find the maximum year and the previous year
        int currentYear = allYears.stream().max(Integer::compareTo).orElse(Year.now().getValue());
        int previousYear = currentYear - 1;

        for (String deliveryManagerName : deliveryManagerNames) {
            Set<String> accountSet = new HashSet<>();
            accountSet.addAll(revenueBudgetSummaryRepository.findAccountsByDeliveryManager(deliveryManagerName));
            accountSet.addAll(revenueGrowthSummaryRepository.findAccountsByDeliveryManager(deliveryManagerName));
            accountSet.addAll(pipelineStateRepository.findAccountsByDeliveryManager(deliveryManagerName));
            accounts.addAll(accountSet);

            for (String account : accountSet) {
                List<RevenueBudgetSummary> currentRevenueBudget = revenueBudgetSummaryRepository.findByDeliveryManagerAndAccount(deliveryManagerName, account);
                List<RevenueGrowthSummary> currentRevenueGrowth = revenueGrowthSummaryRepository.findByDeliveryManagerAndAccount(deliveryManagerName, account);
                List<PipelineState> currentPipelineState = pipelineStateRepository.findByDeliveryManagerAndAccount(deliveryManagerName, account);
                List<BaseLine_RevenueBudgetSummary> previousRevenueBudget = baseLineRevenueBudgetSummaryRepository.findByDeliveryManagerAndAccount(deliveryManagerName, account);
                List<BaseLine_RevenueGrowthSummary> previousRevenueGrowth = baseLineRevenueGrowthSummaryRepository.findByDeliveryManagerAndAccount(deliveryManagerName, account);
                List<BaseLine_PipelineState> previousPipelineState = baseLinePipelineStateRepository.findByDeliveryManagerAndAccount(deliveryManagerName, account);

                String dd = "";
                if (!currentRevenueBudget.isEmpty()) {
                    dd = currentRevenueBudget.get(0).getDeliveryDirector();
                } else if (!currentRevenueGrowth.isEmpty()) {
                    dd = currentRevenueGrowth.get(0).getDeliveryDirector();
                } else if (!currentPipelineState.isEmpty()) {
                    dd = currentPipelineState.get(0).getDeliveryDirector();
                }
                if (!dd.isEmpty()) {
                    deliveryDirectors.add(dd);
                }

                for (RevenueBudgetSummary rbs : currentRevenueBudget) {
                    if (rbs.getFinancialYear() == currentYear || rbs.getFinancialYear() == previousYear) {
                        String key = rbs.getFinancialYear() + "-" + rbs.getQuarter();
                        DataDTO data = currentDataMap.getOrDefault(key, new DataDTO());
                        data.setFinancialYear(rbs.getFinancialYear());
                        data.setQuarter(rbs.getQuarter());
                        data.setRvb_Forecast(data.getRvb_Forecast() + rbs.getForecast());
                        currentDataMap.put(key, data);
                    }
                }

                for (RevenueGrowthSummary rgs : currentRevenueGrowth) {
                    if (rgs.getFinancialYear() == currentYear || rgs.getFinancialYear() == previousYear) {
                        String key = rgs.getFinancialYear() + "-" + rgs.getQuarter();
                        DataDTO data = currentDataMap.getOrDefault(key, new DataDTO());
                        data.setFinancialYear(rgs.getFinancialYear());
                        data.setQuarter(rgs.getQuarter());
                        data.setRvg_Forecast(data.getRvg_Forecast() + rgs.getForecast());
                        currentDataMap.put(key, data);
                    }
                }

                for (PipelineState ps : currentPipelineState) {
                    if (ps.getFinancialYear() == currentYear || ps.getFinancialYear() == previousYear) {
                        String key = ps.getFinancialYear() + "-" + ps.getQuarter();
                        DataDTO data = currentDataMap.getOrDefault(key, new DataDTO());
                        data.setFinancialYear(ps.getFinancialYear());
                        data.setQuarter(ps.getQuarter());
                        data.setTotalPipelineSum(data.getTotalPipelineSum() + ps.getSumOfPipeline_total());
                        currentDataMap.put(key, data);
                    }
                }

                for (BaseLine_RevenueBudgetSummary rbs : previousRevenueBudget) {
                    if (rbs.getFinancialYear() == currentYear || rbs.getFinancialYear() == previousYear) {
                        String key = rbs.getFinancialYear() + "-" + rbs.getQuarter();
                        DataDTO data = previousDataMap.getOrDefault(key, new DataDTO());
                        data.setFinancialYear(rbs.getFinancialYear());
                        data.setQuarter(rbs.getQuarter());
                        data.setRvb_Forecast(data.getRvb_Forecast() + rbs.getForecast());
                        previousDataMap.put(key, data);
                    }
                }

                for (BaseLine_RevenueGrowthSummary rgs : previousRevenueGrowth) {
                    if (rgs.getFinancialYear() == currentYear || rgs.getFinancialYear() == previousYear) {
                        String key = rgs.getFinancialYear() + "-" + rgs.getQuarter();
                        DataDTO data = previousDataMap.getOrDefault(key, new DataDTO());
                        data.setFinancialYear(rgs.getFinancialYear());
                        data.setQuarter(rgs.getQuarter());
                        data.setRvg_Forecast(data.getRvg_Forecast() + rgs.getForecast());
                        previousDataMap.put(key, data);
                    }
                }

                for (BaseLine_PipelineState ps : previousPipelineState) {
                    if (ps.getFinancialYear() == currentYear || ps.getFinancialYear() == previousYear) {
                        String key = ps.getFinancialYear() + "-" + ps.getQuarter();
                        DataDTO data = previousDataMap.getOrDefault(key, new DataDTO());
                        data.setFinancialYear(ps.getFinancialYear());
                        data.setQuarter(ps.getQuarter());
                        data.setTotalPipelineSum(data.getTotalPipelineSum() + ps.getSumOfPipeline_total());
                        previousDataMap.put(key, data);
                    }
                }
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("deliveryDirector", new ArrayList<>(deliveryDirectors));
        response.put("deliveryManager", deliveryManagerNames);
        response.put("account", new ArrayList<>(accounts));
        response.put("previousData", new ArrayList<>(previousDataMap.values()));
        response.put("currentData", new ArrayList<>(currentDataMap.values()));

        return response;
    }


    public Map<String, Object> getDeliveryDirectorData(List<String> deliveryDirectorNames) {
        Set<String> deliveryManagers = new HashSet<>();
        Set<String> accounts = new HashSet<>();
        Map<String, DataDTO> previousDataMap = new HashMap<>();
        Map<String, DataDTO> currentDataMap = new HashMap<>();

        // Temporary set to find the maximum year
        Set<Integer> allYears = new HashSet<>();

        for (String deliveryDirectorName : deliveryDirectorNames) {
            // Fetch all unique accounts for the DD
            Set<String> accountSet = new HashSet<>();
            accountSet.addAll(revenueBudgetSummaryRepository.findAccountsByDeliveryDirector(deliveryDirectorName));
            accountSet.addAll(revenueGrowthSummaryRepository.findAccountsByDeliveryDirector(deliveryDirectorName));
            accountSet.addAll(pipelineStateRepository.findAccountsByDeliveryDirector(deliveryDirectorName));
            accounts.addAll(accountSet);

            for (String account : accountSet) {
                // Fetch data for the current account and DD
                List<RevenueBudgetSummary> currentRevenueBudget = revenueBudgetSummaryRepository.findByDeliveryDirectorAndAccount(deliveryDirectorName, account);
                List<RevenueGrowthSummary> currentRevenueGrowth = revenueGrowthSummaryRepository.findByDeliveryDirectorAndAccount(deliveryDirectorName, account);
                List<PipelineState> currentPipelineState = pipelineStateRepository.findByDeliveryDirectorAndAccount(deliveryDirectorName, account);
                List<BaseLine_RevenueBudgetSummary> previousRevenueBudget = baseLineRevenueBudgetSummaryRepository.findByDeliveryDirectorAndAccount(deliveryDirectorName, account);
                List<BaseLine_RevenueGrowthSummary> previousRevenueGrowth = baseLineRevenueGrowthSummaryRepository.findByDeliveryDirectorAndAccount(deliveryDirectorName, account);
                List<BaseLine_PipelineState> previousPipelineState = baseLinePipelineStateRepository.findByDeliveryDirectorAndAccount(deliveryDirectorName, account);

                // Add years to the set
                currentRevenueBudget.forEach(rbs -> allYears.add(rbs.getFinancialYear()));
                currentRevenueGrowth.forEach(rgs -> allYears.add(rgs.getFinancialYear()));
                currentPipelineState.forEach(ps -> allYears.add(ps.getFinancialYear()));
                previousRevenueBudget.forEach(rbs -> allYears.add(rbs.getFinancialYear()));
                previousRevenueGrowth.forEach(rgs -> allYears.add(rgs.getFinancialYear()));
                previousPipelineState.forEach(ps -> allYears.add(ps.getFinancialYear()));
            }
        }

        // Find the maximum year and the previous year
        int currentYear = allYears.stream().max(Integer::compareTo).orElse(Year.now().getValue());
        int previousYear = currentYear - 1;

        for (String deliveryDirectorName : deliveryDirectorNames) {
            Set<String> accountSet = new HashSet<>();
            accountSet.addAll(revenueBudgetSummaryRepository.findAccountsByDeliveryDirector(deliveryDirectorName));
            accountSet.addAll(revenueGrowthSummaryRepository.findAccountsByDeliveryDirector(deliveryDirectorName));
            accountSet.addAll(pipelineStateRepository.findAccountsByDeliveryDirector(deliveryDirectorName));
            accounts.addAll(accountSet);

            for (String account : accountSet) {
                List<RevenueBudgetSummary> currentRevenueBudget = revenueBudgetSummaryRepository.findByDeliveryDirectorAndAccount(deliveryDirectorName, account);
                List<RevenueGrowthSummary> currentRevenueGrowth = revenueGrowthSummaryRepository.findByDeliveryDirectorAndAccount(deliveryDirectorName, account);
                List<PipelineState> currentPipelineState = pipelineStateRepository.findByDeliveryDirectorAndAccount(deliveryDirectorName, account);
                List<BaseLine_RevenueBudgetSummary> previousRevenueBudget = baseLineRevenueBudgetSummaryRepository.findByDeliveryDirectorAndAccount(deliveryDirectorName, account);
                List<BaseLine_RevenueGrowthSummary> previousRevenueGrowth = baseLineRevenueGrowthSummaryRepository.findByDeliveryDirectorAndAccount(deliveryDirectorName, account);
                List<BaseLine_PipelineState> previousPipelineState = baseLinePipelineStateRepository.findByDeliveryDirectorAndAccount(deliveryDirectorName, account);

                String dm = "";
                if (!currentRevenueBudget.isEmpty()) {
                    dm = currentRevenueBudget.get(0).getDeliveryManager();
                } else if (!currentRevenueGrowth.isEmpty()) {
                    dm = currentRevenueGrowth.get(0).getDeliveryManager();
                } else if (!currentPipelineState.isEmpty()) {
                    dm = currentPipelineState.get(0).getDeliveryManager();
                }
                if (!dm.isEmpty()) {
                    deliveryManagers.add(dm);
                }

                for (RevenueBudgetSummary rbs : currentRevenueBudget) {
                    if (rbs.getFinancialYear() == currentYear || rbs.getFinancialYear() == previousYear) {
                        String key = rbs.getFinancialYear() + "-" + rbs.getQuarter();
                        DataDTO data = currentDataMap.getOrDefault(key, new DataDTO());
                        data.setFinancialYear(rbs.getFinancialYear());
                        data.setQuarter(rbs.getQuarter());
                        data.setRvb_Forecast(data.getRvb_Forecast() + rbs.getForecast());
                        currentDataMap.put(key, data);
                    }
                }

                for (RevenueGrowthSummary rgs : currentRevenueGrowth) {
                    if (rgs.getFinancialYear() == currentYear || rgs.getFinancialYear() == previousYear) {
                        String key = rgs.getFinancialYear() + "-" + rgs.getQuarter();
                        DataDTO data = currentDataMap.getOrDefault(key, new DataDTO());
                        data.setFinancialYear(rgs.getFinancialYear());
                        data.setQuarter(rgs.getQuarter());
                        data.setRvg_Forecast(data.getRvg_Forecast() + rgs.getForecast());
                        currentDataMap.put(key, data);
                    }
                }

                for (PipelineState ps : currentPipelineState) {
                    if (ps.getFinancialYear() == currentYear || ps.getFinancialYear() == previousYear) {
                        String key = ps.getFinancialYear() + "-" + ps.getQuarter();
                        DataDTO data = currentDataMap.getOrDefault(key, new DataDTO());
                        data.setFinancialYear(ps.getFinancialYear());
                        data.setQuarter(ps.getQuarter());
                        data.setTotalPipelineSum(data.getTotalPipelineSum() + ps.getSumOfPipeline_total());
                        currentDataMap.put(key, data);
                    }
                }

                for (BaseLine_RevenueBudgetSummary rbs : previousRevenueBudget) {
                    if (rbs.getFinancialYear() == currentYear || rbs.getFinancialYear() == previousYear) {
                        String key = rbs.getFinancialYear() + "-" + rbs.getQuarter();
                        DataDTO data = previousDataMap.getOrDefault(key, new DataDTO());
                        data.setFinancialYear(rbs.getFinancialYear());
                        data.setQuarter(rbs.getQuarter());
                        data.setRvb_Forecast(data.getRvb_Forecast() + rbs.getForecast());
                        previousDataMap.put(key, data);
                    }
                }

                for (BaseLine_RevenueGrowthSummary rgs : previousRevenueGrowth) {
                    if (rgs.getFinancialYear() == currentYear || rgs.getFinancialYear() == previousYear) {
                        String key = rgs.getFinancialYear() + "-" + rgs.getQuarter();
                        DataDTO data = previousDataMap.getOrDefault(key, new DataDTO());
                        data.setFinancialYear(rgs.getFinancialYear());
                        data.setQuarter(rgs.getQuarter());
                        data.setRvg_Forecast(data.getRvg_Forecast() + rgs.getForecast());
                        previousDataMap.put(key, data);
                    }
                }

                for (BaseLine_PipelineState ps : previousPipelineState) {
                    if (ps.getFinancialYear() == currentYear || ps.getFinancialYear() == previousYear) {
                        String key = ps.getFinancialYear() + "-" + ps.getQuarter();
                        DataDTO data = previousDataMap.getOrDefault(key, new DataDTO());
                        data.setFinancialYear(ps.getFinancialYear());
                        data.setQuarter(ps.getQuarter());
                        data.setTotalPipelineSum(data.getTotalPipelineSum() + ps.getSumOfPipeline_total());
                        previousDataMap.put(key, data);
                    }
                }
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("deliveryDirector", deliveryDirectorNames);
        response.put("deliveryManager", new ArrayList<>(deliveryManagers));
        response.put("account", new ArrayList<>(accounts));
        response.put("previousData", new ArrayList<>(previousDataMap.values()));
        response.put("currentData", new ArrayList<>(currentDataMap.values()));

        return response;
    }




    //service method for getByAccount gets data by account for 2 years-->

    public Map<String, Object> getAccountData(List<String> accountNames) {
        Map<String, Object> response = new HashMap<>();

        if (accountNames == null || accountNames.isEmpty()) {
            throw new IllegalArgumentException("Account list cannot be empty");
        }

        String account = accountNames.get(0); // Assuming only one account in the list for simplicity

        Set<String> deliveryDirectors = new HashSet<>();
        Set<String> deliveryManagers = new HashSet<>();

        List<String> currentDDs = revenueBudgetSummaryRepository.findDeliveryDirectorsByAccount(account);
        currentDDs.addAll(revenueGrowthSummaryRepository.findDeliveryDirectorsByAccount(account));
        currentDDs.addAll(pipelineStateRepository.findDeliveryDirectorsByAccount(account));

        List<String> baselineDDs = baseLineRevenueBudgetSummaryRepository.findDeliveryDirectorsByAccount(account);
        baselineDDs.addAll(baseLineRevenueGrowthSummaryRepository.findDeliveryDirectorsByAccount(account));
        baselineDDs.addAll(baseLinePipelineStateRepository.findDeliveryDirectorsByAccount(account));

        deliveryDirectors.addAll(currentDDs);
        deliveryDirectors.addAll(baselineDDs);

        List<String> currentDMs = revenueBudgetSummaryRepository.findDeliveryManagersByAccount(account);
        currentDMs.addAll(revenueGrowthSummaryRepository.findDeliveryManagersByAccount(account));
        currentDMs.addAll(pipelineStateRepository.findDeliveryManagersByAccount(account));

        List<String> baselineDMs = baseLineRevenueBudgetSummaryRepository.findDeliveryManagersByAccount(account);
        baselineDMs.addAll(baseLineRevenueGrowthSummaryRepository.findDeliveryManagersByAccount(account));
        baselineDMs.addAll(baseLinePipelineStateRepository.findDeliveryManagersByAccount(account));

        deliveryManagers.addAll(currentDMs);
        deliveryManagers.addAll(baselineDMs);

        List<DataDTO> currentData = getCurrentDataForAccount(account);
        List<DataDTO> previousData = getPreviousDataForAccount(account);

        // Implementing logic for fetching only data for currentYear and previousYear
        Set<Integer> allYears = new HashSet<>();
        currentData.forEach(data -> allYears.add(data.getFinancialYear()));
        previousData.forEach(data -> allYears.add(data.getFinancialYear()));

        int currentYear = allYears.stream().max(Integer::compareTo).orElse(Year.now().getValue());
        int previousYear = currentYear - 1;

        List<DataDTO> filteredCurrentData = currentData.stream()
                .filter(data -> data.getFinancialYear() == currentYear || data.getFinancialYear() == previousYear)
                .collect(Collectors.toList());

        List<DataDTO> filteredPreviousData = previousData.stream()
                .filter(data -> data.getFinancialYear() == currentYear || data.getFinancialYear() == previousYear)
                .collect(Collectors.toList());

        response.put("deliveryDirector", new ArrayList<>(deliveryDirectors));
        response.put("deliveryManager", new ArrayList<>(deliveryManagers));
        response.put("currentData", filteredCurrentData);
        response.put("previousData", filteredPreviousData);
        response.put("account", accountNames);

        return response;
    }


    private List<DataDTO> getCurrentDataForAccount(String account) {
        List<DataDTO> currentDataList = new ArrayList<>();

        List<RevenueBudgetSummary> revenueBudgetSummaries = revenueBudgetSummaryRepository.findByAccount(account);
        List<RevenueGrowthSummary> revenueGrowthSummaries = revenueGrowthSummaryRepository.findByAccount(account);
        List<PipelineState> pipelineStates = pipelineStateRepository.findByAccount(account);

        Map<String, DataDTO> dataMap = new HashMap<>();

        for (RevenueBudgetSummary rbs : revenueBudgetSummaries) {
            String key = rbs.getQuarter() + rbs.getFinancialYear();
            DataDTO data = dataMap.getOrDefault(key, new DataDTO(rbs.getQuarter(), rbs.getFinancialYear(), 0.0f, 0.0f, 0.0f));
            data.setRvb_Forecast(rbs.getForecast());
            dataMap.put(key, data);
        }

        for (RevenueGrowthSummary rgs : revenueGrowthSummaries) {
            String key = rgs.getQuarter() + rgs.getFinancialYear();
            DataDTO data = dataMap.getOrDefault(key, new DataDTO(rgs.getQuarter(), rgs.getFinancialYear(), 0.0f, 0.0f, 0.0f));
            data.setRvg_Forecast(rgs.getForecast());
            dataMap.put(key, data);
        }

        for (PipelineState ps : pipelineStates) {
            String key = ps.getQuarter() + ps.getFinancialYear();
            DataDTO data = dataMap.getOrDefault(key, new DataDTO(ps.getQuarter(), ps.getFinancialYear(), 0.0f, 0.0f, 0.0f));
            data.setTotalPipelineSum(ps.getSumOfPipeline_total());
            dataMap.put(key, data);
        }

        currentDataList.addAll(dataMap.values());
        return currentDataList;
    }

    private List<DataDTO> getPreviousDataForAccount(String account) {
        List<DataDTO> previousDataList = new ArrayList<>();

        List<BaseLine_RevenueBudgetSummary> baselineRevenueBudgetSummaries = baseLineRevenueBudgetSummaryRepository.findByAccount(account);
        List<BaseLine_RevenueGrowthSummary> baselineRevenueGrowthSummaries = baseLineRevenueGrowthSummaryRepository.findByAccount(account);
        List<BaseLine_PipelineState> baselinePipelineStates = baseLinePipelineStateRepository.findByAccount(account);

        Map<String, DataDTO> dataMap = new HashMap<>();

        for (BaseLine_RevenueBudgetSummary brbs : baselineRevenueBudgetSummaries) {
            String key = brbs.getQuarter() + brbs.getFinancialYear();
            DataDTO data = dataMap.getOrDefault(key, new DataDTO(brbs.getQuarter(), brbs.getFinancialYear(), 0.0f, 0.0f, 0.0f));
            data.setRvb_Forecast(brbs.getForecast());
            dataMap.put(key, data);
        }

        for (BaseLine_RevenueGrowthSummary brgs : baselineRevenueGrowthSummaries) {
            String key = brgs.getQuarter() + brgs.getFinancialYear();
            DataDTO data = dataMap.getOrDefault(key, new DataDTO(brgs.getQuarter(), brgs.getFinancialYear(), 0.0f, 0.0f, 0.0f));
            data.setRvg_Forecast(brgs.getForecast());
            dataMap.put(key, data);
        }

        for (BaseLine_PipelineState bps : baselinePipelineStates) {
            String key = bps.getQuarter() + bps.getFinancialYear();
            DataDTO data = dataMap.getOrDefault(key, new DataDTO(bps.getQuarter(), bps.getFinancialYear(), 0.0f, 0.0f, 0.0f));
            data.setTotalPipelineSum(bps.getSumOfPipeline_total());
            dataMap.put(key, data);
        }

        previousDataList.addAll(dataMap.values());
        return previousDataList;
    }

















}