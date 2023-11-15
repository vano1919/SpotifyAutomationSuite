Feature: Spotify Web Application Playlist Management

  Background:
    Given I am on the Spotify Login Page
    When I enter valid credentials ValidUsername and ValidPassword and click the Log In button
    Then My Profile Name should be displayed
    And I am on Main Page
    When I create a playlist
    And A new playlist should be added to the playlist list

  Scenario: Create a new playlist
    Then A new playlist should be added to the playlist list

  Scenario: Edit Playlist Details
    When I edit the playlist name to "My Favorite Playlist" and save
    Then The edited playlist name should be displayed

