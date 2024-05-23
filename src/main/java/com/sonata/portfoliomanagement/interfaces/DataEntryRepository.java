package com.sonata.portfoliomanagement.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sonata.portfoliomanagement.model.DataEntry;

import java.util.List;

public interface DataEntryRepository extends JpaRepository<DataEntry, Integer>{
    DataEntry findAllByVertical(String vertical);
    DataEntry findAllByClassification(String classification);
    DataEntry findByDeliveryManager(String deliveryManager);
    DataEntry findByAccount(String account);
    DataEntry findByProjectManager(String projectManager);
    DataEntry findByProjectName(String project);
    
    DataEntry findByQuarter(String quarter);


    List<DataEntry> findAllByVerticalAndClassificationAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarterAndMonthAndDeliveryDirectorAndCategoryAndAnnuityorNonAnnuityAndValue(
            String vertical, String classification, String deliveryManager, String account, String projectManager, String projectName, int financialYear, String quarter, String month, String deliveryDirector, String category, String annuityorNonAnnuity, float value);
    List<DataEntry> findAllByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarter(String vertical, String classification, String deliveryDirector, String deliveryManager, String account, String projectManager, String projectName, int financialYear, String quarter);

    List<DataEntry> findByFinancialYear(int financialYear);
}
