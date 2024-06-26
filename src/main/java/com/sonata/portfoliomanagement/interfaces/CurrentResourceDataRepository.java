package com.sonata.portfoliomanagement.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sonata.portfoliomanagement.model.CurrentResourceData;


public interface CurrentResourceDataRepository extends JpaRepository<CurrentResourceData,Integer>{

}