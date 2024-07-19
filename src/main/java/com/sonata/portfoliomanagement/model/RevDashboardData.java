package com.sonata.portfoliomanagement.model;

import java.util.List;

public class RevDashboardData {
    private List<String> deliveryDirector;
    private List<String> deliveryManager;
    private List<String> accountsNames;
    private List<Integer> financialYears;
    private List<AccountData> accounts;

    // Getters and Setters


    public List<String> getDeliveryDirector() {
        return deliveryDirector;
    }

    public void setDeliveryDirector(List<String> deliveryDirector) {
        this.deliveryDirector = deliveryDirector;
    }

    public List<String> getDeliveryManager() {
        return deliveryManager;
    }

    public void setDeliveryManager(List<String> deliveryManager) {
        this.deliveryManager = deliveryManager;
    }

    public List<String> getAccountsNames() {
        return accountsNames;
    }

    public void setAccountsNames(List<String> accountsNames) {
        this.accountsNames = accountsNames;
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
        private RevenueBudget revenueBudget;
        private RevenueGrowth revenueGrowth;
        private PipelineState pipelineState;

        // Getters and Setters

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

        public RevenueBudget getRevenueBudget() {
            return revenueBudget;
        }

        public void setRevenueBudget(RevenueBudget revenueBudget) {
            this.revenueBudget = revenueBudget;
        }

        public RevenueGrowth getRevenueGrowth() {
            return revenueGrowth;
        }

        public void setRevenueGrowth(RevenueGrowth revenueGrowth) {
            this.revenueGrowth = revenueGrowth;
        }

        public PipelineState getPipelineState() {
            return pipelineState;
        }

        public void setPipelineState(PipelineState pipelineState) {
            this.pipelineState = pipelineState;
        }
    }

    public static class RevenueBudget {
        private double budget;
        private double forecast;
        private double gap;

        // Getters and Setters

        public double getBudget() {
            return budget;
        }

        public void setBudget(double budget) {
            this.budget = budget;
        }

        public double getForecast() {
            return forecast;
        }

        public void setForecast(double forecast) {
            this.forecast = forecast;
        }

        public double getGap() {
            return gap;
        }

        public void setGap(double gap) {
            this.gap = gap;
        }
    }

    public static class RevenueGrowth {
        private double accountExpected;
        private double forecast;
        private double gap;

        // Getters and Setters

        public double getAccountExpected() {
            return accountExpected;
        }

        public void setAccountExpected(double accountExpected) {
            this.accountExpected = accountExpected;
        }

        public double getForecast() {
            return forecast;
        }

        public void setForecast(double forecast) {
            this.forecast = forecast;
        }

        public double getGap() {
            return gap;
        }

        public void setGap(double gap) {
            this.gap = gap;
        }
    }

    public static class PipelineState {
        private double sumOfPipeline_pitch;
        private double sumOfPipeline_opportunity;
        private double sumOfPipeline_shaping;
        private double sumOfPipeline_total;

        // Getters and Setters

        public double getSumOfPipeline_pitch() {
            return sumOfPipeline_pitch;
        }

        public void setSumOfPipeline_pitch(double sumOfPipeline_pitch) {
            this.sumOfPipeline_pitch = sumOfPipeline_pitch;
        }

        public double getSumOfPipeline_opportunity() {
            return sumOfPipeline_opportunity;
        }

        public void setSumOfPipeline_opportunity(double sumOfPipeline_opportunity) {
            this.sumOfPipeline_opportunity = sumOfPipeline_opportunity;
        }

        public double getSumOfPipeline_shaping() {
            return sumOfPipeline_shaping;
        }

        public void setSumOfPipeline_shaping(double sumOfPipeline_shaping) {
            this.sumOfPipeline_shaping = sumOfPipeline_shaping;
        }

        public double getSumOfPipeline_total() {
            return sumOfPipeline_total;
        }

        public void setSumOfPipeline_total(double sumOfPipeline_total) {
            this.sumOfPipeline_total = sumOfPipeline_total;
        }
    }
}
