package org.epam.stepdefinitions.APIsteps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.epam.enumeration.credentialsEnum.UserCredentials;
import org.epam.pages.LoginPage;
import org.epam.pages.MainPage;
import org.junit.Assert;

import static org.epam.stepdefinitions.BaseSteps.webDriver;

public class PlaylistUISteps {

    LoginPage loginPage = new LoginPage ( webDriver );
    MainPage mainPage = new MainPage ( webDriver );


    @Given("I am on the Spotify Login Page")
    public void openSpotifyLoginPage () {
        loginPage.open ( );
    }


    @When("I have valid credentials ValidUsername and ValidPassword user click the Log In button")
    public void theUserHasValidCredentialsValidUsernameAndValidPasswordUserClicksTheLogInButton () {
        String username = UserCredentials.EMAIL.getValue ( );
        String password = UserCredentials.PASSWORD.getValue ( );
        loginPage.loginInToSpotify ( username , password );

    }


    @And("I am on Main Page")
    public void theUserIsOnStatusPage () {

        mainPage.open ( );

    }


    @And("I search and add a track by {string} to the playlist {string}")
    public void theUserSearchesAndAddsATrackByToThePlaylist ( String singer , String playlistNam ) {
        mainPage.addTrackToPlaylist ( singer , playlistNam );
    }


    @Then("The added song by Whitney Houston should be displayed in the playlist.")
    public void theAddedSongByWhitneyHoustonShouldBeDisplayedInThePlaylist () {
        String trackName = mainPage.checkPlaylistForAddedTrack ( );
        Assert.assertTrue ( "The track by Whitney Houston should be in the playlist" , trackName.contains ( "Whitney Houston" ) );
    }

    @And("I Logs in and goes to the Spotify Main Page")
    public void userLogsInAndGoesToTheSpotifyMainPage () {
        loginPage.open ( );
        String username = UserCredentials.EMAIL.getValue ( );
        String password = UserCredentials.PASSWORD.getValue ( );
        loginPage.loginInToSpotify ( username , password );
        mainPage.open ( );
    }


    @Then("I should see updated {string} Playlist details.")
    public void iShouldSeeUpdatedPlaylistDetails ( String playlistName ) {
        String updatedDescription = mainPage.checkPlaylistForUpdatedDetails (playlistName );
        Assert.assertEquals ( playlistName.trim ().toLowerCase (  ), updatedDescription.trim ( ).toLowerCase (  ));
    }

    @Then("I remove all playlist")
    public void iRemoveAllPlaylist () {
        mainPage.removeJustCreatedPlaylist ( );
    }
}