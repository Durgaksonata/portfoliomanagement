package com.sonata.portfoliomanagement.interfaces;

import com.sonata.portfoliomanagement.model.BaseLine_RevenueGrowthSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BaseLine_RevenueGrowthSummaryRepository extends JpaRepository<BaseLine_RevenueGrowthSummary, Integer> {

    List<BaseLine_RevenueGrowthSummary> findByDeliveryManager(String deliveryManager);
    List<BaseLine_RevenueGrowthSummary> findByDeliveryManagerAndAccount(String deliveryManager, String account);

    List<BaseLine_RevenueGrowthSummary> findByAccount(String account);
    List<BaseLine_RevenueGrowthSummary> findByAccountAndDeliveryManager(String account, String deliveryManager);


    //    List<BaseLine_RevenueGrowthSummary> findByDeliveryManager(String deliveryManager);
//    List<BaseLine_RevenueGrowthSummary> findByDeliveryManagerAndAccount(String deliveryManager, String account);
    List<BaseLine_RevenueGrowthSummary> findByDeliveryDirector(String deliveryDirector);
    List<BaseLine_RevenueGrowthSummary> findByDeliveryDirectorAndAccount(String deliveryDirector, String account);
}
