package com.sonata.portfoliomanagement.interfaces;

import com.sonata.portfoliomanagement.model.MD_DeliveryManager;
import com.sonata.portfoliomanagement.model.MD_Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MD_DeliveryManagerRepository extends JpaRepository<MD_DeliveryManager, Integer> {
    void deleteByDeliveryManagers(String deliveryManager);
    //MD_DeliveryManager findByUser(MD_Users user);
}
