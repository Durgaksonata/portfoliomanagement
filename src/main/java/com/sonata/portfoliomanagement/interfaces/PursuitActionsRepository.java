package com.sonata.portfoliomanagement.interfaces;

import com.sonata.portfoliomanagement.model.PursuitActions;
import com.sonata.portfoliomanagement.model.PursuitTracker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface PursuitActionsRepository extends JpaRepository<PursuitActions, Integer> {


    List<PursuitActions> findByDeliveryManagerAndAccountAndPursuitAndActionItemNumberAndActionDescriptionAndActionTypeAndStatusAndActionOwnerAndDueDateAndDependentActionItemAndRemarks(String deliveryManager, String account, String pursuit, String actionItemNumber, String actionDescription, String actionType, String status, String actionOwner, Date dueDate, String dependentActionItem, String remarks);

    List<PursuitActions> findByDeliveryManager(String deliveryManager);}
