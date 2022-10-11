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

  // const baseServerURL = "http://localhost:5000/";
  const baseServerURL =
    "https://us-central1-ar-tour-guide-admin-panel.cloudfunctions.net/app";

  // eslint-disable-next-line
  const [error, setError] = useState(null);
  const [websiteLink, setWebsiteLink] = useState("");
  const [scrapedData, setScrapedData] = useState();
  const [coordinates, setCoordinates] = useState("");

  // center point for WWU on Google map
  const wwuCenter = useMemo(
    () => ({
      lat: 48.73438903,
      lng: -122.48639749,
    }),
    []
  );

  const { isLoaded } = useLoadScript({
    googleMapsApiKey: "AIzaSyC4KhgTKzblt28kngclW9__A2vZUevgdgo",
    libraries,
  });
  if (!isLoaded) return <div>Loading Map...</div>;

  // const onLoad = (drawingManager) => {
  //   console.log(drawingManager);
  // };

  const onPolygonComplete = (polygon) => {
    console.log(polygon);
  };

  // get map coords and scrape website
  const handleFirstSubmit = (e) => {
    e.preventDefault();

    // PLACEHOLDER
    setCoordinates(
      "(48.734682375806216, -122.48841639686137),(48.734342718528104, -122.48918887305766),(48.734300261207, -122.48785849738627)"
    );

    // forward request to server to bypass cors restrictions
    axios
      .get(baseServerURL, { headers: { websiteLink: websiteLink } })
      .then((res) => {
        setScrapedData(parseWeb(res.data));
      })
      .catch((err) => console.error(err));
  };

  // send manually reviewed data and coords to database
  const handleSecondSubmit = (e) => {
    e.preventDefault();

    const responseJson = { coordinates, scrapedData };
    console.log(responseJson);
  };

  // convert camel case to title case for a given string
  const formatWords = (str) => {
    const tempStr = str.replace(/([A-Z])/g, " $1");
    return tempStr.charAt(0).toUpperCase() + tempStr.slice(1);
  };

  return (
    <>
      {/* if scraped data is present, display form to manually review it */}
      {scrapedData ? (
        <Container style={{ marginTop: "80px" }}>
          <h1>Review your Building/Landmark Data</h1>
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
            <Button variant="primary" type="submit">
              Submit
            </Button>
          </Form>
          <br />
        </Container>
      ) : (
        // otherwise, display google map and field to input website link for scraping
        <Container style={{ marginTop: "80px" }}>
          <h1>Add a Building/Landmark</h1>
          <br />
          <Form onSubmit={handleFirstSubmit}>
            <Form.Label>Outline Building/Landmark on Map:</Form.Label>
            {/* <LoadScript
              googleMapsApiKey="AIzaSyC4KhgTKzblt28kngclW9__A2vZUevgdgo"
              libraries={["drawing"]}
            > */}
            <GoogleMap
              zoom={15}
              center={wwuCenter}
              mapContainerClassName="map-container"
            >
              <DrawingManager
                drawingMode="polygon"
                // onLoad={onLoad}
                onPolygonComplete={onPolygonComplete}
              />
            </GoogleMap>
            {/* </LoadScript> */}
            <br />

            <Form.Group className="mb-3">
              <Form.Label>Website Link</Form.Label>
              <Form.Control
                required
                type="text"
                placeholder="Paste link of website to scrape for building/landmark info..."
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

            <Button variant="primary" type="submit">
              Submit
            </Button>
          </Form>
          <br />

          {/* display all errors */}
          {error && <Error error={error} />}
        </Container>
      )}
    </>
  );
};

export default PanelView;
