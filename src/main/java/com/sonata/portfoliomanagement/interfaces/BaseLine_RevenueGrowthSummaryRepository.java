package com.sonata.portfoliomanagement.interfaces;

import com.sonata.portfoliomanagement.model.BaseLine_RevenueGrowthSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("SELECT DISTINCT r.deliveryDirector FROM BaseLine_RevenueGrowthSummary r WHERE r.account = :account")
    List<String> findDeliveryDirectorsByAccount(@Param("account") String account);

    @Query("SELECT DISTINCT r.deliveryManager FROM BaseLine_RevenueGrowthSummary r WHERE r.account = :account")
    List<String> findDeliveryManagersByAccount(@Param("account") String account);
}
