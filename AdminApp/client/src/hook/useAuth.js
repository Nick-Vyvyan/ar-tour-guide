import { useContext } from "react";
import AuthContext from "../context/FirebaseAuthContext";

// use auth context in app
const useAuth = () => useContext(AuthContext);

export default useAuth;
