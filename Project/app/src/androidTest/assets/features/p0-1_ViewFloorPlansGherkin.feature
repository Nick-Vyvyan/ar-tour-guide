Scenario: A prospective student wants to view the floor plans of a building.
  Given Katie's smartphone camera is positioned to be looking at a building
  And she sees the name of the building displayed on the screen
  When she clicks on the tooltip of the building name
  And clicks on the button in the menu that links to the building's floor plans
  Then the floor plans should be displayed on her screen