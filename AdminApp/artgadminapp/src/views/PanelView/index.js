// react imports
import React, { useState, useMemo } from "react";
import { Navigate } from "react-router-dom";
import { GoogleMap, useLoadScript } from "@react-google-maps/api";
import useAuth from "../../hook/useAuth";
import Error from "../../components/Error";

// components
import Container from "react-bootstrap/Container";
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";

const PanelView = (props) => {
  // auth and error
  const { user } = useAuth();

  if (user === null || user === undefined)
    return <Navigate to={{ pathname: "/login" }} />;

  // eslint-disable-next-line
  const [error, setError] = useState(null);
  const [websiteLink, setWebsiteLink] = useState("");

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
  });
  if (!isLoaded) return <div>Loading Map...</div>;

  return (
    <Container style={{ marginTop: "80px" }}>
      <h1>Add a Building/Landmark</h1>
      <br />
      <Form>
        <Form.Label>Outline Building/Landmark on Map:</Form.Label>
        <GoogleMap
          zoom={15}
          center={wwuCenter}
          mapContainerClassName="map-container"
        />
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
  );
};

export default PanelView;
