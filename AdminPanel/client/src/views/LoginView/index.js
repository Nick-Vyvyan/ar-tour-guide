// react imports
import React, { useState, useRef } from "react";
import { getAuth, signInWithEmailAndPassword } from "firebase/auth";
import { Navigate } from "react-router-dom";
import useAuth from "../../hook/useAuth";
import Error from "../../components/Error";

// components
import Container from "react-bootstrap/Container";
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";

const LoginView = (props) => {
  // signed in status
  const { user } = useAuth();
  // references to user info
  const emailRef = useRef(null);
  const passwordRef = useRef(null);
  // errors to display
  const [error, setError] = useState(null);

  // FirebaseAuth config
  const signin = (e) => {
    e.preventDefault();

    // sign in with firebase
    const auth = getAuth();
    signInWithEmailAndPassword(
      auth,
      emailRef.current.value,
      passwordRef.current.value
    )
      .then((userCredential) => {
        console.log(userCredential.user);
      })
      .catch((err) => {
        setError(err.message);
        console.log(err.message);
      });
  };

  // let user see admin panel once signed in
  if (user) return <Navigate to={{ pathname: "/" }} />;

  return (
    <Container>
      <h1>Login to the Admin Panel</h1>
      <br />
      <Form onSubmit={signin}>
        <Form.Group className="mb-3">
          <Form.Label>Email</Form.Label>
          <Form.Control
            required
            ref={emailRef}
            type="email"
            placeholder="Enter email"
          />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Password</Form.Label>
          <Form.Control
            required
            ref={passwordRef}
            type="password"
            placeholder="Enter password"
          />
        </Form.Group>
        {/* display all errors */}
        {error && <Error error={error} />}

        <Button variant="primary" type="submit">
          Submit
        </Button>
      </Form>
      <br />
    </Container>
  );
};

export default LoginView;
