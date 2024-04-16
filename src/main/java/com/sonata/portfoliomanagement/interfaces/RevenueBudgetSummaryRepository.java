package com.sonata.portfoliomanagement.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sonata.portfoliomanagement.model.RevenueBudgetSummary;

import java.util.List;

public interface RevenueBudgetSummaryRepository extends JpaRepository<RevenueBudgetSummary,Integer> {

    List<RevenueBudgetSummary> findByFinancialYear(int financialYear);
    List<RevenueBudgetSummary> findByAccount(String account);
    List<RevenueBudgetSummary> findByDeliveryManager(String deliveryManager);
    List<RevenueBudgetSummary> findByVerticalAndClassification(String vertical, String classification);
    List<RevenueBudgetSummary> findAllByVertical(String vertical);
    List<RevenueBudgetSummary> findByProjectName(String project);
    List<RevenueBudgetSummary> findByVerticalAndClassificationAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYear(String vertical, String classification, String dm, String account, String pm, String project, Integer financialYear);

    List<RevenueBudgetSummary> findByFinancialYearIn(List<Integer> financialYears);

    List<RevenueBudgetSummary> findByAccountIn(List<String> accounts);

    List<RevenueBudgetSummary> findByDeliveryManagerIn(List<String> deliveryManagers);

    List<RevenueBudgetSummary> findByVerticalIn(List<String> verticals);

    List<RevenueBudgetSummary> findByClassificationIn(List<String> classifications);

    List<RevenueBudgetSummary> findByProjectManagerIn(List<String> projectManagers);

    List<RevenueBudgetSummary> findByProjectNameIn(List<String> projectNames);


    List<RevenueBudgetSummary> findBydeliveryManager(String deliveryManager);
    List<RevenueBudgetSummary> findByquarter(String quarter);

    List<RevenueBudgetSummary> findByprojectManager(String getList);


    List<RevenueBudgetSummary> findByProjectManager(String projectManager);




    List<RevenueBudgetSummary> findAllByClassification(String classification);

    List<RevenueBudgetSummary> findByClassification(String classificationName);

    List<RevenueBudgetSummary> findByclassification(String getList);

    List<RevenueBudgetSummary> findByVertical(String getList);

    List<RevenueBudgetSummary> findByFinancialYearInAndProjectNameInAndVerticalInAndClassificationInAndDeliveryManagerInAndAccountInAndProjectManagerIn(List<Integer> financialYear, List<String> projectList, List<String> verticalList, List<String> classificationList, List<String> dmList, List<String> accountList, List<String> pmList);

    List<RevenueBudgetSummary> findByQuarter(String quarter);


}
