package com.sonata.portfoliomanagement.interfaces;

import com.sonata.portfoliomanagement.model.BaseLine_RevenueBudgetSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BaseLine_RevenueBudgetSummaryRepository extends JpaRepository<BaseLine_RevenueBudgetSummary, Integer> {
    List<BaseLine_RevenueBudgetSummary> findByDeliveryManager(String deliveryManager);
    List<BaseLine_RevenueBudgetSummary> findByDeliveryManagerAndAccount(String deliveryManager, String account);

    List<BaseLine_RevenueBudgetSummary> findByAccount(String account);
    List<BaseLine_RevenueBudgetSummary> findByAccountAndDeliveryManager(String account, String deliveryManager);

    //    List<BaseLine_RevenueBudgetSummary> findByDeliveryManager(String deliveryManager);
//    List<BaseLine_RevenueBudgetSummary> findByDeliveryManagerAndAccount(String deliveryManager, String account);
    List<BaseLine_RevenueBudgetSummary> findByDeliveryDirector(String deliveryDirector);
    List<BaseLine_RevenueBudgetSummary> findByDeliveryDirectorAndAccount(String deliveryDirector, String account);

    @Query("SELECT DISTINCT r.deliveryDirector FROM BaseLine_RevenueBudgetSummary r WHERE r.account = :account")
    List<String> findDeliveryDirectorsByAccount(@Param("account") String account);

    @Query("SELECT DISTINCT r.deliveryManager FROM BaseLine_RevenueBudgetSummary r WHERE r.account = :account")
    List<String> findDeliveryManagersByAccount(@Param("account") String account);

    List<BaseLine_RevenueBudgetSummary> findByDeliveryDirectorIn(List<String> deliveryDirectorNames);

    List<BaseLine_RevenueBudgetSummary> findByDeliveryManagerIn(List<String> deliveryManagerNames);
}
