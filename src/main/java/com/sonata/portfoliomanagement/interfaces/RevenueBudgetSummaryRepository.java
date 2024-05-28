package com.sonata.portfoliomanagement.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sonata.portfoliomanagement.model.RevenueBudgetSummary;

import java.time.LocalDate;
import java.util.List;

public interface RevenueBudgetSummaryRepository extends JpaRepository<RevenueBudgetSummary,Integer> {

    List<RevenueBudgetSummary> findByVertical(String getList);
    List<RevenueBudgetSummary> findByFinancialYear(int financialYear);
    List<RevenueBudgetSummary> findByAccount(String account);
    List<RevenueBudgetSummary> findByDeliveryManager(String deliveryManager);
    List<RevenueBudgetSummary> findByVerticalAndClassification(String vertical, String classification);
    List<RevenueBudgetSummary> findAllByVertical(String vertical);
    List<RevenueBudgetSummary> findByProjectName(String project);


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

    List<RevenueBudgetSummary> findByclassification(String getList);


    List<RevenueBudgetSummary> findByVerticalInAndClassificationIn(List<String> verticals, List<String> classifications);

    List<RevenueBudgetSummary> findByFinancialYearInAndProjectNameInAndVerticalInAndClassificationInAndDeliveryManagerInAndAccountInAndProjectManagerInAndQuarterIn(List<Integer> financialYear, List<String> projectList, List<String> verticalList, List<String> classificationList, List<String> dmList, List<String> accountList, List<String> pmList, List<String> quarterList);

    List<RevenueBudgetSummary> findByMonthIn(List<LocalDate> months);

    List<RevenueBudgetSummary> findByProjectManagerInAndClassificationIn(List<String> pmNames, List<String> classifications);

    List<RevenueBudgetSummary> findByAccountInAndClassificationIn(List<String> accounts, List<String> classifications);

    List<RevenueBudgetSummary> findByDeliveryManagerInAndClassificationIn(List<String> deliveryManagers, List<String> classifications);

    List<RevenueBudgetSummary> findGapByFinancialYearInAndProjectNameIn(List<Integer> years, List<String> projectNames);

    List<RevenueBudgetSummary> findGapByFinancialYearInAndProjectNameInAndQuarter(List<Integer> integers, List<String> projectNames, String quarter);


    RevenueBudgetSummary findByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarterAndBudget(String vertical, String classification, String deliveryDirector, String deliveryManager, String account, String projectManager, String projectName, int financialYear, String quarter, float budget);

    void deleteByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarter(String vertical, String classification, String deliveryDirector, String deliveryManager, String account, String projectManager, String projectName, int financialYear, String quarter);

}
