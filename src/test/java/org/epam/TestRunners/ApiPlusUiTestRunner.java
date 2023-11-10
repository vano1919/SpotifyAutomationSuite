package org.epam.TestRunners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;


@CucumberOptions(


        features = "src/test/resources/features/ApiPlusUiFeatures",
        glue = {"org.epam.stepdefinitions"}
)
public class ApiPlusUiTestRunner extends AbstractTestNGCucumberTests {}

