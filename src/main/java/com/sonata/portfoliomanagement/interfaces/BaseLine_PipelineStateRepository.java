package com.sonata.portfoliomanagement.interfaces;

import com.sonata.portfoliomanagement.model.BaseLine_PipelineState;
import org.springframework.data.jpa.repository.JpaRepository;

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


}
