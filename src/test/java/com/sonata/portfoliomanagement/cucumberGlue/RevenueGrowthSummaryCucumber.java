package com.sonata.portfoliomanagement.cucumberGlue;

import com.sonata.portfoliomanagement.controllers.RevenueBudgetSummaryController;
import com.sonata.portfoliomanagement.controllers.RevenueGrowthSummaryController;
import com.sonata.portfoliomanagement.interfaces.RevenueGrowthSummaryRepository;

import com.sonata.portfoliomanagement.model.RevenueBudgetSummary;
import com.sonata.portfoliomanagement.model.RevenueGrowthSummary;
import com.sonata.portfoliomanagement.model.RevenueDTO;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
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

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;

@CucumberContextConfiguration
@SpringBootTest(classes = TestConfig.class)
public class RevenueGrowthSummaryCucumber {

    private RevenueDTO verticalList;

    private RevenueGrowthSummaryCucumber.RevenueGrowthSummaryRepositoryMock revenueRepoMock;
    private ResponseEntity<Set<String>> classificationResponseEntity;




    //feature to give me classifications from verticals
    @Given("the user provides a list of verticals")
    public void theUserProvidesAListOfVerticals() {
        verticalList = new RevenueDTO();
        verticalList.setMyList(Arrays.asList("Vertical1", "Vertical2")); // Example vertical names
    }

    @When("classifications are retrieved by verticals")
    public void classificationsAreRetrievedByVerticals() {
        // Create a mock repository
        revenueRepoMock = new RevenueGrowthSummaryCucumber.RevenueGrowthSummaryRepositoryMock();
        Set<String> expectedClassifications = new HashSet<>(Arrays.asList("Classification1", "Classification2")); // Mock data
        revenueRepoMock.setClassificationByVertical(expectedClassifications);

        // Call the controller method with the mock repository
        RevenueGrowthSummaryController controller = new RevenueGrowthSummaryController(revenueRepoMock);
        classificationResponseEntity = controller.getClassificationByVertical(verticalList);
    }

    @Then("classifications associated with the verticals are returned")
    public void classificationsAssociatedWithTheVerticalsAreReturned() {
        Assert.assertEquals(HttpStatus.OK, classificationResponseEntity.getStatusCode());
        Set<String> expectedClassifications = new HashSet<>(Arrays.asList("Classification1", "Classification2"));
        Assert.assertEquals(expectedClassifications, classificationResponseEntity.getBody());
    }




    static class RevenueGrowthSummaryRepositoryMock implements RevenueGrowthSummaryRepository {

        private Set<String> classificationByVertical;


        @Override
        public List<RevenueGrowthSummary> findByVerticalInAndClassificationIn(List<String> verticals, List<String> classifications) {
            return null;
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
        public RevenueGrowthSummary findByVerticalAndClassificationAndDeliveryManagerAndAccountAndProjectManagerAndProjectNameAndFinancialYearAndQuarter(String vertical, String classification, String deliveryManager, String account, String projectManager, String projectName, int financialYear, String quarter) {
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

        public void setClassificationByVertical(Set<String> expectedClassifications) {
            this.classificationByVertical = classificationByVertical;
        }


        @Override
        public List<RevenueGrowthSummary> findByVerticalIn(List<String> verticals) {
            // Mock implementation to return  classification by verticals-->
            List<RevenueGrowthSummary> revenues = new ArrayList<>();
            for (String classification : classificationByVertical) {
                RevenueGrowthSummary revenue = new RevenueGrowthSummary();
                revenue.setClassification(classification);
                revenues.add(revenue);
            }
            return revenues;
        }
    }
    }
