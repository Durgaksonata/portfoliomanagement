package com.sonata.portfoliomanagement.interfaces;

import com.sonata.portfoliomanagement.model.MD_Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MD_AccountsRepository extends JpaRepository<MD_Accounts, Integer> {

    List<MD_Accounts> findByAccounts(String accounts);

}