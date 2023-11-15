package org.epam.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class StatusPage extends BasePage {

    @FindBy(xpath = "//*[@id=\"root\"]")
    private WebElement loginMessageElement;

    public StatusPage ( WebDriver webDriver ) {
        super ( webDriver );
    }

    public String getLoginMessage () {

        if (waitForUrl ( "status" , true ,30)) {
            return getTextFromElement ( loginMessageElement );

        }
        return "Did not find login message trying again!";
    }


}