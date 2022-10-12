// react imports
import React, { Suspense, lazy } from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { AuthProvider } from "./context/FirebaseAuthContext";

// styles
import "./App.css";
import "bootstrap/dist/css/bootstrap.min.css";

// components
import Spinner from "react-bootstrap/Spinner";

// views
const Header = lazy(() => import("./views/Header"));
const LoginView = lazy(() => import("./views/LoginView"));
const PanelView = lazy(() => import("./views/PanelView"));

const App = () => {
  return (
    <Router>
      <AuthProvider>
        <Routes>
          <Route path="/">
            <Route
              index
              element={
                <Suspense fallback={<Loading />}>
                  <Header />
                  <PanelView />
                </Suspense>
              }
            />
            <Route
              path="/login"
              element={
                <Suspense fallback={<Loading />}>
                  <Header />
                  <LoginView />
                </Suspense>
              }
            />
            <Route
              path="/panel"
              element={
                <Suspense fallback={<Loading />}>
                  <Header />
                  <PanelView />
                </Suspense>
              }
            />
          </Route>
        </Routes>
      </AuthProvider>
    </Router>
  );
};

const Loading = () => {
  return (
    <div className="App">
      <Spinner animation="border" />
    </div>
  );
};

export default App;
