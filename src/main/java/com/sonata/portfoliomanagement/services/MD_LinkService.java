package com.sonata.portfoliomanagement.services;

import com.sonata.portfoliomanagement.interfaces.*;
import com.sonata.portfoliomanagement.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class MD_LinkService {



    @Autowired
    private RevenueBudgetSummaryRepository budgetSummaryRepository;

    @Autowired
    private RevenueGrowthSummaryRepository growthSummaryRepository;
    @Autowired
    private MD_CategoryRepository categoryRepo;

    @Autowired
    private MD_VerticalRepository verticalRepo;

    @Autowired
    private MD_ClassificationRepository classificationRepo;

    @Autowired
    private MD_DeliveryDirectorRepository deliveryDirectorRepo;

    @Autowired
    private MD_DeliveryManagerRepository deliveryManagerRepo;

    @Autowired
    private MD_AccountsRepository accountsRepo;

    @Autowired
    private MD_ProjectManagerRepository projectManagerRepo;

    @Autowired
    private MD_ProjectRepository projectRepo;

    public CompletableFuture<Map<String, List<String>>> getAllUniqueDataAsync() {
        CompletableFuture<List<String>> categoriesFuture = CompletableFuture.supplyAsync(this::getUniqueCategories);
        CompletableFuture<List<String>> verticalsFuture = CompletableFuture.supplyAsync(this::getUniqueVerticals);
        CompletableFuture<List<String>> classificationsFuture = CompletableFuture.supplyAsync(this::getUniqueClassifications);
        CompletableFuture<List<String>> deliveryDirectorsFuture = CompletableFuture.supplyAsync(this::getUniqueDeliveryDirectors);
        CompletableFuture<List<String>> deliveryManagersFuture = CompletableFuture.supplyAsync(this::getUniqueDeliveryManagers);
        CompletableFuture<List<String>> accountsFuture = CompletableFuture.supplyAsync(this::getUniqueAccounts);
        CompletableFuture<List<String>> projectManagersFuture = CompletableFuture.supplyAsync(this::getUniqueProjectManagers);
        CompletableFuture<List<String>> projectsFuture = CompletableFuture.supplyAsync(this::getUniqueProjects);

        return CompletableFuture.allOf(
                categoriesFuture, verticalsFuture, classificationsFuture,
                deliveryDirectorsFuture, deliveryManagersFuture, accountsFuture,
                projectManagersFuture, projectsFuture
        ).thenApply(v -> {
            Map<String, List<String>> uniqueDataMap = new HashMap<>();
            uniqueDataMap.put("categories", categoriesFuture.join());
            uniqueDataMap.put("verticals", verticalsFuture.join());
            uniqueDataMap.put("classifications", classificationsFuture.join());
            uniqueDataMap.put("deliveryDirectors", deliveryDirectorsFuture.join());
            uniqueDataMap.put("deliveryManagers", deliveryManagersFuture.join());
            uniqueDataMap.put("accounts", accountsFuture.join());
            uniqueDataMap.put("projectManagers", projectManagersFuture.join());
            uniqueDataMap.put("projects", projectsFuture.join());
            return uniqueDataMap;
        });
    }

    private List<String> getUniqueCategories() {
        return categoryRepo.findAll().stream()
                .map(MD_Category::getCategory)
                .distinct()
                .collect(Collectors.toList());
    }

    private List<String> getUniqueVerticals() {
        return verticalRepo.findAll().stream()
                .map(MD_Vertical::getVertical)
                .distinct()
                .collect(Collectors.toList());
    }

    private List<String> getUniqueClassifications() {
        return classificationRepo.findAll().stream()
                .map(MD_Classification::getClassification)
                .distinct()
                .collect(Collectors.toList());
    }

    private List<String> getUniqueDeliveryDirectors() {
        return deliveryDirectorRepo.findAll().stream()
                .map(MD_DeliveryDirector::getDeliveryDirector)
                .distinct()
                .collect(Collectors.toList());
    }

    private List<String> getUniqueDeliveryManagers() {
        return deliveryManagerRepo.findAll().stream()
                .map(MD_DeliveryManager::getDelivery_Managers)
                .distinct()
                .collect(Collectors.toList());
    }

    private List<String> getUniqueAccounts() {
        return accountsRepo.findAll().stream()
                .map(MD_Accounts::getAccounts)
                .distinct()
                .collect(Collectors.toList());
    }

    private List<String> getUniqueProjectManagers() {
        return projectManagerRepo.findAll().stream()
                .map(MD_ProjectManager::getProjectManager)
                .distinct()
                .collect(Collectors.toList());
    }

    private List<String> getUniqueProjects() {
        return projectRepo.findAll().stream()
                .map(MD_Project::getProject)
                .distinct()
                .collect(Collectors.toList());
    }



    public Integer getMaxFinancialYear() {
        Integer maxBudgetYear = budgetSummaryRepository.findMaxFinancialYear();
        Integer maxGrowthYear = growthSummaryRepository.findMaxFinancialYear();

        return (maxBudgetYear != null && maxGrowthYear != null) ?
                Math.min(maxBudgetYear, maxGrowthYear) : null;
    }
}