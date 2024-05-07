package com.sonata.portfoliomanagement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import com.sonata.portfoliomanagement.controllers.RevenueBudgetSummaryController;
import com.sonata.portfoliomanagement.interfaces.RevenueBudgetSummaryRepository;
import com.sonata.portfoliomanagement.model.RevenueBudgetSummary;
import com.sonata.portfoliomanagement.model.RevenueDTO;

@SpringBootTest
class PorfolioManagementApplicationTests {

	@Autowired
	private RevenueBudgetSummaryRepository revenueRepo;

	@Autowired
	private RevenueBudgetSummaryController controller;

	@Mock
	private RevenueBudgetSummaryRepository revenuerepo;

	@InjectMocks
	private RevenueBudgetSummaryController Controller;

	@Captor
	private ArgumentCaptor<List<String>> verticalsCaptor;

//Have changed the data type of date to local date as the test cases cannot take date as formatted.
	@Test
//	@Rollback(value=false)
	@Order(1)
	public void getRevenueTest() {
		RevenueBudgetSummary rn = revenueRepo.findById(21).get();
		Assertions.assertThat(rn.getId()).isEqualTo(21);
	}


	@Test
	public void testAccountList() {
		// Create a sample list of accounts
		List<String> inputList = new ArrayList<>();
		inputList.add("Account1");
		inputList.add("Account2");
		inputList.add("Account3");

		// Create an instance of RevenueDTO
		RevenueDTO revenueDTO = new RevenueDTO();

		// Set the accountList
		revenueDTO.setAccountList(inputList);

		// Get the accountList
		List<String> actualAccountList = revenueDTO.getAccountList();

		// Assert that the retrieved accountList matches the expected accountList
		assertEquals(inputList, actualAccountList);
	}

	@Test
	public void testdmList() {
		// Create a sample list of accounts
		List<String> inputList = new ArrayList<>();
		inputList.add("DM1");
		inputList.add("DM2");
		inputList.add("DM3");

		// Create an instance of RevenueDTO
		RevenueDTO revenueDTO = new RevenueDTO();

		// Set the accountList
		revenueDTO.setDmList(inputList);

		// Get the accountList
		List<String> actualdmList = revenueDTO.getDmList();

		// Assert that the retrieved accountList matches the expected accountList
		assertEquals(inputList, actualdmList);
	}

	@Test
	public void testpmList() {
		// Create a sample list of accounts
		List<String> inputList = new ArrayList<>();
		inputList.add("PM1");
		inputList.add("PM2");
		inputList.add("PM3");

		// Create an instance of RevenueDTO
		RevenueDTO revenueDTO = new RevenueDTO();

		// Set the accountList
		revenueDTO.setPmList(inputList);

		// Get the accountList
		List<String> actualpmList = revenueDTO.getPmList();

		// Assert that the retrieved accountList matches the expected accountList
		assertEquals(inputList, actualpmList);
	}

	@Test
	public void testverticalList() {
		// Create a sample list of accounts
		List<String> inputList = new ArrayList<>();
		inputList.add("Vertical1");
		inputList.add("Vertical2");
		inputList.add("Vertical3");

		// Create an instance of RevenueDTO
		RevenueDTO revenueDTO = new RevenueDTO();

		// Set the accountList
		revenueDTO.setVerticalList(inputList);

		// Get the accountList
		List<String> actualverticalList = revenueDTO.getVerticalList();

		// Assert that the retrieved accountList matches the expected accountList
		assertEquals(inputList, actualverticalList);
	}

	@Test
	public void testclassificationList() {
		// Create a sample list of accounts
		List<String> inputList = new ArrayList<>();
		inputList.add("Classification1");
		inputList.add("Classification2");
		inputList.add("Classification3");

		// Create an instance of RevenueDTO
		RevenueDTO revenueDTO = new RevenueDTO();

		// Set the accountList
		revenueDTO.setClassificationList(inputList);

		// Get the accountList
		List<String> actualclassificationList = revenueDTO.getClassificationList();

		// Assert that the retrieved accountList matches the expected accountList
		assertEquals(inputList, actualclassificationList);
	}

	@Test
	public void testprojectList() {
		// Create a sample list of accounts
		List<String> inputList = new ArrayList<>();
		inputList.add("Project1");
		inputList.add("Project2");
		inputList.add("Project3");

		// Create an instance of RevenueDTO
		RevenueDTO revenueDTO = new RevenueDTO();

		// Set the accountList
		revenueDTO.setProjectList(inputList);

		// Get the accountList
		List<String> actualprojectList = revenueDTO.getProjectList();

		// Assert that the retrieved accountList matches the expected accountList
		assertEquals(inputList, actualprojectList);
	}

	@Test
	public void testFYList() {
		// Create a sample list of accounts
		List<Integer> inputList = new ArrayList<>();
		inputList.add(2022);
		inputList.add(2023);
		inputList.add(2024);

		// Create an instance of RevenueDTO
		RevenueDTO revenueDTO = new RevenueDTO();

		// Set the accountList
		revenueDTO.setFinancialYear(inputList);

		// Get the accountList
		List<Integer> actualfyList = revenueDTO.getFinancialYear();

		// Assert that the retrieved accountList matches the expected accountList
		assertEquals(inputList, actualfyList);
	}

	@Test
	public void testquarterList() {
		// Create a sample list of accounts
		List<String> inputList = new ArrayList<>();
		inputList.add("Q1");
		inputList.add("Q2");
		inputList.add("Q3");

		// Create an instance of RevenueDTO
		RevenueDTO revenueDTO = new RevenueDTO();

		// Set the accountList
		revenueDTO.setQuarterList(inputList);

		// Get the accountList
		List<String> actualquarterList = revenueDTO.getQuarterList();

		// Assert that the retrieved accountList matches the expected accountList
		assertEquals(inputList, actualquarterList);
	}

	@Test
	public void getListOfRevenueTest() {

		List<RevenueBudgetSummary> rev = revenueRepo.findAll();

		Assertions.assertThat(rev.size()).isGreaterThan(0);

	}

	@Test
	public void testGetyYear() {
		// Given
		int fianancialYear = 2023;
		//LocalDate date = LocalDate.of(2023, 8, 9);
		RevenueBudgetSummary revenue1 = new RevenueBudgetSummary(21, "Retail", "Non_MDU", "Girish","Sudhakar S", "Astra Zeneca", "Abhishek Raj", "AZ_Braindata_feed_QA", 2023, LocalDate.of(2023, 8, 9), "Q3", 3, 3, 3); // Initialize with sample data
		revenueRepo.save(revenue1);


		// When
		List<RevenueBudgetSummary> result = revenueRepo.findByFinancialYear(fianancialYear);

		// Then
		assertEquals(1, result.size());
		// Add more assertions based on your expected results
	}

	@Test
	public void testGetydeliveryManager() {
		// Given
		String deliveryManager = "Sudhakar S";
		//LocalDate date = LocalDate.of(2023, 8, 9);
		RevenueBudgetSummary revenue1 = new RevenueBudgetSummary(21, "Retail", "Non_MDU","Girish", "Sudhakar S", "Astra Zeneca", "Abhishek Raj", "AZ_Braindata_feed_QA", 2023, LocalDate.of(2023, 8, 9), "Q3", 3, 3, 3); // Initialize with sample data
		revenueRepo.save(revenue1);


		// When
		List<RevenueBudgetSummary> result = revenueRepo.findBydeliveryManager(deliveryManager);

		// Then
		assertEquals(1, result.size());
		// Add more assertions based on your expected results
	}

	@Test
	public void testGetyQuarter() {
		// Given
		String quarter = "Q3";
		//LocalDate date = LocalDate.of(2023, 8, 9);
		RevenueBudgetSummary revenue1 = new RevenueBudgetSummary(21, "Retail", "Non_MDU", "Girish","Sudhakar S", "Astra Zeneca", "Abhishek Raj", "AZ_Braindata_feed_QA", 2023, LocalDate.of(2023, 8, 9), "Q3", 3, 3, 3); // Initialize with sample data
		revenueRepo.save(revenue1);


		// When
		List<RevenueBudgetSummary> result = revenueRepo.findByquarter(quarter);

		// Then
		assertEquals(1, result.size());
		// Add more assertions based on your expected results
	}

	@Test
	public void testGetByVertical() {
		// Given
		String vertical = "Retail";
		//LocalDate date = LocalDate.of(2023, 8, 9);
		RevenueBudgetSummary revenue1 = new RevenueBudgetSummary(21, "Retail", "Non_MDU","Girish", "Sudhakar S", "Astra Zeneca", "Abhishek Raj", "AZ_Braindata_feed_QA", 2023, LocalDate.of(2023, 8, 9), "Q3", 3, 3, 3); // Initialize with sample data
		revenueRepo.save(revenue1);


		// When
		List<RevenueBudgetSummary> result = revenueRepo.findAllByVertical(vertical);

		// Then
		assertEquals(1, result.size());
		// Add more assertions based on your expected results
	}

	@Test
	public void testGetByprojectManager() {
		// Given
		String projectManager = "Abhishek Raj";
		//LocalDate date = LocalDate.of(2023, 8, 9);
		RevenueBudgetSummary revenue1 = new RevenueBudgetSummary(21, "Retail", "Non_MDU","Girish", "Sudhakar S", "Astra Zeneca", "Abhishek Raj", "AZ_Braindata_feed_QA", 2023, LocalDate.of(2023, 8, 9), "Q3", 3, 3, 3); // Initialize with sample data
		revenueRepo.save(revenue1);


		// When
		List<RevenueBudgetSummary> result = revenueRepo.findByprojectManager(projectManager);

		// Then
		assertEquals(1, result.size());
		// Add more assertions based on your expected results
	}

	@Test
	public void testGetByclassification() {
		// Given
		String classification = "Non_MDU";
		//LocalDate date = LocalDate.of(2023, 8, 9);
		RevenueBudgetSummary revenue1 = new RevenueBudgetSummary(21, "Retail", "Non_MDU","Girish", "Sudhakar S", "Astra Zeneca", "Abhishek Raj", "AZ_Braindata_feed_QA", 2023, LocalDate.of(2023, 8, 9), "Q3", 3, 3, 3); // Initialize with sample data
		revenueRepo.save(revenue1);


		// When
		List<RevenueBudgetSummary> result = revenueRepo.findByClassification(classification);

		// Then
		assertEquals(1, result.size());
		// Add more assertions based on your expected results
	}

	@Test
	public void testGetByprojectName() {
		// Given
		String projectName = "AZ_Braindata_feed_QA";
		//LocalDate date = LocalDate.of(2023, 8, 9);
		RevenueBudgetSummary revenue1 = new RevenueBudgetSummary(21, "Retail", "Non_MDU","Girish", "Sudhakar S", "Astra Zeneca", "Abhishek Raj", "AZ_Braindata_feed_QA", 2023, LocalDate.of(2023, 8, 9), "Q3", 3, 3, 3); // Initialize with sample data
		revenueRepo.save(revenue1);


		// When
		List<RevenueBudgetSummary> result = revenueRepo.findByProjectName(projectName);

		// Then
		assertEquals(1, result.size());
		// Add more assertions based on your expected results
	}

	@Test
	public void testGetByAccount() {
		// Given
		String account = "Astra Zeneca";
		//LocalDate date = LocalDate.of(2023, 8, 9);
		RevenueBudgetSummary revenue1 = new RevenueBudgetSummary(21, "Retail", "Non_MDU", "Sudhakar S","Girish", "Astra Zeneca", "Abhishek Raj", "AZ_Braindata_feed_QA", 2023, LocalDate.of(2023, 8, 9), "Q3", 3, 3, 3); // Initialize with sample data
		revenueRepo.save(revenue1);


		// When
		List<RevenueBudgetSummary> result = revenueRepo.findByAccount(account);

		// Then
		assertEquals(1, result.size());
		// Add more assertions based on your expected results
	}

	@Test
	public void testGetDMByVerticalAndClassification() {
		// Mock dependency
		RevenueBudgetSummaryRepository revenueRepo = mock(RevenueBudgetSummaryRepository.class);
		// Instantiate the controller
		RevenueBudgetSummaryController revenueController = new RevenueBudgetSummaryController(revenueRepo);

		// Create sample request body
		RevenueDTO dmList = new RevenueDTO();
		dmList.setVerticalList(Arrays.asList("Vertical1", "Vertical2"));
		dmList.setClassificationList(Arrays.asList("Class1", "Class2"));

		// Create sample revenues
		List<RevenueBudgetSummary> revenues = Arrays.asList(
				new RevenueBudgetSummary(1, "Vertical1", "Class1", "DD1","DM1", "Account1", "ProjectManager1", "Project1", 2024, LocalDate.now(), "Q1", 1000, 800, 200),
				new RevenueBudgetSummary(2, "Vertical2", "Class2", "DD2","DM2", "Account2", "ProjectManager2", "Project2", 2024, LocalDate.now(), "Q2", 1500, 1200, 300)
		);

		// Simulate repository behavior
		when(revenueRepo.findByVerticalInAndClassificationIn(anyList(), anyList())).thenReturn(revenues);

		// Call controller method
		ResponseEntity<Set<String>> response = revenueController.getDMByVerticalAndClassification(dmList);

		// Verify behavior
		verify(revenueRepo).findByVerticalInAndClassificationIn(Arrays.asList("Vertical1", "Vertical2"), Arrays.asList("Class1", "Class2"));

		// Verify response
		Set<String> expectedDMs = new HashSet<>(Arrays.asList("DM1", "DM2"));
		assert response.getBody() != null;
		assert response.getBody().equals(expectedDMs);
	}

	@Test
	public void testGetaccountByDMAndClassification() {
		// Mock dependency
		RevenueBudgetSummaryRepository revenueRepo = mock(RevenueBudgetSummaryRepository.class);
		// Instantiate the controller
		RevenueBudgetSummaryController revenueController = new RevenueBudgetSummaryController(revenueRepo);

		// Create sample request body
		RevenueDTO accountList = new RevenueDTO();
		accountList.setDmList(Arrays.asList("DM1", "DM2"));
		accountList.setClassificationList(Arrays.asList("Class1", "Class2"));

		// Create sample revenues
		List<RevenueBudgetSummary> revenues = Arrays.asList(
				new RevenueBudgetSummary(1, "Vertical1", "Class1", "DD1","DM1", "Account1", "ProjectManager1", "Project1", 2024, LocalDate.now(), "Q1", 1000, 800, 200),
				new RevenueBudgetSummary(2, "Vertical2", "Class2", "DD2","DM2", "Account2", "ProjectManager2", "Project2", 2024, LocalDate.now(), "Q2", 1500, 1200, 300)
		);

		// Simulate repository behavior
		when(revenueRepo.findByDeliveryManagerInAndClassificationIn(anyList(), anyList())).thenReturn(revenues);

		// Call controller method
		ResponseEntity<Set<String>> response = revenueController.getAccountByDMAndClassification(accountList);

		// Verify behavior
		verify(revenueRepo).findByDeliveryManagerInAndClassificationIn(Arrays.asList("DM1", "DM2"), Arrays.asList("Class1", "Class2"));

		// Verify response
		Set<String> expectedAccountss = new HashSet<>(Arrays.asList("Account1", "Account2"));
		assert response.getBody() != null;
		assert response.getBody().equals(expectedAccountss);


	}


	@Test                                                        //dm      by  vertical and  classification
	public void testgetProjectsByPMAndClassification() {       //project by  pm       and  classification
		// Mock dependency
		RevenueBudgetSummaryRepository revenueRepo = mock(RevenueBudgetSummaryRepository.class);
		// Instantiate the controller
		RevenueBudgetSummaryController revenueController = new RevenueBudgetSummaryController(revenueRepo);

		// Create sample request body
		RevenueDTO projectList = new RevenueDTO();
		projectList.setPmList(Arrays.asList("ProjectManager1", "ProjectManager2"));
		projectList.setClassificationList(Arrays.asList("Class1", "Class2"));

		// Create sample revenues
		List<RevenueBudgetSummary> revenues = Arrays.asList(
				new RevenueBudgetSummary(1, "Vertical1", "Class1", "DM1","DD1", "Account1", "ProjectManager1", "Project1", 2024, LocalDate.now(), "Q1", 1000, 800, 200),
				new RevenueBudgetSummary(2, "Vertical2", "Class2", "DM2","DD2", "Account2", "ProjectManager2", "Project2", 2024, LocalDate.now(), "Q2", 1500, 1200, 300)
		);

		// Simulate repository behavior
		when(revenueRepo.findByProjectManagerInAndClassificationIn(anyList(), anyList())).thenReturn(revenues);

		// Call controller method
		ResponseEntity<Set<String>> response = revenueController.getProjectsByPMAndClassification(projectList);

		// Verify behavior
		verify(revenueRepo).findByProjectManagerInAndClassificationIn(Arrays.asList("ProjectManager1", "ProjectManager2"), Arrays.asList("Class1", "Class2"));

		// Verify response
		Set<String> expectedprojects = new HashSet<>(Arrays.asList("Project1", "Project2"));
		assert response.getBody() != null;
		assert response.getBody().equals(expectedprojects);
	}

	@Test
	public void testGetpmByaccountAndClassification() {
		// Mock dependency
		RevenueBudgetSummaryRepository revenueRepo = mock(RevenueBudgetSummaryRepository.class);
		// Instantiate the controller
		RevenueBudgetSummaryController revenueController = new RevenueBudgetSummaryController(revenueRepo);

		// Create sample request body
		RevenueDTO pmList = new RevenueDTO();
		pmList.setAccountList(Arrays.asList("Account1", "Account2"));
		pmList.setClassificationList(Arrays.asList("Class1", "Class2"));

		// Create sample revenues
		List<RevenueBudgetSummary> revenues = Arrays.asList(
				new RevenueBudgetSummary(1, "Vertical1", "Class1","DD1", "DM1", "Account1", "ProjectManager1", "Project1", 2024, LocalDate.now(), "Q1", 1000, 800, 200),
				new RevenueBudgetSummary(2, "Vertical2", "Class2","DD2","DM2", "Account2", "ProjectManager2", "Project2", 2024, LocalDate.now(), "Q2", 1500, 1200, 300)
		);

		// Simulate repository behavior
		when(revenueRepo.findByAccountInAndClassificationIn(anyList(), anyList())).thenReturn(revenues);

		// Call controller method
		ResponseEntity<Set<String>> response = revenueController.getPMByAccountAndClassification(pmList);

		// Verify behavior
		verify(revenueRepo).findByAccountInAndClassificationIn(Arrays.asList("Account1", "Account2"), Arrays.asList("Class1", "Class2"));

		// Verify response
		Set<String> expectedpms = new HashSet<>(Arrays.asList("ProjectManager1", "ProjectManager2"));
		assert response.getBody() != null;
		assert response.getBody().equals(expectedpms);
	}

	@Test
	public void testGetClassificationByVertical() {
		// Mock request data
		RevenueDTO verticalList = new RevenueDTO();
		verticalList.setMyList(Arrays.asList("Vertical1", "Vertical2"));

		// Mock data for revenue budget summaries
		List<RevenueBudgetSummary> mockRevenues1 = Arrays.asList(
				new RevenueBudgetSummary(1, "Vertical1", "Classification1","DD1", "DM1", "Account1", "PM1", "Project1", 2024, LocalDate.of(2024, 4, 1), "Q1", 1000, 900, 100),
				new RevenueBudgetSummary(2, "Vertical1", "Classification2","DD2","DM2", "Account2", "PM2", "Project2", 2024, LocalDate.of(2024, 4, 1), "Q1", 1500, 1300, 200)
		);
		List<RevenueBudgetSummary> mockRevenues2 = Arrays.asList(
				new RevenueBudgetSummary(3, "Vertical2", "Classification1","DD3", "DM3", "Account3", "PM3", "Project3", 2024, LocalDate.of(2024, 4, 1), "Q1", 1200, 1100, 100)
		);

		// Mock repository behavior
		when(revenuerepo.findByVerticalIn(Arrays.asList("Vertical1", "Vertical2"))).thenReturn(mockRevenues1);
		when(revenuerepo.findByVerticalIn(Arrays.asList("Vertical2"))).thenReturn(mockRevenues2);

		// Call the method
		ResponseEntity<Set<String>> responseEntity = Controller.getClassificationByVertical(verticalList);
		Set<String> classificationNames = responseEntity.getBody();

		// Verify the result
		Set<String> expectedClassificationNames = new HashSet<>(Arrays.asList("Classification1", "Classification2"));
		assertEquals(expectedClassificationNames, classificationNames);

		// Verify interactions
		verify(revenuerepo, times(1)).findByVerticalIn(verticalsCaptor.capture());
		List<String> capturedVerticals = verticalsCaptor.getValue();
		assertEquals(Arrays.asList("Vertical1", "Vertical2"), capturedVerticals);
	}

	@Test
	public void testCreateRevenueBudgetSummary() {
		// Mocking
		RevenueBudgetSummary revenue = new RevenueBudgetSummary(
				1, "Vertical", "Classification","Delivery Director", "Delivery Manager", "Account",
				"Project Manager", "Project Name", 2024, LocalDate.now(), "Quarter",
				10000, 9000, 1000
		);
		RevenueBudgetSummary createdRevenue = new RevenueBudgetSummary();
		createdRevenue.setId(1); // Assuming setId method is available
		RevenueBudgetSummaryRepository revenueRepo = mock(RevenueBudgetSummaryRepository.class);
		when(revenueRepo.save(revenue)).thenReturn(createdRevenue);

		// Call the method to be tested
		RevenueBudgetSummaryController controller = new RevenueBudgetSummaryController(revenueRepo);
		ResponseEntity<RevenueBudgetSummary> responseEntity = controller.createRevenueBudgetSummary(revenue);

		// Assertions
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertEquals(createdRevenue, responseEntity.getBody());
		verify(revenueRepo, times(1)).save(revenue); // Ensure that the save method was called exactly once with the correct parameter
	}


	@Test
	public void testGetUniqueVerticals() {
		// Mock data
		RevenueBudgetSummary mockItem1 = Mockito.mock(RevenueBudgetSummary.class);
		Mockito.when(mockItem1.getVertical()).thenReturn("Vertical1");
		RevenueBudgetSummary mockItem2 = Mockito.mock(RevenueBudgetSummary.class);
		Mockito.when(mockItem2.getVertical()).thenReturn("Vertical1");
		RevenueBudgetSummary mockItem3 = Mockito.mock(RevenueBudgetSummary.class);
		Mockito.when(mockItem3.getVertical()).thenReturn("Vertical2");

		// Populate itemList with mock items
		List<RevenueBudgetSummary> vertical = new ArrayList<>();
		vertical.add(mockItem1);
		vertical.add(mockItem2);
		vertical.add(mockItem3);

		RevenueBudgetSummaryController revenueController = new RevenueBudgetSummaryController(revenueRepo);
		revenueController.setverticalList(vertical);

		// Calling the method under test
		Set<String> uniqueNames = revenueController.getUniqueVerticals();

		// Expected set of unique names
		Set<String> expectedUniqueNames = new HashSet<>();
		expectedUniqueNames.add("Vertical1");
		expectedUniqueNames.add("Vertical2");

		// Asserting that the method returns the expected unique names
		assertEquals(expectedUniqueNames, uniqueNames);
	}

	@Test
	public void testDeleteRevenueVsBudgetById_Exists() {
		// Mocking
		int id = 1;
		RevenueBudgetSummaryRepository revenueRepo = mock(RevenueBudgetSummaryRepository.class);
		when(revenueRepo.existsById(id)).thenReturn(true);

		// Call the method to be tested
		RevenueBudgetSummaryController controller = new RevenueBudgetSummaryController(revenueRepo);
		ResponseEntity<?> responseEntity = controller.deleteRevenueVsBudgetById(id);

		// Assertions
		assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
		verify(revenueRepo, times(1)).existsById(id); // Ensure that existsById was called once with the correct parameter
		verify(revenueRepo, times(1)).deleteById(id); // Ensure that deleteById was called once with the correct parameter
	}

	@Test
	public void testGetUniqueaccounts() {
		// Mock data
		RevenueBudgetSummary mockItem1 = Mockito.mock(RevenueBudgetSummary.class);
		Mockito.when(mockItem1.getAccount()).thenReturn("Accounts1");
		RevenueBudgetSummary mockItem2 = Mockito.mock(RevenueBudgetSummary.class);
		Mockito.when(mockItem2.getAccount()).thenReturn("Accounts1");
		RevenueBudgetSummary mockItem3 = Mockito.mock(RevenueBudgetSummary.class);
		Mockito.when(mockItem3.getAccount()).thenReturn("Accounts2");

		// Populate itemList with mock items
		List<RevenueBudgetSummary> account = new ArrayList<>();
		account.add(mockItem1);
		account.add(mockItem2);
		account.add(mockItem3);

		RevenueBudgetSummaryController revenueController = new RevenueBudgetSummaryController(revenueRepo);
		revenueController.setaccountList(account);

		// Calling the method under test
		Set<String> uniqueNames = revenueController.getUniqueAccounts();

		// Expected set of unique names
		Set<String> expectedUniqueNames = new HashSet<>();
		expectedUniqueNames.add("Accounts1");
		expectedUniqueNames.add("Accounts2");

		// Asserting that the method returns the expected unique names
		assertEquals(expectedUniqueNames, uniqueNames);
	}

	@Test
	public void testGetUniquepms() {
		// Mock data
		RevenueBudgetSummary mockItem1 = Mockito.mock(RevenueBudgetSummary.class);
		Mockito.when(mockItem1.getProjectManager()).thenReturn("pm1");
		RevenueBudgetSummary mockItem2 = Mockito.mock(RevenueBudgetSummary.class);
		Mockito.when(mockItem2.getProjectManager()).thenReturn("pm1");
		RevenueBudgetSummary mockItem3 = Mockito.mock(RevenueBudgetSummary.class);
		Mockito.when(mockItem3.getProjectManager()).thenReturn("pm2");

		// Populate itemList with mock items
		List<RevenueBudgetSummary> pm = new ArrayList<>();
		pm.add(mockItem1);
		pm.add(mockItem2);
		pm.add(mockItem3);

		RevenueBudgetSummaryController revenueController = new RevenueBudgetSummaryController(revenueRepo);
		revenueController.setpmlist(pm);

		// Calling the method under test
		Set<String> uniqueNames = revenueController.getUniquePms();

		// Expected set of unique names
		Set<String> expectedUniqueNames = new HashSet<>();
		expectedUniqueNames.add("pm1");
		expectedUniqueNames.add("pm2");

		// Asserting that the method returns the expected unique names
		assertEquals(expectedUniqueNames, uniqueNames);
	}

	@Test
	public void testGetUniqueclassification() {
		// Mock data
		RevenueBudgetSummary mockItem1 = Mockito.mock(RevenueBudgetSummary.class);
		Mockito.when(mockItem1.getClassification()).thenReturn("Classification1");
		RevenueBudgetSummary mockItem2 = Mockito.mock(RevenueBudgetSummary.class);
		Mockito.when(mockItem2.getClassification()).thenReturn("Classification1");
		RevenueBudgetSummary mockItem3 = Mockito.mock(RevenueBudgetSummary.class);
		Mockito.when(mockItem3.getClassification()).thenReturn("Classification2");

		// Populate itemList with mock items
		List<RevenueBudgetSummary> classification = new ArrayList<>();
		classification.add(mockItem1);
		classification.add(mockItem2);
		classification.add(mockItem3);

		RevenueBudgetSummaryController revenueController = new RevenueBudgetSummaryController(revenueRepo);
		revenueController.setclassificationlist(classification);

		// Calling the method under test
		Set<String> uniqueNames = revenueController.getUniqueclassification();

		// Expected set of unique names
		Set<String> expectedUniqueNames = new HashSet<>();
		expectedUniqueNames.add("Classification1");
		expectedUniqueNames.add("Classification2");

		// Asserting that the method returns the expected unique names
		assertEquals(expectedUniqueNames, uniqueNames);
	}

	@Test
	public void testGetUniqueproject() {
		// Mock data
		RevenueBudgetSummary mockItem1 = Mockito.mock(RevenueBudgetSummary.class);
		Mockito.when(mockItem1.getProjectName()).thenReturn("Project1");
		RevenueBudgetSummary mockItem2 = Mockito.mock(RevenueBudgetSummary.class);
		Mockito.when(mockItem2.getProjectName()).thenReturn("Project1");
		RevenueBudgetSummary mockItem3 = Mockito.mock(RevenueBudgetSummary.class);
		Mockito.when(mockItem3.getProjectName()).thenReturn("Project2");

		// Populate itemList with mock items
		List<RevenueBudgetSummary> projectname = new ArrayList<>();
		projectname.add(mockItem1);
		projectname.add(mockItem2);
		projectname.add(mockItem3);

		RevenueBudgetSummaryController revenueController = new RevenueBudgetSummaryController(revenueRepo);
		revenueController.setprojectlist(projectname);

		// Calling the method under test
		Set<String> uniqueNames = revenueController.getUniqueproject();

		// Expected set of unique names
		Set<String> expectedUniqueNames = new HashSet<>();
		expectedUniqueNames.add("Project1");
		expectedUniqueNames.add("Project2");

		// Asserting that the method returns the expected unique names
		assertEquals(expectedUniqueNames, uniqueNames);
	}

	@Test
	public void testGetUniqueFY() {
		// Mock data
		RevenueBudgetSummary mockItem1 = Mockito.mock(RevenueBudgetSummary.class);
		Mockito.when(mockItem1.getFinancialYear()).thenReturn(2023);
		RevenueBudgetSummary mockItem2 = Mockito.mock(RevenueBudgetSummary.class);
		Mockito.when(mockItem2.getFinancialYear()).thenReturn(2023);
		RevenueBudgetSummary mockItem3 = Mockito.mock(RevenueBudgetSummary.class);
		Mockito.when(mockItem3.getFinancialYear()).thenReturn(2024);

		// Populate itemList with mock items
		List<RevenueBudgetSummary> FY = new ArrayList<>();
		FY.add(mockItem1);
		FY.add(mockItem2);
		FY.add(mockItem3);

		RevenueBudgetSummaryController revenueController = new RevenueBudgetSummaryController(revenueRepo);
		revenueController.setFYlist(FY);

		// Calling the method under test
		Set<Integer> uniqueNames = revenueController.getUniqueFY();

		// Expected set of unique names
		Set<Integer> expectedUniqueNames = new HashSet<>();
		expectedUniqueNames.add(2023);
		expectedUniqueNames.add(2024);

		// Asserting that the method returns the expected unique names
		assertEquals(expectedUniqueNames, uniqueNames);
	}

	@Test
	public void testGetUniquequarter() {
		// Mock data
		RevenueBudgetSummary mockItem1 = Mockito.mock(RevenueBudgetSummary.class);
		Mockito.when(mockItem1.getQuarter()).thenReturn("Quarter1");
		RevenueBudgetSummary mockItem2 = Mockito.mock(RevenueBudgetSummary.class);
		Mockito.when(mockItem2.getQuarter()).thenReturn("Quarter1");
		RevenueBudgetSummary mockItem3 = Mockito.mock(RevenueBudgetSummary.class);
		Mockito.when(mockItem3.getQuarter()).thenReturn("Quarter2");

		// Populate itemList with mock items
		List<RevenueBudgetSummary> quarter = new ArrayList<>();
		quarter.add(mockItem1);
		quarter.add(mockItem2);
		quarter.add(mockItem3);

		RevenueBudgetSummaryController revenueController = new RevenueBudgetSummaryController(revenueRepo);
		revenueController.setquarterlist(quarter);

		// Calling the method under test
		Set<String> uniqueNames = revenueController.getUniqueQuarter();

		// Expected set of unique names
		Set<String> expectedUniqueNames = new HashSet<>();
		expectedUniqueNames.add("Quarter1");
		expectedUniqueNames.add("Quarter2");

		// Asserting that the method returns the expected unique names
		assertEquals(expectedUniqueNames, uniqueNames);
	}

	@Test
	public void testGetUniqueDMs() {
		// Mock data
		RevenueBudgetSummary mockItem1 = Mockito.mock(RevenueBudgetSummary.class);
		Mockito.when(mockItem1.getDeliveryManager()).thenReturn("DM1");
		RevenueBudgetSummary mockItem2 = Mockito.mock(RevenueBudgetSummary.class);
		Mockito.when(mockItem2.getDeliveryManager()).thenReturn("DM1");
		RevenueBudgetSummary mockItem3 = Mockito.mock(RevenueBudgetSummary.class);
		Mockito.when(mockItem3.getDeliveryManager()).thenReturn("DM2");

		// Populate itemList with mock items
		List<RevenueBudgetSummary> dm = new ArrayList<>();
		dm.add(mockItem1);
		dm.add(mockItem2);
		dm.add(mockItem3);

		RevenueBudgetSummaryController revenueController = new RevenueBudgetSummaryController(revenueRepo);
		revenueController.setdmlist(dm);

		// Calling the method under test
		Set<String> uniqueNames = revenueController.getUniquedm();

		// Expected set of unique names
		Set<String> expectedUniqueNames = new HashSet<>();
		expectedUniqueNames.add("DM1");
		expectedUniqueNames.add("DM2");

		// Asserting that the method returns the expected unique names
		assertEquals(expectedUniqueNames, uniqueNames);
	}

	@Test
	public void testgetDMByVertical() {
// Mock request data
		RevenueDTO verticalList = new RevenueDTO();
		verticalList.setMyList(Arrays.asList("Vertical1", "Vertical2"));
// Mock data for revenue budget summaries
		List<RevenueBudgetSummary> mockRevenues1 = Arrays.asList(
				new RevenueBudgetSummary(1, "Vertical1", "Classification1", "DD1","DM1", "Account1", "PM1", "Project1", 2024, LocalDate.of(2024, 4, 1), "Q1", 1000, 900, 100),
				new RevenueBudgetSummary(2, "Vertical1", "Classification2", "DD2","DM2", "Account2", "PM2", "Project2", 2024, LocalDate.of(2024, 4, 1), "Q1", 1500, 1300, 200)
		);
		List<RevenueBudgetSummary> mockRevenues2 = Arrays.asList(
				new RevenueBudgetSummary(3, "Vertical2", "Classification1", "DD1","DM1", "Account3", "PM3", "Project3", 2024, LocalDate.of(2024, 4, 1), "Q1", 1200, 1100, 100)
		);
// Mock repository behavior
		when(revenuerepo.findByVerticalIn(Arrays.asList("Vertical1", "Vertical2"))).thenReturn(mockRevenues1);
		when(revenuerepo.findByVerticalIn(Arrays.asList("Vertical2"))).thenReturn(mockRevenues2);
// Call the method
		ResponseEntity<Set<String>> responseEntity = Controller.getDMByVertical(verticalList);
		Set<String> deliveryManagerNames = responseEntity.getBody();
// Verify the result
		Set<String> expecteddeliveryManagerNames = new HashSet<>(Arrays.asList("DM1", "DM2"));
		assertEquals(expecteddeliveryManagerNames, deliveryManagerNames);
// Verify interactions
		verify(revenuerepo, times(1)).findByVerticalIn(verticalsCaptor.capture());
		List<String> capturedVerticals = verticalsCaptor.getValue();
		assertEquals(Arrays.asList("Vertical1", "Vertical2"), capturedVerticals);
	}

	@Test
	public void testGetAccountByDM() {
		// Mock request data
		RevenueDTO dmList = new RevenueDTO();
		dmList.setMyList(Arrays.asList("DM1", "DM2"));

		// Mock data for revenue budget summaries
		List<RevenueBudgetSummary> mockRevenues1 = Arrays.asList(
				new RevenueBudgetSummary(1, "Vertical1", "Classification1","DD1", "DM1", "Account1", "PM1", "Project1", 2024, LocalDate.of(2024, 4, 1), "Q1", 1000, 900, 100),
				new RevenueBudgetSummary(2, "Vertical1", "Classification2","DD1", "DM1", "Account2", "PM2", "Project2", 2024, LocalDate.of(2024, 4, 1), "Q1", 1500, 1300, 200)
		);
		List<RevenueBudgetSummary> mockRevenues2 = Arrays.asList(
				new RevenueBudgetSummary(3, "Vertical2", "Classification1", "DD2","DM2", "Account1", "PM3", "Project3", 2024, LocalDate.of(2024, 4, 1), "Q1", 1200, 1100, 100)
		);

		// Mock repository behavior
		when(revenuerepo.findByDeliveryManagerIn(Arrays.asList("DM1", "DM2"))).thenReturn(mockRevenues1);
		when(revenuerepo.findByDeliveryManagerIn(Arrays.asList("DM2"))).thenReturn(mockRevenues2);

		// Call the method
		ResponseEntity<Set<String>> responseEntity = Controller.getAccountByDM(dmList);
		Set<String> accountNames = responseEntity.getBody();

		// Verify the result
		Set<String> expectedAccountNames = new HashSet<>(Arrays.asList("Account1", "Account2"));
		assertEquals(expectedAccountNames, accountNames);

		// Verify interactions
		verify(revenuerepo, times(1)).findByDeliveryManagerIn(verticalsCaptor.capture());
		List<String> capturedVerticals = verticalsCaptor.getValue();
		assertEquals(Arrays.asList("DM1", "DM2"), capturedVerticals);
	}

	@Test
	public void testgetprojectBypM() {
		// Mock request data
		RevenueDTO pmList = new RevenueDTO();
		pmList.setMyList(Arrays.asList("PM1", "PM2"));

		// Mock data for revenue budget summaries
		List<RevenueBudgetSummary> mockRevenues1 = Arrays.asList(
				new RevenueBudgetSummary(1, "Vertical1", "Classification1","DD1", "DM1", "Account1", "PM1", "Project1", 2024, LocalDate.of(2024, 4, 1), "Q1", 1000, 900, 100),
				new RevenueBudgetSummary(2, "Vertical1", "Classification2","DD1","DM1", "Account2", "PM1", "Project2", 2024, LocalDate.of(2024, 4, 1), "Q1", 1500, 1300, 200)
		);
		List<RevenueBudgetSummary> mockRevenues2 = Arrays.asList(
				new RevenueBudgetSummary(3, "Vertical2", "Classification1","DD2", "DM2", "Account1", "PM2", "Project1", 2024, LocalDate.of(2024, 4, 1), "Q1", 1200, 1100, 100)
		);

		// Mock repository behavior
		when(revenuerepo.findByProjectManagerIn(Arrays.asList("PM1", "PM2"))).thenReturn(mockRevenues1);
		when(revenuerepo.findByProjectManagerIn(Arrays.asList("PM2"))).thenReturn(mockRevenues2);

		// Call the method
		ResponseEntity<Set<String>> responseEntity = Controller.getprojectBypM(pmList);
		Set<String> projectNames = responseEntity.getBody();

		// Verify the result
		Set<String> expectedProjectNames = new HashSet<>(Arrays.asList("Project1", "Project2"));
		assertEquals(expectedProjectNames, projectNames);

		// Verify interactions
		verify(revenuerepo, times(1)).findByProjectManagerIn(verticalsCaptor.capture());
		List<String> capturedVerticals = verticalsCaptor.getValue();
		assertEquals(Arrays.asList("PM1", "PM2"), capturedVerticals);
	}

	@Test
	public void testgetpmByacnt() {
		// Mock request data
		RevenueDTO accountList = new RevenueDTO();
		accountList.setMyList(Arrays.asList("Account1", "Account2"));

		// Mock data for revenue budget summaries
		List<RevenueBudgetSummary> mockRevenues1 = Arrays.asList(
				new RevenueBudgetSummary(1, "Vertical1", "Classification1","DD1", "DM1", "Account1", "PM1", "Project1", 2024, LocalDate.of(2024, 4, 1), "Q1", 1000, 900, 100),
				new RevenueBudgetSummary(2, "Vertical1", "Classification2", "DD2","DM2", "Account1", "PM2", "Project2", 2024, LocalDate.of(2024, 4, 1), "Q1", 1500, 1300, 200)
		);
		List<RevenueBudgetSummary> mockRevenues2 = Arrays.asList(
				new RevenueBudgetSummary(3, "Vertical2", "Classification1", "DD3","DM3", "Account2", "PM1", "Project3", 2024, LocalDate.of(2024, 4, 1), "Q1", 1200, 1100, 100)
		);

		// Mock repository behavior
		when(revenuerepo.findByAccountIn(Arrays.asList("Account1", "Account2"))).thenReturn(mockRevenues1);
		when(revenuerepo.findByAccountIn(Arrays.asList("Account2"))).thenReturn(mockRevenues2);

		// Call the method
		ResponseEntity<Set<String>> responseEntity = Controller.getpmByacnt(accountList);
		Set<String> projectManagerNames = responseEntity.getBody();

		// Verify the result
		Set<String> expectedProjectManagerNames = new HashSet<>(Arrays.asList("PM2", "PM1"));
		assertEquals(expectedProjectManagerNames, projectManagerNames);

		// Verify interactions
		verify(revenuerepo, times(1)).findByAccountIn(verticalsCaptor.capture());
		List<String> capturedVerticals = verticalsCaptor.getValue();
		assertEquals(Arrays.asList("Account1", "Account2"), capturedVerticals);
	}

	@Test
	public void testgetDMByClassification() {
		// Mock request data
		RevenueDTO classificationList = new RevenueDTO();
		classificationList.setMyList(Arrays.asList("Classification1", "Classification2"));

		// Mock data for revenue budget summaries
		List<RevenueBudgetSummary> mockRevenues1 = Arrays.asList(
				new RevenueBudgetSummary(1, "Vertical1", "Classification1","DD1", "DM1", "Account1", "PM1", "Project1", 2024, LocalDate.of(2024, 4, 1), "Q1", 1000, 900, 100),
				new RevenueBudgetSummary(2, "Vertical1", "Classification1","DD2", "DM2", "Account1", "PM2", "Project2", 2024, LocalDate.of(2024, 4, 1), "Q1", 1500, 1300, 200)
		);
		List<RevenueBudgetSummary> mockRevenues2 = Arrays.asList(
				new RevenueBudgetSummary(3, "Vertical2", "Classification2", "DD1","DM1", "Account2", "PM1", "Project3", 2024, LocalDate.of(2024, 4, 1), "Q1", 1200, 1100, 100)
		);

		// Mock repository behavior
		when(revenuerepo.findByClassificationIn(Arrays.asList("Classification1", "Classification2"))).thenReturn(mockRevenues1);
		when(revenuerepo.findByClassificationIn(Arrays.asList("Classification2"))).thenReturn(mockRevenues2);

		// Call the method
		ResponseEntity<Set<String>> responseEntity = Controller.getDMByClassification(classificationList);
		Set<String> deliveryManagerNames = responseEntity.getBody();

		// Verify the result
		Set<String> expectedDeliveryManagerNames = new HashSet<>(Arrays.asList("DM2", "DM1"));
		assertEquals(expectedDeliveryManagerNames, deliveryManagerNames);

		// Verify interactions
		verify(revenuerepo, times(1)).findByClassificationIn(verticalsCaptor.capture());
		List<String> capturedVerticals = verticalsCaptor.getValue();
		assertEquals(Arrays.asList("Classification1", "Classification2"), capturedVerticals);
	}

	@Test
	public void testgetFinancialYearByProject() {
		// Mock request data
		RevenueDTO projectList = new RevenueDTO();
		projectList.setMyList(Arrays.asList("Project1", "Project2"));

		// Mock data for revenue budget summaries
		List<RevenueBudgetSummary> mockRevenues1 = Arrays.asList(
				new RevenueBudgetSummary(1, "Vertical1", "Classification1","DD1", "DM1", "Account1", "PM1", "Project1", 2021, LocalDate.of(2021, 4, 1), "Q1", 1000, 900, 100),
				new RevenueBudgetSummary(2, "Vertical1", "Classification1", "DD2","DM2", "Account1", "PM2", "Project1", 2022, LocalDate.of(2022, 4, 1), "Q1", 1500, 1300, 200)
		);
		List<RevenueBudgetSummary> mockRevenues2 = Arrays.asList(
				new RevenueBudgetSummary(3, "Vertical2", "Classification2", "DD2","DM2", "Account2", "PM1", "Project2", 2021, LocalDate.of(2021, 4, 1), "Q1", 1200, 1100, 100)
		);

		// Mock repository behavior
		when(revenuerepo.findByProjectNameIn(Arrays.asList("Project1", "Project2"))).thenReturn(mockRevenues1);
		when(revenuerepo.findByProjectNameIn(Arrays.asList("Project2"))).thenReturn(mockRevenues2);

		// Call the method
		ResponseEntity<Set<Integer>> responseEntity = Controller.getFinancialYearByProject(projectList);
		Set<Integer> financialYearNames = responseEntity.getBody();

		// Verify the result
		Set<Integer> expectedfinancialYearNames = new HashSet<>(Arrays.asList(2022, 2021));
		assertEquals(expectedfinancialYearNames, financialYearNames);

		// Verify interactions
		verify(revenuerepo, times(1)).findByProjectNameIn(verticalsCaptor.capture());
		List<String> capturedVerticals = verticalsCaptor.getValue();
		assertEquals(Arrays.asList("Project1", "Project2"), capturedVerticals);

//		verify(revenuerepo, times(1)).findByProjectNameIn(Arrays.asList("Project1", "Project2"));
//		verify(revenuerepo, times(1)).findByProjectNameIn(Arrays.asList("Project2"));
//	}
	}
}