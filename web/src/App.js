import React, { Component, useState,useEffect } from 'react';
import './App.css';
import Login from './components/Login';
import "./styles.css"

class App extends Component {
  render() {
    return (
      <div className='App'>
        <Login />
      </div>
    )
  } 
}

export default App