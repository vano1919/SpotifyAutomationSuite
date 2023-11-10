package org.epam.pages;

import org.epam.enumeration.pageLinksEnum.LinksEnum;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Objects;

public class LoginPage extends BasePage {

    @FindBy(xpath = "//*[@id='login-username']")
    private WebElement loginInputFieldElement;

    @FindBy(xpath = "//*[@id='login-password']")
    private WebElement passwordInputFieldElement;

    @FindBy(xpath = "//*[@id='login-button']")
    private WebElement loginButtonElement;

    @FindBy(xpath = "//div[@data-encore-id=\"banner\"]/span[contains(@class, 'Message-sc-15vkh7g-0')]")
    private WebElement wrongCredentialsErrorMessageElement;


    public LoginPage ( WebDriver webDriver ) {
        super ( webDriver );
    }

    public void open () {
        webDriver.get ( LinksEnum.LOGIN_PAGE_URL.getUrl ( ) );
    }

    public void loginInToSpotify ( String login , String password ) {
        WebElement loginField = waitForElementCondition ( ExpectedConditions.visibilityOf ( loginInputFieldElement ) ,5);
        loginField.clear ( );
        passwordInputFieldElement.clear ( );
        loginField.sendKeys ( login );
        passwordInputFieldElement.sendKeys ( password );

        do {
            try {
                loginButtonElement.click ( );
            } catch (Exception ignored) {
            }

            try {
                String wrongCredentialsMessage = wrongCredentialsErrorMessageElement.getText ( );
                if (Objects.equals ( wrongCredentialsMessage , "Incorrect username or password." )) {
                    break;
                }
            } catch (Exception ignored) {
            }
        } while (webDriver.getCurrentUrl ( ).contains ( "login" ));
    }


}


