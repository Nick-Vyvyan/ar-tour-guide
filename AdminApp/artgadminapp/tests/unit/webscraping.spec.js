const { expect } = require("chai");
const { parse } = require("./../../src/scripts/PageParser");

describe("webparsing", () => {
  it("returns correct data for Arntzen Hall", () => {
    const returnedData = parse(
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
    };
    expect(returnedData).to.deep.equal(expectedData);
  });

  it("returns correct data for Bond Hall", () => {
    const returnedData = parse(
      "./tests/unit/webscrapingTestAssets/BHTest.html"
    );
    const expectedData = {
      buildingName: "Bond Hall",
      buildingTypes: ["Academic"],
      floorplanLink: "https://cpd.wwu.edu/files/2020-04/BH.pdf",
      departmentsOffices: [],
      computerLabs: ["BH 319"],
      dining: [],
    };
    expect(returnedData).to.deep.equal(expectedData);
  });

  it("returns correct data for Performing Arts Center", () => {
    const returnedData = parse(
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
    };
    expect(returnedData).to.deep.equal(expectedData);
  });

  it("returns correct data for Ridgeway Delta", () => {
    const returnedData = parse(
      "./tests/unit/webscrapingTestAssets/RDTest.html"
    );
    const expectedData = {
      buildingName: "Ridgeway Delta",
      buildingTypes: ["University Residence"],
      floorplanLink: "https://cpd.wwu.edu/files/2020-01/RD.pdf",
      departmentsOffices: [],
      computerLabs: ["See Ridgeway Commons and Ridgeway Sigma"],
      dining: [["Ridgeway Commons", "https://www.wwu.edu/building/rc"]],
    };
    expect(returnedData).to.deep.equal(expectedData);
  });

  it("returns correct data for Viking Union", () => {
    const returnedData = parse(
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
    };
    expect(returnedData).to.deep.equal(expectedData);
  });
});
