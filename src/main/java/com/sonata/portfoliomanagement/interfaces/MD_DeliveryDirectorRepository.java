package com.sonata.portfoliomanagement.interfaces;


import com.sonata.portfoliomanagement.model.MD_DeliveryDirector;
import com.sonata.portfoliomanagement.model.MD_Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface MD_DeliveryDirectorRepository extends JpaRepository<MD_DeliveryDirector, Integer>{


    void deleteByDeliveryDirector(String deliveryDirector);

    List<MD_DeliveryDirector> findByDeliveryDirector(String deliveryDirector);


}