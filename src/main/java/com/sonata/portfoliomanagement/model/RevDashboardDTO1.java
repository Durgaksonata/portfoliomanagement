package com.sonata.portfoliomanagement.model;

import java.util.ArrayList;
import java.util.List;

public class RevDashboardDTO1 {
    private List<String> deliveryDirectors;
    private List<String> deliveryManagers;
    private List<String> accountNames;
    private List<Integer> financialYears;
    private List<AccountData> accounts;

    public RevDashboardDTO1() {
        this.deliveryDirectors = new ArrayList<>();
        this.deliveryManagers = new ArrayList<>();
        this.accountNames = new ArrayList<>();
        this.financialYears = new ArrayList<>();
        this.accounts = new ArrayList<>();
    }

    public RevDashboardDTO1(List<String> deliveryDirectors, List<String> deliveryManagers, List<String> accountNames, List<Integer> financialYears) {
        this.deliveryDirectors = deliveryDirectors;
        this.deliveryManagers = deliveryManagers;
        this.accountNames = accountNames;
        this.financialYears = financialYears;
        this.accounts = new ArrayList<>();
    }

    // Getters and setters
    public List<String> getDeliveryDirectors() {
        return deliveryDirectors;
    }

    public void setDeliveryDirectors(List<String> deliveryDirectors) {
        this.deliveryDirectors = deliveryDirectors;
    }

    public List<String> getDeliveryManagers() {
        return deliveryManagers;
    }

    public void setDeliveryManagers(List<String> deliveryManagers) {
        this.deliveryManagers = deliveryManagers;
    }

    public List<String> getAccountNames() {
        return accountNames;
    }

    public void setAccountNames(List<String> accountNames) {
        this.accountNames = accountNames;
    }

    public List<Integer> getFinancialYears() {
        return financialYears;
    }

    public void setFinancialYears(List<Integer> financialYears) {
        this.financialYears = financialYears;
    }

    public List<AccountData> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountData> accounts) {
        this.accounts = accounts;
    }

    public static class AccountData {
        private String account;
        private int financialYear;
        private String quarter;
        private RevenueBudgetSummary revenueBudget;
        private RevenueGrowthSummary revenueGrowth;
        private PipelineState pipelineStatus;

        public AccountData(String account, int financialYear, String quarter,
                           RevenueBudgetSummary revenueBudget,
                           RevenueGrowthSummary revenueGrowth,
                           PipelineState pipelineStatus) {
            this.account = account;
            this.financialYear = financialYear;
            this.quarter = quarter;
            this.revenueBudget = revenueBudget;
            this.revenueGrowth = revenueGrowth;
            this.pipelineStatus = pipelineStatus;
        }

        // Getters and setters
        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public int getFinancialYear() {
            return financialYear;
        }

        public void setFinancialYear(int financialYear) {
            this.financialYear = financialYear;
        }

        public String getQuarter() {
            return quarter;
        }

        public void setQuarter(String quarter) {
            this.quarter = quarter;
        }

        public RevenueBudgetSummary getRevenueBudget() {
            return revenueBudget;
        }

        public void setRevenueBudget(RevenueBudgetSummary revenueBudget) {
            this.revenueBudget = revenueBudget;
        }

        public RevenueGrowthSummary getRevenueGrowth() {
            return revenueGrowth;
        }

        public void setRevenueGrowth(RevenueGrowthSummary revenueGrowth) {
            this.revenueGrowth = revenueGrowth;
        }

        public PipelineState getPipelineStatus() {
            return pipelineStatus;
        }

        public void setPipelineStatus(PipelineState pipelineStatus) {
            this.pipelineStatus = pipelineStatus;
        }
    }

    public static class RevenueBudgetSummary {
        private float budget;
        private float forecast;
        private float gap;

        public RevenueBudgetSummary(float budget, float forecast, float gap) {
            this.budget = budget;
            this.forecast = forecast;
            this.gap = gap;
        }

        // Getters and setters
        public float getBudget() {
            return budget;
        }

        public void setBudget(float budget) {
            this.budget = budget;
        }

        public float getForecast() {
            return forecast;
        }

        public void setForecast(float forecast) {
            this.forecast = forecast;
        }

        public float getGap() {
            return gap;
        }

        public void setGap(float gap) {
            this.gap = gap;
        }
    }

    public static class RevenueGrowthSummary {
        private float accountExpected;
        private float forecast;
        private float gap;

        public RevenueGrowthSummary(float accountExpected, float forecast, float gap) {
            this.accountExpected = accountExpected;
            this.forecast = forecast;
            this.gap = gap;
        }

        // Getters and setters
        public float getAccountExpected() {
            return accountExpected;
        }

        public void setAccountExpected(float accountExpected) {
            this.accountExpected = accountExpected;
        }

        public float getForecast() {
            return forecast;
        }

        public void setForecast(float forecast) {
            this.forecast = forecast;
        }

        public float getGap() {
            return gap;
        }

        public void setGap(float gap) {
            this.gap = gap;
        }
    }

    public static class PipelineState {
        private float sumOfPipeline_pitch;
        private float sumOfPipeline_opportunity;
        private float sumOfPipeline_total;
        private float sumOfPipeline_shaping;

        public PipelineState(float sumOfPipeline_pitch, float sumOfPipeline_opportunity, float sumOfPipeline_total,
                             float sumOfPipeline_shaping) {
            this.sumOfPipeline_pitch = sumOfPipeline_pitch;
            this.sumOfPipeline_opportunity = sumOfPipeline_opportunity;
            this.sumOfPipeline_total = sumOfPipeline_total;
            this.sumOfPipeline_shaping = sumOfPipeline_shaping;
        }

        // Getters and setters
        public float getSumOfPipeline_pitch() {
            return sumOfPipeline_pitch;
        }

        public void setSumOfPipeline_pitch(float sumOfPipeline_pitch) {
            this.sumOfPipeline_pitch = sumOfPipeline_pitch;
        }

        public float getSumOfPipeline_opportunity() {
            return sumOfPipeline_opportunity;
        }

        public void setSumOfPipeline_opportunity(float sumOfPipeline_opportunity) {
            this.sumOfPipeline_opportunity = sumOfPipeline_opportunity;
        }

        public float getSumOfPipeline_total() {
            return sumOfPipeline_total;
        }

        public void setSumOfPipeline_total(float sumOfPipeline_total) {
            this.sumOfPipeline_total = sumOfPipeline_total;
        }

        public float getSumOfPipeline_shaping() {
            return sumOfPipeline_shaping;
        }

        public void setSumOfPipeline_shaping(float sumOfPipeline_shaping) {
            this.sumOfPipeline_shaping = sumOfPipeline_shaping;
        }
    }
}