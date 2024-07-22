package com.sonata.portfoliomanagement.interfaces;

import com.sonata.portfoliomanagement.model.BaseLine_PipelineState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BaseLine_PipelineStateRepository extends JpaRepository<BaseLine_PipelineState, Integer> {

    List<BaseLine_PipelineState> findByDeliveryManager(String deliveryManager);
    List<BaseLine_PipelineState> findByDeliveryManagerAndAccount(String deliveryManager, String account);

    List<BaseLine_PipelineState> findByAccount(String account);
    List<BaseLine_PipelineState> findByAccountAndDeliveryManager(String account, String deliveryManager);

    //    List<BaseLine_PipelineState> findByDeliveryManager(String deliveryManager);
//    List<BaseLine_PipelineState> findByDeliveryManagerAndAccount(String deliveryManager, String account);
    List<BaseLine_PipelineState> findByDeliveryDirector(String deliveryDirector);
    List<BaseLine_PipelineState> findByDeliveryDirectorAndAccount(String deliveryDirector, String account);


    @Query("SELECT DISTINCT r.deliveryDirector FROM BaseLine_PipelineState r WHERE r.account = :account")
    List<String> findDeliveryDirectorsByAccount(@Param("account") String account);

    @Query("SELECT DISTINCT r.deliveryManager FROM BaseLine_PipelineState r WHERE r.account = :account")
    List<String> findDeliveryManagersByAccount(@Param("account") String account);

    List<BaseLine_PipelineState> findByDeliveryDirectorIn(List<String> deliveryDirectorNames);

    List<BaseLine_PipelineState> findByDeliveryManagerIn(List<String> deliveryManagerNames);
}
