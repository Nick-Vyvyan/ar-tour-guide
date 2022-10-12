const express = require("express");
const cors = require("cors");
const axios = require("axios").default;
require("dotenv").config();
const port = process.env.SERVER_PORT || 5000;
const app = express();

app.use(cors());

app.get("/", (req, res) => {
  axios
    .get(req.headers.websitelink)
    .then((r) => res.send(r.data))
    .catch((err) => console.error(err));
});

app.use(express.json());
app.use(require("./routes/db"));
const dbo = require("./mongoConnect");

app.listen(port, () => {
  dbo.connectToServer(function (err) {
    if (err) console.error(err);
  });

  console.log(`Server running on port: ${port}`);
});
