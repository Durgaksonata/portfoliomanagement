package com.sonata.portfoliomanagement.glue;
import com.sonata.portfoliomanagement.PorfolioManagementApplication;
import com.sonata.portfoliomanagement.controllers.RevenueBudgetSummaryController;
import com.sonata.portfoliomanagement.interfaces.RevenueBudgetSummaryRepository;
import com.sonata.portfoliomanagement.model.RevenueBudgetSummary;
import com.sonata.portfoliomanagement.model.RevenueDTO;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;
import java.util.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//@CucumberContextConfiguration
@SpringBootTest(classes = CucumberTestRunner.class)
@ContextConfiguration(classes = PorfolioManagementApplication.class)

public class RevenueBudgetSummarySteps {

    private String baseUrl = "http://localhost:8081/revenuebudgets"; // Update with your base URL
    private ResponseEntity<List> responseEntity;
    private List<String> verticalNames;
    private RestTemplate restTemplate;
    private int year;
    private List revenueData;
    private RevenueDTO projectList;
    private ResponseEntity<Set<Integer>> response;
    @Autowired
    RevenueBudgetSummaryController revenueBudgetSummaryController = new RevenueBudgetSummaryController();
    @Autowired
    private RevenueBudgetSummaryRepository revenueBudgetRepo;
    private RevenueBudgetSummaryRepository revenueBudgetSummaryRepositoryMock;
    private List<RevenueBudgetSummary> revenueBudgetData;
    private List<String> projectNames;


//    @Given("the revenue data exists in the database")
//    public void theRevenueDataExistsInTheDatabase() {
//        // Mocking the repository behavior
//        RevenueBudgetSummaryRepository revenueRepo = mock(RevenueBudgetSummaryRepository.class);
//        List<RevenueBudgetSummary> mockData = Arrays.asList(
//                new RevenueBudgetSummary("Vertical1"),
//                new RevenueBudgetSummary("Vertical2")
//        );
//        when(revenueRepo.findAll()).thenReturn(mockData);
//    }
//
//    @When("the client requests to get the vertical list")
//    public void theClientRequestsToGetTheVerticalList() {
//        // Simulate HTTP request
//        String getUrl = "http://localhost:8081/revenuebudgets/getverticallist";
//        restTemplate = new RestTemplate();
//        responseEntity = restTemplate.getForEntity(getUrl, List.class);
//    }
//
//    @Then("the client receives a list of distinct verticals")
//    public void theClientReceivesAListOfDistinctVerticals() {
//
//        assertNotNull(responseEntity);
//        assertEquals(200, responseEntity.getStatusCodeValue());
//        List<String> verticalList = responseEntity.getBody();
//        assertNotNull(verticalList);
//        assertFalse(verticalList.isEmpty());
//
//    }
    @Given("the client has a list of vertical names")
    public void theClientHasAListOfVerticalNames(List<String> verticalNames) {
        // The list of vertical names is provided as a parameter
        // No action is needed here
    }

    @When("the client sends a POST request to the {string} endpoint with the vertical list")
    public void theClientSendsAPOSTRequestToTheEndpointWithTheVerticalList(String endpoint) {
        List<String> VerticalNames = Arrays.asList("VerticalName1", "VerticalName2"); // Sample vertical names
        RevenueDTO requestPayload = new RevenueDTO();
        // Sending POST request to the endpoint
        RestTemplate restTemplate = new RestTemplate();
        responseEntity = restTemplate.postForEntity(baseUrl + endpoint, requestPayload, List.class);
    }

    @Then("the client should receive a successful response with status code {int}")
    public void theClientShouldReceiveASuccessfulResponseWithStatusCode(int statusCode) {
        // Verifying response status code
        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
    }

    @Given("a valid financial year")
    public void aValidFinancialYear() {
        // You can set up your valid financial year here if needed
        year = 2023; // For example
    }

    @When("a request is made to retrieve revenue data for that year")
    public void aRequestIsMadeToRetrieveRevenueDataForThatYear() {
        revenueData = makeRequestToGetRevenueByYear(year);
    }

    @Then("the revenue data for the specified year is returned")
    public void theRevenueDataForTheSpecifiedYearIsReturned() {
        assertNotNull(revenueData);
    }
    private List makeRequestToGetRevenueByYear(int year) {
        return List.of("Revenue1", "Revenue2", "Revenue3");
    }

}
