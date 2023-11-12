package org.epam.testRunners;


import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features/apiFeatures",
        glue = {"org.epam.stepdefinitions"}
)

public class ApiTestRunner extends AbstractTestNGCucumberTests {
}
