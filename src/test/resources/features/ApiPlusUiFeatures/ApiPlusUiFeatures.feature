Feature: Playlist Management Using UI Plus API

  Background:
    Given I have the necessary Spotify authorization
    When I create a new playlist named "New Playlist" with "New playlist description" and set its visibility to "false"
    Then I check for status code 201
    And I should see the playlist details

  Scenario: Add a track to the playlist
    Given I am on the Spotify Login Page
    When I have valid credentials ValidUsername and ValidPassword user click the Log In button
    And I am on Main Page
    And I search and add a track by "Whitney Elizabeth Houston" to the playlist "New Playlist"
    Then The added song by Whitney Houston should be displayed in the playlist.

  Scenario: Update an existing playlist's details
    And I update the playlist with a new name "Summer Hits Updated" with "Updated Test Playlist Description" and set its visibility to "true"
    Then I check for status code 200
    And I Logs in and goes to the Spotify Main Page
    Then I should see updated "Summer Hits Updated" Playlist details.
    Then I remove all playlist




