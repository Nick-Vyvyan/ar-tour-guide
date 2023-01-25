const { MongoClient } = require("mongodb");
const { ObjectId } = require("mongodb/lib/bson");
const Db = process.env.ATLAS_URI;
const client = new MongoClient(Db, {
  useNewUrlParser: true,
  useUnifiedTopology: true,
});

var _db;

module.exports = {
  connectToServer: function (callback) {
    client.connect(function (err, db) {
      // Verify we got a good "db" object
      if (db) {
        _db = db.db("artourguide");
        console.log("Successfully connected to MongoDB.");
      }

      // nuclear option
      //_db.collection("search").deleteOne({})

      // post nuclear option
      //_db.collection("search").insertOne({index: {}})

      // reset search ids
      /*_db.collection("structures").find({}).toArray((err, T) => {
        T.forEach((v, i) => {
          let query = {_id: ObjectId(v._id)}
          _db.collection("structures").updateOne(query, {$set: {id: i}})
        }) 
        
      })*/

      return callback(err);
    });
  },

  getDb: function () {
    return _db;
  },
};
