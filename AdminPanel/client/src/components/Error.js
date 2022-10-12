import React from "react";

/**
 * Used for general error messages.
 */
const Error = ({ error }) => {
  if (error) return <p className="error">Error: {error}</p>;
  else return "";
};

export default Error;
