package org.epam.pages;

import org.epam.enumeration.pageLinksEnum.LinksEnum;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class MainPage extends BasePage {

    @FindBy(xpath = "//div[contains(@aria-labelledby, 'spotify:playlist') and @role='button']")
    private WebElement playlistButtonInSideFrameElement;


    @FindBy(xpath = "//li[contains(@class, 'LU0q0itTx2613uiATSig')]//span[text()='Search']")
    private WebElement searchButtonToFindTrackElement;

    @FindBy(xpath = "//input[@data-testid=\"search-input\"]")
    private WebElement searchFieldToFindTrackElement;

    @FindBy(xpath = "//button[contains(@aria-label, 'Whitney')]/ancestor::div[@role='presentation']")
    private WebElement firstTrackToAddElement;


    @FindBy(xpath = "//div[@role='row' and @aria-rowindex='2']")
    private WebElement addedTrackNameElement;

    @FindBy(xpath = "//button[.//span[text()='Delete']]")
    private WebElement confirmDeletePlaylistButtonElement;

    @FindBy(xpath = "//span[normalize-space(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'))='create your first playlist']")
    private WebElement noPlaylistMessageElement;

    public MainPage ( WebDriver webDriver ) {
        super ( webDriver );

    }

    public void open () {
        waitForUrl ( "status" , true );
        webDriver.get ( LinksEnum.MAIN_PAGE_URL.getUrl ( ) );
    }


    public void addTrackToPlaylist ( String artist , String playlistName ) {
        int attempts = 0;
        int maxAttempts = 5;
        boolean trackAdded = false;

        while (attempts < maxAttempts && !trackAdded) {
            try {
                String xpathExpression = String.format ( "//span[contains(text(),'%s')]" , playlistName.trim ( ) );
                clickElement ( searchButtonToFindTrackElement );
                inputText ( searchFieldToFindTrackElement , artist );

                dragAndDropElementUntilUrlChanges (
                        waitForElementCondition ( ExpectedConditions.elementToBeClickable ( firstTrackToAddElement ) ,5) ,
                        getElementFromDynamicXpath ( xpathExpression )
                );

                trackAdded = true;
            } catch (Exception e) {
                webDriver.navigate ( ).refresh ( );
                attempts++;
            }
        }

        if (!trackAdded) {
            throw new IllegalStateException ( "Could not add the track to the playlist after " + maxAttempts + " attempts." );
        }
    }

    public String checkPlaylistForAddedTrack () {
        String trackName = getTextFromElement ( addedTrackNameElement );
        clickElement ( addedTrackNameElement );
        return trackName;
    }

    public String checkPlaylistForUpdatedDetails ( String playlistName ) {

        String xpathExpression = String.format ( "//span[contains(text(),'%s')]" , playlistName.trim ( ) );

        String updatedPlaylistName;

        while (true)
            try {
                updatedPlaylistName = getTextFromElement ( getElementFromDynamicXpath ( xpathExpression ) );
                break;

            } catch (Exception e) {
                webDriver.navigate ( ).refresh ( );
            }
        return updatedPlaylistName;
    }

    public void removeJustCreatedPlaylist () {
        int exceptionCounter = 0;
        while (true) {
            try {
                sendKeys ( playlistButtonInSideFrameElement , Keys.DELETE );
                sendKeys ( confirmDeletePlaylistButtonElement , Keys.ENTER );
                exceptionCounter = 0;

            } catch (Exception ignored) {
                exceptionCounter += 1;
                try {
                    noPlaylistMessageElement.getText ( );
                    break;
                } catch (Exception e) {
                    if (exceptionCounter > 5) {
                        webDriver.navigate ( ).refresh ( );
                        exceptionCounter = 0;
                    }

                }

            }


        }

    }


}

