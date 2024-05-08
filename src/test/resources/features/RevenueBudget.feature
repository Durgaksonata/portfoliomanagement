#Retrieving the list of verticals
Feature: Retrieving RevenueBudgetSummaryMethods
  Scenario: Successful retrieval of vertical list
    Given the revenue data exists in the database
    When the client requests to get the vertical list
    Then the client receives a list of distinct verticals

  #Classificarion by vertical
  Scenario: Successful retrieval of classification names by vertical
    Given the client has a list of vertical names
      | VerticalName1 |
      | VerticalName2 |
    When the client sends a POST request to the "/classificationbyvertical" endpoint with the vertical list
    Then the client should receive a successful response with status code 200


# Retrieving specific financial year data
  Scenario: Retrieving revenue data for a valid financial year
    Given a valid financial year
    When a request is made to retrieve revenue data for that year
    Then the revenue data for the specified year is returned
