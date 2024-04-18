package com.sonata.portfoliomanagement.cucumberGlue;

import com.sonata.portfoliomanagement.controllers.RevenueBudgetSummaryController;
import com.sonata.portfoliomanagement.interfaces.RevenueBudgetSummaryRepository;
import com.sonata.portfoliomanagement.model.RevenueBudgetSummary;
import com.sonata.portfoliomanagement.model.RevenueDTO;
import io.cucumber.java.en.*;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.Assert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.*;
import java.util.function.Function;
import static org.junit.jupiter.api.Assertions.assertEquals;

@CucumberContextConfiguration
@SpringBootTest(classes = TestConfig.class)
public class FinancialYearByProjectSteps {

    private RevenueDTO projectList;
    private RevenueDTO accountList;
    private RevenueDTO pmList;

    private RevenueDTO dmList;
    private ResponseEntity<Set<Integer>> financialYearResponseEntity;
    private ResponseEntity<Set<String>> accountResponseEntity;
    private ResponseEntity<Set<String>> projectResponseEntity;
    private ResponseEntity<Set<String>> projectManagerResponseEntity;

    private RevenueBudgetSummaryRepositoryMock revenueRepoMock;
    private ResponseEntity<Set<String>> responseEntity;


    //feature to give me financialYear by projectList
    @Given("the client provides a list of projects")
    public void theClientProvidesAListOfProjects() {
        projectList = new RevenueDTO();
        projectList.setMyList(Arrays.asList("project1"));
    }

    @When("the financial year is retrieved by project")
    public void theFinancialYearIsRetrievedByProject() {
        // Create a mock repository
        revenueRepoMock = new RevenueBudgetSummaryRepositoryMock();
        revenueRepoMock.setFinancialYearsByProject(Collections.singleton(2023)); // Mock data

        // Call the controller method with the mock repository
        RevenueBudgetSummaryController controller = new RevenueBudgetSummaryController(revenueRepoMock);
        financialYearResponseEntity = controller.getFinancialYearByProject(projectList);
    }

    @Then("the financial years for the projects are returned")
    public void theFinancialYearsForTheProjectsAreReturned() {
        Assert.assertEquals(HttpStatus.OK, financialYearResponseEntity.getStatusCode());
        Set<Integer> expectedYears = new HashSet<>(Collections.singletonList(2023));
        Assert.assertEquals(expectedYears, financialYearResponseEntity.getBody());
    }


    //feature to give me accounts from delivery managers
    @Given("the client provides a list of delivery managers")
    public void theClientProvidesAListOfDeliveryManagers() {
        dmList = new RevenueDTO();
        dmList.setMyList(Arrays.asList("DM1", "DM2")); // Example delivery manager names
    }

    @When("the accounts are retrieved by delivery managers")
    public void theAccountsAreRetrievedByDeliveryManagers() {
        // Create a mock repository
        revenueRepoMock = new RevenueBudgetSummaryRepositoryMock();
        Set<String> expectedAccounts = new HashSet<>(Arrays.asList("Account1", "Account2")); // Mock data
        revenueRepoMock.setAccountsByDeliveryManager(expectedAccounts);

        // Call the controller method with the mock repository
        RevenueBudgetSummaryController controller = new RevenueBudgetSummaryController(revenueRepoMock);
        accountResponseEntity = controller.getAccountByDM(dmList);
    }

    @Then("the accounts associated with the delivery managers are returned")
    public void theAccountsAssociatedWithTheDeliveryManagersAreReturned() {
        Assert.assertEquals(HttpStatus.OK, accountResponseEntity.getStatusCode());
        Set<String> expectedAccounts = new HashSet<>(Arrays.asList("Account1", "Account2"));
        Assert.assertEquals(expectedAccounts, accountResponseEntity.getBody());
    }

    //feature to give me projects from project managers-->
    @Given("the client provides a list of project managers")
    public void theClientProvidesAListOfProjectManagers() {
        pmList = new RevenueDTO();
        pmList.setMyList(Arrays.asList("PM1", "PM2"));
    }

    @When("the projects are retrieved by project managers")
    public void theProjectsAreRetrievedByProjectManagers() {
        // Create a mock repository
        revenueRepoMock = new RevenueBudgetSummaryRepositoryMock();
        Set<String> expectedProjects = new HashSet<>(Arrays.asList("Project1", "Project2")); // Mock data
        revenueRepoMock.setProjectsByProjectManager(expectedProjects);

        // Call the controller method with the mock repository
        RevenueBudgetSummaryController controller = new RevenueBudgetSummaryController(revenueRepoMock);
        projectResponseEntity = controller.getProjectByPm(pmList);
    }

    @Then("the projects associated with the project managers are returned")
    public void theProjectsForTheProjectManagersAreReturned() {
        Assert.assertEquals(HttpStatus.OK, projectResponseEntity.getStatusCode());
        Set<String> expectedProjects = new HashSet<>(Arrays.asList("Project1", "Project2"));
        Assert.assertEquals(expectedProjects, projectResponseEntity.getBody());
    }


    // Mock repository implementation for testing
    static class RevenueBudgetSummaryRepositoryMock implements RevenueBudgetSummaryRepository {
        private Set<Integer> financialYearsByProject;
        private Set<String> accountsByDeliveryManager;

        private  Set<String> projectsByProjectManager;

        public void setFinancialYearsByProject(Set<Integer> financialYearsByProject) {
            this.financialYearsByProject = financialYearsByProject;
        }

        public void setAccountsByDeliveryManager(Set<String> accountsByDeliveryManager) {
            this.accountsByDeliveryManager = accountsByDeliveryManager;
        }

        @Override
        public List<RevenueBudgetSummary> findByProjectNameIn(List<String> projectNames) {
            // Mock implementation to return financial years for projects
            List<RevenueBudgetSummary> revenues = new ArrayList<>();
            for (Integer year : financialYearsByProject) {
                RevenueBudgetSummary revenue = new RevenueBudgetSummary();
                revenue.setFinancialYear(year);
                revenues.add(revenue);
            }
            return revenues;
        }

        @Override
        public List<RevenueBudgetSummary> findBydeliveryManager(String deliveryManager) {
            return null;
        }

        @Override
        public List<RevenueBudgetSummary> findByquarter(String quarter) {
            return null;
        }

        @Override
        public List<RevenueBudgetSummary> findByprojectManager(String getList) {
            return null;
        }

        @Override
        public List<RevenueBudgetSummary> findByProjectManager(String projectManager) {
            return null;
        }

        @Override
        public List<RevenueBudgetSummary> findAllByClassification(String classification) {
            return null;
        }

        @Override
        public List<RevenueBudgetSummary> findByClassification(String classificationName) {
            return null;
        }

        @Override
        public List<RevenueBudgetSummary> findByclassification(String getList) {
            return null;
        }

        @Override
        public List<RevenueBudgetSummary> findByVertical(String getList) {
            return null;
        }

        @Override
        public List<RevenueBudgetSummary> findByFinancialYearInAndProjectNameInAndVerticalInAndClassificationInAndDeliveryManagerInAndAccountInAndProjectManagerInAndQuarterIn(List<Integer> financialYear, List<String> projectList, List<String> verticalList, List<String> classificationList, List<String> dmList, List<String> accountList, List<String> pmList, List<String> quarterList) {
            return null;
        }

        @Override
        public List<RevenueBudgetSummary> findByVerticalInAndClassificationIn(List<String> verticals, List<String> classifications) {
            return null;
        }

        @Override
        public List<RevenueBudgetSummary> findByFinancialYear(int financialYear) {
            return null;
        }

        @Override
        public List<RevenueBudgetSummary> findByAccount(String account) {
            return null;
        }

        @Override
        public List<RevenueBudgetSummary> findByDeliveryManager(String deliveryManager) {
            return null;
        }

        @Override
        public List<RevenueBudgetSummary> findByVerticalAndClassification(String vertical, String classification) {
            return null;
        }

        @Override
        public List<RevenueBudgetSummary> findAllByVertical(String vertical) {
            return null;
        }

        @Override
        public List<RevenueBudgetSummary> findByProjectName(String project) {
            return null;
        }

        @Override
        public List<RevenueBudgetSummary> findByVerticalAndClassificationAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYear(String vertical, String classification, String dm, String account, String pm, String project, Integer financialYear) {
            return null;
        }

        @Override
        public List<RevenueBudgetSummary> findByFinancialYearIn(List<Integer> financialYears) {
            return null;
        }

        @Override
        public List<RevenueBudgetSummary> findByAccountIn(List<String> accounts) {
            return null;
        }

        @Override
        public List<RevenueBudgetSummary> findByDeliveryManagerIn(List<String> deliveryManagers) {
            // Mock implementation to return accounts for delivery managers
            List<RevenueBudgetSummary> revenues = new ArrayList<>();
            for (String account : accountsByDeliveryManager) {
                RevenueBudgetSummary revenue = new RevenueBudgetSummary();
                revenue.setAccount(account);
                revenues.add(revenue);
            }
            return revenues;
        }

        @Override
        public List<RevenueBudgetSummary> findByProjectManagerIn(List<String> pmNames) {
            // Mock implementation to return projects from project managers
            List<RevenueBudgetSummary> revenues = new ArrayList<>();
            for (String project : projectsByProjectManager) {
                RevenueBudgetSummary revenue = new RevenueBudgetSummary();
                revenue.setProjectName(project);
                revenues.add(revenue);
            }
            return revenues;
        }

        public void setProjectsByProjectManager(Set<String> projectsByProjectManager) {
            this.projectsByProjectManager = projectsByProjectManager;
        }

        @Override
        public List<RevenueBudgetSummary> findByVerticalIn(List<String> verticals) {
            return null;
        }

        @Override
        public List<RevenueBudgetSummary> findByClassificationIn(List<String> classifications) {
            return null;
        }

        @Override
        public void flush() {

        }

        @Override
        public <S extends RevenueBudgetSummary> S saveAndFlush(S entity) {
            return null;
        }

        @Override
        public <S extends RevenueBudgetSummary> List<S> saveAllAndFlush(Iterable<S> entities) {
            return null;
        }

        @Override
        public void deleteAllInBatch(Iterable<RevenueBudgetSummary> entities) {

        }

        @Override
        public void deleteAllByIdInBatch(Iterable<Integer> integers) {

        }

        @Override
        public void deleteAllInBatch() {

        }

        @Override
        public RevenueBudgetSummary getOne(Integer integer) {
            return null;
        }

        @Override
        public RevenueBudgetSummary getById(Integer integer) {
            return null;
        }

        @Override
        public RevenueBudgetSummary getReferenceById(Integer integer) {
            return null;
        }

        @Override
        public <S extends RevenueBudgetSummary> Optional<S> findOne(Example<S> example) {
            return Optional.empty();
        }

        @Override
        public <S extends RevenueBudgetSummary> List<S> findAll(Example<S> example) {
            return null;
        }

        @Override
        public <S extends RevenueBudgetSummary> List<S> findAll(Example<S> example, Sort sort) {
            return null;
        }

        @Override
        public <S extends RevenueBudgetSummary> Page<S> findAll(Example<S> example, Pageable pageable) {
            return null;
        }

        @Override
        public <S extends RevenueBudgetSummary> long count(Example<S> example) {
            return 0;
        }

        @Override
        public <S extends RevenueBudgetSummary> boolean exists(Example<S> example) {
            return false;
        }

        @Override
        public <S extends RevenueBudgetSummary, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
            return null;
        }

        @Override
        public <S extends RevenueBudgetSummary> S save(S entity) {
            return null;
        }

        @Override
        public <S extends RevenueBudgetSummary> List<S> saveAll(Iterable<S> entities) {
            return null;
        }

        @Override
        public Optional<RevenueBudgetSummary> findById(Integer integer) {
            return Optional.empty();
        }

        @Override
        public boolean existsById(Integer integer) {
            return false;
        }

        @Override
        public List<RevenueBudgetSummary> findAll() {
            return null;
        }

        @Override
        public List<RevenueBudgetSummary> findAllById(Iterable<Integer> integers) {
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
        public void delete(RevenueBudgetSummary entity) {

        }

        @Override
        public void deleteAllById(Iterable<? extends Integer> integers) {

        }

        @Override
        public void deleteAll(Iterable<? extends RevenueBudgetSummary> entities) {

        }

        @Override
        public void deleteAll() {

        }

        @Override
        public List<RevenueBudgetSummary> findAll(Sort sort) {
            return null;
        }

        @Override
        public Page<RevenueBudgetSummary> findAll(Pageable pageable) {
            return null;
        }

    }
}
