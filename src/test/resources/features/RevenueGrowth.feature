#Get revenue growth data by specific financial year
Feature: Retrieving data by financial year
Scenario: Retrieving revenue growth data for a valid financial year
Given a valid financial year is provided
When the user requests revenue growth data for that year
Then the system should return the revenue growth data for that year

#Get revenue growth data by vertical and classifications
Scenario: Data retrieval with multiple verticals and classifications
Given a request is made with multiple verticals and classifications
When the request is processed
Then the response contains data with all the provided verticals and classifications


