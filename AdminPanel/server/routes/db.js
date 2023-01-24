const express = require("express");
var ObjectId = require("mongodb").ObjectID;
const { removeStopwords } = require("stopword");

// recordRoutes is an instance of the express router.
// We use it to define our routes.
// The router will be added as a middleware and will take control of requests starting with path /record.
const recordRoutes = express.Router();

// This will help us connect to the database
const dbo = require("../mongoConnect");

// This help convert the id from string to ObjectId for the _id.
require("mongodb").ObjectId;

// This section will help you get a list of all the records.
recordRoutes.route("/db").get(function (req, res) {
  let db_connect = dbo.getDb();
  db_connect
    .collection("structures")
    .find({})
    .toArray(function (err, result) {
      if (err) throw err;
      res.json(result);
    });
});

// gets the search table
recordRoutes.route("/search").get(function (req, res) {
  let db_connect = dbo.getDb();
  db_connect
    .collection("search")
    .findOne({}, function (err, result) {
      if (err) throw err;
      res.json(result);
    });
});

// This section will help you get a single record by id
recordRoutes.route("/db/:id").get(function (req, res) {
  let db_connect = dbo.getDb();
  let myquery = { _id: ObjectId(req.params.id) };
  db_connect.collection("structures").findOne(myquery, function (err, result) {
    if (err) throw err;
    res.json(result);
  });
});

// This section will help you create a new record.
recordRoutes.route("/db/add").post(function (req, response) {
  let db_connect = dbo.getDb();

  let myobj = {
    scrapedData: req.body.body.scrapedData,
    isLandmark: req.body.body.isLandmark,
    centerPoint: req.body.body.centerPoint,
    websiteLink: req.body.body.websiteLink,
  };

  // get current number of docs in structures collection
  db_connect.collection("structures").find({}).sort({id:-1}).limit(1).toArray(function (err, num) {
    if (err) throw err

    // in this case the maxnum and id are the same as we are adding a new entry
    let id = num[0].id + 1
    addStructureToSearchIndex(req.body.body.scrapedData, db_connect, id)

    myobj.id = maxNum
    db_connect.collection("structures").insertOne(myobj, function (err, res) {
      if (err) throw err;
      response.json(res);
    });
  })
  

  

  
});

// This section will help you update a record by id.
recordRoutes.route("/update/:id").post(function (req, response) {
  let db_connect = dbo.getDb();
  let myquery = { _id: ObjectId(req.params.id) };
  let newvalues = {
    $set: {
      scrapedData: req.body.body.scrapedData,
    },
  };

  let p2 = db_connect.collection("structures").find(myquery).toArray()

  Promise.all([p2]).then((values) => {
    console.log(values)
    const id = values[1][0].id
    addStructureToSearchIndex(req.body.body.scrapedData, db_connect, id)
  })

  db_connect
    .collection("structures")
    .updateOne(myquery, newvalues, function (err, res) {
      if (err) throw err;
      console.log("1 document updated");
      response.json(res);
    });

});

// This section will help you delete a record
recordRoutes.route("/:id").delete((req, response) => {
  let db_connect = dbo.getDb();
  let myquery = { _id: ObjectId(req.params.id) };

  // get current structure id and search index
  // clean index of current id
  // update index at mongo
  Promise.all([
    db_connect.collection("structures").findOne(myquery), 
    db_connect.collection("search").findOne({})])
    .then((values) => {
      db_connect.collection("search").update(
        {}, 
        {$set: {index: cleanIndex(values[1], values[0].id)}}, 
        (err, res) => {
          if (err) throw err
          console.log("search index updated")
      })
  })

  // delete current structure from mongo
  db_connect.collection("structures").deleteOne(myquery, function (err, obj) {
    if (err) throw err;
    console.log("1 document deleted");
    response.json(obj);
  });
});

// change all columns of id to 0
const cleanIndex = (index, id) => {
  for (let key in index) {
    index[key][id] = 0
  }

  return index
}

const addStructureToSearchIndex = (buildingData, db, id) => {

  db.collection("search").findOne({}, function (err, result) {
    if (err) throw err

    // get tokens and convert to set
    const searchTokens = new Set(getSearchTokens(buildingData))
  
    // get index object
    let index = {}
    if (result) {
      index = result.index
    }

    if (index[Object.keys(index)]) {
      const indexSize = index[Object.keys(index)].length
    }
    
    maxSize = max(indexSize, id)

    // clean index if updating
    cleanIndex(index, id)

    // if new entry
    if (id == maxSize) {
      // new
      for (let key in index) {
        if (searchTokens.has(key)) {
          index[key].push(1)
          searchTokens.delete(key)
        } else {
          index[key].push(0)
        }
      }

      // what's left in searchtokens is not in the index
      // add these tokens to the index
      searchTokens.forEach((token) => {
        index[token] = Array(maxSize).fill(0)
        index[token].push(1)
      })
    } else {
      // updating
      for (let key in index) {
        if (searchTokens.has(key)) {
          index[key][id] = 1
          searchTokens.delete(key)
        }
      }

      searchTokens.forEach((token) => {
        index[token] = Array(maxSize+1).fill(0)
        index[token][id] = 1
      })
    }
    
    db.collection("search").update({}, {$set: {index: index}}, (err, res) => {
      if (err) throw err
      console.log("search index updated")
    })
  })

  

  
  // have array of search tokens
  // add to index
  // push updated index to mongo
  
}

const removeStructureFromSearchIndex = (id) => {
  // get existing search index from mongo
  // convert to local object

  // remove all instances of this id
  // push updated index to mongo
}

const getSearchTokens = (buildingData) => {
  let searchString = 
  [buildingData.structureName,
   buildingData.searchTerms]
   .join(' ')

  // for each relevant field
  // add all strings to one big string
  // clean up punctuation
  // remove stop words
  // split words into array
  // add document to search index
  if (! buildingData.isLandmark) {
    // relevant fields
    searchString += ' ' +
    [
     buildingData.buildingCode,
     buildingData.structureTypes,
     buildingData.departmentsOffices,
    ]
    .join(' ')
 
    if (buildingData.computerLabs !== "") {
      searchString += " computer lab"
    }
    
    if (buildingData.genderNeutralRestrooms !== "") {
      searchString += " bathroom restroom gender neutral toilet washroom"
    }

    if (buildingData.dining !== "") {
      searchString += " dining diner restaurant eatery cafe cafeteria food drink shop lunch dinner breakfast eat "
      
      // remove URLs and add names to searchString
      let diningOptions = buildingData.dining.split(' ')
      diningOptions = diningOptions.filter(token => ! (token.includes("http") || token.includes("N/A") || token.includes("None") || token === "-"));

      searchString += diningOptions.join(' ')
    }

    searchString = searchString.toLowerCase()
    searchString = removeSymbols(searchString)

    return removeStopwords(searchString.split(/\s+/))
  }
}

const removeSymbols = (str) => {
  const newVar = str.split("").filter(token => token.match(/[a-zA-Z0-9\s]/i))
  return newVar.join('')
};

module.exports = recordRoutes;
