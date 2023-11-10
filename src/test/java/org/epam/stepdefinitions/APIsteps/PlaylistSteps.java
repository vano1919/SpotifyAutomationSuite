package org.epam.stepdefinitions.APIsteps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.epam.apiClasses.DtoClasses.*;
import org.epam.apiClasses.playlistClient.PlaylistClient;
import org.epam.enumeration.credentialsEnum.UserCredentials;
import org.epam.enumeration.pageLinksEnum.LinksEnum;
import org.epam.stepdefinitions.BaseSteps;

import java.util.List;
import java.util.function.Predicate;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;

public class PlaylistSteps {

    private final String bearerToken = UserCredentials.TOKEN.getValue ( );
    private final String userId = UserCredentials.USERID.getValue ( );
    private Response response;
    private RetrievePlaylistDTO playlist;
    private final PlaylistClient playlistClient;

    public PlaylistSteps () {
        this.playlistClient = new PlaylistClient ( );
    }

    @Given("I have the necessary Spotify authorization")
    public void iHaveTheNecessarySpotifyAuthorization () {
        RestAssured.baseURI = LinksEnum.API_BASE_URI.getUrl ( );
    }

    @When("I create a new playlist named {string} with {string} and set its visibility to {string}")
    public void iCreateANewPlaylistNamedWithAndSetItsVisibilityTo ( String playlistName , String playlistDescription , String isPublic ) {
        CreatePlaylistDTO createPlaylistDTO = new CreatePlaylistDTO ( );

        createPlaylistDTO.setName ( playlistName );
        createPlaylistDTO.setDescription ( playlistDescription );
        createPlaylistDTO.setPublic ( Boolean.parseBoolean ( isPublic ) );

        Predicate<Response> playlistCreatedPredicate = r -> {
            RetrievePlaylistDTO retrievedPlaylist = r.as ( RetrievePlaylistDTO.class );
            return playlistName.equals ( retrievedPlaylist.getName ( ) ) &&
                    playlistDescription.equals ( retrievedPlaylist.getDescription ( ) );
        };

        response = playlistClient.sendRequestWithRetry (
                () -> given ( )
                        .header ( "Authorization" , "Bearer " + bearerToken )
                        .header ( "Content-Type" , "application/json" )
                        .body ( createPlaylistDTO )
                        .post ( "/users/" + userId + "/playlists" )
                        .thenReturn ( ) ,
                playlistCreatedPredicate
        );

        playlist = response.as ( RetrievePlaylistDTO.class );
        BaseSteps.putInStorage ( "playlistName" , playlistName );
        BaseSteps.putInStorage ( "PlaylistDescription" , playlistDescription );
        BaseSteps.putInStorage ( "isPublic" , String.valueOf ( isPublic ) );
        BaseSteps.putInStorage ( "playlistID" , playlist.getId ( ) );
    }

    @And("I should see the playlist details")
    public void iShouldSeeThePlaylistDetails () {
        playlist = response.as ( RetrievePlaylistDTO.class );
        assertEquals ( BaseSteps.getFromStorage ( "playlistName" ) , playlist.getName ( ) );
        assertEquals ( BaseSteps.getFromStorage ( "PlaylistDescription" ) , playlist.getDescription ( ) );
        assertEquals ( Boolean.parseBoolean ( BaseSteps.getFromStorage ( "isaPublic" ) ) , playlist.isPublic ( ) );
        BaseSteps.putInStorage ( "playlistID" , playlist.getId ( ) );
    }

    @And("I update the playlist with a new name {string} with {string} and set its visibility to {string}")
    public void iUpdateThePlaylistWithANewNameWithAndSetItsVisibilityTo ( String newName , String newDescription , String newIsPublic ) {
        String playlistId = BaseSteps.getFromStorage ( "playlistID" );
        CreatePlaylistDTO updatePlaylistDTO = new CreatePlaylistDTO ( );
        updatePlaylistDTO.setName ( newName );
        updatePlaylistDTO.setDescription ( newDescription );
        updatePlaylistDTO.setPublic ( Boolean.parseBoolean ( newIsPublic ) );

        try {
            Predicate<Response> playlistUpdatedPredicate = r -> {
                RetrievePlaylistDTO retrievedPlaylist = r.as ( RetrievePlaylistDTO.class );
                return newName.equals ( retrievedPlaylist.getName ( ) );
            };
        } catch (Exception ignored) {
        }

        Predicate<Response> playlistUpdatedPredicate = r -> r.getStatusCode ( ) == 200;


        response = playlistClient.sendRequestWithRetry (
                () -> given ( )
                        .header ( "Authorization" , "Bearer " + bearerToken )
                        .header ( "Content-Type" , "application/json" )
                        .body ( updatePlaylistDTO )
                        .put ( "/playlists/" + playlistId )
                        .thenReturn ( ) ,
                playlistUpdatedPredicate

        );

    }


    @When("I add tracks to the playlist with URIs {string}")
    public void iAddTracksToThePlaylistWithURIs ( String uris ) {
        String playlistId = BaseSteps.getFromStorage ( "playlistID" );
        AddTrackDTO addTrackDTO = new AddTrackDTO ( );
        addTrackDTO.setUris ( List.of ( uris.split ( "," ) ) );

        Predicate<Response> tracksAddedPredicate = r -> r.getStatusCode ( ) == 201;

        response = playlistClient.sendRequestWithRetry (
                () -> given ( )
                        .header ( "Authorization" , "Bearer " + bearerToken )
                        .header ( "Content-Type" , "application/json" )
                        .body ( addTrackDTO )
                        .post ( "/playlists/" + playlistId + "/tracks" ) ,
                tracksAddedPredicate
        );

        BaseSteps.putInStorage ( "addedTrackUris" , addTrackDTO.getUris ( ).toString ( ) );
    }

    @When("I remove tracks from the playlist with URIs {string}")
    public void iRemoveTracksFromThePlaylistWithURIs ( String uris ) {
        String playlistId = BaseSteps.getFromStorage ( "playlistID" );
        RemoveTrackDTO removeTrackDTO = new RemoveTrackDTO ( );
        removeTrackDTO.setUris ( List.of ( uris.split ( "," ) ) );

        Predicate<Response> tracksRemovedPredicate = r -> r.getStatusCode ( ) == 200;

        response = playlistClient.sendRequestWithRetry (
                () -> given ( )
                        .header ( "Authorization" , "Bearer " + bearerToken )
                        .header ( "Content-Type" , "application/json" )
                        .body ( removeTrackDTO )
                        .delete ( "/playlists/" + playlistId + "/tracks" ) ,
                tracksRemovedPredicate
        );

        BaseSteps.putInStorage ( "removedTrackUris" , removeTrackDTO.getUris ( ).toString ( ) );
    }

    @And("I confirm that tracks have been added to the playlist")
    public void iConfirmThatTracksHaveBeenAddedToThePlaylist () {
        AddTrackResponseDTO addTrackResponseDTO = response.getBody ( ).as ( AddTrackResponseDTO.class );
        assertNotNull ( "The snapshot_id should not be null" , addTrackResponseDTO.getSnapshot_id ( ) );
        assertFalse ( "The snapshot_id should not be empty" , addTrackResponseDTO.getSnapshot_id ( ).isEmpty ( ) );
    }

    @And("I confirm that tracks have been removed from the playlist")
    public void iConfirmThatTracksHaveBeenRemovedFromThePlaylist () {
        AddTrackResponseDTO addTrackResponseDTO = response.getBody ( ).as ( AddTrackResponseDTO.class );
        String playlistId = BaseSteps.getFromStorage ( "playlistID" );
        List<String> removedUris = List.of ( BaseSteps.getFromStorage ( "removedTrackUris" ).split ( "," ) );

        Predicate<Response> tracksRemovedConfirmationPredicate = r -> {
            List<String> trackUris = r.jsonPath ( ).getList ( "items.track.uri" );
            return removedUris.stream ( ).noneMatch ( trackUris::contains );
        };

        response = playlistClient.sendRequestWithRetry (
                () -> given ( )
                        .header ( "Authorization" , "Bearer " + bearerToken )
                        .get ( "/playlists/" + playlistId + "/tracks" ) ,
                tracksRemovedConfirmationPredicate
        );

        assertNotNull ( "The snapshot_id should not be null" , addTrackResponseDTO.getSnapshot_id ( ) );
        assertFalse ( "The snapshot_id should not be empty" , addTrackResponseDTO.getSnapshot_id ( ).isEmpty ( ) );
    }

    @Then("I check for status code {int}")
    public void iCheckForStatusCode ( int expectedStatusCode ) {
        assertEquals ( expectedStatusCode , response.getStatusCode ( ) );
    }


}


