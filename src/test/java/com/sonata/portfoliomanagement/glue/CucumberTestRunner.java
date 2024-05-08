package com.sonata.portfoliomanagement.glue;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features", glue = "com.sonata.portfoliomanagement.glue")
public class CucumberTestRunner {
}
