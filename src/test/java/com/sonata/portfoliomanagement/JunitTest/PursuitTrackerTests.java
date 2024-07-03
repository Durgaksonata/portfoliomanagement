package com.sonata.portfoliomanagement.JunitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.sonata.portfoliomanagement.controllers.PursuitTrackerController;
import com.sonata.portfoliomanagement.interfaces.MD_PursuitProbabilityRepository;
import com.sonata.portfoliomanagement.interfaces.PursuitActionsRepository;
import com.sonata.portfoliomanagement.interfaces.PursuitTrackerRepository;
import com.sonata.portfoliomanagement.model.MD_PursuitProbability;
import com.sonata.portfoliomanagement.model.PursuitTracker;
import com.sonata.portfoliomanagement.model.PursuitTrackerDTO;
import com.sonata.portfoliomanagement.services.PursuitTrackerService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class)
public class PursuitTrackerTests {

    @InjectMocks
    private PursuitTrackerController pursuitTrackerController;

    @Mock
    private PursuitTrackerService pursuitTrackerService;

    @Mock
    private PursuitTrackerRepository pursuitTrackerRepository;

    @Mock
    private MD_PursuitProbabilityRepository mdPursuitProbabilityRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    public void testUpdatePursuitTrackers_Success() {
//        // Given
//        PursuitTracker pursuitTracker1 = new PursuitTracker(1, "John Doe", 101, "Jane Smith", "Account1",
//                "Type1", 100000.0f, LocalDate.of(2023, 6, 1), "Status1", "Stage1",
//                50, "Project1", "Potential1", LocalDate.of(2024, 12, 31), "Remarks1", null);
//
//        // Modify some fields to ensure changes are detected
//        pursuitTracker1.setDeliveryDirector("Updated Delivery Director");
//        pursuitTracker1.setStage("New Stage");
//
//        List<PursuitTracker> pursuitTrackers = Collections.singletonList(pursuitTracker1);
//
//        // Mock service method behavior
//        when(pursuitTrackerService.calculateStage("Status1", "Type1")).thenReturn("New Stage");
//        when(pursuitTrackerService.calculatePursuitProbability("Status1", "Type1")).thenReturn(50);
//
//        // Mock repository behavior
//        when(pursuitTrackerRepository.findById(1)).thenReturn(Optional.of(pursuitTracker1));
//        when(pursuitTrackerRepository.save(pursuitTracker1)).thenReturn(pursuitTracker1);
//
//        // Mock MD_PursuitProbabilityRepository behavior
//        when(mdPursuitProbabilityRepository.findByPursuitStatusAndType("Status1", "Type1"))
//                .thenReturn(Optional.of(new MD_PursuitProbability(1, "Status1", "Type1", 50, "New Stage")));
//
//        // When
//        ResponseEntity<Map<String, Object>> responseEntity = pursuitTrackerController.updatePursuitTrackers(pursuitTrackers);
//
//        // Then
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals("Data successfully updated.", responseEntity.getBody().get("message"));
//
//        // Additional assertions to verify modified fields
//        assertEquals("Updated Delivery Director", pursuitTracker1.getDeliveryDirector());
//        assertEquals("New Stage", pursuitTracker1.getStage());
//
//        // Verify method invocations
//        verify(pursuitTrackerRepository, times(1)).findById(1);
//        verify(pursuitTrackerRepository, times(1)).save(pursuitTracker1);
//    }


    @Test
    public void testUpdatePursuitTrackers_Success() {
        // Given
        PursuitTracker pursuitTracker1 = new PursuitTracker(1, "John Doe", 101, "Jane Smith", "Account1",
                "Type1", 100000.0f, LocalDate.of(2023, 6, 1), "Status1", "Stage1",
                50, "Project1", "Potential1", LocalDate.of(2024, 12, 31), "Remarks1", null);

        List<PursuitTracker> pursuitTrackers = Collections.singletonList(pursuitTracker1);

        // Mock service method behavior
        when(pursuitTrackerService.calculateStage("Status1", "Type1")).thenReturn("Stage1"); // Assuming no change in stage
        when(pursuitTrackerService.calculatePursuitProbability("Status1", "Type1")).thenReturn(50);

        // Mock repository behavior
        when(pursuitTrackerRepository.findById(1)).thenReturn(Optional.of(pursuitTracker1));

        // When
        ResponseEntity<Map<String, Object>> responseEntity = pursuitTrackerController.updatePursuitTrackers(pursuitTrackers);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("No changes detected.", responseEntity.getBody().get("message"));

        // Verify method invocations
        verify(pursuitTrackerRepository, times(1)).findById(1);
        verify(pursuitTrackerRepository, never()).save(any()); // Ensure save method is not called
//    }

        // Add more test cases to cover edge cases, such as duplicate entries, missing data, etc.
    }



    @Mock
    private PursuitActionsRepository pursuitActionsRepository;

    @Test
    public void testGetPursuitTrackers() {
        // Mock data
        PursuitTracker pursuitTracker1 = new PursuitTracker(1, "John Doe", 101, "Jane Smith", "Acme Inc.", "Sales", 100000.0f,
                LocalDate.of(2024, 6, 1), "Active", "Planning", 80, "Project", "High", LocalDate.of(2024, 7, 1), "Some remarks", null);

        PursuitTracker pursuitTracker2 = new PursuitTracker(2, "Alice Cooper", 102, "Bob Brown", "Tech Solutions", "Project", 75000.0f,
                LocalDate.of(2024, 5, 15), "Closed", "Execution", 90, "Pursuit", "Medium", LocalDate.of(2024, 6, 30), "No remarks", null);

        List<PursuitTracker> mockPursuitTrackers = new ArrayList<>();
        mockPursuitTrackers.add(pursuitTracker1);
        mockPursuitTrackers.add(pursuitTracker2);

        // Mock repository behavior
        when(pursuitTrackerRepository.findAll()).thenReturn(mockPursuitTrackers);

        // Call controller method
        List<PursuitTrackerDTO> dtos = pursuitTrackerController.getPursuitTrackers();

        // Assertions
        assertEquals(2, dtos.size()); // Ensure correct number of DTOs

        // Verify content of first DTO
        PursuitTrackerDTO dto1 = dtos.get(0);
        assertEquals(1, dto1.getId());
        assertEquals("John Doe", dto1.getDeliveryDirector());
        assertEquals("Jane Smith", dto1.getDeliveryManager());
        assertEquals("Acme Inc.", dto1.getAccount());
        assertEquals("Sales", dto1.getType());
        assertEquals(100000.0f, dto1.getTcv());
        assertEquals(LocalDate.of(2024, 6, 1), dto1.getIdentifiedmonth());
        assertEquals("Active", dto1.getPursuitstatus());
        assertEquals("Project", dto1.getProjectorPursuit());
        assertEquals("High", dto1.getPursuitorpotential());
        assertEquals(LocalDate.of(2024, 7, 1), dto1.getLikelyClosureorActualClosure());
        assertEquals("Some remarks", dto1.getRemarks());

        // Verify content of second DTO
        PursuitTrackerDTO dto2 = dtos.get(1);
        assertEquals(2, dto2.getId());
        assertEquals("Alice Cooper", dto2.getDeliveryDirector());
        assertEquals("Bob Brown", dto2.getDeliveryManager());
        assertEquals("Tech Solutions", dto2.getAccount());
        assertEquals("Project", dto2.getType());
        assertEquals(75000.0f, dto2.getTcv());
        assertEquals(LocalDate.of(2024, 5, 15), dto2.getIdentifiedmonth());
        assertEquals("Closed", dto2.getPursuitstatus());
        assertEquals("Pursuit", dto2.getProjectorPursuit());
        assertEquals("Medium", dto2.getPursuitorpotential());
        assertEquals(LocalDate.of(2024, 6, 30), dto2.getLikelyClosureorActualClosure());
        assertEquals("No remarks", dto2.getRemarks());
    }

    @Test
    public void testDeletePursuitTrackersNotFOund() {
        // Mock data
        List<Integer> pursuitTrackerIds = Arrays.asList(1, 2, 3);
        PursuitTracker pursuitTracker1 = new PursuitTracker(1, "John Doe", 101, "Jane Smith", "Acme Inc.", "Sales", 100000.0f,
                null, "Active", "Planning", 80, "Project", "High", null, "Some remarks", null);

        PursuitTracker pursuitTracker2 = new PursuitTracker(2, "Alice Cooper", 102, "Bob Brown", "Tech Solutions", "Project", 75000.0f,
                null, "Closed", "Execution", 90, "Pursuit", "Medium", null, "No remarks", null);

        List<PursuitTracker> mockPursuitTrackers = Arrays.asList(pursuitTracker1, pursuitTracker2);

        // Mock repository behavior for findById
        when(pursuitTrackerRepository.findById(1)).thenReturn(Optional.of(pursuitTracker1));
        when(pursuitTrackerRepository.findById(2)).thenReturn(Optional.of(pursuitTracker2));
        when(pursuitTrackerRepository.findById(3)).thenReturn(Optional.empty()); // Not found case

        // Mock repository behavior for findByPursuit (related PursuitActions)
        when(pursuitActionsRepository.findByPursuit(anyString())).thenReturn(Collections.emptyList());

        // Mock repository behavior for delete
        doNothing().when(pursuitTrackerRepository).delete(any());

        // Call controller method
        ResponseEntity<Map<String, Object>> responseEntity = pursuitTrackerController.deletePursuitTrackers(pursuitTrackerIds);

        // Assertions
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        Map<String, Object> response = responseEntity.getBody();
        assertEquals("Some entries were not found.", response.get("message"));
        assertEquals(Collections.singletonList(3), response.get("notFoundIds"));
    }



    @Test
    public void testDeletePursuitTrackers_EntriesFound() {
        // Mock data
        List<Integer> pursuitTrackerIds = Arrays.asList(1, 2);
        PursuitTracker pursuitTracker1 = new PursuitTracker(1, "John Doe", 101, "Jane Smith", "Acme Inc.", "Sales", 100000.0f,
                null, "Active", "Planning", 80, "Project", "High", null, "Some remarks", null);

        PursuitTracker pursuitTracker2 = new PursuitTracker(2, "Alice Cooper", 102, "Bob Brown", "Tech Solutions", "Project", 75000.0f,
                null, "Closed", "Execution", 90, "Pursuit", "Medium", null, "No remarks", null);

        // Mock repository behavior for findById
        when(pursuitTrackerRepository.findById(1)).thenReturn(Optional.of(pursuitTracker1));
        when(pursuitTrackerRepository.findById(2)).thenReturn(Optional.of(pursuitTracker2));

        // Call controller method
        ResponseEntity<Map<String, Object>> responseEntity = pursuitTrackerController.deletePursuitTrackers(pursuitTrackerIds);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Object> response = responseEntity.getBody();
        assertEquals("Entries successfully deleted.", response.get("message"));
        assertEquals(Arrays.asList(1, 2), response.get("deletedIds"));

        // Verify repository method calls
        verify(pursuitTrackerRepository, times(1)).findById(1);
        verify(pursuitTrackerRepository, times(1)).findById(2);
        //verify(repository, never()).delete(any()); // Ensure delete was not called in this test case
        verify(pursuitTrackerRepository, times(1)).delete(pursuitTracker1);
        verify(pursuitTrackerRepository, times(1)).delete(pursuitTracker2);
    }

    @Test
    public void testAddPursuitTrackers_Success() {
        // Given
        PursuitTracker pursuitTracker = new PursuitTracker();
        pursuitTracker.setDeliveryManager("John Doe");
        pursuitTracker.setDeliveryDirector("Jane Smith");
        pursuitTracker.setAccount("Symetra.");
        pursuitTracker.setType("Sales");
        pursuitTracker.setTcv(100000.0f);
        pursuitTracker.setIdentifiedmonth(LocalDate.of(2024, 6, 1));
        pursuitTracker.setPursuitstatus("Active");
        pursuitTracker.setStage("Planning");
        pursuitTracker.setPursuitProbability(80);
        pursuitTracker.setProjectorPursuit("Project");
        pursuitTracker.setPursuitorpotential("Pursuit");
        pursuitTracker.setLikelyClosureorActualClosure(LocalDate.of(2024, 7, 1));
        pursuitTracker.setRemarks("Find out components of Phase 4  and provide proposal");

        List<PursuitTracker> pursuitTrackers = Collections.singletonList(pursuitTracker);

        // Mock service method behavior
        when(pursuitTrackerService.calculateStage(anyString(), anyString())).thenReturn("Planning");
        when(pursuitTrackerService.calculatePursuitProbability(anyString(), anyString())).thenReturn(80);

        // Mock repository behavior
        when(pursuitTrackerRepository.findByProjectorPursuit(anyString())).thenReturn(Optional.empty());
        when(pursuitTrackerRepository.save(any())).thenReturn(pursuitTracker);

        // When
        ResponseEntity<?> responseEntity = pursuitTrackerController.addPursuitTrackers(pursuitTrackers);

        // Then
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Data Saved Successfully!", ((Map<String, Object>) responseEntity.getBody()).get("message"));

        // Verify repository method calls
        verify(pursuitTrackerRepository, times(1)).findByProjectorPursuit("Project");
        verify(pursuitTrackerRepository, times(1)).save(pursuitTracker);
    }


}
