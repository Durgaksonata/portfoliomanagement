package com.sonata.portfoliomanagement.model;

public class SummaryData {

    private int financialYear;
    private String quarter;
    private float RVB_Forecast;
    private float RVG_Forecast;
    private float totalPipelineSum;

    // Default constructor
    public SummaryData() {
    }

    // Constructor for initialization with financialYear and quarter only
    public SummaryData(int financialYear, String quarter) {
        this.financialYear = financialYear;
        this.quarter = quarter;
    }

    // Full constructor
    public SummaryData(int financialYear, String quarter, float RVB_Forecast, float RVG_Forecast, float totalPipelineSum) {
        this.financialYear = financialYear;
        this.quarter = quarter;
        this.RVB_Forecast = RVB_Forecast;
        this.RVG_Forecast = RVG_Forecast;
        this.totalPipelineSum = totalPipelineSum;
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

    public float getRVB_Forecast() {
        return RVB_Forecast;
    }

    public void setRVB_Forecast(float RVB_Forecast) {
        this.RVB_Forecast = RVB_Forecast;
    }

    public float getRVG_Forecast() {
        return RVG_Forecast;
    }

    public void setRVG_Forecast(float RVG_Forecast) {
        this.RVG_Forecast = RVG_Forecast;
    }

    public float getTotalPipelineSum() {
        return totalPipelineSum;
    }

    public void setTotalPipelineSum(float totalPipelineSum) {
        this.totalPipelineSum = totalPipelineSum;
    }

    @Override
    public String toString() {
        return "SummaryData{" +
                "financialYear=" + financialYear +
                ", quarter='" + quarter + '\'' +
                ", RVB_Forecast=" + RVB_Forecast +
                ", RVG_Forecast=" + RVG_Forecast +
                ", totalPipelineSum=" + totalPipelineSum +
                '}';
    }
}