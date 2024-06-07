package com.sonata.portfoliomanagement.interfaces;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sonata.portfoliomanagement.model.DataEntry;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DataEntryRepository extends JpaRepository<DataEntry,Integer> {
    DataEntry findAllByVertical(String vertical);

    DataEntry findAllByClassification(String classification);

    DataEntry findByDeliveryManager(String deliveryManager);

    DataEntry findByAccount(String account);

    DataEntry findByProjectManager(String projectManager);

    DataEntry findByProjectName(String project);

    DataEntry findByQuarter(String quarter);

    List<DataEntry> findByFinancialYear(int financialYear, Sort sort);


    List<DataEntry> findAllByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarter(String vertical, String classification, String deliveryDirector, String deliveryManager, String account, String projectManager, String projectName, int financialYear, String quarter);


    List<DataEntry> findAllByOrderByIdDesc();

    List<DataEntry> findAllByFinancialYear(int financialYear);


    List<DataEntry> findAllByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarterAndMonth(String vertical, String classification, String deliveryDirector, String deliveryManager, String account, String projectManager, String projectName, int financialYear, String quarter, String month);


    List<DataEntry> findAll();


    List<DataEntry> findAllByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarterAndBudget(String vertical, String classification, String deliveryDirector, String deliveryManager, String account, String projectManager, String projectName, int financialYear, String quarter, float budget);






}
