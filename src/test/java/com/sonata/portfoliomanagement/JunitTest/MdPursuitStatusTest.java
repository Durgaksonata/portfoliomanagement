package com.sonata.portfoliomanagement.JunitTest;

import com.sonata.portfoliomanagement.controllers.MD_PursuitStatusController;
import com.sonata.portfoliomanagement.interfaces.MD_PursuitStatusRepository;
import com.sonata.portfoliomanagement.model.MD_PursuitStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MdPursuitStatusTest {

    @Mock
    private MD_PursuitStatusRepository pursuitRepo;

    @InjectMocks
    private MD_PursuitStatusController pursuitStatusController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllData() {
        // Mock data
        MD_PursuitStatus pursuitStatus1 = new MD_PursuitStatus(1, "Active");
        MD_PursuitStatus pursuitStatus2 = new MD_PursuitStatus(2, "Inactive");
        List<MD_PursuitStatus> pursuitStatuses = Arrays.asList(pursuitStatus1, pursuitStatus2);

        // Mock repository behavior
        when(pursuitRepo.findAll()).thenReturn(pursuitStatuses);

        // Call controller method
        ResponseEntity<?> response = pursuitStatusController.getAllData();

        // Verify response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pursuitStatuses, response.getBody());
    }

    @Test
    void testGetAllData_NotFound() {
        // Mock repository behavior
        when(pursuitRepo.findAll()).thenReturn(Collections.emptyList());

        // Call controller method
        ResponseEntity<?> response = pursuitStatusController.getAllData();

        // Verify response
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No pursuit statuses found.", response.getBody());
    }

    // Add more test cases for edge cases as needed






    @Test
    void testCreateMdPursuitStatus() {
        // Mock data
        MD_PursuitStatus newPursuitStatus = new MD_PursuitStatus(1, "Active");

        // Mock repository behavior
        when(pursuitRepo.findByPursuitStatus("Active")).thenReturn(Optional.empty());
        when(pursuitRepo.save(any())).thenReturn(newPursuitStatus);

        // Call controller method
        ResponseEntity<Object> response = pursuitStatusController.createMdPursuitStatus(newPursuitStatus);

        // Verify response
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Data added: Pursuit Status 'Active' added successfully.", response.getBody());
    }
    @Test
    void testUpdateMdPursuitStatus() {
        // Mock data
        MD_PursuitStatus existingPursuitStatus = new MD_PursuitStatus(1, "Active");
        MD_PursuitStatus updatedPursuitStatus = new MD_PursuitStatus(1, "Inactive");

        // Mock repository behavior
        when(pursuitRepo.findById(1)).thenReturn(Optional.of(existingPursuitStatus));
        when(pursuitRepo.findByPursuitStatus("Inactive")).thenReturn(Optional.empty());
        when(pursuitRepo.save(any())).thenReturn(updatedPursuitStatus);

        // Call controller method
        ResponseEntity<Map<String, Object>> response = pursuitStatusController.updateMdPursuitStatus(updatedPursuitStatus);

        // Verify response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Pursuit Status name updated from 'Active' to 'Inactive'. ",
                response.getBody().get("message"));
        assertEquals(updatedPursuitStatus, response.getBody().get("updatedPursuitStatus"));
    }

    @Test
    void testDeletePursuitStatusesByIds_Success() {
        // Mock data
        List<Integer> idsToDelete = Arrays.asList(1, 2);
        MD_PursuitStatus status1 = new MD_PursuitStatus(1, "Active");
        MD_PursuitStatus status2 = new MD_PursuitStatus(2, "Inactive");

        // Mock repository behavior
        when(pursuitRepo.findById(1)).thenReturn(Optional.of(status1));
        when(pursuitRepo.findById(2)).thenReturn(Optional.of(status2));

        // Call controller method
        ResponseEntity<String> response = pursuitStatusController.deletePursuitStatusesByIds(idsToDelete);

        // Verify repository interactions
        verify(pursuitRepo, times(1)).findById(1);
        verify(pursuitRepo, times(1)).findById(2);
        verify(pursuitRepo, times(1)).deleteById(1);
        verify(pursuitRepo, times(1)).deleteById(2);

        // Verify response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Pursuit Statuses '[Active, Inactive]' deleted successfully.", response.getBody());
    }
}


