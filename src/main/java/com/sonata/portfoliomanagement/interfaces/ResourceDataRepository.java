package com.sonata.portfoliomanagement.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sonata.portfoliomanagement.model.ResourceData;


public interface ResourceDataRepository extends JpaRepository<ResourceData,Integer>{

}