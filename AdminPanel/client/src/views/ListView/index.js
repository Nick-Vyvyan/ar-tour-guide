// react imports
import React from "react";
import { Navigate } from "react-router-dom";
import useAuth from "../../hook/useAuth";

// components
import Container from "react-bootstrap/Container";
import StructInformation from "../../components/StructInformation";
import "./styles.css";

const ListView = (props) => {
  // auth and error
  const { user } = useAuth();

  if (user === null || user === undefined)
    return <Navigate to={{ pathname: "/login" }} />;

  return (
    <Container>
      <h1>Add or Remove Structures</h1>
      <a class="linkStyle" href="/panel">
        <div class="buttonStyle" style={{ textAlign: "center" }}>
          Add a Structure
        </div>
      </a>
      <StructInformation />
      <br />
    </Container>
  );
};

export default ListView;
