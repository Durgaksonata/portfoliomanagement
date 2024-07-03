package com.sonata.portfoliomanagement.JunitTest;


import com.sonata.portfoliomanagement.controllers.DataEntryController;
import com.sonata.portfoliomanagement.interfaces.DataEntryRepository;
import com.sonata.portfoliomanagement.interfaces.RevenueBudgetSummaryRepository;
import com.sonata.portfoliomanagement.interfaces.RevenueGrowthSummaryRepository;
import com.sonata.portfoliomanagement.model.DataEntry;
import com.sonata.portfoliomanagement.model.DataEntryDTO;
import com.sonata.portfoliomanagement.services.DataEntryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DataEntryTests {

    @Mock
    private DataEntryService dataEntryService;

    @Mock
    private DataEntryRepository dataEntryRepository;

    @InjectMocks
    private DataEntryController dataEntryController;


    @Mock
    private RevenueBudgetSummaryRepository revenueBudgetSummaryRepository;

    @Mock
    private RevenueGrowthSummaryRepository revenueGrowthSummaryRepository;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllData() {
        List<DataEntry> testData = new ArrayList<>();
        // Add test data to the list

        when(dataEntryRepository.findAll()).thenReturn(testData);

        ResponseEntity<List<DataEntry>> response = dataEntryController.getAllData();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testData, response.getBody());
    }

    @Test
    public void testCreateDataEntries() {
        List<DataEntryDTO> testDataEntryDTOs = new ArrayList<>();
        // Add test data entry DTOs to the list

        // Mock the service method to return the saved data entries
        List<DataEntry> savedDataEntries = new ArrayList<>();
        // Add saved data entries to the list

        when(dataEntryService.isDuplicateEntry(any(DataEntryDTO.class))).thenReturn(false);
        when(dataEntryService.createDataEntryFromDTO(any(DataEntryDTO.class))).thenReturn(new DataEntry());
        when(dataEntryService.saveDataEntry(any(DataEntry.class))).thenReturn(new DataEntry());

        ResponseEntity<Map<String, Object>> response = dataEntryController.createDataEntries(testDataEntryDTOs);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Map<String, Object> responseBody = response.getBody();
        assertEquals("Data successfully created.", responseBody.get("message"));
        assertEquals(savedDataEntries, responseBody.get("data"));
    }

    @Test
    public void testGetFinancialYearList() {
        // Create sample data entries with different financial years
        List<DataEntry> testData = new ArrayList<>();
        testData.add(new DataEntry(1, "January", "Vertical1", "Classification1", "Director1", "Manager1", "Account1", "ProjectManager1", "Project1", "Category1", "Annuity", 100.0f, "Type1", 2022, "Q1", 50, "Stage1", 50.0f, 30.0f, 40.0f, 10.0f, 20.0f, 1000.0f, 500.0f, 1500.0f, 200.0f, 300.0f, 400.0f, 10000.0f));
        testData.add(new DataEntry(2, "February", "Vertical2", "Classification2", "Director2", "Manager2", "Account2", "ProjectManager2", "Project2", "Category2", "Non-Annuity", 200.0f, "Type2", 2022, "Q2", 70, "Stage2", 60.0f, 40.0f, 50.0f, 20.0f, 30.0f, 2000.0f, 1000.0f, 3000.0f, 300.0f, 400.0f, 500.0f, 20000.0f));
        testData.add(new DataEntry(3, "March", "Vertical3", "Classification3", "Director3", "Manager3", "Account3", "ProjectManager3", "Project3", "Category3", "Annuity", 300.0f, "Type3", 2023, "Q3", 80, "Stage3", 70.0f, 50.0f, 60.0f, 30.0f, 40.0f, 3000.0f, 1500.0f, 4500.0f, 400.0f, 500.0f, 600.0f, 30000.0f));

        // Mock the repository to return the sample data
        when(dataEntryRepository.findAll()).thenReturn(testData);

        // Invoke the controller method
        List<Integer> result = dataEntryController.getFinancialYearList();

        // Verify the result
        List<Integer> expected = testData.stream()
                .map(DataEntry::getFinancialYear)
                .distinct()
                .collect(Collectors.toList());
        assertEquals(expected, result);
    }




    @Test
    void testUpdateDataEntries() {
        // Create a sample list of DataEntryDTO objects
        List<DataEntryDTO> dataEntryDTOList = new ArrayList<>();

        DataEntryDTO dataEntryDTO = new DataEntryDTO();
        dataEntryDTO.setId(1);
        dataEntryDTO.setMonth("January");
        dataEntryDTO.setVertical("Vertical1");
        dataEntryDTO.setClassification("Classification1");
        dataEntryDTO.setDeliveryManager("DeliveryManager1");
        dataEntryDTO.setAccount("Account1");
        dataEntryDTO.setProjectManager("ProjectManager1");
        dataEntryDTO.setProjectName("ProjectName1");
        dataEntryDTO.setFinancialYear(2023);
        dataEntryDTO.setQuarter("Q1");
        dataEntryDTO.setDeliveryDirector("DeliveryDirector1");
        dataEntryDTO.setCategory("Category1");
        dataEntryDTO.setAnnuityorNonAnnuity("Annuity");
        dataEntryDTO.setValue(1000.0f);
        dataEntryDTO.setBudget(500.0f); // Set the budget field to avoid NullPointerException

        // Add the dataEntryDTO to the list
        dataEntryDTOList.add(dataEntryDTO);

        // Create a corresponding DataEntry object
        DataEntry existingDataEntry = new DataEntry();
        existingDataEntry.setId(1);
        existingDataEntry.setMonth("December");
        existingDataEntry.setVertical("Vertical1");
        existingDataEntry.setClassification("Classification1");
        existingDataEntry.setDeliveryManager("DeliveryManager1");
        existingDataEntry.setAccount("Account1");
        existingDataEntry.setProjectManager("ProjectManager1");
        existingDataEntry.setProjectName("ProjectName1");
        existingDataEntry.setFinancialYear(2023);
        existingDataEntry.setQuarter("Q4");
        existingDataEntry.setDeliveryDirector("DeliveryDirector1");
        existingDataEntry.setCategory("Category1");
        existingDataEntry.setAnnuityorNonAnnuity("Non-Annuity");
        existingDataEntry.setValue(2000.0f);
        existingDataEntry.setBudget(600.0f); // Set the budget field for the existing data entry

        // Mocking dataEntryRepo.findById() method to return the existing data entry
        when(dataEntryRepository.findById(1)).thenReturn(Optional.of(existingDataEntry));

        // Mocking dataEntryService.saveDataEntry() method to return the updated data entry
        when(dataEntryService.saveDataEntry(any())).thenAnswer(invocation -> {
            DataEntry updatedDataEntry = invocation.getArgument(0);
            return updatedDataEntry;
        });

        // Call the method to be tested
        ResponseEntity<Map<String, Object>> responseEntity = dataEntryController.updateDataEntries(dataEntryDTOList);

        // Verify that the service method is called once for each DataEntryDTO in the list
        verify(dataEntryService, times(dataEntryDTOList.size())).saveDataEntry(any());

        // Verify the response status code
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Verify the response message
        assertEquals("Data successfully updated.", responseEntity.getBody().get("message"));

        // Verify the changes map in the response
        assertTrue(responseEntity.getBody().containsKey("changes"));
        Map<String, Object> changes = (Map<String, Object>) responseEntity.getBody().get("changes");
        assertNotNull(changes);
        assertEquals("January", changes.get("month"));
        assertEquals("Annuity", changes.get("annuityorNonAnnuity"));
        assertEquals(1000.0f, changes.get("value"));
        assertEquals(500.0f, changes.get("budget"));
        // Add other assertions for changes...
    }




}