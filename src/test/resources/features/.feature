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

    #get delivery managers from vertical and classification
  Scenario: Retrieving Delivery Managers by Verticals and Classification
    Given the client provides a list of verticals and classifications
    When the delivery managers are retrieved by verticals and classifications
    Then the delivery managers associated with the verticals and classifications are returned

     #get accounts by delivery managers
  Scenario: Retrieve accounts for a list of delivery managers
    Given the client provides a list of delivery managers
    When the accounts are retrieved by delivery managers
    Then the accounts associated with the delivery managers are returned

    #get accounts by delivery managers and classification
  Scenario: Retrieving Accounts by Delivery Managers and Classification
    Given the client provides a list of delivery managers and classifications
    When the accounts are retrieved by delivery managers and classifications
    Then the accounts associated with the delivery managers and classifications are returned

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

    #get projects by project managers and classification
  Scenario: Retrieving Projects by Project Managers and Classification
    Given the client provides a list of project managers and classifications
    When the projects are retrieved by project managers and classifications
    Then the projects associated with the project managers and classifications are returned
#
#    #get all data by criteria
#  Scenario: Retrieving Financial Data by Criteria
#    Given the client provides criteria for financial data retrieval
#    When the financial data is retrieved based on the provided criteria
#    Then the financial data matching the criteria is returned