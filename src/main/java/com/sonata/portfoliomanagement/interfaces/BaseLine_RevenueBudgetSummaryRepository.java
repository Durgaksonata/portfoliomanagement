package com.sonata.portfoliomanagement.interfaces;

import com.sonata.portfoliomanagement.model.BaseLine_RevenueBudgetSummary;
import org.springframework.data.jpa.repository.JpaRepository;

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

}
