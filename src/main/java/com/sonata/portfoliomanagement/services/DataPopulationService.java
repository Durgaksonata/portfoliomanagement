package com.sonata.portfoliomanagement.services;


import com.sonata.portfoliomanagement.interfaces.DataEntryRepository;
import com.sonata.portfoliomanagement.interfaces.RevenueBudgetSummaryRepository;
import com.sonata.portfoliomanagement.interfaces.RevenueGrowthSummaryRepository;
import com.sonata.portfoliomanagement.model.DataEntry;
import com.sonata.portfoliomanagement.model.RevenueBudgetSummary;
import com.sonata.portfoliomanagement.model.RevenueGrowthSummary;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class DataPopulationService {

    @Autowired
    private DataEntryRepository dataEntryRepository;

    @Autowired
    private RevenueBudgetSummaryRepository revenueBudgetSummaryRepository;

    @Autowired
    private RevenueGrowthSummaryRepository revenueGrowthSummaryRepository;

    private static final Logger logger = LoggerFactory.getLogger(DataPopulationService.class);


}
