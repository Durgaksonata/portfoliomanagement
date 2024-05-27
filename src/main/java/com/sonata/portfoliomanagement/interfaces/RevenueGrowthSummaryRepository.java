package com.sonata.portfoliomanagement.interfaces;


import com.sonata.portfoliomanagement.model.RevenueBudgetSummary;
import com.sonata.portfoliomanagement.model.RevenueDTO;
import com.sonata.portfoliomanagement.model.RevenueGrowthSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RevenueGrowthSummaryRepository extends JpaRepository<RevenueGrowthSummary,Integer> {
    RevenueGrowthSummary findByVerticalAndClassificationAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarter(String vertical, String classification, String deliveryManager, String account, String projectManager, String projectName, int financialYear, String quarter);

    List<RevenueGrowthSummary> findByVerticalIn(List<String> verticalNames);


    List<RevenueGrowthSummary> findByDeliveryManagerInAndClassificationIn(List<String> dmNames, List<String> classifications);

    List<RevenueGrowthSummary> findByAccountInAndClassificationIn(List<String> accountNames, List<String> classifications);

    List<RevenueGrowthSummary> findByProjectManagerInAndClassificationIn(List<String> pmNames, List<String> classifications);

    List<RevenueGrowthSummary> findByProjectNameIn(List<String> projectNames);

    List<RevenueGrowthSummary> findByFinancialYearInAndProjectNameInAndVerticalInAndClassificationInAndDeliveryManagerInAndAccountInAndProjectManagerInAndQuarterIn(List<Integer> financialYear, List<String> projectList, List<String> verticalList, List<String> classificationList, List<String> dmList, List<String> accountList, List<String> pmList, List<String> quarterList);

    List<RevenueGrowthSummary> findByFinancialYear(int financialYear);

    List<RevenueGrowthSummary> findAllByVertical(String vertical);

    List<RevenueGrowthSummary> findAllByClassification(String classification);

    List<RevenueGrowthSummary> findBydeliveryManager(String deliveryManager);

    List<RevenueGrowthSummary> findByAccount(String account);

    List<RevenueGrowthSummary> findByProjectName(String project);

    List<RevenueGrowthSummary> findByProjectManager(String projectManager);

    List<RevenueGrowthSummary> findByaccount(String getList);


    List<RevenueGrowthSummary> findByquarter(String quarter);

    List<RevenueGrowthSummary> findByVertical(String getList);

    List<RevenueGrowthSummary> findByclassification(String getList);

    List<RevenueGrowthSummary> findByprojectManager(String getList);

    List<RevenueGrowthSummary> findByAccountIn(List<String> accountNames);

    List<RevenueGrowthSummary> findByProjectManagerIn(List<String> pmNames);

    List<RevenueGrowthSummary> findByDeliveryManagerIn(List<String> dmNames);

    List<RevenueGrowthSummary> findByClassificationIn(List<String> classificationNames);


    RevenueGrowthSummary findByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarter(String vertical, String classification, String deliveryDirector, String deliveryManager, String account, String projectManager, String projectName, int financialYear, String quarter);

    List<RevenueGrowthSummary> findByFinancialYear(Integer year);

    List<RevenueGrowthSummary> findDmByVerticalInAndClassificationIn(List<String> verticals, List<String> classifications);

    List<RevenueGrowthSummary> findByVerticalInAndClassificationIn(List<String> verticals, List<String> classifications);

    void deleteByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarter(String vertical, String classification, String deliveryDirector, String deliveryManager, String account, String projectManager, String projectName, int financialYear, String quarter);
}
