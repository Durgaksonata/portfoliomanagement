package com.sonata.portfoliomanagement.glue;

import com.sonata.portfoliomanagement.PorfolioManagementApplication;
import com.sonata.portfoliomanagement.controllers.RevenueGrowthSummaryController;
import com.sonata.portfoliomanagement.interfaces.RevenueGrowthSummaryRepository;
import com.sonata.portfoliomanagement.model.RevenueDTO;
import com.sonata.portfoliomanagement.model.RevenueGrowthSummary;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.mockito.Mockito.when;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import java.util.*;
import java.util.function.Function;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@CucumberContextConfiguration
@SpringBootTest(classes = CucumberTestRunner.class)
@ContextConfiguration(classes = PorfolioManagementApplication.class)
public class RevenueGrowthSummarySteps {

    @Autowired
    RevenueGrowthSummaryController revenueGrowthController = new RevenueGrowthSummaryController();
    private RevenueDTO requestDTO;
    @Autowired
    private RevenueGrowthSummaryRepository revenuegrowthRepo ;
    private ResponseEntity<Set<Map<String, Object>>> responseEntity;

    private RevenueGrowthSummaryRepository revenueGrowthSummaryRepositoryMock;

    private List<RevenueGrowthSummary> revenueGrowthData;

    public RevenueGrowthSummarySteps() {
    }


    //Get revenue growth data by vertical and classifications

//    @Given("a request is made with multiple verticals and classifications")
//    public void aRequestIsMadeWithMultipleVerticalsAndClassifications() {
//        // Prepare mock request data
//        requestDTO = new RevenueDTO();
//        List<String> verticalList = new ArrayList<>(Arrays.asList("Vertical1", "Vertical2"));
//        List<String> classificationList = new ArrayList<>(Arrays.asList("Class1", "Class2"));
//        requestDTO.setVerticalList(verticalList);
//        requestDTO.setClassificationList(classificationList);
//
//        // Mock repository response
//        List<RevenueGrowthSummary> dataList = new ArrayList<>();
//        // Add mock data items as per your test scenario
//        RevenueGrowthSummary mockDataItem1 = new RevenueGrowthSummary(/* Add necessary constructor arguments */);
//        // Add necessary setters to set mock data
//        dataList.add(mockDataItem1);
//        // You can add more mock data items as needed
//        // Mock repository method to return mock data
//        when(revenuegrowthRepo.findByVerticalInAndClassificationIn(verticalList, classificationList)).thenReturn(dataList);
//    }
//
//    @When("the request is processed")
//    public void theRequestIsProcessed() {
//        // Initialize controller with mocked repository
//        RevenueGrowthSummaryController controller = new RevenueGrowthSummaryController(revenuegrowthRepo);
//        responseEntity = controller.getDataByVerticalAndClassification(requestDTO);
//    }
//
//    @Then("the response contains data with all the provided verticals and classifications")
//    public void theResponseContainsDataWithAllTheProvidedVerticalsAndClassifications() {
//        assertNotNull(responseEntity);
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        Set<Map<String, Object>> responseData = responseEntity.getBody();
//        assertNotNull(responseData);
//
//    }

    @Given("a request is made with multiple verticals and classifications")
    public void aRequestIsMadeWithMultipleVerticalsAndClassifications() {
        // Prepare request data
        requestDTO = new RevenueDTO();
        List<String> verticalList = new ArrayList<>(Arrays.asList("Vertical1", "Vertical2"));
        List<String> classificationList = new ArrayList<>(Arrays.asList("Class1", "Class2"));
        requestDTO.setVerticalList(verticalList);
        requestDTO.setClassificationList(classificationList);
    }
    @When("the request is processed")
    public void theRequestIsProcessed() {
        // Initialize controller with mocked repository
        RevenueGrowthSummaryController controller = new RevenueGrowthSummaryController(revenuegrowthRepo);
        // Mock the behavior of the repository method that the controller calls
        List<RevenueGrowthSummary> mockData = new ArrayList<>();
        // Add mock data as per your requirements
        RevenueGrowthSummary mockEntry1 = new RevenueGrowthSummary();
        mockEntry1.setId(1);
        mockEntry1.setVertical("Vertical1");
        mockEntry1.setClassification("Class1");
        mockData.add(mockEntry1);

        // Assuming your controller method calls findByVerticalInAndClassificationIn method with the same arguments
        when(revenuegrowthRepo.findByVerticalInAndClassificationIn(anyList(), anyList()))
                .thenReturn(mockData);

        responseEntity = controller.getDataByVerticalAndClassification(requestDTO);
    }
    @Then("the response contains data with all the provided verticals and classifications")
    public void theResponseContainsDataWithAllTheProvidedVerticalsAndClassifications() {
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Set<Map<String, Object>> responseData = responseEntity.getBody();
        assertNotNull(responseData);
        // Add more assertions based on the expected structure/content of responseData
    }




    //Get revenue growth data by specific financial year
    @Given("a valid financial year is provided")
    public void aValidFinancialYearIsProvided() {
        revenueGrowthSummaryRepositoryMock = mock(RevenueGrowthSummaryRepository.class);
        revenueGrowthController = new RevenueGrowthSummaryController(revenueGrowthSummaryRepositoryMock);
    }

    @When("the user requests revenue growth data for that year")
    public void theUserRequestsRevenueGrowthDataForThatYear() {
        int validYear = 2023; // Assuming 2023 is a valid financial year
        revenueGrowthData = Arrays.asList(new RevenueGrowthSummary(), new RevenueGrowthSummary()); // Mocked data
        when(revenueGrowthSummaryRepositoryMock.findByFinancialYear(validYear)).thenReturn(revenueGrowthData);
    }

    @Then("the system should return the revenue growth data for that year")
    public void theSystemShouldReturnTheRevenueGrowthDataForThatYear() {
        List<RevenueGrowthSummary> actualData = revenueGrowthController.getByFinancialYear(2023);
        assertEquals(revenueGrowthData, actualData);
    }


    static class revenueGrowthSummaryRepositoryMock implements RevenueGrowthSummaryRepository {
        @Override
        public RevenueGrowthSummary findByVerticalAndClassificationAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarter(String vertical, String classification, String deliveryManager, String account, String projectManager, String projectName, int financialYear, String quarter) {
            return null;
        }

        @Override
        public List<RevenueGrowthSummary> findByVerticalIn(List<String> verticalNames) {
            return null;
        }

        @Override
        public List<RevenueGrowthSummary> findByVerticalInAndClassificationIn(List<String> verticals, List<String> classifications) {

            List<RevenueGrowthSummary> revenues = new ArrayList<>();
            for (String vertical : verticals) {
                for (String classification : classifications) {
                    RevenueGrowthSummary revenue = new RevenueGrowthSummary();
                    revenue.setVertical(vertical);
                    revenue.setClassification(classification);
                    revenues.add(revenue);
                }
            }
            return revenues;
        }

        @Override
        public void deleteByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarter(String vertical, String classification, String deliveryDirector, String deliveryManager, String account, String projectManager, String projectName, int financialYear, String quarter) {

        }


        @Override
        public List<RevenueGrowthSummary> findByDeliveryManagerInAndClassificationIn(List<String> dmNames, List<String> classifications) {
            return null;
        }

        @Override
        public List<RevenueGrowthSummary> findByAccountInAndClassificationIn(List<String> accountNames, List<String> classifications) {
            return null;
        }

        @Override
        public List<RevenueGrowthSummary> findByProjectManagerInAndClassificationIn(List<String> pmNames, List<String> classifications) {
            return null;
        }

        @Override
        public List<RevenueGrowthSummary> findByProjectNameIn(List<String> projectNames) {
            return null;
        }

        @Override
        public List<RevenueGrowthSummary> findByFinancialYearInAndProjectNameInAndVerticalInAndClassificationInAndDeliveryManagerInAndAccountInAndProjectManagerInAndQuarterIn(List<Integer> financialYear, List<String> projectList, List<String> verticalList, List<String> classificationList, List<String> dmList, List<String> accountList, List<String> pmList, List<String> quarterList) {
            return null;
        }

        @Override
        public List<RevenueGrowthSummary> findByFinancialYear(int financialYear) {
            return null;
        }

        @Override
        public List<RevenueGrowthSummary> findAllByVertical(String vertical) {
            return null;
        }

        @Override
        public List<RevenueGrowthSummary> findAllByClassification(String classification) {
            return null;
        }

        @Override
        public List<RevenueGrowthSummary> findBydeliveryManager(String deliveryManager) {
            return null;
        }

        @Override
        public List<RevenueGrowthSummary> findByAccount(String account) {
            return null;
        }

        @Override
        public List<RevenueGrowthSummary> findByProjectName(String project) {
            return null;
        }

        @Override
        public List<RevenueGrowthSummary> findByProjectManager(String projectManager) {
            return null;
        }

        @Override
        public List<RevenueGrowthSummary> findByaccount(String getList) {
            return null;
        }

        @Override
        public List<RevenueGrowthSummary> findByquarter(String quarter) {
            return null;
        }

        @Override
        public List<RevenueGrowthSummary> findByVertical(String getList) {
            return null;
        }

        @Override
        public List<RevenueGrowthSummary> findByclassification(String getList) {
            return null;
        }

        @Override
        public List<RevenueGrowthSummary> findByprojectManager(String getList) {
            return null;
        }

        @Override
        public List<RevenueGrowthSummary> findByAccountIn(List<String> accountNames) {
            return null;
        }

        @Override
        public List<RevenueGrowthSummary> findByProjectManagerIn(List<String> pmNames) {
            return null;
        }

        @Override
        public List<RevenueGrowthSummary> findByDeliveryManagerIn(List<String> dmNames) {
            return null;
        }

        @Override
        public List<RevenueGrowthSummary> findByClassificationIn(List<String> classificationNames) {
            return null;
        }


        @Override
        public RevenueGrowthSummary findByVerticalAndClassificationAndDeliveryDirectorAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarter(String vertical, String classification, String deliveryDirector, String deliveryManager, String account, String projectManager, String projectName, int financialYear, String quarter) {
            return null;
        }

        @Override
        public List<RevenueGrowthSummary> findByFinancialYear(Integer year) {
            return null;
        }

        @Override
        public List<RevenueGrowthSummary> findDmByVerticalInAndClassificationIn(List<String> verticals, List<String> classifications) {
            return null;
        }



        @Override
        public void flush() {

        }

        @Override
        public <S extends RevenueGrowthSummary> S saveAndFlush(S entity) {
            return null;
        }

        @Override
        public <S extends RevenueGrowthSummary> List<S> saveAllAndFlush(Iterable<S> entities) {
            return null;
        }

        @Override
        public void deleteAllInBatch(Iterable<RevenueGrowthSummary> entities) {

        }

        @Override
        public void deleteAllByIdInBatch(Iterable<Integer> integers) {

        }

        @Override
        public void deleteAllInBatch() {

        }

        @Override
        public RevenueGrowthSummary getOne(Integer integer) {
            return null;
        }

        @Override
        public RevenueGrowthSummary getById(Integer integer) {
            return null;
        }

        @Override
        public RevenueGrowthSummary getReferenceById(Integer integer) {
            return null;
        }

        @Override
        public <S extends RevenueGrowthSummary> Optional<S> findOne(Example<S> example) {
            return Optional.empty();
        }

        @Override
        public <S extends RevenueGrowthSummary> List<S> findAll(Example<S> example) {
            return null;
        }

        @Override
        public <S extends RevenueGrowthSummary> List<S> findAll(Example<S> example, Sort sort) {
            return null;
        }

        @Override
        public <S extends RevenueGrowthSummary> Page<S> findAll(Example<S> example, Pageable pageable) {
            return null;
        }

        @Override
        public <S extends RevenueGrowthSummary> long count(Example<S> example) {
            return 0;
        }

        @Override
        public <S extends RevenueGrowthSummary> boolean exists(Example<S> example) {
            return false;
        }

        @Override
        public <S extends RevenueGrowthSummary, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
            return null;
        }

        @Override
        public <S extends RevenueGrowthSummary> S save(S entity) {
            return null;
        }

        @Override
        public <S extends RevenueGrowthSummary> List<S> saveAll(Iterable<S> entities) {
            return null;
        }

        @Override
        public Optional<RevenueGrowthSummary> findById(Integer integer) {
            return Optional.empty();
        }

        @Override
        public boolean existsById(Integer integer) {
            return false;
        }

        @Override
        public List<RevenueGrowthSummary> findAll() {
            return null;
        }

        @Override
        public List<RevenueGrowthSummary> findAllById(Iterable<Integer> integers) {
            return null;
        }

        @Override
        public long count() {
            return 0;
        }

        @Override
        public void deleteById(Integer integer) {

        }

        @Override
        public void delete(RevenueGrowthSummary entity) {

        }

        @Override
        public void deleteAllById(Iterable<? extends Integer> integers) {

        }

        @Override
        public void deleteAll(Iterable<? extends RevenueGrowthSummary> entities) {

        }

        @Override
        public void deleteAll() {

        }

        @Override
        public List<RevenueGrowthSummary> findAll(Sort sort) {
            return null;
        }

        @Override
        public Page<RevenueGrowthSummary> findAll(Pageable pageable) {
            return null;
        }
    }
}


