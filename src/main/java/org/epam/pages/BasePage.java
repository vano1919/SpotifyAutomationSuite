package org.epam.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {


    protected static WebDriver webDriver;
    protected static Actions actions;


    protected BasePage ( WebDriver webDriver ) {
        BasePage.webDriver = webDriver;

        actions = new Actions ( webDriver );

        PageFactory.initElements ( webDriver , this );
    }

    public WebElement getElementFromDynamicXpath ( String xpathExpression ) {

        return new WebDriverWait ( webDriver , Duration.ofSeconds ( 5 ) )
                .until ( ExpectedConditions.presenceOfElementLocated ( By.xpath ( xpathExpression ) ) );


    }


    public static WebElement waitForElementCondition ( ExpectedCondition<WebElement> condition , int timeout ) {
        WebDriverWait wait = new WebDriverWait ( webDriver , Duration.ofSeconds ( timeout ) );
        return wait.until ( condition );
    }


    public boolean waitForUrl ( String expectedUrlPart , boolean shouldContain ,int duration) {
        WebDriverWait wait = new WebDriverWait ( webDriver , Duration.ofSeconds ( duration ) );

        try {
            if (shouldContain) {
                return wait.until ( ExpectedConditions.urlContains ( expectedUrlPart ) );
            } else {
                return wait.until ( ExpectedConditions.not ( ExpectedConditions.urlContains ( expectedUrlPart ) ) );
            }
        } catch (Exception ignored) {
            return !shouldContain;
        }


    }

    public void clickElement ( WebElement element ) {
        waitForElementCondition ( ExpectedConditions.elementToBeClickable ( element ) , 10 ).click ( );
    }

    public void inputText ( WebElement element , String text ) {
        WebElement target = waitForElementCondition ( ExpectedConditions.elementToBeClickable ( element ) , 10 );
        target.click ( );
        target.clear ( );
        target.sendKeys ( text , Keys.ENTER );
    }

    public void sendKeys ( WebElement element , Keys keys ) {
        WebElement target = waitForElementCondition ( ExpectedConditions.elementToBeClickable ( element ) , 3 );
        target.sendKeys ( keys );
    }

    public String getTextFromElement ( WebElement element ) {
        int maxRetries = 15;
        int retryCount = 0;

        while (retryCount < maxRetries) {
            try {
                return waitForElementCondition ( ExpectedConditions.visibilityOf ( element ) , 5 ).getText ( );
            } catch (Exception e) {
                retryCount++;
                webDriver.navigate ( ).refresh ( );
            }
        }
        return "";
    }

    public void dragAndDropElementUntilUrlChanges ( WebElement source , WebElement target ) {
        actions.dragAndDrop ( source , target ).perform ( );

        actions.click ( target ).perform ( );

        waitForUrl ( "playlist" , true ,30);
    }



    public void clearFieldUsingKey ( WebElement element ) {
        waitForElementCondition ( ExpectedConditions.visibilityOf ( element ) , 10 ).sendKeys ( "1" , Keys.BACK_SPACE );
    }


    public void contextClick ( WebElement element ) {


        actions.contextClick ( waitForElementCondition ( ExpectedConditions.elementToBeClickable ( element ) ,10) ).perform ( );
    }

}