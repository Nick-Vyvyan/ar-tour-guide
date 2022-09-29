const express = require("express");
const cors = require("cors");
const axios = require("axios").default;

const PORT = 5000;

const app = express();
app.use(cors());

app.get("/", (req, res) => {
  axios
    .get(req.headers.websitelink)
    .then((r) => res.send(r.data))
    .catch((err) => console.error(err));
});

app.listen(PORT, () => console.log(`Server running on port: ${PORT}`));
