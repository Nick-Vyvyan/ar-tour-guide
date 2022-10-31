// react imports
import React, { useState, useMemo } from "react";
import { Navigate } from "react-router-dom";
import {
  useLoadScript,
  GoogleMap,
  DrawingManager,
} from "@react-google-maps/api";
import useAuth from "../../hook/useAuth";
import Error from "../../components/Error";

// components
import Container from "react-bootstrap/Container";
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";

// other imports
const { parseWeb } = require("./../../../src/scripts/PageParser");
const axios = require("axios").default;

// google map libraries to import
const libraries = ["drawing"];

const PanelView = (props) => {
  // auth and error
  const { user } = useAuth();

  if (user === null || user === undefined)
    return <Navigate to={{ pathname: "/login" }} />;

  // const baseServerURL = "http://localhost:5000";
  const baseServerURL =
    "https://us-central1-ar-tour-guide-admin-panel.cloudfunctions.net/app";

  // eslint-disable-next-line
  const [error, setError] = useState(null);
  const [websiteLink, setWebsiteLink] = useState("");
  const [coordinates, setCoordinates] = useState("");
  const [scrapedData, setScrapedData] = useState();
  const [isLandmark, setIsLandmark] = useState(false);

  // center point for WWU on Google map
  const wwuCenter = useMemo(
    () => ({
      lat: 48.73438903,
      lng: -122.48639749,
    }),
    []
  );

  // load in google map with required libraries
  const { isLoaded } = useLoadScript({
    googleMapsApiKey: "AIzaSyC4KhgTKzblt28kngclW9__A2vZUevgdgo",
    libraries,
  });
  if (!isLoaded) return <div>Loading Map...</div>;

  // const onLoad = (drawingManager) => {
  //   console.log(drawingManager);
  // };

  // get coordinates of finished polygon
  const onPolygonComplete = (polygon) => {
    setCoordinates(
      polygon
        .getPath()
        .getArray()
        .toString()
    );
  };

  // get map coords and scrape website
  const handleFirstSubmit = (e) => {
    e.preventDefault();

    // make sure structure is outlined on map first
    if (coordinates === "")
      return window.alert(
        "Please outline a building/landmark on the map using the polygon tool!"
      );

    // forward request to server to bypass cors restrictions
    axios
      .get(baseServerURL, { headers: { websiteLink: websiteLink } })
      .then((res) => {
        // catch errors
        if (res.status !== 200) {
          const message = `An error occurred: ${res.statusText}`;
          return window.alert(message);
        }

        setScrapedData(parseWeb(res.data));
      })
      .catch((err) => console.error(err));
  };

  // send manually reviewed data and coords to database
  const handleSecondSubmit = (e) => {
    e.preventDefault();

    // calculate center point of building
    let centerPointX = 0;
    let centerPointY = 0;
    let coordArray = coordinates.split(/(?<=\))(,\s*)(?=\()/);

    for (let i = 0; i < coordArray.length; i++) {
      let tempString = coordArray[i].substring(1, coordArray[i].length - 1);
      let tempArray = tempString.split(",");
      centerPointX += parseFloat(tempArray[0]);
      centerPointY += parseFloat(tempArray[1]);
    }

    const centerPoint =
      "(" +
      centerPointX / coordArray.length +
      "," +
      centerPointY / coordArray.length +
      ")";

    // put all structure info together into one json
    const requestJson = { scrapedData, isLandmark, websiteLink, centerPoint };

    // send structure info to backend to be saved to database
    axios
      .post(`${baseServerURL}/db/add`, {
        headers: { "Content-Type": "application/json" },
        body: requestJson,
      })
      .then((res) => {
        // catch errors
        if (res.status !== 200) {
          const message = `An error occurred: ${res.statusText}`;
          return window.alert(message);
        }

        // reset state info
        setWebsiteLink("");
        setCoordinates("");
        setScrapedData();
        setIsLandmark(false);
      })
      .catch((err) => console.error(err));
  };

  // convert camel case to title case for a given string
  const formatWords = (str) => {
    const tempStr = str.replace(/([A-Z])/g, " $1");
    return tempStr.charAt(0).toUpperCase() + tempStr.slice(1);
  };

  return (
    <>
      {/* if coordinates and scraped data are present, display form to manually review it */}
      {coordinates !== "" && scrapedData ? (
        <Container>
          <h1>Review your Building/Landmark Data</h1>
          <p>
            Your supplied coordinates are: <b>{coordinates}</b>
          </p>
          <br />
          <Form onSubmit={handleSecondSubmit}>
            <Form.Group className="mb-3">
              {Object.keys(scrapedData).map((key, index) => {
                return (
                  <div key={index}>
                    <Form.Label>{formatWords(key)}</Form.Label>
                    <Form.Control
                      type="text"
                      value={scrapedData[key]}
                      onChange={(e) =>
                        setScrapedData({
                          ...scrapedData,
                          [key]: e.target.value,
                        })
                      }
                    />
                    <br />
                  </div>
                );
              })}
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Check
                type="checkbox"
                label="Is this structure a landmark?"
                checked={isLandmark}
                onChange={(e) => setIsLandmark(e.target.checked)}
              />
            </Form.Group>
            {/* display all errors */}
            {error && <Error error={error} />}

            <Button variant="primary" type="submit">
              Submit
            </Button>
          </Form>
          <br />
        </Container>
      ) : (
        // otherwise, display google map and field to input website link for scraping
        <Container>
          <h1>Add a Building/Landmark</h1>
          <br />
          <Form onSubmit={handleFirstSubmit}>
            <Form.Label>Outline Building/Landmark on Map:</Form.Label>
            <GoogleMap
              zoom={15}
              center={wwuCenter}
              tilt="0"
              mapTypeId="hybrid"
              mapContainerClassName="map-container"
            >
              <DrawingManager
                defaultDrawingMode="polygon"
                onPolygonComplete={onPolygonComplete}
                defaultOptions={{
                  polygonOptions: {
                    strokeColor: "#ffff00",
                  },
                }}
              />
            </GoogleMap>
            <br />

            <Form.Group className="mb-3">
              <Form.Label>Website Link</Form.Label>
              <Form.Control
                required
                type="text"
                placeholder="Paste link of website to scrape..."
                value={websiteLink}
                onChange={(e) => {
                  try {
                    new URL(e.target.value);
                    setWebsiteLink(e.target.value);
                  } catch (_) {
                    setWebsiteLink("");
                  }
                }}
              />
            </Form.Group>
            {/* display all errors */}
            {error && <Error error={error} />}

            <Button variant="primary" type="submit">
              Submit
            </Button>
          </Form>
          <br />
        </Container>
      )}
    </>
  );
};

export default PanelView;
