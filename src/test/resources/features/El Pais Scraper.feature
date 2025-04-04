@ElPaisScraper
Feature: El Pais Opinion Article Scraper and Translator

  Background:
    Given I launch the El Pais website

  @language-check
  Scenario: Verify El Pais site is in Spanish
    Given I verify the El Pais website is in Spanish

  @fetch
  Scenario: Fetch 5 Opinion articles with titles and content
    When  I navigate to the Opinion section and fetch 5 articles

  @translate
  Scenario: Translate the article titles to English
    When  I navigate to the Opinion section and fetch 5 articles
    And   I translate article titles

  @analyze
  Scenario: Analyze repeated words in translated titles
    When  I navigate to the Opinion section and fetch 5 articles
    And   I translate article titles
    Then  I analyze repeated words in translated titles
