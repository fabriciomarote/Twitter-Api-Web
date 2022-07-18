import React from 'react'
import {
    Route,
    Navigate
  } from "react-router-dom";
import axios from 'axios';

const PrivateRoute = ({path, component}) => {
    const isLogged = !!localStorage.getItem("token");
    if (!isLogged) return <Navigate to="/login" />;
    axios.defaults.headers['authorization'] = localStorage.getItem('token');

    return (
        <Route path={path} element={component}/>
    );
}

export default PrivateRoute;

