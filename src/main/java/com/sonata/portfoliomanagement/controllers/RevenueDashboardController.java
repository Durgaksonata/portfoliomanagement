package com.sonata.portfoliomanagement.controllers;

import com.sonata.portfoliomanagement.interfaces.BaseLine_RevenueBudgetSummaryRepository;
import com.sonata.portfoliomanagement.interfaces.BaseLine_RevenueGrowthSummaryRepository;
import com.sonata.portfoliomanagement.interfaces.RevenueBudgetSummaryRepository;
import com.sonata.portfoliomanagement.interfaces.RevenueGrowthSummaryRepository;
import com.sonata.portfoliomanagement.model.BaseLine_RevenueBudgetSummary;
import com.sonata.portfoliomanagement.model.BaseLine_RevenueGrowthSummary;
import com.sonata.portfoliomanagement.model.RevenueBudgetSummary;
import com.sonata.portfoliomanagement.model.RevenueGrowthSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173" )
@RestController
@RequestMapping("revenuedashboard")
public class RevenueDashboardController {

    @Autowired
    private RevenueBudgetSummaryRepository revenueBudgetSummaryRepository;

    @Autowired
    private RevenueGrowthSummaryRepository revenueGrowthSummaryRepository;

    @Autowired
    private BaseLine_RevenueBudgetSummaryRepository baseLineRevenueBudgetSummaryRepository;

    @Autowired
    private BaseLine_RevenueGrowthSummaryRepository baseLineRevenueGrowthSummaryRepository;

    @GetMapping("/baseline-data")
    public String createBaselineData() {
        LocalDate currentTimestamp = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        String currentFormattedDate = currentTimestamp.format(formatter);
        int currentFinancialYear = getCurrentFinancialYear();


        // Check for existing timestamps in Baseline Revenue Budget
        List<BaseLine_RevenueBudgetSummary> existingBaselineRevenueBudgetSummaries = baseLineRevenueBudgetSummaryRepository.findAll();
        boolean budgetTimestampExists = existingBaselineRevenueBudgetSummaries.stream()
                .anyMatch(b -> b.getBaselineTimestamp().format(formatter).equals(currentFormattedDate));

        // Check for existing timestamps in Baseline Revenue Growth
        List<BaseLine_RevenueGrowthSummary> existingBaselineRevenueGrowthSummaries = baseLineRevenueGrowthSummaryRepository.findAll();
        boolean growthTimestampExists = existingBaselineRevenueGrowthSummaries.stream()
                .anyMatch(b -> b.getBaselineTimestamp().format(formatter).equals(currentFormattedDate));

        if (budgetTimestampExists || growthTimestampExists) {
            return "Data for the provided Timestamp already exists";
        }

        // Copy Revenue Budget data to Baseline Revenue Budget
        List<RevenueBudgetSummary> revenueBudgetSummaries = revenueBudgetSummaryRepository.findAll().stream()
                .filter(rbs -> rbs.getFinancialYear() == currentFinancialYear || rbs.getFinancialYear() == currentFinancialYear - 1)
                .collect(Collectors.toList());
        List<BaseLine_RevenueBudgetSummary> baselineRevenueBudgetSummaries = revenueBudgetSummaries.stream().map(rbs -> {
            BaseLine_RevenueBudgetSummary baseline = new BaseLine_RevenueBudgetSummary();
            baseline.setDeliveryDirector(rbs.getDeliveryDirector());
            baseline.setDeliveryManager(rbs.getDeliveryManager());
            baseline.setAccount(rbs.getAccount());
            baseline.setBudget(rbs.getBudget());
            baseline.setForecast(rbs.getForecast());
            baseline.setGap(rbs.getGap());
            baseline.setBaselineTimestamp(currentTimestamp);
            return baseline;
        }).collect(Collectors.toList());
        baseLineRevenueBudgetSummaryRepository.saveAll(baselineRevenueBudgetSummaries);

        // Copy Revenue Growth data to Baseline Revenue Growth
        List<RevenueGrowthSummary> revenueGrowthSummaries = revenueGrowthSummaryRepository.findAll().stream()
                .filter(rgs -> rgs.getFinancialYear() == currentFinancialYear || rgs.getFinancialYear() == currentFinancialYear - 1)
                .collect(Collectors.toList());
        List<BaseLine_RevenueGrowthSummary> baselineRevenueGrowthSummaries = revenueGrowthSummaries.stream().map(rgs -> {
            BaseLine_RevenueGrowthSummary baseline = new BaseLine_RevenueGrowthSummary();
            baseline.setDeliveryDirector(rgs.getDeliveryDirector());
            baseline.setDeliveryManager(rgs.getDeliveryManager());
            baseline.setAccount(rgs.getAccount());
            baseline.setAccountExpected(rgs.getAccountExpected());
            baseline.setForecast(rgs.getForecast());
            baseline.setGap(rgs.getGap());
            baseline.setBaselineTimestamp(currentTimestamp);
            return baseline;
        }).collect(Collectors.toList());
        baseLineRevenueGrowthSummaryRepository.saveAll(baselineRevenueGrowthSummaries);

        return "Baseline data created successfully";
    }

    private int getCurrentFinancialYear() {
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        if (today.getMonthValue() >= Month.APRIL.getValue()) {
            return year;
        } else {
            return year - 1;
        }
    }


}
