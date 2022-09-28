// react imports
import React, { useState } from "react";
import { Navigate } from "react-router-dom";
import useAuth from "../../hook/useAuth";
import Error from "../../components/Error";

// components
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";

const PanelView = (props) => {
  // auth and error
  const { user } = useAuth();
  // eslint-disable-next-line
  const [error, setError] = useState(null);

  const [xCoord, setXCoord] = useState("");
  const [yCoord, setYCoord] = useState("");
  const [websiteLink, setWebsiteLink] = useState("");

  if (user === null || user === undefined)
    return <Navigate to={{ pathname: "/login" }} />;

  return (
    <Container style={{ marginTop: "80px" }}>
      <h1>Enter Information to Add a Building/Landmark</h1>
      <br />
      <Form>
        <Form.Group className="mb-3">
          <Form.Label>GPS Coordinates</Form.Label>
          <Row>
            <Col>
              <Form.Control
                required
                type="text"
                placeholder="Enter GPS x-coordinate..."
                value={xCoord}
                onChange={(e) =>
                  e.target.value.match(/^[0-9]*[.,]?[0-9]*$/) &&
                  setXCoord(e.target.value)
                }
              />
            </Col>
            <Col>
              <Form.Control
                required
                type="text"
                placeholder="Enter GPS y-coordinate..."
                value={yCoord}
                onChange={(e) =>
                  e.target.value.match(/^[0-9]*[.,]?[0-9]*$/) &&
                  setYCoord(e.target.value)
                }
              />
            </Col>
          </Row>
        </Form.Group>

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

        <Button variant="primary" type="submit">
          Submit
        </Button>
      </Form>

      {/* display all errors */}
      {error && <Error error={error} />}
    </Container>
  );
};

export default PanelView;
