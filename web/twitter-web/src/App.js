import React from 'react';
import MainPage from './components/MainPage';
import Login from './components/Login';
import Register from './components/Register';
import Home from './components/Home';
import Profile from './components/Profile';
import PublicRoute from './routes/PublicRoute';
import PrivateRoute from './routes/PrivateRoute';
import User from './components/User';
import Tweet from './components/Tweet';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Search from './components/Search';
import AddTweet from './components/AddTweet';

const App = () => {
  return (
    <BrowserRouter>  
      <Routes>
        <Route exact path="/mainPage" element={<MainPage/>}/>
        <Route path="/login" element={<Login/>}/>
        <Route path="/register" element={<Register/>}/>
        <Route path="/home" element={<Home/>}/>
        <Route path="/profile" element={<Profile/>}/>
        <Route path="/user/:id" element={<User/>}/>
        <Route path="/search" element={<Search/>}/>
        <Route path="/tweet/:id" element={<Tweet/>}/>
        <Route path="/tweet" element={<AddTweet/>}/>
        <Route path="*" element={<MainPage/>}/>
      </Routes>     
    </BrowserRouter>
  );
}

export default App;