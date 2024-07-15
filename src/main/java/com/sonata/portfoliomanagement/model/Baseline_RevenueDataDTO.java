package com.sonata.portfoliomanagement.model;

import java.time.LocalDate;
import java.util.List;

public class Baseline_RevenueDataDTO {



    private LocalDate timestamp;
    private List<BaseLine_RevenueBudgetSummary> revenueBudget;
    private List<BaseLine_RevenueGrowthSummary> revenueGrowth;
    private List<BaseLine_PipelineState> pipelineStates;

    // Getters and Setters
    public LocalDate getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
    }

    public List<BaseLine_RevenueBudgetSummary> getRevenueBudget() {
        return revenueBudget;
    }

    public void setRevenueBudget(List<BaseLine_RevenueBudgetSummary> revenueBudget) {
        this.revenueBudget = revenueBudget;
    }

    public List<BaseLine_RevenueGrowthSummary> getRevenueGrowth() {
        return revenueGrowth;
    }

    public void setRevenueGrowth(List<BaseLine_RevenueGrowthSummary> revenueGrowth) {
        this.revenueGrowth = revenueGrowth;
    }


    public List<BaseLine_PipelineState> getPipelineStates() {
        return pipelineStates;
    }

    public void setPipelineStates(List<BaseLine_PipelineState> pipelineStates) {
        this.pipelineStates = pipelineStates;
    }



}
