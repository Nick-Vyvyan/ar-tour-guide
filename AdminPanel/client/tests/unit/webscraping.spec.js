const { expect } = require("chai");
const { parseWeb } = require("./../../src/scripts/PageParser");

describe("webparsing", () => {
  it("returns correct data for Arntzen Hall", () => {
    const returnedData = parseWeb(
      "./tests/unit/webscrapingTestAssets/AHTest.html"
    );
    const expectedData = {
      buildingName: "Arntzen Hall",
      buildingTypes: ["Academic", "Campus Services"],
      floorplanLink: "https://cpd.wwu.edu/files/2020-04/AH.pdf",
      departmentsOffices: [
        "Anthropology",
        "Environmental Studies",
        "Urban and Environmental Planning and Policy",
        "Political Science",
        "Sociology",
      ],
      computerLabs: [
        "AH 01 (Sociology)",
        "AH 02",
        "AH 05",
        "AH 16 (Spatial Analysis Lab)",
      ],
      dining: [["The Atrium", "https://wwu.campusdish.com/LocationsAndMenus"]],
      genderNeutralRestrooms: [],
      accessibilityInfo: [
        "Accessible parking to the east (Lot 17 G)",
        "Button activated entrances are located on the west side of the building",
        "Elevators provide access to all levels",
        "Accessible restrooms are located on the Concourse level (Basement)",
        "Access to the Environmental Studies building is provided via the basement connection"
      ]
    };
    expect(returnedData).to.deep.equal(expectedData);
  });

  it("returns correct data for Bond Hall", () => {
    const returnedData = parseWeb(
      "./tests/unit/webscrapingTestAssets/BHTest.html"
    );
    const expectedData = {
      buildingName: "Bond Hall",
      buildingTypes: ["Academic"],
      floorplanLink: "https://cpd.wwu.edu/files/2020-04/BH.pdf",
      departmentsOffices: [],
      computerLabs: ["BH 319"],
      dining: [],
      genderNeutralRestrooms: [
        "157",
        "207",
        "307",
        "403B"
      ],
      accessibilityInfo: [
        "Accessible parking to the east or west",
        "Button activated entrances are located on the northeast side of building",
        "An elevator offers access to all floors except the 2nd and 3rd half floors (both half floors are accessible via ramps or lifts)",
        "Accessible restrooms are located on the Mezzanine level",
        "Many doors in this building are 2' 8\" wide"
      ]
    };
    expect(returnedData).to.deep.equal(expectedData);
  });

  it("returns correct data for Performing Arts Center", () => {
    const returnedData = parseWeb(
      "./tests/unit/webscrapingTestAssets/PATest.html"
    );
    const expectedData = {
      buildingName: "Performing Arts Center",
      buildingTypes: ["Academic", "Events"],
      floorplanLink: "https://cpd.wwu.edu/files/2020-01/PA.pdf",
      departmentsOffices: [
        "Box Office",
        "College of Fine and Performing Arts",
        "Music",
        "Music Library",
        "Dance",
      ],
      computerLabs: [],
      dining: [],
      genderNeutralRestrooms: [
        "150A",
        "151A",
        "390"
      ],
      accessibilityInfo: [
        "Accessible parking on the southwest side of the building",
        "Accessible rest rooms"
      ]
    };
    expect(returnedData).to.deep.equal(expectedData);
  });

  it("returns correct data for Ridgeway Delta", () => {
    const returnedData = parseWeb(
      "./tests/unit/webscrapingTestAssets/RDTest.html"
    );
    const expectedData = {
      buildingName: "Ridgeway Delta",
      buildingTypes: ["University Residence"],
      floorplanLink: "https://cpd.wwu.edu/files/2020-01/RD.pdf",
      departmentsOffices: [],
      computerLabs: ["See Ridgeway Commons and Ridgeway Sigma"],
      dining: [["Ridgeway Commons", "https://www.wwu.edu/building/rc"]],
      genderNeutralRestrooms: [
        "2"
      ],
      accessibilityInfo: [
        "This building is not wheelchair accessible.",
        "Accessible parking to west (Lot 15R)."
      ]
    };
    expect(returnedData).to.deep.equal(expectedData);
  });

  it("returns correct data for Viking Union", () => {
    const returnedData = parseWeb(
      "./tests/unit/webscrapingTestAssets/VUTest.html"
    );
    const expectedData = {
      buildingName: "Viking Union",
      buildingTypes: ["Campus Services", "Events"],
      floorplanLink: "https://cpd.wwu.edu/files/2020-01/VU.pdf",
      departmentsOffices: [
        "Associated Students",
        "Bookstore, Associated Students",
        "Ethnic Student Center",
        "LGBTQ+ Western",
        "Office of Student Life",
        "Off Campus Living",
        "Outdoor Center",
        "Student Advocacy and Identity Resource Center",
      ],
      computerLabs: [],
      dining: [
        [
          "Viking Union Market",
          "https://wwu.campusdish.com/LocationsAndMenus/VikingUnionMarket",
        ],
        [
          "Viking Union Cafe",
          "https://wwu.campusdish.com/LocationsAndMenus/VikingUnionCafe",
        ],
        [
          "The Underground Coffeehouse",
          "https://wwu.campusdish.com/LocationsAndMenus/UndergroundCoffeehouse",
        ],
        ["Vendor's Row", "https://vu.wwu.edu/dining-spaces"],
      ],
      genderNeutralRestrooms: [
        "351",
        "353",
        "714",
        "715",
        "716",
        "717"
      ],
      accessibilityInfo: [
        "Automatic doors at High St. and the VU Plaza on the 6th floor and on the 1st floor at the Garden St. entrance.",
        "Accessible restrooms on floors 3, 4, 5 and 6.",
        "Accessible parking on the north side of the building (Lot 6V)."
      ]
    };
    expect(returnedData).to.deep.equal(expectedData);
  });
});
