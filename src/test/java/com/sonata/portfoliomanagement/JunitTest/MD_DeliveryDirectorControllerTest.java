package com.sonata.portfoliomanagement.JunitTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.sonata.portfoliomanagement.controllers.MD_DeliveryDirectorController;

import com.sonata.portfoliomanagement.interfaces.MD_DeliveryDirectorRepository;
import com.sonata.portfoliomanagement.model.MD_DeliveryDirector;

@ExtendWith(MockitoExtension.class)
public class MD_DeliveryDirectorControllerTest {

    @Mock
    private MD_DeliveryDirectorRepository deliveryDirectorRepository;

    @InjectMocks
    private MD_DeliveryDirectorController deliveryDirectorController;

    @Test
    public void testGetAllData() {
        // Mock data
        MD_DeliveryDirector director1 = new MD_DeliveryDirector(1, "Director 1");
        MD_DeliveryDirector director2 = new MD_DeliveryDirector(2, "Director 2");
        List<MD_DeliveryDirector> mockData = Arrays.asList(director1, director2);

        // Mock repository behavior
        when(deliveryDirectorRepository.findAll()).thenReturn(mockData);

        // Call controller method
        ResponseEntity<List<MD_DeliveryDirector>> response = deliveryDirectorController.getAllData();

        // Verify response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("Director 1", response.getBody().get(0).getDeliveryDirector());
        assertEquals("Director 2", response.getBody().get(1).getDeliveryDirector());
    }

    @Test
    public void testGetUniqueData() {
        // Mock data
        MD_DeliveryDirector director1 = new MD_DeliveryDirector(1, "Director 1");
        MD_DeliveryDirector director2 = new MD_DeliveryDirector(2, "Director 2");
        List<MD_DeliveryDirector> mockData = Arrays.asList(director1, director2);

        // Mock repository behavior
        when(deliveryDirectorRepository.findAll()).thenReturn(mockData);

        // Call controller method
        ResponseEntity<?> response = deliveryDirectorController.getUniqueData();

        // Verify response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof List);
        List<String> uniqueDirectors = (List<String>) response.getBody();
        assertEquals(2, uniqueDirectors.size());
        assertTrue(uniqueDirectors.contains("Director 1"));
        assertTrue(uniqueDirectors.contains("Director 2"));
    }
}