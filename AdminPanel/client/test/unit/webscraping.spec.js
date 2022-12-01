const { expect } = require("chai");
const { parseWeb } = require("../../src/scripts/PageParser");
const fs = require("fs");

describe("webparsing", () => {
  it("returns correct data for Arntzen Hall", () => {
    const data = fs.readFileSync("./test/unit/webscrapingTestAssets/AHTest.html", "utf8");
    const returnedData = parseWeb(data);
    const expectedData = {
      buildingName: "Arntzen Hall",
      buildingTypes: "Academic, Campus Services",
      floorplanLink: "https://cpd.wwu.edu/files/2020-04/AH.pdf",
      departmentsOffices: 
        "Anthropology, Environmental Studies, Urban and Environmental Planning and Policy, Political Science, Sociology",
      computerLabs:
        "AH 01 (Sociology), AH 02, AH 05, AH 16 (Spatial Analysis Lab)",
      dining: "The Atrium - https://wwu.campusdish.com/LocationsAndMenus",
      genderNeutralRestrooms: "",
      accessibilityInfo: 
        "Button activated entrances are located on the west side of the building, Elevators provide access to all levels, Accessible restrooms are located on the Concourse level (Basement), Access to the Environmental Studies building is provided via the basement connection, Accessible parking to the east (Lot 17 G)",
    };
    expect(returnedData).to.deep.equal(expectedData);
  });

  it("returns correct data for Bond Hall", () => {
    const data = fs.readFileSync("./test/unit/webscrapingTestAssets/BHTest.html", "utf8");
    const returnedData = parseWeb(data);
    const expectedData = {
      buildingName: "Bond Hall",
      buildingTypes: "Academic",
      floorplanLink: "https://cpd.wwu.edu/files/2020-04/BH.pdf",
      departmentsOffices: "",
      computerLabs: "BH 319",
      dining: "",
      genderNeutralRestrooms: "157, 207, 307, 403B",
      accessibilityInfo: 
       `Button activated entrances are located on the northeast side of building, An elevator offers access to all floors except the 2nd and 3rd half floors (both half floors are accessible via ramps or lifts), Accessible restrooms are located on the Mezzanine level, Many doors in this building are 2' 8" wide, Accessible parking to the east or west`
    };
    expect(returnedData).to.deep.equal(expectedData);
  });

  it("returns correct data for Performing Arts Center", () => {
    const data = fs.readFileSync("./test/unit/webscrapingTestAssets/PATest.html", "utf8");
    const returnedData = parseWeb(data);
    const expectedData = {
      buildingName: "Performing Arts Center",
      buildingTypes: "Academic, Events",
      floorplanLink: "https://cpd.wwu.edu/files/2020-01/PA.pdf",
      departmentsOffices: 
        `Box Office, College of Fine and Performing Arts, Music, Music Library, Dance`
      ,
      computerLabs: "",
      dining: "",
      genderNeutralRestrooms: "150A, 151A, 390",
      accessibilityInfo: 
       `Accessible parking on the southwest side of the building, Accessible entrances, Accessible rest rooms`
    };
    expect(returnedData).to.deep.equal(expectedData);
  });

  it("returns correct data for Ridgeway Delta", () => {
    const data = fs.readFileSync("./test/unit/webscrapingTestAssets/RDTest.html", "utf8");
    const returnedData = parseWeb(data);
    const expectedData = {
      buildingName: "Ridgeway Delta",
      buildingTypes: "University Residence",
      floorplanLink: "https://cpd.wwu.edu/files/2020-01/RD.pdf",
      departmentsOffices: "",
      computerLabs: "See Ridgeway Commons and Ridgeway Sigma",
      dining: "Ridgeway Commons - https://www.wwu.edu/building/rc",
      genderNeutralRestrooms: "2",
      accessibilityInfo: 
        `This building is not wheelchair accessible., Accessible parking to west (Lot 15R).`
      ,
    };
    expect(returnedData).to.deep.equal(expectedData);
  });

  it("returns correct data for Viking Union", () => {
    const data = fs.readFileSync("./test/unit/webscrapingTestAssets/VUTest.html", "utf8");
    const returnedData = parseWeb(data);
    const expectedData = {
      buildingName: "Viking Union",
      buildingTypes: "Campus Services, Events",
      floorplanLink: "https://cpd.wwu.edu/files/2020-01/VU.pdf",
      departmentsOffices:
        `Associated Students, Bookstore, Associated Students, Ethnic Student Center, LGBTQ+ Western, Office of Student Life, Off Campus Living, Outdoor Center, Student Advocacy and Identity Resource Center`,
      computerLabs: "",
      dining:
        `Viking Union Market - https://wwu.campusdish.com/LocationsAndMenus/VikingUnionMarket, Viking Union Cafe - https://wwu.campusdish.com/LocationsAndMenus/VikingUnionCafe, The Underground Coffeehouse - https://wwu.campusdish.com/LocationsAndMenus/UndergroundCoffeehouse, Vendor's Row - https://vu.wwu.edu/dining-spaces`,
      genderNeutralRestrooms: "351, 353, 714, 715, 716, 717",
      accessibilityInfo:
        `Automatic doors at High St. and the VU Plaza on the 6th floor and on the 1st floor at the Garden St. entrance., Accessible restrooms on floors 3, 4, 5 and 6., Accessible parking on the north side of the building (Lot 6V).`,
    };
    expect(returnedData).to.deep.equal(expectedData);
  });

  it("returns correct data for Split Stone", () => {
    const data = fs.readFileSync("./test/unit/webscrapingTestAssets/SSTest.html", "utf8");
    const returnedData = parseWeb(data);
    const expectedData = {
      buildingName: "Sarah Sze, Split Stone (Northwest), 2019",
      description: 
        `As with all Sarah’s work, Split Stone (Northwest) emerges from the site and is intimately related to its location. Found along one of the campus’s central pathways, the pair of stones act like a cairn along a trail, drawing viewers to it from the many surrounding pathways. The piece creates a choreography of anticipation and surprise, as well as an intimate moment for pause and contemplation., The pair of sculptures are fabricated from a single boulder, split in two. One half stands silhouetted against the landscape and the other sits partially submerged, as if it had always occupied that location. In the interior of both sculptures, viewers discover a photographic image constructed from fragments of color, like a split open geode revealing a world inside. The image, created by incising the cut surface of the stone with of a dot-matrix pattern mosaic, captures a scene of the sky at sunset. The same image is mirrored on the other half of the boulder, as if the stone in its core contained a fixed image of the sky set in place through the forces of gravity and pressure., Split Stone (Northwest) explores the idea of landscape and image in many forms: images of landscapes; sculptures as landscapes in themselves; and the altered landscape of the Western Washington Campus. The project plays with landscape and sculpture, as well as painting, printmaking and the production of images. It references both the speed and ubiquity of contemporary image capture and ancient forms of mark making, bringing the painstaking process of stone engraving and a sense of physical gravity, weight, and authorship into our contemporary context, where anonymous and fleeting digital images have become a kind of debris that constantly swirls around us., By recording images in pixels and then fixing them in stone and pigment, Sze explores the fragility of time passing and our desire for weight and permanence in the face of both overwhelming natural forces and the ubiquitous images that surround us daily.`
    };
    expect(returnedData).to.deep.equal(expectedData);
  });

  it("returns correct data for For Handel", () => {
    const data = fs.readFileSync("./test/unit/webscrapingTestAssets/FHTest.html", "utf8");
    const returnedData = parseWeb(data);
    const expectedData = {
      buildingName: "Mark di Suvero, For Handel, 1975",
      description:
        `Walk around the sculpture with Google Street View, Di Suvero's knowledge of music and sensitivity to the relationship of art and architecture led him to create a soaring sculpture dedicated to the composer George Frederic Handel. Di Suvero's work rises not only from the roof of the rehearsal hall below but also projects beyond this roof/plaza and against a magnificent view of water, mountains and sky. Sometimes di Suvero is considered an \"action sculptor\" in the way he draws directly with the steel I- beams. In running his own truck cranes, in using his welding torch and in directing the blocks and cables, he attempts to build multi- dimensional structures which seem to overcome physical laws., Di Suvero works with both the I-beams of modern buildings and the discarded materials of modern life. When he came to campus di Suvero found that a work he had been carrying around in his mind would fit the space of the newly reconstructed Music building and plaza. He has often stated that his sculptural ideas evolve in \'\'dreamtime ... pure music of the mind.\" He likes music, whether classical or jazz, because it is an example of disciplined emotion. He also can relate to the rigorous labor and challenges involved in the construction trade and engineering. Di Suvero's For Handel (1975) originally combined a hanging wooden platform or swinging bed with the steel girders of modern technology. The fact that the bed was removed soon after the sculpture was erected does not diminish the sculpture's impact or meaning.`
    };
    expect(returnedData).to.deep.equal(expectedData);
  });
});
