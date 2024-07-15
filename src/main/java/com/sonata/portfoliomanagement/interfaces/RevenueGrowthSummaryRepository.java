package com.sonata.portfoliomanagement.interfaces;


import com.sonata.portfoliomanagement.model.RevenueBudgetSummary;
import com.sonata.portfoliomanagement.model.RevenueDTO;
import com.sonata.portfoliomanagement.model.RevenueGrowthSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
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


    boolean existsByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarter(
            String vertical, String classification, String deliveryDirector, String deliveryManager, String account, String projectManager, String projectName, int financialYear, String quarter);
    List<RevenueGrowthSummary> findByFinancialYear(Integer year);

    List<RevenueGrowthSummary> findDmByVerticalInAndClassificationIn(List<String> verticals, List<String> classifications);

    List<RevenueGrowthSummary> findByVerticalInAndClassificationIn(List<String> verticals, List<String> classifications);

    void deleteByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarter(String vertical, String classification, String deliveryDirector, String deliveryManager, String account, String projectManager, String projectName, int financialYear, String quarter);



    RevenueGrowthSummary findByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarter(String vertical, String classification, String deliveryDirector, String deliveryManager, String account, String projectManager, String projectName, int financialYear, String quarter);

    boolean existsByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarterAndMonth(String vertical, String classification, String deliveryDirector, String deliveryManager, String account, String projectManager, String projectName, int financialYear, String quarter, String month);


    List<RevenueGrowthSummary> findByDeliveryManager(String deliveryManager);
    List<RevenueGrowthSummary> findByDeliveryManagerAndAccount(String deliveryManager, String account);
    @Query("SELECT DISTINCT r.account FROM RevenueGrowthSummary r WHERE r.deliveryManager = :deliveryManager")
    List<String> findAccountsByDeliveryManager(@Param("deliveryManager") String deliveryManager);

    List<RevenueGrowthSummary> findByAccountAndDeliveryManager(String account, String deliveryManager);
    @Query("SELECT DISTINCT r.deliveryManager FROM RevenueGrowthSummary r WHERE r.account = :account")
    List<String> findDeliveryManagersByAccount(@Param("account") String account);

    List<RevenueGrowthSummary> findByDeliveryDirector(String deliveryDirector);

    //List<RevenueGrowthSummary> findByDeliveryManager(String deliveryManager);
    // List<RevenueGrowthSummary> findByDeliveryManagerAndAccount(String deliveryManager, String account);
    //@Query("SELECT DISTINCT r.account FROM RevenueGrowthSummary r WHERE r.deliveryManager = :deliveryManager")
    //List<String> findAccountsByDeliveryManager(@Param("deliveryManager") String deliveryManager);
    List<RevenueGrowthSummary> findByDeliveryDirectorAndAccount(String deliveryDirector, String account);
    @Query("SELECT DISTINCT r.account FROM RevenueGrowthSummary r WHERE r.deliveryDirector = :deliveryDirector")
    List<String> findAccountsByDeliveryDirector(@Param("deliveryDirector") String deliveryDirector);

    List<RevenueGrowthSummary> findAllByDeliveryManager(String deliveryManager);


    List<RevenueGrowthSummary> findByFinancialYearIn(List<Integer> currentYear);
}
