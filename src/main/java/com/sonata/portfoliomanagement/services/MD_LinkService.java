package com.sonata.portfoliomanagement.services;

import com.sonata.portfoliomanagement.interfaces.*;
import com.sonata.portfoliomanagement.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MD_LinkService {

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

    public Map<String, List<String>> getAllUniqueData() {
        Map<String, List<String>> uniqueDataMap = new HashMap<>();

        uniqueDataMap.put("verticals", getUniqueVerticals());
        uniqueDataMap.put("classifications", getUniqueClassifications());
        uniqueDataMap.put("deliveryDirectors", getUniqueDeliveryDirectors());
        uniqueDataMap.put("deliveryManagers", getUniqueDeliveryManagers());
        uniqueDataMap.put("accounts", getUniqueAccounts());
        uniqueDataMap.put("projectManagers", getUniqueProjectManagers());
        uniqueDataMap.put("projects", getUniqueProjects());
        uniqueDataMap.put("categories", getUniqueCategories());

        return uniqueDataMap;
    }

    private List<String> getUniqueCategories() {
        List<MD_Category> categories = categoryRepo.findAll();
        Set<String> uniqueCategoriesSet = new HashSet<>();
        List<String> uniqueCategories = new ArrayList<>();

        for (MD_Category category : categories) {
            if (uniqueCategoriesSet.add(category.getCategory())) {
                uniqueCategories.add(category.getCategory());
            }
        }
        return uniqueCategories;
    }

    private List<String> getUniqueVerticals() {
        List<MD_Vertical> verticals = verticalRepo.findAll();
        Set<String> uniqueVerticalsSet = new HashSet<>();
        List<String> uniqueVerticals = new ArrayList<>();

        for (MD_Vertical vertical : verticals) {
            if (uniqueVerticalsSet.add(vertical.getVertical())) {
                uniqueVerticals.add(vertical.getVertical());
            }
        }
        return uniqueVerticals;
    }

    private List<String> getUniqueClassifications() {
        List<MD_Classification> classifications = classificationRepo.findAll();
        Set<String> uniqueClassificationsSet = new HashSet<>();
        List<String> uniqueClassifications = new ArrayList<>();

        for (MD_Classification classification : classifications) {
            if (uniqueClassificationsSet.add(classification.getClassification())) {
                uniqueClassifications.add(classification.getClassification());
            }
        }
        return uniqueClassifications;
    }

    private List<String> getUniqueDeliveryDirectors() {
        List<MD_DeliveryDirector> deliveryDirectors = deliveryDirectorRepo.findAll();
        Set<String> uniqueDeliveryDirectorsSet = new HashSet<>();
        List<String> uniqueDeliveryDirectors = new ArrayList<>();

        for (MD_DeliveryDirector deliveryDirector : deliveryDirectors) {
            if (uniqueDeliveryDirectorsSet.add(deliveryDirector.getDeliveryDirector())) {
                uniqueDeliveryDirectors.add(deliveryDirector.getDeliveryDirector());
            }
        }
        return uniqueDeliveryDirectors;
    }

    private List<String> getUniqueDeliveryManagers() {
        List<MD_DeliveryManager> deliveryManagers = deliveryManagerRepo.findAll();
        Set<String> uniqueDeliveryManagersSet = new HashSet<>();
        List<String> uniqueDeliveryManagers = new ArrayList<>();

        for (MD_DeliveryManager deliveryManager : deliveryManagers) {
            if (uniqueDeliveryManagersSet.add(deliveryManager.getDelivery_Managers())) {
                uniqueDeliveryManagers.add(deliveryManager.getDelivery_Managers());
            }
        }
        return uniqueDeliveryManagers;
    }

    private List<String> getUniqueAccounts() {
        List<MD_Accounts> accounts = accountsRepo.findAll();
        Set<String> uniqueAccountsSet = new HashSet<>();
        List<String> uniqueAccounts = new ArrayList<>();

        for (MD_Accounts account : accounts) {
            if (uniqueAccountsSet.add(account.getAccounts())) {
                uniqueAccounts.add(account.getAccounts());
            }
        }
        return uniqueAccounts;
    }

    private List<String> getUniqueProjectManagers() {
        List<MD_ProjectManager> projectManagers = projectManagerRepo.findAll();
        Set<String> uniqueProjectManagersSet = new HashSet<>();
        List<String> uniqueProjectManagers = new ArrayList<>();

        for (MD_ProjectManager projectManager : projectManagers) {
            if (uniqueProjectManagersSet.add(projectManager.getProjectManager())) {
                uniqueProjectManagers.add(projectManager.getProjectManager());
            }
        }
        return uniqueProjectManagers;
    }

    private List<String> getUniqueProjects() {
        List<MD_Project> projects = projectRepo.findAll();
        Set<String> uniqueProjectsSet = new HashSet<>();
        List<String> uniqueProjects = new ArrayList<>();

        for (MD_Project project : projects) {
            if (uniqueProjectsSet.add(project.getProject())) {
                uniqueProjects.add(project.getProject());
            }
        }
        return uniqueProjects;
    }
}