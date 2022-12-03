Scenario: A prospective student wants to know the name of a nearby building.
  Given John is using his smartphone camera to view the WWU campus in the app
  And he is close to a building
  When the building enters his camera's field of vision
  Then the screen displays a tooltip with the name of the building
  And positions it near the center of the building to be clicked