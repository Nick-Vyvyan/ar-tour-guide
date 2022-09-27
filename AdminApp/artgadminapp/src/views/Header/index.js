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
        <Navbar.Brand>AR Tour Guide Admin Panel</Navbar.Brand>
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
