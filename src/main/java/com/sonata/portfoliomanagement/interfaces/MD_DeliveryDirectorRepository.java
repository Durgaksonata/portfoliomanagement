package com.sonata.portfoliomanagement.interfaces;


import com.sonata.portfoliomanagement.model.MD_DeliveryDirector;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MD_DeliveryDirectorRepository extends JpaRepository<MD_DeliveryDirector, Integer>{
    Optional<MD_DeliveryDirector> findByDeliveryDirector(String deliveryDirector);
}