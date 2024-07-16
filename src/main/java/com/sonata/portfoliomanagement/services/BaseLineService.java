package com.sonata.portfoliomanagement.services;

import com.sonata.portfoliomanagement.interfaces.*;
import com.sonata.portfoliomanagement.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.*;

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




    public List<DeliveryManagerDataDTO> getAccountData(List<String> accountNames) {
        List<DeliveryManagerDataDTO> responseList = new ArrayList<>();

        // Temporary set to find the maximum year
        Set<Integer> allYears = new HashSet<>();

        for (String accountName : accountNames) {
            // Fetch all unique delivery managers for the account
            Set<String> deliveryManagers = new HashSet<>();
            deliveryManagers.addAll(revenueBudgetSummaryRepository.findDeliveryManagersByAccount(accountName));
            deliveryManagers.addAll(revenueGrowthSummaryRepository.findDeliveryManagersByAccount(accountName));
            deliveryManagers.addAll(pipelineStateRepository.findDeliveryManagersByAccount(accountName));

            for (String deliveryManager : deliveryManagers) {
                // Fetch data for the current delivery manager and account
                List<RevenueBudgetSummary> currentRevenueBudget = revenueBudgetSummaryRepository.findByAccountAndDeliveryManager(accountName, deliveryManager);
                List<RevenueGrowthSummary> currentRevenueGrowth = revenueGrowthSummaryRepository.findByAccountAndDeliveryManager(accountName, deliveryManager);
                List<PipelineState> currentPipelineState = pipelineStateRepository.findByAccountAndDeliveryManager(accountName, deliveryManager);
                List<BaseLine_RevenueBudgetSummary> previousRevenueBudget = baseLineRevenueBudgetSummaryRepository.findByAccountAndDeliveryManager(accountName, deliveryManager);
                List<BaseLine_RevenueGrowthSummary> previousRevenueGrowth = baseLineRevenueGrowthSummaryRepository.findByAccountAndDeliveryManager(accountName, deliveryManager);
                List<BaseLine_PipelineState> previousPipelineState = baseLinePipelineStateRepository.findByAccountAndDeliveryManager(accountName, deliveryManager);

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

        for (String accountName : accountNames) {
            Map<String, DeliveryManagerDataDTO> accountDataMap = new HashMap<>();

            // Fetch all unique delivery managers for the account
            Set<String> deliveryManagers = new HashSet<>();
            deliveryManagers.addAll(revenueBudgetSummaryRepository.findDeliveryManagersByAccount(accountName));
            deliveryManagers.addAll(revenueGrowthSummaryRepository.findDeliveryManagersByAccount(accountName));
            deliveryManagers.addAll(pipelineStateRepository.findDeliveryManagersByAccount(accountName));

            for (String deliveryManager : deliveryManagers) {
                // Fetch data for the current delivery manager and account
                List<RevenueBudgetSummary> currentRevenueBudget = revenueBudgetSummaryRepository.findByAccountAndDeliveryManager(accountName, deliveryManager);
                List<RevenueGrowthSummary> currentRevenueGrowth = revenueGrowthSummaryRepository.findByAccountAndDeliveryManager(accountName, deliveryManager);
                List<PipelineState> currentPipelineState = pipelineStateRepository.findByAccountAndDeliveryManager(accountName, deliveryManager);
                List<BaseLine_RevenueBudgetSummary> previousRevenueBudget = baseLineRevenueBudgetSummaryRepository.findByAccountAndDeliveryManager(accountName, deliveryManager);
                List<BaseLine_RevenueGrowthSummary> previousRevenueGrowth = baseLineRevenueGrowthSummaryRepository.findByAccountAndDeliveryManager(accountName, deliveryManager);
                List<BaseLine_PipelineState> previousPipelineState = baseLinePipelineStateRepository.findByAccountAndDeliveryManager(accountName, deliveryManager);

                // Extract DD name from current data
                String dd = "";
                if (!currentRevenueBudget.isEmpty()) {
                    dd = currentRevenueBudget.get(0).getDeliveryDirector();
                } else if (!currentRevenueGrowth.isEmpty()) {
                    dd = currentRevenueGrowth.get(0).getDeliveryDirector();
                } else if (!currentPipelineState.isEmpty()) {
                    dd = currentPipelineState.get(0).getDeliveryDirector();
                }

                DeliveryManagerDataDTO accountData = accountDataMap.getOrDefault(dd, new DeliveryManagerDataDTO());
                accountData.setDeliveryDirector(dd);
                accountData.setAccount(accountName);

                List<String> dms = accountData.getDeliveryManager() != null ? accountData.getDeliveryManager() : new ArrayList<>();
                dms.add(deliveryManager);
                accountData.setDeliveryManager(dms);

                // Aggregating current data
                Map<String, DataDTO> currentDataMap = new HashMap<>();
                for (RevenueBudgetSummary rbs : currentRevenueBudget) {
                    if (rbs.getFinancialYear() == currentYear || rbs.getFinancialYear() == previousYear) {
                        String key = rbs.getFinancialYear() + "-" + rbs.getQuarter();
                        DataDTO data = currentDataMap.getOrDefault(key, new DataDTO());
                        data.setFinancialYear(rbs.getFinancialYear());
                        data.setQuarter(rbs.getQuarter());
                        data.setRvb_Forecast(data.getRvb_Forecast() + rbs.getForecast()); // Summing forecast
                        currentDataMap.put(key, data);
                    }
                }

                for (RevenueGrowthSummary rgs : currentRevenueGrowth) {
                    if (rgs.getFinancialYear() == currentYear || rgs.getFinancialYear() == previousYear) {
                        String key = rgs.getFinancialYear() + "-" + rgs.getQuarter();
                        DataDTO data = currentDataMap.getOrDefault(key, new DataDTO());
                        data.setFinancialYear(rgs.getFinancialYear());
                        data.setQuarter(rgs.getQuarter());
                        data.setRvg_Forecast(data.getRvg_Forecast() + rgs.getForecast()); // Summing forecast
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

                accountData.setCurrent(new ArrayList<>(currentDataMap.values()));

                // Aggregating previous data
                Map<String, DataDTO> previousDataMap = new HashMap<>();
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

                accountData.setPrevious(new ArrayList<>(previousDataMap.values()));
                accountDataMap.put(dd, accountData);
            }

            responseList.addAll(accountDataMap.values());
        }

        return responseList;
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


















}