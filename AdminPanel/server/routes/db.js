const express = require("express");
var ObjectId = require("mongodb").ObjectID;

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

// // This section will help you get a single record by id
// recordRoutes.route("/db/:id").get(function (req, res) {
//   let db_connect = dbo.getDb();
//   let myquery = { _id: ObjectId(req.params.id) };
//   db_connect.collection("structures").findOne(myquery, function (err, result) {
//     if (err) throw err;
//     res.json(result);
//   });
// });

// This section will help you create a new record.
recordRoutes.route("/db/add").post(function (req, response) {
  let db_connect = dbo.getDb();

  let myobj = {
    scrapedData: req.body.body.scrapedData,
    isLandmark: req.body.body.isLandmark,
    centerPoint: req.body.body.centerPoint,
    websiteLink: req.body.body.websiteLink,
  };
  db_connect.collection("structures").insertOne(myobj, function (err, res) {
    if (err) throw err;
    response.json(res);
  });
});

// // This section will help you update a record by id.
// recordRoutes.route("/update/:id").post(function (req, response) {
//   let db_connect = dbo.getDb();
//   let myquery = { _id: ObjectId(req.params.id) };
//   let newvalues = {
//     $set: {
//       name: req.body.name,
//       position: req.body.position,
//       level: req.body.level,
//     },
//   };
//   db_connect
//     .collection("structures")
//     .updateOne(myquery, newvalues, function (err, res) {
//       if (err) throw err;
//       console.log("1 document updated");
//       response.json(res);
//     });
// });

// This section will help you delete a record
recordRoutes.route("/:id").delete((req, response) => {
  let db_connect = dbo.getDb();
  let myquery = { _id: ObjectId(req.params.id) };
  db_connect.collection("structures").deleteOne(myquery, function (err, obj) {
    if (err) throw err;
    console.log("1 document deleted");
    response.json(obj);
  });
});

module.exports = recordRoutes;
