import React from 'react'
import {
    Route,
    Navigate
  } from "react-router-dom";

const PublicRoute = ({path, component}) => {
    const isLogged = !!localStorage.getItem("token");
    if (isLogged) return <Navigate to="/home" />;

    return (
        <Route path={path} element={component}/>
    );
}

export default PublicRoute;

