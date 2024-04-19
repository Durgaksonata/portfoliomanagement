package com.sonata.portfoliomanagement.interfaces;

import com.sonata.portfoliomanagement.model.DataEntryDTO;
import com.sonata.portfoliomanagement.model.RevenueBudgetSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sonata.portfoliomanagement.model.DataEntry;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DataEntryRepository extends JpaRepository<DataEntry, Integer>{

    DataEntry findAllByVertical(String vertical);
    DataEntry findAllByClassification(String classification);
    DataEntry findByDeliveryManager(String deliveryManager);
    DataEntry findByAccount(String account);
    DataEntry findByProjectManager(String projectManager);
    DataEntry findByProjectName(String project);
    DataEntry findByFinancialYear(int financialYear);
    DataEntry findByQuarter(String quarter);

    List<DataEntry> findAllByVerticalAndClassificationAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarter(String verticals, String classifications, String deliverManagers, String accounts, String projectManagers, String projectNames, int financialYears, String quarters);








}
