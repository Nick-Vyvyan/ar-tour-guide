import React, { createContext, useState, useEffect } from "react";
import Spinner from "react-bootstrap/Spinner";
import { getAuth, signOut, onAuthStateChanged } from "firebase/auth";
// eslint-disable-next-line
import app from "../util/firebase";

// initial state to be changed if user is signed in or out
const initialAuthState = {
  isAuthenticated: false,
  initializing: true,
  user: null,
};

// context of auth to be used throughout app
const AuthContext = createContext({
  ...initialAuthState,
  logout: () => Promise.resolve(),
});

// set context based on auth state
export const AuthProvider = ({ children }) => {
  const [state, setState] = useState(initialAuthState);

  const logout = () => {
    const auth = getAuth();
    return signOut(auth);
  };

  const setLogin = (user) => {
    setState({
      isAuthenticated: true,
      initializing: false,
      user: user,
    });
  };

  const setLogout = (user) => {
    setState({
      isAuthenticated: false,
      initializing: false,
      user: null,
    });
  };

  useEffect(() => {
    const auth = getAuth();
    const unsubscribe = onAuthStateChanged(auth, (user) => {
      if (user) {
        // user is logged in
        setLogin(user);
      } else {
        // user is logged out
        setLogout(user);
      }
    });

    return unsubscribe;
  }, []);

  // page still loading
  if (state.initializing) {
    return <Spinner />;
  }

  return (
    <AuthContext.Provider
      value={{
        ...state,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export default AuthContext;
