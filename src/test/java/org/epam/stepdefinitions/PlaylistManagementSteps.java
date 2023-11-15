package org.epam.stepdefinitions;

import dev.failsafe.internal.util.Assert;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.epam.pages.MainPage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PlaylistManagementSteps {

    MainPage mainPage = new MainPage ( BaseSteps.webDriver );

    @And("I am on Main Page")
    public void iAmOnStatusPage () {
        mainPage.open ( );
    }

    @When("I create a playlist")
    public void iCreateAPlaylist () {
        mainPage.createPlaylist ( );
    }

    @Then("A new playlist should be added to the playlist list")
    public void aNewPlaylistShouldBeAddedToThePlaylistList () {
        boolean successStatus = mainPage.isPlaylistAdded ( );
        Assert.isTrue ( successStatus , "The new playlist added to the playlist list but sidebar did not updated" );
    }

    @When("I edit the playlist name to {string} and save")
    public void iEditThePlaylistNameToAndSave ( String PlaylistName ) {
        String editedPlaylist = mainPage.editPlaylist ( PlaylistName );
        BaseSteps.putInStorage ( "playlistName" , editedPlaylist );
    }

    @Then("The edited playlist name should be displayed")
    public void theEditedPlaylistNameShouldBeDisplayed () {
        String editedPlaylist = BaseSteps.getFromStorage ( "playlistName" );
        org.junit.Assert.assertNotNull ( "The edited playlist name should be displayed" , editedPlaylist );
        org.junit.Assert.assertTrue ( "The edited playlist should be added to the playlist list" , mainPage.isPlaylistAdded ( ) );
    }

    @Given("I have added a playlist")
    public void iHaveAddedAPlaylist () {
        mainPage.getJustCreatedPlaylistName ( );
    }

    @When("I remove the playlist")
    public void iRemoveAPlaylist () {
        boolean playlistsStatus = mainPage.removeJustCreatedPlaylist ( );
        BaseSteps.putInStorage ( "playlistsStatus" , String.valueOf ( playlistsStatus ) );
    }

    @Then("The playlist should be removed from the playlists.")
    public void thePlaylistShouldBeRemovedFromThePlaylists () {
        boolean playlistsStatus = Boolean.parseBoolean ( BaseSteps.getFromStorage ( "playlistsStatus" ) );
        assertTrue ( playlistsStatus );
    }

    @And("I search and add a track by {string} to the playlist {string}")
    public void iSearchAndAddATrackByToThePlaylist ( String singer , String playlistNam ) {
        mainPage.addTrackToPlaylist ( singer , playlistNam );
    }

    @Then("The added song by Whitney Houston should be displayed in the playlist.")
    public void theAddedSongByWhitneyHoustonShouldBeDisplayedInThePlaylist () {
        String trackName = mainPage.checkPlaylistForAddedTrack ( );
        assertTrue ( "The track by Whitney Houston should be in the playlist" , trackName.contains ( "Whitney Houston" ) );
    }

    @Then("I should see updated {string} Playlist details.")
    public void iShouldSeeUpdatedPlaylistDetails ( String playlistName ) {
        String updatedDescription = mainPage.checkPlaylistForUpdatedDetails ( playlistName );
        assertEquals ( playlistName.trim ( ).toLowerCase ( ) , updatedDescription.trim ( ).toLowerCase ( ) );
    }

    @When("I remove the track")
    public void iRemoveTheTrack () {
        mainPage.removeTrackFromThePlayList ( );
    }

    @Then("The song status {string} should not be displayed in the playlist.")
    public void theSongStatusShouldNotBeDisplayedInThePlaylist ( String expectedStatusText ) {
        String actualTrackStatus = mainPage.checkPlaylistForRemovedTrack ( );
        assertEquals ( expectedStatusText , actualTrackStatus );
    }

}

