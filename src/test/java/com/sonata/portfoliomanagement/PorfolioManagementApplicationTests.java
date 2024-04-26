package com.sonata.portfoliomanagement;

import com.sonata.portfoliomanagement.cucumberGlue.TestConfig;
import com.sonata.portfoliomanagement.interfaces.RevenueBudgetSummaryRepository;
import com.sonata.portfoliomanagement.model.RevenueBudgetSummary;
import io.cucumber.spring.CucumberContextConfiguration;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
@CucumberContextConfiguration
@SpringBootTest(classes = TestConfig.class)
public class PorfolioManagementApplicationTests {



	@Test
	void contextLoads() {
	}


}
