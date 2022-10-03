// react imports
import React, { useState, useMemo } from "react";
import { Navigate } from "react-router-dom";
import { GoogleMap, LoadScript, DrawingManager } from "@react-google-maps/api";
import useAuth from "../../hook/useAuth";
import Error from "../../components/Error";

// components
import Container from "react-bootstrap/Container";
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";

// other imports
const { parseWeb } = require("./../../../src/scripts/PageParser");
const axios = require("axios").default;

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

  // center point for WWU on Google map
  const wwuCenter = useMemo(
    () => ({
      lat: 48.73438903,
      lng: -122.48639749,
    }),
    []
  );

  // const { isLoaded } = useLoadScript({
  //   googleMapsApiKey: "AIzaSyC4KhgTKzblt28kngclW9__A2vZUevgdgo",
  // });
  // if (!isLoaded) return <div>Loading Map...</div>;

  const onLoad = (drawingManager) => {
    console.log(drawingManager);
  };

  const onPolygonComplete = (polygon) => {
    console.log(polygon);
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    // forward request to server to bypass cors restrictions
    axios
      .get(baseServerURL, { headers: { websiteLink: websiteLink } })
      .then((res) => {
        setScrapedData(parseWeb(res.data));
      })
      .catch((err) => console.error(err));
  };

  return (
    <Container style={{ marginTop: "80px" }}>
      <h1>Add a Building/Landmark</h1>
      <br />
      <Form onSubmit={handleSubmit}>
        <Form.Label>Outline Building/Landmark on Map:</Form.Label>
        <LoadScript
          googleMapsApiKey="AIzaSyC4KhgTKzblt28kngclW9__A2vZUevgdgo"
          libraries={["drawing"]}
        >
          <GoogleMap
            zoom={15}
            center={wwuCenter}
            mapContainerClassName="map-container"
          >
            <DrawingManager
              drawingMode={"polygon"}
              onLoad={onLoad}
              onPolygonComplete={onPolygonComplete}
            />
          </GoogleMap>
        </LoadScript>
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

      {scrapedData && (
        <>
          <u>Your scraped data is:</u>
          {Object.keys(scrapedData).map((key, index) => {
            return (
              <div key={index}>
                {key}: {scrapedData[key]}
              </div>
            );
          })}
          <br />
        </>
      )}

      {/* display all errors */}
      {error && <Error error={error} />}
    </Container>
  );
};

export default PanelView;
