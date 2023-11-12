package org.epam.testRunners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;


@CucumberOptions(

        features = "src/test/resources/features/uiFeatures",
        glue = {"org.epam.stepdefinitions"}
)
public class UiTestRunner extends AbstractTestNGCucumberTests {
}

