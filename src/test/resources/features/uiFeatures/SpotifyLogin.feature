Feature: Spotify Web Application Negative Login Testing

  Scenario: Login with empty credentials
    Given I am on the Spotify Login Page For Negative Tests
    When I leave both credentials empty and click the Log In button
    Then I should see error messages for both fields

  Scenario: Login with incorrect credentials
    Given I am on the Spotify Login Page For Negative Tests
    When I type incorrect credentials and click the Log In button
    Then I should see an error message for incorrect credentials
