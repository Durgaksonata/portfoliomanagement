package com.sonata.portfoliomanagement.interfaces;

import com.sonata.portfoliomanagement.model.CostData;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CostDataRepository extends JpaRepository<CostData,Integer> {

}