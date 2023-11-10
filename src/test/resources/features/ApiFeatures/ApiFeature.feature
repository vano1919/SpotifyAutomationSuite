Feature: Playlist Management

  Background:
    Given I have the necessary Spotify authorization
    When I create a new playlist named "Summer Hits" with "Test Playlist Description" and set its visibility to "false"

  Scenario: Create a new playlist

    Then I check for status code 201
    And I should see the playlist details

  Scenario: Update an existing playlist's details
    And I should see the playlist details
    And I update the playlist with a new name "Summer Hits Updated" with "Updated Test Playlist Description" and set its visibility to "true"
    Then I check for status code 200

  Scenario: Add tracks to the existing playlist

    And I should see the playlist details
    When I add tracks to the playlist with URIs "spotify:track:4iV5W9uYEdYUVa79Axb7Rh,spotify:track:1301WleyT98MSxVHPZCA6M"
    Then I check for status code 201
    And I confirm that tracks have been added to the playlist

  Scenario: Remove tracks from the existing playlist
    And I should see the playlist details
    When I add tracks to the playlist with URIs "spotify:track:4iV5W9uYEdYUVa79Axb7Rh,spotify:track:1301WleyT98MSxVHPZCA6M"
    When I remove tracks from the playlist with URIs "spotify:track:4iV5W9uYEdYUVa79Axb7Rh"
    Then I check for status code 200
    And I confirm that tracks have been removed from the playlist
