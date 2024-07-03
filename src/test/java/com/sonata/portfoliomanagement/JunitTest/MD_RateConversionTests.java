package com.sonata.portfoliomanagement.JunitTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sonata.portfoliomanagement.controllers.MD_RateConversionController;
import com.sonata.portfoliomanagement.interfaces.MD_RateConversionRepository;
import com.sonata.portfoliomanagement.model.MD_RateConversion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@ExtendWith(SpringExtension.class)
@WebMvcTest(MD_RateConversionController.class)
public class MD_RateConversionTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MD_RateConversionRepository rateConversionRepository;

    private MD_RateConversion rateConversion1;
    private MD_RateConversion rateConversion2;

    @BeforeEach
    void setUp() {
        rateConversion1 = new MD_RateConversion(1, "2023", "January", "Q1", 74.5f, 1.39f);
        rateConversion2 = new MD_RateConversion(2, "2023", "February", "Q1", 75.0f, 1.38f);
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetAllRateConversions() throws Exception {
        when(rateConversionRepository.findAll()).thenReturn(Arrays.asList(rateConversion1, rateConversion2));

        mockMvc.perform(get("/mdrateconversion/get"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].financialYear", is(rateConversion1.getFinancialYear())))
                .andExpect(jsonPath("$[1].financialYear", is(rateConversion2.getFinancialYear())));
    }

    @Test
    void testCreateRateConversion_Success() throws Exception {
        when(rateConversionRepository.findByFinancialYearAndMonthAndQuarter(
                rateConversion1.getFinancialYear(), rateConversion1.getMonth(), rateConversion1.getQuarter()))
                .thenReturn(Collections.emptyList());

        when(rateConversionRepository.save(any(MD_RateConversion.class))).thenReturn(rateConversion1);

        mockMvc.perform(post("/mdrateconversion/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(rateConversion1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", containsString("Rate conversion created successfully.")))
                .andExpect(jsonPath("$.createdRateConversion.financialYear", is(rateConversion1.getFinancialYear())));
    }

    @Test
    void testCreateRateConversion_Duplicate() throws Exception {
        when(rateConversionRepository.findByFinancialYearAndMonthAndQuarter(
                rateConversion1.getFinancialYear(), rateConversion1.getMonth(), rateConversion1.getQuarter()))
                .thenReturn(Collections.singletonList(rateConversion1));

        mockMvc.perform(post("/mdrateconversion/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(rateConversion1)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", containsString("already exists")));
    }

    @Test
    void testUpdateRateConversion_Success() throws Exception {
        when(rateConversionRepository.findById(anyInt())).thenReturn(Optional.of(rateConversion1));
        when(rateConversionRepository.findByFinancialYearAndMonthAndQuarter(
                anyString(), anyString(), anyString())).thenReturn(Collections.emptyList());
        when(rateConversionRepository.save(any(MD_RateConversion.class))).thenReturn(rateConversion1);

        MD_RateConversion updatedRateConversion = new MD_RateConversion(1, "2023", "March", "Q1", 76.0f, 1.37f);

        mockMvc.perform(put("/mdrateconversion/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedRateConversion)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("Month changed from 'January' to 'March'. USD to INR rate changed from '74.5' to '76.0'. GBP to USD rate changed from '1.39' to '1.37'.")))
                .andExpect(jsonPath("$.updatedRateConversion.financialYear", is(updatedRateConversion.getFinancialYear())));
    }



    @Test
    void testUpdateRateConversion_NotFound() throws Exception {
        when(rateConversionRepository.findById(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(put("/mdrateconversion/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(rateConversion1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteRateConversionsByIds_Success() throws Exception {
        List<Integer> idsToDelete = Arrays.asList(1, 2, 3);

        // Mock repository behavior
        MD_RateConversion rate1 = new MD_RateConversion(1, "2023", "January", "Q1", 74.5f, 1.39f);
        MD_RateConversion rate2 = new MD_RateConversion(2, "2023", "February", "Q1", 75.0f, 1.38f);
        MD_RateConversion rate3 = new MD_RateConversion(3, "2023", "March", "Q1", 76.0f, 1.37f);

        when(rateConversionRepository.findById(1)).thenReturn(Optional.of(rate1));
        when(rateConversionRepository.findById(2)).thenReturn(Optional.of(rate2));
        when(rateConversionRepository.findById(3)).thenReturn(Optional.of(rate3));

        // Perform the DELETE request and check the response
        mockMvc.perform(delete("/mdrateconversion/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(idsToDelete)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("deleted successfully")));

        // Verify delete operations
        verify(rateConversionRepository, times(1)).deleteById(1);
        verify(rateConversionRepository, times(1)).deleteById(2);
        verify(rateConversionRepository, times(1)).deleteById(3);
    }



    @Test
    void testDeleteRateConversionsByIds_NotFound() throws Exception {
        List<Integer> idsToDelete = Arrays.asList(99, 100);  // Non-existent IDs

        // Mock repository behavior to return empty for non-existent IDs
        when(rateConversionRepository.findById(99)).thenReturn(Optional.empty());
        when(rateConversionRepository.findById(100)).thenReturn(Optional.empty());

        // Perform the DELETE request and check the response
        mockMvc.perform(delete("/mdrateconversion/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(idsToDelete)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("No rate conversions found")));

        // Verify that delete operations were not called since IDs were not found
        verify(rateConversionRepository, times(0)).deleteById(99);
        verify(rateConversionRepository, times(0)).deleteById(100);
    }






}
