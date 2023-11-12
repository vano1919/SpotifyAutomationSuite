package org.epam.pages;

import org.epam.enumeration.pageLinksEnum.LinksEnum;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class MainPage extends BasePage {


    @FindBy(xpath = "//button[contains(@class, 'Button-sc-1dqy6lx-0') and @aria-label='Create playlist or folder']")
    private WebElement playlistCreatorButtonElement;

    @FindBy(xpath = "//span[text()='Create a new playlist']")
    private WebElement createPlaylistButtonElement;

    @FindBy(xpath = "//button[contains(@aria-label, 'Edit details')]")
    private WebElement playlistNameInPlaylistElement;

    @FindBy(xpath = "//input[@data-testid='playlist-edit-details-name-input']")
    private WebElement editPlaylistFieldElement;

    @FindBy(xpath = "//p[contains(@id, 'listrow-title-spotify:playlist:') and contains(., 'Playlist')]")
    private WebElement playlistNameInMainFrameElement;

    @FindBy(xpath = "//p[contains(@id, 'spotify:playlist')]")
    private WebElement playlistNameInSideFrameElement;


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

    @FindBy(xpath = "//h1[contains(text(), \"find something for your playlis\")]")
    private WebElement addedTrackStatusElement;

    @FindBy(xpath = "//button[contains(., 'Remove from this playlist')]")
    private WebElement removedTrackButtonElement;


    @FindBy(xpath = "//button[.//span[text()='Delete']]")
    private WebElement confirmDeletePlaylistButtonElement;

    @FindBy(xpath = "//span[normalize-space(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'))='create your first playlist']")
    private WebElement noPlaylistMessageElement;


    public MainPage ( WebDriver webDriver ) {
        super ( webDriver );

    }

    public void open () {
        webDriver.get ( LinksEnum.MAIN_PAGE_URL.getUrl ( ) );
    }

    public void createPlaylist () {
        final int MAX_RETRIES = 10;  // Let's limit the number of retries to avoid infinite loops.
        int retries = 0;

        clickElement ( playlistCreatorButtonElement );
        clickElement ( createPlaylistButtonElement );

        while (retries < MAX_RETRIES) {
            if (!waitForUrl ( "playlist" ,true,5)) {
                // The URL doesn't contain "playlist", so let's try to create it.
                clickElement ( playlistCreatorButtonElement );
                clickElement ( createPlaylistButtonElement );
                retries++;
            } else {
                // The URL contains "playlist" refresh, and so we're done.
                break;
            }
        }

    }

    public boolean isPlaylistAdded () {
        waitForUrl ( "playlist" , true ,30);

        String playlistNameFromMainFrame = getTextFromElement ( playlistNameInMainFrameElement );
        String xpathExpression = String.format ( "//span[contains(text(),'%s')]" , playlistNameFromMainFrame );
        return !getTextFromElement ( getElementFromDynamicXpath ( xpathExpression ) ).isBlank ( );
    }


    public String editPlaylist ( String newName ) {
        clickElement ( playlistNameInPlaylistElement );
        inputText ( editPlaylistFieldElement , newName );
        return getTextFromElement ( playlistNameInPlaylistElement );
    }

    public void addTrackToPlaylist ( String artist , String playlistName ) {
        int attempts = 0;
        int maxAttempts = 15;
        boolean trackAdded = false;

        while (attempts < maxAttempts && !trackAdded) {
            try {
                String xpathExpression = String.format ( "//span[contains(text(),'%s')]" , playlistName.trim ( ) );
                clickElement ( searchButtonToFindTrackElement );
                inputText ( searchFieldToFindTrackElement , artist );

                dragAndDropElementUntilUrlChanges (
                        waitForElementCondition ( ExpectedConditions.elementToBeClickable ( firstTrackToAddElement ) , 5 ) ,
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
        return getTextFromElement ( addedTrackNameElement );
    }

    public void removeTrackFromThePlayList () {
        waitForUrl ( "playlist" , true ,30);
        getTextFromElement ( addedTrackNameElement );
        contextClick ( addedTrackNameElement );
        clickElement ( removedTrackButtonElement );
    }


    public String checkPlaylistForRemovedTrack () {
        return getTextFromElement ( addedTrackStatusElement );
    }


    public void getJustCreatedPlaylistName () {
        getTextFromElement ( playlistNameInSideFrameElement );
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

    public boolean removeJustCreatedPlaylist () {
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
        return true;
    }

}

