const fs = require("fs");

function parse(page) {
  const output = {};
  const data = fs.readFileSync(page, "utf8");

  // get general building info
  output.buildingName = getContentBetweenTags(
    "<span>",
    "</span>",
    getContentBetweenTags('<h1 class="page-title">', "</h1>", data)
  );

  output.buildingTypes = getContentBetweenTags(
    '<span class="field-content">',
    "</span",
    data
  ).split(", ");

  const floorPlanElement = getParentTag(
    '<span class="material-icons" aria-hidden="true">domain</span>',
    data
  );
  if (floorPlanElement !== null) {
    output.floorplanLink = getContentBetweenTags(
      'href="',
      '"',
      floorPlanElement
    );
  }

  // get Departments and Offices
  const departmentsOfficesElements = getDivListContent(
    'href="https://www.wwu.edu/taxonomy',
    data
  );

  if (departmentsOfficesElements) {
    departmentsOfficesElements.forEach(
      (s, i, arr) =>
        (arr[i] = s
          .substring(s.search(">") + 1)
          .substring(0, s.substring(s.search(">") + 1).search("<"))
          .trim())
    );
  }

  output.departmentsOffices = departmentsOfficesElements;

  // get computer labs
  output.computerLabs = getValuesAsList(
    getContentBetweenTags(
      '<div class="field field--name-field-computer-labs field--type-text-long field--label-hidden field-item">',
      "</div>",
      data
    )
  );

  if (!output.computerLabs) {
    output.computerLabs = [];
  }

  output.computerLabs.forEach((v, i, arr) => {
    arr[i] = removeTagsFromString(v);
  });

  // get gender neutral restrooms
  output.genderNeutralRestrooms = parseRoomNumbers(
    getContentBetweenTags(
      "<p>",
      "</p>",
      getContentBetweenTags(
        'class="field field--name-field-gender-neutral-restrooms field--type-text-long field--label-hidden field-item">',
        "</div>",
        data
      )
    )
  );

  // get dining data
  const diningData = getValuesAsList(
    getContentBetweenTags(
      '<div class="field field--name-field-dining field--type-text-long field--label-hidden field-item">',
      "</div>",
      data
    )
  );

  output.dining = [];

  if (diningData) {
    diningData.forEach((v) => {
      const diningLink = getContentBetweenTags('href="', '">', v);
      const diningName = getContentBetweenTags('">', "</a>", v).trim();
      output.dining.push([diningName, diningLink]);
    });
  }

  // get accessibilty info
  output.accessibilityInfo = getListContent(
    getContentBetweenTags(
      'class="field field--name-field-accessibility field--type-text-long field--label-hidden field-item">',
      "</div>",
      data
    )
  );

  return output;
}

function parseWeb(data) {
  const output = {};

  // get general building info
  output.buildingName = getContentBetweenTags(
    "<span>",
    "</span>",
    getContentBetweenTags('<h1 class="page-title">', "</h1>", data)
  );

  output.buildingTypes = getContentBetweenTags(
    '<span class="field-content">',
    "</span",
    data
  ).split(", ");

  const floorPlanElement = getParentTag(
    '<span class="material-icons" aria-hidden="true">domain</span>',
    data
  );
  if (floorPlanElement !== null) {
    output.floorplanLink = getContentBetweenTags(
      'href="',
      '"',
      floorPlanElement
    );
  }

  // get Departments and Offices
  const departmentsOfficesElements = getDivListContent(
    'href="https://www.wwu.edu/taxonomy',
    data
  );

  if (departmentsOfficesElements) {
    departmentsOfficesElements.forEach(
      (s, i, arr) =>
        (arr[i] = s
          .substring(s.search(">") + 1)
          .substring(0, s.substring(s.search(">") + 1).search("<"))
          .trim())
    );
  }

  output.departmentsOffices = departmentsOfficesElements;

  // get computer labs
  output.computerLabs = getValuesAsList(
    getContentBetweenTags(
      '<div class="field field--name-field-computer-labs field--type-text-long field--label-hidden field-item">',
      "</div>",
      data
    )
  );

  if (!output.computerLabs) {
    output.computerLabs = [];
  }

  output.computerLabs.forEach((v, i, arr) => {
    arr[i] = removeTagsFromString(v);
  });

  // get gender neutral restrooms
  output.genderNeutralRestrooms = parseRoomNumbers(
    getContentBetweenTags(
      "<p>",
      "</p>",
      getContentBetweenTags(
        'class="field field--name-field-gender-neutral-restrooms field--type-text-long field--label-hidden field-item">',
        "</div>",
        data
      )
    )
  );

  // get dining data
  const diningData = getValuesAsList(
    getContentBetweenTags(
      '<div class="field field--name-field-dining field--type-text-long field--label-hidden field-item">',
      "</div>",
      data
    )
  );

  output.dining = [];

  if (diningData) {
    diningData.forEach((v) => {
      const diningLink = getContentBetweenTags('href="', '">', v);
      const diningName = getContentBetweenTags('">', "</a>", v).trim();
      output.dining.push([diningName, diningLink]);
    });
  }

  // get accessibilty info
  output.accessibilityInfo = getListContent(
    getContentBetweenTags(
      'class="field field--name-field-accessibility field--type-text-long field--label-hidden field-item">',
      "</div>",
      data
    )
  );

  return output;
}

// returns string between two user provided tags in data
function getContentBetweenTags(tag1, tag2, data) {
  if (data === null) {
    return null;
  }

  const entry = data.search(tag1);

  if (entry === -1) {
    return null;
  }

  data = data.substring(entry + tag1.length);
  const exit = data.search(tag2);

  if (exit === -1) {
    return null;
  }

  data = data.substring(0, exit);
  return data;
}

// gets values from list if list of content,
// otherwise gets singular value from between element tags
function getValuesAsList(data) {
  if (data === null) {
    return null;
  }

  if (data.search("<li>") > -1) {
    return getListContent(data);
  } else {
    // returns data between first and last tag
    const firstTag = data.substring(0, data.search(">") + 1);
    const lastTag = data.substring(data.length - firstTag.length - 1);
    return [getContentBetweenTags(firstTag, lastTag, data)];
  }
}

// get content between li tags in an unordered list including sublists
function getListContent(data) {
  let ret = [];

  if (!data) {
    return [];
  }

  let listElements = data.substring(4, data.length - 5);

  while (listElements.indexOf("<li>") >= 0) {
    // if found ul in current list item
    if (
      listElements.indexOf("<ul>") !== -1 &&
      listElements.indexOf("<ul>") < listElements.indexOf("</li>")
    ) {
      // call func recursively on inside unordered list
      ret = ret.concat(
        getListContent(
          listElements.substring(
            listElements.indexOf("<ul>"),
            listElements.indexOf("</ul>") + 5
          )
        )
      );

      listElements = listElements.substring(listElements.indexOf("</ul>") + 10);
    } else {
      ret.push(
        String(
          listElements.substring(
            listElements.indexOf("<li>") + 4,
            listElements.indexOf("</li>")
          )
        )
      );

      listElements = listElements.substring(listElements.indexOf("</li>") + 4);
    }
  }

  return ret;
}

// gets preceding tag - will only return
// parent if element is single child
function getParentTag(content, data) {
  const entry = data.search(content);

  if (entry === -1) {
    return null;
  }

  let mod = entry;
  while (data[mod - 1] !== "<") {
    mod--;
  }
  return data.substring(mod, entry);
}

// get content from div constructed from list
function getDivListContent(entryTag, data) {
  const temp = [];

  const entry = data.search(entryTag);

  if (entry < 0) {
    return [];
  }

  data = data.substring(entry + entryTag.length);
  let search = data.search(entryTag);
  while (search !== -1) {
    temp.push(data.substring(0, search));
    data = data.substring(search + entryTag.length);
    search = data.search(entryTag);
  }

  temp.push(data);

  return temp;
}

// removes all html opening and closing tags from string
// does not work for nested tags
function removeTagsFromString(data) {
  if (data.search("<") < 0) {
    return data;
  }

  let retString = "";
  while (data.search("<") > -1) {
    retString += data.substring(0, data.search("<"));
    data = data.substring(data.search(">") + 1);
  }

  return retString;
}

// gets string of room numbers and coverts to list
function parseRoomNumbers(data) {
  if (!data) {
    return [];
  }

  // get past initial non-numerical chars
  let i = 0;
  while (data[i].charCodeAt(0) < 48 || data[i].charCodeAt(0) > 57) {
    i++;
  }

  data = data.substring(i);

  if (!data) {
    return [];
  }

  return data.split(", ");
}

module.exports = { parse, parseWeb };
