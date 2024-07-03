package com.sonata.portfoliomanagement.JunitTest;

import com.sonata.portfoliomanagement.controllers.PursuitActionsController;
import com.sonata.portfoliomanagement.interfaces.PursuitActionsRepository;
import com.sonata.portfoliomanagement.interfaces.PursuitTrackerRepository;
import com.sonata.portfoliomanagement.model.PursuitActions;
import com.sonata.portfoliomanagement.model.PursuitTracker;
import com.sonata.portfoliomanagement.services.PursuitActionsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PursuitActionsTests {

    @Mock
    private PursuitActionsService pursuitActionsService;


    @Mock
    private PursuitActionsRepository pursuitActionRepo;


    @Mock
    private PursuitTrackerRepository pursuitTrackerRepo;

    @InjectMocks
    private PursuitActionsController pursuitActionsController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUpdatePursuitActionsByActionItemNumber_NoChangesDetected() {
        // Mock data
        List<PursuitActions> updatedPursuitActionsList = Arrays.asList(
                new PursuitActions(1, "John Doe", 101, "Jane Smith", "ABC Inc.", "Pursuit A", "001", "Description A", "Type A", "Active", "Owner A", new Date(), null, "Remarks A", null)
        );

        // Mock existing PursuitActions in the database
        PursuitActions existingPursuitActions = new PursuitActions(1, "John Doe", 101, "Jane Smith", "ABC Inc.", "Pursuit A", "001", "Description A", "Type A", "Active", "Owner A", new Date(), null, "Remarks A", null);
        when(pursuitActionsService.findById(1)).thenReturn(Optional.of(existingPursuitActions));

        // Call the controller method
        ResponseEntity<Map<String, Object>> responseEntity = pursuitActionsController.updatePursuitActionsByActionItemNumber(updatedPursuitActionsList);

        // Verify response entity
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Map<String, Object> responseBody = responseEntity.getBody();
        assertEquals("No changes detected for ID: 1", responseBody.get("message"));

        // Verify service method invocations
        verify(pursuitActionsService, times(1)).findById(1); // Ensure findById is called once with ID 1
        verify(pursuitActionsService, never()).save(any(PursuitActions.class)); // Ensure save method is never called
    }

    // Add more test cases for scenarios where changes are detected, duplicate entries, validation failures, etc.



    @Test
    public void testGetAllPursuitIds() {
        // Mock data
        List<PursuitTracker> pursuitTrackers = Arrays.asList(
                new PursuitTracker(1, "John Doe", 101, "Jane Smith", "ABC Inc.", "Type A", 10000.0f, null,
                        "Active", "Stage 1", 75, "Project", "Potential", null, "Some remarks", null),
                new PursuitTracker(2, "Alice Brown", 102, "Bob Johnson", "XYZ Corp.", "Type B", 20000.0f, null,
                        "Inactive", "Stage 2", 50, "Pursuit", "Potential", null, "Other remarks", null)
        );

        // Mock behavior of repository
        when(pursuitTrackerRepo.findAll()).thenReturn(pursuitTrackers);

        // Call controller method
        ResponseEntity<?> responseEntity = pursuitActionsController.getAllPursuitIds();

        // Verify the result
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isInstanceOf(List.class);

        List<Integer> pursuitIds = (List<Integer>) responseEntity.getBody();
        assertThat(pursuitIds).containsExactly(101, 102); // Ensure pursuit ids are correctly returned

        // Verify repository method invocation
        verify(pursuitTrackerRepo, times(1)).findAll();
    }




    @Test
    public void testGetAllData() {
        // Mock data
        PursuitActions pursuitAction1 = new PursuitActions(1, "John Doe", 101, "Jane Smith", "Account A", "Pursuit A", "AI001", "Action description 1", "Type A", "Open", "Owner A", new Date(), "AI0002", "Remarks A", null);
        PursuitActions pursuitAction2 = new PursuitActions(2, "Jane Smith", 102, "John Doe", "Account B", "Pursuit B", "AI002", "Action description 2", "Type B", "Closed", "Owner B", new Date(), "AI001", "Remarks B", null);
        List<PursuitActions> pursuitActionsList = Arrays.asList(pursuitAction1, pursuitAction2);

        // Mock repository behavior
        when(pursuitActionRepo.findAll()).thenReturn(pursuitActionsList);

        // Call the controller method
        ResponseEntity<List<PursuitActions>> responseEntity = pursuitActionsController.getAllData();

        // Verify response status
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Verify returned data
        List<PursuitActions> returnedPursuitActions = responseEntity.getBody();
        assertEquals(pursuitActionsList.size(), returnedPursuitActions.size());
        assertEquals(pursuitAction1.getId(), returnedPursuitActions.get(0).getId());
        assertEquals(pursuitAction2.getId(), returnedPursuitActions.get(1).getId());

        // Verify repository method was called once
        verify(pursuitActionRepo, times(1)).findAll();
    }


    @Test
    public void testUpdatePursuitActionsByActionItemNumber_Success() {
        // Mock data
        List<PursuitActions> updatedPursuitActionsList = new ArrayList<>();
        PursuitActions updatedPursuitAction = new PursuitActions();
        updatedPursuitAction.setId(1);
        updatedPursuitAction.setDeliveryManager("Updated Manager");
        updatedPursuitAction.setDeliveryDirector("Updated Director");
        updatedPursuitAction.setAccount("Updated Account");
        updatedPursuitAction.setPursuit("Updated Pursuit");
        updatedPursuitAction.setActionItemNumber("Updated Item Number");
        updatedPursuitAction.setActionDescription("Updated Description");
        updatedPursuitAction.setActionType("Updated Type");
        updatedPursuitAction.setStatus("Updated Status");
        updatedPursuitAction.setActionOwner("Updated Owner");
        updatedPursuitAction.setDueDate(new Date());
        updatedPursuitAction.setDependentActionItem("Updated Dependent Item");
        updatedPursuitAction.setRemarks("Updated Remarks");
        updatedPursuitActionsList.add(updatedPursuitAction);

        // Existing PursuitActions in the database
        PursuitActions existingPursuitAction = new PursuitActions();
        existingPursuitAction.setId(1);
        existingPursuitAction.setDeliveryManager("John Doe");
        existingPursuitAction.setDeliveryDirector("Jane Doe");
        existingPursuitAction.setAccount("Account ABC");
        existingPursuitAction.setPursuit("Pursuit XYZ");
        existingPursuitAction.setActionItemNumber("001");
        existingPursuitAction.setActionDescription("Description of action");
        existingPursuitAction.setActionType("Type A");
        existingPursuitAction.setStatus("Status A");
        existingPursuitAction.setActionOwner("Owner A");
        existingPursuitAction.setDueDate(new Date());
        existingPursuitAction.setDependentActionItem("Dependent Item A");
        existingPursuitAction.setRemarks("Remarks A");

        // Mocking behavior for service findById
        when(pursuitActionsService.findById(1)).thenReturn(Optional.of(existingPursuitAction));

        // Mocking behavior for save
        when(pursuitActionsService.save(any(PursuitActions.class))).thenReturn(updatedPursuitAction);

        // Call the controller method
        ResponseEntity<Map<String, Object>> response = pursuitActionsController.updatePursuitActionsByActionItemNumber(updatedPursuitActionsList);

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Data successfully updated.", response.getBody().get("message"));
        assertNotNull(response.getBody().get("changes"));

        // Verify service method calls
        verify(pursuitActionsService, times(1)).findById(1); // Verify findById was called for the ID
        verify(pursuitActionsService, times(1)).save(any(PursuitActions.class)); // Verify save was called once
    }



    @Test
    public void testDeletePursuitActionsByIds() {
        // Mock data
        List<Integer> idsToDelete = Arrays.asList(1, 2, 3);
        List<String> mockActionItemNumbers = Arrays.asList("AI-001", "AI-002", "AI-003");

        // Mock PursuitActions objects with action item numbers
        PursuitActions pa1 = new PursuitActions();
        pa1.setActionItemNumber(mockActionItemNumbers.get(0));
        PursuitActions pa2 = new PursuitActions();
        pa2.setActionItemNumber(mockActionItemNumbers.get(1));
        PursuitActions pa3 = new PursuitActions();
        pa3.setActionItemNumber(mockActionItemNumbers.get(2));

        // Mock behavior for service findById
        when(pursuitActionsService.findById(1)).thenReturn(Optional.of(pa1));
        when(pursuitActionsService.findById(2)).thenReturn(Optional.of(pa2));
        when(pursuitActionsService.findById(3)).thenReturn(Optional.of(pa3));

        // Call the method under test
        ResponseEntity<Map<String, Object>> response = pursuitActionsController.deletePursuitActionsByIds(idsToDelete);

        // Verify behavior and assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("PursuitActions with specified actionItemNumbers: AI-001, AI-002, AI-003 have been deleted.", response.getBody().get("message"));
        assertEquals("AI-001, AI-002, AI-003", response.getBody().get("deletedActionItemNumbers"));

        // Verify service method calls
        verify(pursuitActionsService, times(idsToDelete.size())).findById(anyInt()); // Verify findById was called for each ID
        verify(pursuitActionsService, times(idsToDelete.size())).delete(any()); // Verify delete was called for each ID
    }


    @Test
    public void testGetPursuitActionsByPursuitId() {
        // Mock data
        int pursuitId = 2;

        // Mock repository method returning empty list
        when(pursuitActionRepo.findByPursuitid(anyInt())).thenReturn(new ArrayList<>());

        // Call controller method
        ResponseEntity<?> responseEntity = pursuitActionsController.getPursuitActionsByPursuitId(pursuitId);

        // Assertions
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("No PursuitActions found with pursuitId: " + pursuitId, responseEntity.getBody());
    }

}


