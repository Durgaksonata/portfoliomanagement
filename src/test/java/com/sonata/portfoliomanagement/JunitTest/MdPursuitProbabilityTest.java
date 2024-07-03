package com.sonata.portfoliomanagement.JunitTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sonata.portfoliomanagement.controllers.MD_PursuitProbabilityController;
import com.sonata.portfoliomanagement.interfaces.MD_PursuitProbabilityRepository;
import com.sonata.portfoliomanagement.model.MD_PursuitProbability;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class MdPursuitProbabilityTest {


    @Mock
    private MD_PursuitProbabilityRepository pursuitProbabilityRepo;

    @InjectMocks
    private MD_PursuitProbabilityController pursuitProbabilityController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPursuitProbabilities() {
        // Mock data
        MD_PursuitProbability probability1 = new MD_PursuitProbability(1, "Active", "Type1", 50, "Stage1");
        MD_PursuitProbability probability2 = new MD_PursuitProbability(2, "Inactive", "Type2", 30, "Stage2");
        List<MD_PursuitProbability> probabilities = Arrays.asList(probability1, probability2);

        // Mock repository behavior
        when(pursuitProbabilityRepo.findAll()).thenReturn(probabilities);

        // Call controller method
        ResponseEntity<List<MD_PursuitProbability>> response = pursuitProbabilityController.getAllPursuitProbabilities();

        // Verify response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(probabilities, response.getBody());
    }
    @Test
    void testCreatePursuitProbability() {
        // Mock data
        MD_PursuitProbability newProbability = new MD_PursuitProbability(null, "Active", "Type1", 50, "Stage1");

        // Mock repository behavior
        when(pursuitProbabilityRepo.findByPursuitStatusAndType("Active", "Type1")).thenReturn(Optional.empty());
        when(pursuitProbabilityRepo.save(any())).thenReturn(newProbability);

        // Call controller method
        ResponseEntity<Object> response = pursuitProbabilityController.createPursuitProbability(newProbability);

        // Verify response
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newProbability, response.getBody());
    }
    @Test
    void testUpdatePursuitProbability() {
        // Mock data
        MD_PursuitProbability existingProbability = new MD_PursuitProbability(1, "Active", "Type1", 50, "Stage1");
        MD_PursuitProbability updatedProbability = new MD_PursuitProbability(1, "Inactive", "Type1", 60, "Stage2");

        // Mock repository behavior
        when(pursuitProbabilityRepo.findById(1)).thenReturn(Optional.of(existingProbability));
        when(pursuitProbabilityRepo.findByPursuitStatusAndType("Inactive", "Type1")).thenReturn(Optional.empty());
        when(pursuitProbabilityRepo.save(any())).thenReturn(updatedProbability);

        // Call controller method
        ResponseEntity<Map<String, Object>> response = pursuitProbabilityController.updatePursuitProbability(updatedProbability);

        // Verify response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Pursuit Status updated from 'Active' to 'Inactive'. Probability changed from '50' to '60'. Stage changed from 'Stage1' to 'Stage2'. ",
                response.getBody().get("message"));
        assertEquals(updatedProbability, response.getBody().get("updatedPursuitProbability"));
    }
    @Test
    void testDeletePursuitProbabilitiesByIds() {
        // Mock data
        List<Integer> idsToDelete = Arrays.asList(1, 2);
        MD_PursuitProbability probability1 = new MD_PursuitProbability(1, "Active", "Type1", 50, "Stage1");
        MD_PursuitProbability probability2 = new MD_PursuitProbability(2, "Inactive", "Type2", 30, "Stage2");

        // Mock repository behavior
        when(pursuitProbabilityRepo.findById(1)).thenReturn(Optional.of(probability1));
        when(pursuitProbabilityRepo.findById(2)).thenReturn(Optional.of(probability2));

        // Call controller method
        ResponseEntity<String> response = pursuitProbabilityController.deletePursuitProbabilitiesByIds(idsToDelete);

        // Verify response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Pursuit Probabilities [Pursuit Status: Active, Type: Type1, Pursuit Status: Inactive, Type: Type2] deleted successfully",
                response.getBody());
    }



}
