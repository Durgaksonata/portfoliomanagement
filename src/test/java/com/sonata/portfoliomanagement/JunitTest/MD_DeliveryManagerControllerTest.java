package com.sonata.portfoliomanagement.JunitTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.sonata.portfoliomanagement.controllers.MD_DeliveryManagerController;
import com.sonata.portfoliomanagement.interfaces.MD_DeliveryManagerRepository;
import com.sonata.portfoliomanagement.model.MD_DeliveryManager;

@ExtendWith(MockitoExtension.class)
public class MD_DeliveryManagerControllerTest {

    @Mock
    private MD_DeliveryManagerRepository dmRepo;

    @InjectMocks
    private MD_DeliveryManagerController dmController;

    private List<MD_DeliveryManager> mockDeliveryManagers;

    @BeforeEach
    public void setup() {
        // Initialize mock data
        MD_DeliveryManager dm1 = new MD_DeliveryManager(1, "Manager 1");
        MD_DeliveryManager dm2 = new MD_DeliveryManager(2, "Manager 2");
        mockDeliveryManagers = Arrays.asList(dm1, dm2);
    }

    @Test
    public void testGetAllData() {
        // Mock repository behavior
        when(dmRepo.findAll()).thenReturn(mockDeliveryManagers);

        // Call controller method
        ResponseEntity<List<MD_DeliveryManager>> response = dmController.getAllData();

        // Verify response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("Manager 1", response.getBody().get(0).getDelivery_Managers());
        assertEquals("Manager 2", response.getBody().get(1).getDelivery_Managers());
    }

    @Test
    public void testGetUniqueData() {
        // Mock repository behavior
        when(dmRepo.findAll()).thenReturn(mockDeliveryManagers);

        // Call controller method
        ResponseEntity<?> response = dmController.getUniqueData();

        // Verify response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof List);
        List<String> uniqueManagers = (List<String>) response.getBody();
        assertEquals(2, uniqueManagers.size());
        assertTrue(uniqueManagers.contains("Manager 1"));
        assertTrue(uniqueManagers.contains("Manager 2"));
    }

    @Test
    public void testCreateMdDm() {
        // Mock repository behavior
        MD_DeliveryManager newDm = new MD_DeliveryManager(null, "New Manager");
        when(dmRepo.save(any())).thenReturn(newDm);

        // Call controller method
        ResponseEntity<MD_DeliveryManager> response = dmController.createMdDm(newDm);

        // Verify response
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("New Manager", response.getBody().getDelivery_Managers());
    }

    @Test
    public void testUpdateMdDm() {
        // Mock repository behavior
        int idToUpdate = 1;
        MD_DeliveryManager updatedDm = new MD_DeliveryManager(idToUpdate, "Updated Manager");
        when(dmRepo.existsById(idToUpdate)).thenReturn(true);
        when(dmRepo.save(any())).thenReturn(updatedDm);

        // Call controller method
        ResponseEntity<MD_DeliveryManager> response = dmController.updateMdDm(idToUpdate, updatedDm);

        // Verify response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(idToUpdate, response.getBody().getId());
        assertEquals("Updated Manager", response.getBody().getDelivery_Managers());
    }

    @Test
    public void testUpdateMdDm_NotFound() {
        // Mock repository behavior
        int idToUpdate = 999; // Non-existent ID
        MD_DeliveryManager updatedDm = new MD_DeliveryManager(idToUpdate, "Updated Manager");
        when(dmRepo.existsById(idToUpdate)).thenReturn(false);

        // Call controller method
        ResponseEntity<MD_DeliveryManager> response = dmController.updateMdDm(idToUpdate, updatedDm);

        // Verify response
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}