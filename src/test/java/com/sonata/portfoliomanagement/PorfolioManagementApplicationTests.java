package com.sonata.portfoliomanagement;

import com.sonata.portfoliomanagement.controllers.RevenueBudgetSummaryController;
import com.sonata.portfoliomanagement.interfaces.RevenueBudgetSummaryRepository;
import com.sonata.portfoliomanagement.model.RevenueBudgetSummary;
import com.sonata.portfoliomanagement.model.RevenueDTO;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.Assert;
import org.junit.runner.RunWith;
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

@SpringBootTest
class PorfolioManagementApplicationTests {

}
