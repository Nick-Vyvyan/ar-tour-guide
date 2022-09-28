const fs = require('fs')

function parse (page) {
  const output = {}
  const data = fs.readFileSync(page, 'utf8')

  // get general building info
  output.buildingName = getContentBetweenTags(
    '<span>',
    '</span>',
    getContentBetweenTags('<h1 class="page-title">', '</h1>', data)
  )

  output.buildingTypes = getContentBetweenTags(
    '<span class="field-content">',
    '</span',
    data
  ).split(', ')

  const floorPlanElement = getParentTag(
    '<span class="material-icons" aria-hidden="true">domain</span>',
    data
  )
  if (floorPlanElement !== null) {
    output.floorplanLink = getContentBetweenTags(
      'href="',
      '"',
      floorPlanElement
    )
  }

  // get Departments and Offices
  const departmentOfficeElements = getDivListContent(
    'class="field-item"><a href="/taxonomy',
    data
  )

  departmentOfficeElements.forEach(
    (s, i, arr) =>
      (arr[i] = s.substring(26).substring(0, s.substring(26).search('<')))
  )

  output.departmentOffices = departmentOfficeElements

  // get computer labs
  output.computerLabs = getListContent(
    getContentBetweenTags(
      '<div class="field field--name-field-computer-labs field--type-text-long field--label-hidden field-item">',
      '</div>',
      data
    )
  )

  const diningData = getListContent(
    getContentBetweenTags(
      '<div class="field field--name-field-dining field--type-text-long field--label-hidden field-item">',
      '</div>',
      data
    )
  )

  output.dining = []

  diningData.forEach((v) => {
    const diningLink = getContentBetweenTags('<a href="', '">', v)
    const diningName = getContentBetweenTags('">', '</a>', v)
    output.dining.push([diningName, diningLink])
  })

  // console.log('output')
  // console.log(output)
  return output
}

function getContentBetweenTags (tag1, tag2, data) {
  if (data === null) {
    return null
  }

  const entry = data.search(tag1)

  if (entry === -1) {
    return null
  }

  data = data.substring(entry + tag1.length)
  const exit = data.search(tag2)

  if (exit === -1) {
    return null
  }

  data = data.substring(0, exit)
  return data
}

function getListContent (data) {
  const ret = []

  let listElements = getContentBetweenTags('<ul>', '</ul>', data)

  if (listElements === null) {
    return null
  }

  while (listElements.indexOf('<li>') > 0) {
    ret.push(
      listElements.substring(
        listElements.indexOf('<li>') + 4,
        listElements.indexOf('</li>')
      )
    )
    listElements = listElements.substring(listElements.indexOf('</li>') + 4)
  }

  return ret
}

function getParentTag (content, data) {
  const entry = data.search(content)

  if (entry === -1) {
    return null
  }

  let mod = entry
  while (data[mod - 1] !== '<') {
    mod--
  }
  return data.substring(mod, entry)
}

function getDivListContent (entryTag, data) {
  const temp = []

  const entry = data.search(entryTag)
  data = data.substring(entry + entryTag.length)
  let search = data.search(entryTag)
  while (search !== -1) {
    temp.push(data.substring(0, search))
    data = data.substring(search + entryTag.length)
    search = data.search(entryTag)
  }

  temp.push(data)

  return temp
}

module.exports = { parse }
