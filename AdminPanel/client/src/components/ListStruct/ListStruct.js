import React from "react";
import { useNavigate } from "react-router-dom";
import "./styles.css";

const axios = require("axios").default;

function ListStruct(props) {
  const navigate = useNavigate();

  //const baseServerURL = "http://localhost:5000";
  const baseServerURL =
    "https://us-central1-ar-tour-guide-admin-panel.cloudfunctions.net/app";

  const editStructure = () => {
    axios
      .get(baseServerURL + "/db/" + props.id)
      .then((res) => {
        // catch errors
        if (res.status !== 200) {
          const message = `An error occurred: ${res.statusText}`;
          return window.alert(message);
        }

        navigate("/panel", { state: { structureData: res.data } });
      })
      .catch((err) => console.error(err));
  };

  const removeStructure = () => {
    if (
      window.confirm("Are you sure you want to delete " + props.name + "?") ===
      false
    ) {
      return;
    }

    axios.delete(baseServerURL + "/" + props.id);
    navigate("/");
  };

  return (
    <div className="flexRowStyle">
      <h3>{props.name}</h3>
      <div>
        <div
          onClick={editStructure}
          class="structButtonStyle"
          style={{ marginBottom: "10px" }}
        >
          Edit Structure
        </div>
        <div onClick={removeStructure} class="structButtonStyle">
          Remove Structure
        </div>
      </div>
    </div>
  );
}

export default ListStruct;
