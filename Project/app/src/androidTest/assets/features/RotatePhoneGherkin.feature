Scenario: A prospective student turns their phone to view another side of campus.
  Given Sean has already seen the buildings he is currently looking at
  When he turns his phone away from its current location
  And towards at least one other building
  Then the tooltips and information for the new buildings should appear on his screen