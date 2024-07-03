package com.sonata.portfoliomanagement.interfaces;

import com.sonata.portfoliomanagement.model.AccountBudgets;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountBudgetsRepository extends JpaRepository<AccountBudgets, Integer> {
}
