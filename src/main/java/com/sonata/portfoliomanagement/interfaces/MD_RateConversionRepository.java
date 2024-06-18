package com.sonata.portfoliomanagement.interfaces;

import com.sonata.portfoliomanagement.model.MD_RateConversion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MD_RateConversionRepository extends JpaRepository<MD_RateConversion, Integer> {
    List<MD_RateConversion> findByFinancialYearAndMonthAndQuarter(String financialYear, String month, String quarter);
}
