import React, { useEffect, useState } from 'react';
import ListStruct from './ListStruct';

const axios = require("axios").default;

export default function StructInformation() {
  const [structInformation, setStructInformation] = useState({});

  useEffect(() => {
    //const baseServerURL = "http://localhost:5000";
    const baseServerURL =
    "https://us-central1-ar-tour-guide-admin-panel.cloudfunctions.net/app";

    axios
      .get(`${baseServerURL}/db`, {
        headers: { "Content-Type": "application/json" },
      })
      .then((res) => {
        // catch errors
        if (res.status !== 200) {
          const message = `An error occurred: ${res.statusText}`;
          return window.alert(message);
        }
        console.log(res.data)
        setStructInformation(Array.from(res.data))
      })
      .catch((err) => console.error(err));
  }, [])

  let structures = null

  if (structInformation.length) {
    structures = structInformation.map((data, id)=> {
      return <ListStruct name={data.scrapedData.buildingName.toString()} id={data._id.toString()} key={id} />
    })
  } else {
    structures = <h3> loading... </h3>
  }

  let listStyle = {
    display: 'flex',
    gap: '10px',
    flexDirection: 'column'
  }
  
  return(
    <div>
      <h2>Structures</h2>
      <div style={listStyle}>
        {structures}
      </div>
    </div>
  )
}