Scenario: A prospective student wants to know the departments that are located inside a building.
  Given Tom has found a building on campus
  And he has brought up the menu for the building information
  When he reads through the content of the menu
  Then the departments inside the building should be listed