Feature: Spotify Web Application Track Management

  Background:
    Given I am on the Spotify Login Page
    When I enter valid credentials ValidUsername and ValidPassword and click the Log In button
    Then My Profile Name should be displayed
    And I am on Main Page
    When I create a playlist
    And A new playlist should be added to the playlist list
    And I search and add a track by "Whitney Elizabeth Houston" to the playlist "My Playlist"

  Scenario: Add a track to the playlist
    Then The added song by Whitney Houston should be displayed in the playlist.

  Scenario: Remove a track from the playlist
    When I remove the track
    Then The song status "Let's find something for your playlist" should not be displayed in the playlist.

  Scenario: Remove playlist from the playlists
    Given I have added a playlist
    When I remove the playlist
    Then The playlist should be removed from the playlists.
