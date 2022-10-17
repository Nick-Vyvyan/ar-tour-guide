// react imports
import React, { useState, useRef } from "react";
import { getAuth, signInWithEmailAndPassword } from "firebase/auth";
import { Navigate } from "react-router-dom";
import useAuth from "../../hook/useAuth";
import Error from "../../components/Error";

// components
import Container from "react-bootstrap/Container";
import ListStruct from "../../components/ListStruct";
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";
import { isValidTimestamp } from "@firebase/util";
import StructInformation from "../../components/StructInformation";
import { InfoBox } from "@react-google-maps/api";

const axios = require("axios").default;

const ListView = (props) => {
  // auth and error
  const { user } = useAuth();

  if (user === null || user === undefined)
    return <Navigate to={{ pathname: "/login" }} />;

  const buttonStyle = {
    backgroundColor: 'LightSteelBlue',
    textDecoration: 'none',
    padding: '10px',
    borderRadius: '5px',
    display: 'block',
    width: '150px',
  }

  const linkStyle = {
    textDecoration: 'none',
    color: 'black'
  }
  
  return (
    <Container>
      <h1>Add or Remove Structures</h1>
      <br />
      <a style={linkStyle} href="/panel" > 
        <div style={buttonStyle}>
          Add a Structure
        </div>
      </a> 
      <br />
      <StructInformation />
      
    </Container>
  );
};

export default ListView;
