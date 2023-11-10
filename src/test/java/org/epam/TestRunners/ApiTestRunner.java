package org.epam.TestRunners;


import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features/ApiFeatures",
        glue = {"org.epam.stepdefinitions"}
)
public class ApiTestRunner extends AbstractTestNGCucumberTests {}
