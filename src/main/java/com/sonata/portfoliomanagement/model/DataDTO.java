package com.sonata.portfoliomanagement.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataDTO {

    public DataDTO() {
    }

    public DataDTO(String quarter, int financialYear, float totalPipelineSum, float rvb_Forecast, float rvg_Forecast) {
        this.quarter = quarter;
        this.financialYear = financialYear;
        this.totalPipelineSum = totalPipelineSum;
        this.rvb_Forecast = rvb_Forecast;
        this.rvg_Forecast = rvg_Forecast;
    }

    private String quarter;
    private int financialYear;
    private float totalPipelineSum;
    private float rvb_Forecast;
    private float rvg_Forecast;
    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public int getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(int financialYear) {
        this.financialYear = financialYear;
    }

    public float getTotalPipelineSum() {
        return totalPipelineSum;
    }

    public void setTotalPipelineSum(float totalPipelineSum) {
        this.totalPipelineSum = totalPipelineSum;
    }

    public float getRvb_Forecast() {
        return rvb_Forecast;
    }

    public void setRvb_Forecast(float rvb_Forecast) {
        this.rvb_Forecast = rvb_Forecast;
    }

    public float getRvg_Forecast() {
        return rvg_Forecast;
    }

    public void setRvg_Forecast(float rvg_Forecast) {
        this.rvg_Forecast = rvg_Forecast;
    }



}