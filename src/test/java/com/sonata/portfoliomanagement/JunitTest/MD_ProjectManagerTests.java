package com.sonata.portfoliomanagement.JunitTest;

import com.sonata.portfoliomanagement.controllers.MD_ProjectManagerController;
import com.sonata.portfoliomanagement.interfaces.MD_ProjectManagerRepository;
import com.sonata.portfoliomanagement.model.MD_ProjectManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MD_ProjectManagerTests {

    @Mock
    private MD_ProjectManagerRepository projectManagerRepository;

    @InjectMocks
    private MD_ProjectManagerController projectManagerController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllProjectManagers_Success() {
        // Mock data
        List<MD_ProjectManager> mockProjectManagers = new ArrayList<>();
        mockProjectManagers.add(new MD_ProjectManager(1, "John Doe"));
        mockProjectManagers.add(new MD_ProjectManager(2, "Jane Smith"));

        // Mock repository behavior
        when(projectManagerRepository.findAll()).thenReturn(mockProjectManagers);

        // Call the controller method
        ResponseEntity<List<MD_ProjectManager>> response = projectManagerController.getAllProjectManagers();

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size()); // Assuming two project managers are returned
        assertEquals("John Doe", response.getBody().get(0).getProjectManager());
        assertEquals("Jane Smith", response.getBody().get(1).getProjectManager());

        // Verify repository method was called once
        verify(projectManagerRepository, times(1)).findAll();
    }

    @Test
    public void testGetUniqueProjectManagers_Success() {
        // Mock data
        List<MD_ProjectManager> mockProjectManagers = new ArrayList<>();
        mockProjectManagers.add(new MD_ProjectManager(1, "John Doe"));
        mockProjectManagers.add(new MD_ProjectManager(2, "Jane Smith"));
        mockProjectManagers.add(new MD_ProjectManager(3, "John Doe")); // Duplicate name

        // Mock repository behavior
        when(projectManagerRepository.findAll()).thenReturn(mockProjectManagers);

        // Call the controller method
        ResponseEntity<List<String>> response = projectManagerController.getUniqueProjectManagers();

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<String> uniqueProjectManagers = response.getBody();
        assertEquals(2, uniqueProjectManagers.size()); // Expecting two unique names
        assertEquals("John Doe", uniqueProjectManagers.get(0));
        assertEquals("Jane Smith", uniqueProjectManagers.get(1));

        // Verify repository method was called once
        verify(projectManagerRepository, times(1)).findAll();
    }


}
