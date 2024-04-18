package com.sonata.portfoliomanagement.cucumberGlue;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;

@TestConfiguration
@ComponentScan(basePackages = {"com.sonata.portfoliomanagement.model", "com.sonata.portfoliomanagement.cucumberGlue"})
public class TestConfig {

}
