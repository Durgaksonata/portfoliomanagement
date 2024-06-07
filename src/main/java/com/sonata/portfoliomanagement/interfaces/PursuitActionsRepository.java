package com.sonata.portfoliomanagement.interfaces;

import com.sonata.portfoliomanagement.model.PursuitActions;
import com.sonata.portfoliomanagement.model.PursuitTracker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface PursuitActionsRepository extends JpaRepository<PursuitActions, Integer> {
    List<PursuitActions> findByPursuitid(int pursuitid);



    void deleteByPursuitid(int pursuitid);

    List<PursuitActions> findByActionItemNumber(String actionItemNumber);



    List<PursuitActions> findByDeliveryManagerAndDeliveryDirectorAndAccountAndPursuitAndActionItemNumberAndActionDescriptionAndActionTypeAndStatusAndActionOwnerAndDueDateAndDependentActionItemAndRemarks(String deliveryManager, String deliveryDirector, String account, String pursuit, String actionItemNumber, String actionDescription, String actionType, String status, String actionOwner, Date dueDate, String dependentActionItem, String remarks);

    List<PursuitActions> findByPursuit(String projectOrPursuit);
}
