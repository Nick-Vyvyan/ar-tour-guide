import React from "react";
import "./styles.css"

const axios = require("axios").default;

function ListStruct(props) {

  const removeBuilding = () => {
    if (window.confirm("Are you sure you want to delete " + props.name + "?") === false) {
      return
    }

    //const baseServerURL = "http://localhost:5000";
    const baseServerURL =
    "https://us-central1-ar-tour-guide-admin-panel.cloudfunctions.net/app";

    axios
      .delete(baseServerURL + "/" + props.id)
  }

  return (
    <div class="flexRowStyle">
      <h3>{props.name}</h3>
      <div onClick={removeBuilding} class="structButtonStyle">Remove Structure</div>
    </div>
  );
}

export default ListStruct;