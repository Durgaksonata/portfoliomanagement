package com.sonata.portfoliomanagement.interfaces;

import com.sonata.portfoliomanagement.model.MD_DeliveryManager;
import com.sonata.portfoliomanagement.model.MD_Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MD_DeliveryManagerRepository extends JpaRepository<MD_DeliveryManager, Integer> {
    void deleteByDeliveryManagers(String deliveryManager);

    List<MD_DeliveryManager> findByDeliveryManagers(String deliveryManagers);

    //MD_DeliveryManager findByUser(MD_Users user);
}
