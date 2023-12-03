package org.epam.stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.epam.apiClasses.dtoClasses.*;
import org.epam.apiClasses.playlistClient.PlaylistClient;
import org.epam.enumeration.credentialsEnum.UserCredentials;
import org.epam.enumeration.pageLinksEnum.LinksEnum;

import java.util.List;
import java.util.function.Predicate;

import static org.junit.Assert.*;

public class PlaylistSteps {

    private PlaylistClient playlistClient;
    private Response response;
    private RetrievePlaylistDTO playlist;
    private final String userId = UserCredentials.USERID.getValue ( );

    @Given("I have the necessary Spotify authorization")
    public void iHaveTheNecessarySpotifyAuthorization () {
        RestAssured.baseURI = LinksEnum.API_BASE_URI.getUrl ( );
        String bearerToken = UserCredentials.TOKEN.getValue ( );
        playlistClient = new PlaylistClient ( bearerToken );
    }

    @When("I create a new playlist named {string} with {string} and set its visibility to {string}")
    public void iCreateANewPlaylist ( String name , String description , String isPublic ) {
        CreatePlaylistDTO createPlaylist = new CreatePlaylistDTO ( name , description , Boolean.parseBoolean ( isPublic ) );

        Predicate<Response> isCreated = response -> {
            RetrievePlaylistDTO retrieved = response.as ( RetrievePlaylistDTO.class );
            return name.equals ( retrieved.getName ( ) ) && description.equals ( retrieved.getDescription ( ) );
        };

        response = playlistClient.sendRequestWithRetry (
                () -> playlistClient.createPlaylistRequest ( userId , createPlaylist ).get ( ) ,
                isCreated
        );

        playlist = response.as ( RetrievePlaylistDTO.class );
        BaseSteps.putInStorage ( "playlistName" , name );
        BaseSteps.putInStorage ( "PlaylistDescription" , description );
        BaseSteps.putInStorage ( "isPublic" , isPublic );
        BaseSteps.putInStorage ( "playlistID" , playlist.getId ( ) );
    }

    @And("I should see the playlist details")
    public void iShouldSeeThePlaylistDetails () {
        playlist = response.as ( RetrievePlaylistDTO.class );
        assertEquals ( BaseSteps.getFromStorage ( "playlistName" ) , playlist.getName ( ) );
        assertEquals ( BaseSteps.getFromStorage ( "PlaylistDescription" ) , playlist.getDescription ( ) );
        assertEquals ( Boolean.parseBoolean ( BaseSteps.getFromStorage ( "isPublic" ) ) , playlist.isPublic ( ) );
        BaseSteps.putInStorage ( "playlistID" , playlist.getId ( ) );
    }

    @And("I update the playlist with a new name {string} with {string} and set its visibility to {string}")
    public void iUpdateThePlaylist ( String newName , String newDescription , String newIsPublic ) {
        String playlistId = BaseSteps.getFromStorage ( "playlistID" );
        CreatePlaylistDTO updatePlaylist = new CreatePlaylistDTO ( newName , newDescription , Boolean.parseBoolean ( newIsPublic ) );

        Predicate<Response> isUpdated = r -> r.getStatusCode ( ) == 200;

        response = playlistClient.sendRequestWithRetry (
                () -> playlistClient.updatePlaylistRequest ( playlistId , updatePlaylist ).get ( ) ,
                isUpdated
        );
    }

    @When("I add tracks to the playlist with URIs {string}")
    public void iAddTracksToThePlaylist ( String uris ) {
        String playlistId = BaseSteps.getFromStorage ( "playlistID" );
        AddTrackDTO addTracks = new AddTrackDTO ( List.of ( uris.split ( "," ) ) );

        Predicate<Response> tracksAdded = r -> r.getStatusCode ( ) == 201;

        response = playlistClient.sendRequestWithRetry (
                () -> playlistClient.addTracksToPlaylistRequest ( playlistId , addTracks ).get ( ) ,
                tracksAdded
        );

        BaseSteps.putInStorage ( "addedTrackUris" , addTracks.getUris ( ).toString ( ) );
    }

    @When("I remove tracks from the playlist with URIs {string}")
    public void iRemoveTracksFromThePlaylist ( String uris ) {
        String playlistId = BaseSteps.getFromStorage ( "playlistID" );
        RemoveTrackDTO removeTracks = new RemoveTrackDTO ( List.of ( uris.split ( "," ) ) );

        Predicate<Response> tracksRemoved = r -> r.getStatusCode ( ) == 200;

        response = playlistClient.sendRequestWithRetry (
                () -> playlistClient.removeTracksFromPlaylistRequest ( playlistId , removeTracks ).get ( ) ,
                tracksRemoved
        );

        BaseSteps.putInStorage ( "removedTrackUris" , removeTracks.getUris ( ).toString ( ) );
    }

    @And("I confirm that tracks have been added to the playlist")
    public void iConfirmTracksAdded () {
        AddTrackResponseDTO addTrackResponse = response.getBody ( ).as ( AddTrackResponseDTO.class );

        assertNotNull ( "The snapshot_id should not be null" , addTrackResponse.getSnapshot_id ( ) );
        assertFalse ( "The snapshot_id should not be empty" , addTrackResponse.getSnapshot_id ( ).isEmpty ( ) );
    }

    @And("I confirm that tracks have been removed from the playlist")
    public void iConfirmTracksRemoved () {
        AddTrackResponseDTO addTrackResponse = response.getBody ( ).as ( AddTrackResponseDTO.class );
        assertNotNull ( "The snapshot_id should not be null" , addTrackResponse.getSnapshot_id ( ) );
        assertFalse ( "The snapshot_id should not be empty" , addTrackResponse.getSnapshot_id ( ).isEmpty ( ) );
        String playlistId = BaseSteps.getFromStorage ( "playlistID" );
        List<String> removedUris = List.of ( BaseSteps.getFromStorage ( "removedTrackUris" ).split ( "," ) );

        Predicate<Response> tracksRemovedConfirmation = r -> {
            List<String> trackUris = r.jsonPath ( ).getList ( "items.track.uri" );
            return removedUris.stream ( ).noneMatch ( trackUris::contains );
        };

        response = playlistClient.sendRequestWithRetry ( () -> playlistClient.getPlaylistTracksRequest ( playlistId ).get ( ) , tracksRemovedConfirmation );
    }

    @Then("I check for status code {int}")
    public void iCheckForStatusCode ( int expectedStatusCode ) {
        assertEquals ( expectedStatusCode , response.getStatusCode ( ) );
    }


}

