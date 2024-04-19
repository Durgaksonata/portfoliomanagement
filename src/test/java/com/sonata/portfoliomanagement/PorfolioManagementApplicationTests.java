package com.sonata.portfoliomanagement;

import com.sonata.portfoliomanagement.interfaces.RevenueBudgetSummaryRepository;
import com.sonata.portfoliomanagement.model.RevenueBudgetSummary;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PorfolioManagementApplicationTests {

	@Autowired
	RevenueBudgetSummaryRepository revenueRepo;

	@Test
	void contextLoads() {
	}

	@Test
//	@Rollback(value=false)
	@Order(1)
	public void getRevenueTest() {
		RevenueBudgetSummary rn=revenueRepo.findById(10).get();
		Assertions.assertThat(rn.getId()).isEqualTo(10);
	}

}
