import React from "react";

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

  let flexRowStyle = {
    display: 'flex',
    flexDirection: 'row',
    backgroundColor: 'LightSteelBlue',
    padding: '10px',
    borderRadius: '5px',
    justifyContent: 'space-between'
  }

  let buttonStyle = {
    backgroundColor: 'darkred',
    padding: '5px',
    borderRadius: '5px',
    color: 'white'
  }

  return (
    <div style={flexRowStyle}>
      <h3>{props.name}</h3>
      <div onClick={removeBuilding} style={buttonStyle}>Remove Structure</div>
    </div>
  );
}

export default ListStruct;