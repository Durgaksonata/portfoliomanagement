package com.sonata.portfoliomanagement.interfaces;

import com.sonata.portfoliomanagement.model.PipelineState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PipelineStateRepository extends JpaRepository<PipelineState, Integer> {

    List<PipelineState> findByDeliveryManager(String deliveryManager);
    List<PipelineState> findByDeliveryManagerAndAccount(String deliveryManager, String account);
    @Query("SELECT DISTINCT r.account FROM PipelineState r WHERE r.deliveryManager = :deliveryManager")
    List<String> findAccountsByDeliveryManager(@Param("deliveryManager") String deliveryManager);

    List<PipelineState> findByAccount(String account);
    List<PipelineState> findByAccountAndDeliveryManager(String account, String deliveryManager);
    @Query("SELECT DISTINCT r.deliveryManager FROM PipelineState r WHERE r.account = :account")
    List<String> findDeliveryManagersByAccount(@Param("account") String account);
    List<PipelineState> findByDeliveryDirector(String deliveryDirector);

    //   List<PipelineState> findByDeliveryManager(String deliveryManager);
//      List<PipelineState> findByDeliveryManagerAndAccount(String deliveryManager, String account);
    //@Query("SELECT DISTINCT r.account FROM PipelineState r WHERE r.deliveryManager = :deliveryManager")
//      List<String> findAccountsByDeliveryManager(@Param("deliveryManager") String deliveryManager);
//      List<PipelineState> findByDeliveryDirector(String deliveryDirector);
    List<PipelineState> findByDeliveryDirectorAndAccount(String deliveryDirector, String account);
    @Query("SELECT DISTINCT r.account FROM PipelineState r WHERE r.deliveryDirector = :deliveryDirector")
    List<String> findAccountsByDeliveryDirector(@Param("deliveryDirector") String deliveryDirector);
    List<PipelineState> findAllByDeliveryManager(String deliveryManager);
//      List<PipelineState> findByAccount(String account);



    PipelineState findByAccountAndFinancialYearAndQuarter(String account, int financialYear, String quarter);

    List<PipelineState> findByFinancialYearIn(List<Integer> currentYear);

    @Query("SELECT DISTINCT r.deliveryDirector FROM PipelineState r WHERE r.account = :account")
    List<String> findDeliveryDirectorsByAccount(@Param("account") String account);





    List<PipelineState> findByDeliveryDirectorAndFinancialYearIn(String deliveryDirector, List<Integer> financialYears);

    // Custom query to fetch data by delivery manager and financial years
    @Query("SELECT r FROM PipelineState r WHERE r.deliveryManager = :deliveryManager AND r.financialYear IN :financialYears")
    List<PipelineState> findByDeliveryManagerAndFinancialYears(@Param("deliveryManager") String deliveryManager, @Param("financialYears") List<Integer> financialYears);


    List<PipelineState> findByDeliveryDirectorIn(List<String> deliveryDirectorNames);

    List<PipelineState> findByDeliveryManagerIn(List<String> deliveryManagerNames);
}
