package com.sonata.portfoliomanagement.cucumberGlue;

import com.sonata.portfoliomanagement.controllers.RevenueBudgetSummaryController;
import com.sonata.portfoliomanagement.interfaces.RevenueBudgetSummaryRepository;
import com.sonata.portfoliomanagement.model.RevenueBudgetSummary;
import com.sonata.portfoliomanagement.model.RevenueDTO;
import io.cucumber.java.en.*;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.Setter;
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
    private RevenueDTO verticalList;
    private RevenueDTO classificationList;
    private RevenueDTO dmList;
    private RevenueDTO dmClassificationList;

    private RevenueDTO accountList;
    private RevenueDTO pmAccountClassificationList;
    private RevenueDTO pmList;
    private RevenueDTO pmClassificationList;

    private RevenueDTO projectList;
    private RevenueDTO criteria;


    private ResponseEntity<Set<String>> classificationResponseEntity;
    private ResponseEntity<Set<String>> dmResponseEntity;
    private ResponseEntity<Set<String>> accountResponseEntity;
    private ResponseEntity<Set<String>> pmResponseEntity;
    private ResponseEntity<Set<String>> projectResponseEntity;
    private ResponseEntity<Set<Integer>> financialYearResponseEntity;
    private ResponseEntity<List<RevenueBudgetSummary>> responseEntity;


    private RevenueBudgetSummaryRepositoryMock revenueRepoMock;

    //feature to give me classifications from verticals
    @Given("the client provides a list of verticals")
    public void theClientProvidesAListOfVerticals() {
        verticalList = new RevenueDTO();
        verticalList.setMyList(Arrays.asList("Vertical1", "Vertical2")); // Example vertical names
    }

    @When("the classifications are retrieved by verticals")
    public void theClassificationsAreRetrievedByVerticals() {
        // Create a mock repository
        revenueRepoMock = new RevenueBudgetSummaryRepositoryMock();
        Set<String> expectedClassifications = new HashSet<>(Arrays.asList("Classification1", "Classification2")); // Mock data
        revenueRepoMock.setClassificationByVertical(expectedClassifications);

        // Call the controller method with the mock repository
        RevenueBudgetSummaryController controller = new RevenueBudgetSummaryController(revenueRepoMock);
        classificationResponseEntity = controller.getClassificationByVertical(verticalList);
    }

    @Then("the classifications associated with the verticals are returned")
    public void theClassificationsAssociatedWithTheVerticalsAreReturned() {
        Assert.assertEquals(HttpStatus.OK, classificationResponseEntity.getStatusCode());
        Set<String> expectedClassifications = new HashSet<>(Arrays.asList("Classification1", "Classification2"));
        Assert.assertEquals(expectedClassifications, classificationResponseEntity.getBody());
    }

    //feature to give me delivery managers from classification
    @Given("the client provides a list of classifications")
    public void theClientProvidesAListOfClassifications() {
        classificationList = new RevenueDTO();
        classificationList.setMyList(Arrays.asList("Classification1", "Classification2")); // Example classification names
    }

    @When("the delivery managers are retrieved by classifications")
    public void theDeliveryManagersAreRetrievedByClassifications() {
        // Create a mock repository
        revenueRepoMock = new RevenueBudgetSummaryRepositoryMock();
        Set<String> expectedDMs = new HashSet<>(Arrays.asList("DM1", "DM2")); // Mock data
        revenueRepoMock.setDeliveryManagerByClassification(expectedDMs);

        // Call the controller method with the mock repository
        RevenueBudgetSummaryController controller = new RevenueBudgetSummaryController(revenueRepoMock);
        dmResponseEntity = controller.getDMByClassification(classificationList);
    }

    @Then("the delivery managers associated with the classifications are returned")
    public void theDeliveryManagersAssociatedWithTheClassificationsAreReturned() {
        Assert.assertEquals(HttpStatus.OK, dmResponseEntity.getStatusCode());
        Set<String> expectedDMs = new HashSet<>(Arrays.asList("DM1", "DM2"));
        Assert.assertEquals(expectedDMs, dmResponseEntity.getBody());
    }

    //feature to give me delivery managers from vertical and classification-->
    @Given("the client provides a list of verticals and classifications")
    public void theClientProvidesAListOfVerticalsAndClassifications() {
        dmList = new RevenueDTO();
        dmList.setVerticalList(Arrays.asList("Vertical1", "Vertical2")); // Example vertical names
        dmList.setClassificationList(Arrays.asList("Classification1", "Classification2")); // Example classification names
    }

    @When("the delivery managers are retrieved by verticals and classifications")
    public void theDeliveryManagersAreRetrievedByVerticalsAndClassifications() {
        // Create a mock repository
        revenueRepoMock = new RevenueBudgetSummaryRepositoryMock();
        Set<String> expectedDMs = new HashSet<>(Arrays.asList("DM1", "DM2")); // Mock data
        revenueRepoMock.setDeliveryManagerByVerticalAndClassification(expectedDMs);

        // Call the controller method with the mock repository
        RevenueBudgetSummaryController controller = new RevenueBudgetSummaryController(revenueRepoMock);
        dmResponseEntity = controller.getDMByVerticalAndClassification(dmList);
    }

    @Then("the delivery managers associated with the verticals and classifications are returned")
    public void theDeliveryManagersAssociatedWithTheVerticalsAndClassificationsAreReturned() {
        Assert.assertEquals(HttpStatus.OK, dmResponseEntity.getStatusCode());
        Set<String> expectedDMs = new HashSet<>(Arrays.asList("DM1", "DM2"));
        Assert.assertEquals(expectedDMs, dmResponseEntity.getBody());
    }

    //feature to give me accounts from delivery managers-->
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

    //feature to get accounts from delivery managers and classification

    @Given("the client provides a list of delivery managers and classifications")
    public void theClientProvidesAListOfDeliveryManagersAndClassifications() {
        dmClassificationList = new RevenueDTO();
        dmClassificationList.setDmList(Arrays.asList("DM1", "DM2")); // Example delivery manager names
        dmClassificationList.setClassificationList(Arrays.asList("Classification1", "Classification2")); // Example classification names
    }

    @When("the accounts are retrieved by delivery managers and classifications")
    public void theAccountsAreRetrievedByDeliveryManagersAndClassifications() {
        // Create a mock repository
        revenueRepoMock = new RevenueBudgetSummaryRepositoryMock();
        Set<String> expectedAccounts = new HashSet<>(Arrays.asList("Account1", "Account2")); // Mock data
        revenueRepoMock.setAccountsByDMAndClassification(expectedAccounts);

        // Call the controller method with the mock repository
        RevenueBudgetSummaryController controller = new RevenueBudgetSummaryController(revenueRepoMock);
        accountResponseEntity = controller.getAccountByDMAndClassification(dmClassificationList);
    }

    @Then("the accounts associated with the delivery managers and classifications are returned")
    public void theAccountsAssociatedWithTheDeliveryManagersAndClassificationsAreReturned() {
        Assert.assertEquals(HttpStatus.OK, accountResponseEntity.getStatusCode());
        Set<String> expectedAccounts = new HashSet<>(Arrays.asList("Account1", "Account2"));
        Assert.assertEquals(expectedAccounts, accountResponseEntity.getBody());
    }

    //get projectManagers from account-->
    @Given("the client provides a list of accounts")
    public void theClientProvidesAListOfAccountNames() {
        accountList = new RevenueDTO();
        accountList.setMyList(Arrays.asList("Account1", "Account2"));
    }

    @When("the project managers are retrieved by account names")
    public void theProjectManagersAreRetrievedByAccountNames() {
        // Create a mock repository
        revenueRepoMock = new RevenueBudgetSummaryRepositoryMock();
        Set<String> expectedPMs = new HashSet<>(Arrays.asList("PM1", "PM2")); // Mock data
        revenueRepoMock.setPMsByAccount(expectedPMs);

        // Call the controller method with the mock repository
        RevenueBudgetSummaryController controller = new RevenueBudgetSummaryController(revenueRepoMock);
        pmResponseEntity = controller.getpmByacnt(accountList);
    }

    @Then("the project managers associated with the accounts are returned")
    public void theProjectManagersForTheAccountsAreReturned() {
        Assert.assertEquals(HttpStatus.OK, pmResponseEntity.getStatusCode());
        Set<String> expectedPMs = new HashSet<>(Arrays.asList("PM1", "PM2"));
        Assert.assertEquals(expectedPMs, pmResponseEntity.getBody());
    }

    //feature to get project managers from account and classification-->
    @Given("the client provides a list of accounts and classifications")
    public void theClientProvidesAListOfAccountsAndClassifications() {
        pmAccountClassificationList = new RevenueDTO();
        pmAccountClassificationList.setMyList(Arrays.asList("Account1", "Account2")); // Example account names
        pmAccountClassificationList.setClassificationList(Arrays.asList("Classification1", "Classification2")); // Example classification names
    }

    @When("the project managers are retrieved by accounts and classifications")
    public void theProjectManagersAreRetrievedByAccountsAndClassifications() {
        // Create a mock repository
        revenueRepoMock = new RevenueBudgetSummaryRepositoryMock();
        Set<String> expectedPMs = new HashSet<>(Arrays.asList("PM1", "PM2")); // Mock data
        revenueRepoMock.setProjectManagerByAccountAndClassification(expectedPMs);

        // Call the controller method with the mock repository
        RevenueBudgetSummaryController controller = new RevenueBudgetSummaryController(revenueRepoMock);
        pmResponseEntity = controller.getPMByAccountAndClassification(pmAccountClassificationList);
    }

    @Then("the project managers associated with the accounts and classifications are returned")
    public void theProjectManagersAssociatedWithTheAccountsAndClassificationsAreReturned() {
        Assert.assertEquals(HttpStatus.OK, pmResponseEntity.getStatusCode());
        Set<String> expectedPMs = new HashSet<>(Arrays.asList("PM1", "PM2"));
        Assert.assertEquals(expectedPMs, pmResponseEntity.getBody());
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

    //feature to give me projects from project managers and classification-->

    @Given("the client provides a list of project managers and classifications")
    public void theClientProvidesAListOfProjectManagersAndClassifications() {
        pmClassificationList = new RevenueDTO();
        pmClassificationList.setPmList(Arrays.asList("PM1", "PM2")); // Example project manager names
        pmClassificationList.setClassificationList(Arrays.asList("Classification1", "Classification2")); // Example classification names
    }

    @When("the projects are retrieved by project managers and classifications")
    public void theProjectsAreRetrievedByProjectManagersAndClassifications() {
        // Create a mock repository
        revenueRepoMock = new RevenueBudgetSummaryRepositoryMock();
        Set<String> expectedProjects = new HashSet<>(Arrays.asList("Project1", "Project2")); // Mock data
        revenueRepoMock.setProjectsByPMAndClassification(expectedProjects);

        // Call the controller method with the mock repository
        RevenueBudgetSummaryController controller = new RevenueBudgetSummaryController(revenueRepoMock);
        projectResponseEntity = controller.getProjectsByPMAndClassification(pmClassificationList);
    }

    @Then("the projects associated with the project managers and classifications are returned")
    public void theProjectsAssociatedWithTheProjectManagersAndClassificationsAreReturned() {
        Assert.assertEquals(HttpStatus.OK, projectResponseEntity.getStatusCode());
        Set<String> expectedProjects = new HashSet<>(Arrays.asList("Project1", "Project2"));
        Assert.assertEquals(expectedProjects, projectResponseEntity.getBody());
    }

    //feature to give me financialYear by projectList-->
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

    //feature to give all the data by giving filters(vertical,classification,dm,account,pm,project,fy,quarter)-->
//    @Given("the client provides criteria for financial data retrieval")
//    public void the_client_provides_criteria_for_financial_data_retrieval() {
//        criteria = new RevenueDTO();
//        // Set criteria fields
//        criteria.setFinancialYear(Collections.singletonList(2024));
//        criteria.setProjectList(Arrays.asList("Project1", "Project2"));
//        criteria.setVerticalList(Arrays.asList("Vertical1", "Vertical2"));
//        criteria.setClassificationList(Arrays.asList("Classification1", "Classification2"));
//        criteria.setDmList(Arrays.asList("DM1", "DM2"));
//        criteria.setAccountList(Arrays.asList("Account1", "Account2"));
//        criteria.setPmList(Arrays.asList("PM1", "PM2"));
//        criteria.setQuarterList(Arrays.asList("Q1", "Q2"));
//    }
//
//    @When("the financial data is retrieved based on the provided criteria")
//    public void the_financial_data_is_retrieved_based_on_the_provided_criteria() {
//        // Call the controller method with the provided criteria
//        RevenueBudgetSummaryController controller = new RevenueBudgetSummaryController(revenueRepoMock);
//        responseEntity = controller.getFinancialDataByCriteria(criteria);
//    }
//
//    @Then("the financial data matching the criteria is returned")
//    public void theFinancialDataMatchingTheCriteriaIsReturned() {
//        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        // Validate the returned financial data if necessary
//        List<RevenueBudgetSummary> financialData = responseEntity.getBody();
//        // Add assertions to validate the financial data
//    }

    // Mock repository implementation for testing
    static class RevenueBudgetSummaryRepositoryMock implements RevenueBudgetSummaryRepository {

        private Set<String> classificationByVertical;

        private Set<String> deliveryManagerByClassification ;
        private Set<String> deliveryManagerByVerticalAndClassification;

        private Set<String> accountsByDeliveryManager;
        private Set<String> accountsByDMAndClassification;

        private  Set<String> projectManagerByAccount;
        private  Set<String> projectManagerByAccountAndClassification;

        private  Set<String> projectsByProjectManager;
        private  Set<String> projectsByPMAndClassification;

        private Set<Integer> financialYearsByProject;
        private Set<String> quartersByFinancialYear;
        private Set<String> getAllData;


        public void setClassificationByVertical(Set<String> classificationByVertical){
            this.classificationByVertical = classificationByVertical;
        }

        public void setDeliveryManagerByClassification(Set<String> deliveryManagerByClassification){
            this.deliveryManagerByClassification = deliveryManagerByClassification;
        }

        public void setDeliveryManagerByVerticalAndClassification(Set<String> deliveryManagerByVerticalAndClassification){
            this.deliveryManagerByVerticalAndClassification = deliveryManagerByVerticalAndClassification;
        }
        public void setAccountsByDeliveryManager(Set<String> accountsByDeliveryManager) {
            this.accountsByDeliveryManager = accountsByDeliveryManager;
        }


        public void setAccountsByDMAndClassification(Set<String> accountsByDMAndClassification) {
            this.accountsByDMAndClassification = accountsByDMAndClassification;
        }

        public void setPMsByAccount(Set<String> projectManagerByAccount) {
            this.projectManagerByAccount = projectManagerByAccount;
        }

        public void setProjectManagerByAccountAndClassification(Set<String> projectManagerByAccountAndClassification) {
            this.projectManagerByAccountAndClassification = projectManagerByAccountAndClassification;
        }

        public void setProjectsByProjectManager(Set<String> projectsByProjectManager) {
            this.projectsByProjectManager = projectsByProjectManager;
        }

        public void setProjectsByPMAndClassification(Set<String> projectsByPMAndClassification) {
            this.projectsByPMAndClassification = projectsByPMAndClassification;
        }

        public void setFinancialYearsByProject(Set<Integer> financialYearsByProject) {
            this.financialYearsByProject = financialYearsByProject;
        }

        public void setQuartersByFinancialYear(Set<String> quartersByFinancialYear) {
            this.quartersByFinancialYear = quartersByFinancialYear;
        }

        @Override
        public List<RevenueBudgetSummary> findByVerticalIn(List<String> verticals) {

            // Mock implementation to return  classification by verticals-->
            List<RevenueBudgetSummary> revenues = new ArrayList<>();
            for (String classification : classificationByVertical) {
                RevenueBudgetSummary revenue = new RevenueBudgetSummary();
                revenue.setClassification(classification);
                revenues.add(revenue);
            }
            return revenues;
        }

        @Override
        public List<RevenueBudgetSummary> findByClassificationIn(List<String> classifications) {
            // Mock implementation to return deliveryManagers for classification
            List<RevenueBudgetSummary> revenues = new ArrayList<>();
            for (String dms : deliveryManagerByClassification) {
                RevenueBudgetSummary revenue = new RevenueBudgetSummary();
                revenue.setDeliveryManager(dms);
                revenues.add(revenue);
            }
            return revenues;
        }

        @Override
        public List<RevenueBudgetSummary> findByVerticalInAndClassificationIn(List<String> verticals, List<String> classifications) {
            List<RevenueBudgetSummary> revenues = new ArrayList<>();
            for (String dms : deliveryManagerByVerticalAndClassification) {
                RevenueBudgetSummary revenue = new RevenueBudgetSummary();
                revenue.setDeliveryManager(dms);
                revenues.add(revenue);
            }
            return revenues;
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
        public List<RevenueBudgetSummary> findByDeliveryManagerInAndClassificationIn(List<String> dmNames, List<String> classifications) {
            List<RevenueBudgetSummary> revenues = new ArrayList<>();
            for (String account : accountsByDMAndClassification) {
                RevenueBudgetSummary revenue = new RevenueBudgetSummary();
                revenue.setAccount(account);
                revenues.add(revenue);
            }
            return revenues;
        }

        @Override
        public List<RevenueBudgetSummary> findByAccountIn(List<String> accounts) {
            List<RevenueBudgetSummary> revenues = new ArrayList<>();
            for (String pms : projectManagerByAccount) {
                RevenueBudgetSummary revenue = new RevenueBudgetSummary();
                revenue.setProjectManager(pms);
                revenues.add(revenue);
            }
            return revenues;
        }

        @Override
        public List<RevenueBudgetSummary> findByAccountInAndClassificationIn(List<String> accountNames, List<String> classifications) {
            List<RevenueBudgetSummary> revenues = new ArrayList<>();
            for (String pms : projectManagerByAccountAndClassification) {
                RevenueBudgetSummary revenue = new RevenueBudgetSummary();
                revenue.setProjectManager(pms);
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

        @Override
        public List<RevenueBudgetSummary> findByProjectManagerInAndClassificationIn(List<String> pmNames, List<String> classifications) {
            List<RevenueBudgetSummary> revenues = new ArrayList<>();
            for (String project : projectsByPMAndClassification) {
                RevenueBudgetSummary revenue = new RevenueBudgetSummary();
                revenue.setProjectName(project);
                revenues.add(revenue);
            }
            return revenues;
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
        public RevenueBudgetSummary findByVerticalAndClassificationAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarter(String verticals, String classifications, String deliverManagers, String accounts, String projectManagers, String projectNames, int financialYears, String quarters) {
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
        public List<RevenueBudgetSummary> findByVerticalAndClassification(String vertical, String classification) {
            return null;
        }

        @Override
        public List<RevenueBudgetSummary> findByFinancialYearIn(List<Integer> financialYears) {
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
