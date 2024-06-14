package com.sonata.portfoliomanagement.interfaces;

import com.sonata.portfoliomanagement.model.PursuitActions;
import com.sonata.portfoliomanagement.model.PursuitTracker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface PursuitActionsRepository extends JpaRepository<PursuitActions, Integer> {
   // List<PursuitActions> findByPursuitid(String pursuitid);

   // void deleteByPursuitid(int pursuitid);

    List<PursuitActions> findByDeliveryManager(String deliveryManager);
    List<PursuitActions> findByPursuit(String pursuit);

    long countByPursuit(String pursuit);

    List<PursuitActions> findByDeliveryManagerAndDeliveryDirectorAndAccountAndPursuitAndActionItemNumberAndActionDescriptionAndActionTypeAndStatusAndActionOwnerAndDueDateAndDependentActionItemAndRemarks(String deliveryManager, String deliveryDirector, String account, String pursuit, String actionItemNumber, String actionDescription, String actionType, String status, String actionOwner, Date dueDate, String dependentActionItem, String remarks);

    void deleteByPursuitid(int pursuitid);

    List<PursuitActions> findByPursuitid(int pursuitid);
}