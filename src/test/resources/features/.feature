#get financial year by projects
Feature: Retrieving Financial Years by Project
  Scenario: Retrieve financial years for a list of projects
    Given the client provides a list of projects
    When the financial year is retrieved by project
    Then the financial years for the projects are returned

    #get classifications by verticals
  Scenario: Retrieving Classifications by Verticals
    Given the client provides a list of verticals
    When the classifications are retrieved by verticals
    Then the classifications associated with the verticals are returned

     #get delivery managers from classification
  Scenario: Retrieving Delivery Managers by Classifications
    Given the client provides a list of classifications
    When the delivery managers are retrieved by classifications
    Then the delivery managers associated with the classifications are returned

     #get accounts by delivery managers
  Scenario: Retrieve accounts for a list of delivery managers
    Given the client provides a list of delivery managers
    When the accounts are retrieved by delivery managers
    Then the accounts associated with the delivery managers are returned

    # get project managers by accounts
  Scenario: Retrieve project managers for a list of accounts
    Given the client provides a list of accounts
    When the project managers are retrieved by account names
    Then the project managers associated with the accounts are returned

    #get project managers by account and classification
  Scenario: Retrieving Project Managers by Account and Classification
    Given the client provides a list of accounts and classifications
    When the project managers are retrieved by accounts and classifications
    Then the project managers associated with the accounts and classifications are returned

    #get projects by project managers
  Scenario: Retrieve projects for a list of project managers
    Given the client provides a list of project managers
    When the projects are retrieved by project managers
    Then the projects associated with the project managers are returned