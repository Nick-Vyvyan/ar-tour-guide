// react imports
import React from "react";
import useAuth from "../../hook/useAuth";

// components
import Container from "react-bootstrap/Container";
import Navbar from "react-bootstrap/Navbar";
import Button from "react-bootstrap/Button";

const Header = () => {
  const { user, logout } = useAuth();

  return (
    <Navbar fixed="top" expand="lg" bg="dark" variant="dark">
      <Container>
        <Navbar.Brand>
          <span className="align-middle">
            <a
              href="/"
              style={{
                textDecoration: "none",
                color: "inherit",
              }}
            >
              <img
                alt="Logo"
                src="/Logo.png"
                width="30"
                height="30"
                style={{ borderRadius: "50%", border: "solid white 2px" }}
                className="d-inline-block align-top"
              />{" "}
              <span
                style={{
                  paddingLeft: "5px",
                }}
              >
                AR Tour Guide Admin Panel
              </span>
            </a>
          </span>
        </Navbar.Brand>
        {user && (
          <Navbar.Collapse className="justify-content-end">
            <Button variant="primary" onClick={() => logout()}>
              Logout
            </Button>
          </Navbar.Collapse>
        )}
      </Container>
    </Navbar>
  );
};

export default Header;
