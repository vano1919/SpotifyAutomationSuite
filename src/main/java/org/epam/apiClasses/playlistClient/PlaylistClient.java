package org.epam.apiClasses.playlistClient;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.epam.apiClasses.dtoClasses.AddTrackDTO;
import org.epam.apiClasses.dtoClasses.CreatePlaylistDTO;
import org.epam.apiClasses.dtoClasses.RemoveTrackDTO;

import java.util.function.Predicate;
import java.util.function.Supplier;

import static io.restassured.RestAssured.given;

public class PlaylistClient {
    public static final int MAX_RETRIES = 20;
    private final String bearerToken;

    public PlaylistClient ( String bearerToken ) {
        this.bearerToken = bearerToken;
    }

    private RequestSpecification requestSpecification () {
        return given ( )
                .header ( "Authorization" , "Bearer " + bearerToken )
                .contentType ( ContentType.JSON );
    }

    private Response sendRequest ( Supplier<Response> requestSupplier ) {
        return requestSupplier.get ( );
    }

    public Response sendRequestWithRetry ( Supplier<Response> requestSupplier , Predicate<Response> responsePredicate ) {
        Response response;
        for (int retries = 0; retries < MAX_RETRIES; retries++) {
            response = sendRequest ( requestSupplier );
            if (responsePredicate.test ( response )) {
                return response;
            } else {
                System.out.println ( "Response did not meet the expected criteria, retrying..." );
            }
        }
        throw new RuntimeException ( "Request failed after " + MAX_RETRIES + " retries" );
    }

    public Supplier<Response> createPlaylistRequest ( String userId , CreatePlaylistDTO playlist ) {
        return () -> post ( "/users/" + userId + "/playlists" , playlist );
    }

    public Supplier<Response> updatePlaylistRequest ( String playlistId , CreatePlaylistDTO playlist ) {
        return () -> put ( "/playlists/" + playlistId , playlist );
    }

    public Supplier<Response> addTracksToPlaylistRequest ( String playlistId , AddTrackDTO tracks ) {
        return () -> post ( "/playlists/" + playlistId + "/tracks" , tracks );
    }

    public Supplier<Response> removeTracksFromPlaylistRequest ( String playlistId , RemoveTrackDTO tracks ) {
        return () -> delete ( "/playlists/" + playlistId + "/tracks" , tracks );
    }

    public Supplier<Response> getPlaylistTracksRequest ( String playlistId ) {
        return () -> get ( "/playlists/" + playlistId + "/tracks" );
    }

    private Response post ( String path , Object body ) {
        return requestSpecification ( )
                .body ( body )
                .post ( path )
                .andReturn ( );
    }

    private Response put ( String path , Object body ) {
        return requestSpecification ( )
                .body ( body )
                .put ( path )
                .andReturn ( );
    }

    private Response delete ( String path , Object body ) {
        return requestSpecification ( )
                .body ( body )
                .delete ( path )
                .andReturn ( );
    }

    private Response get ( String path ) {
        return requestSpecification ( )
                .get ( path )
                .andReturn ( );
    }
}