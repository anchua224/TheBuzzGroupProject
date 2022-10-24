import React, { Component } from 'react'
import Ideas from './components/Ideas'
import Login from './components/Login'
import './App.css'


class App extends Component {
  render() {
    return (
      <div className='App'>
        <Login />
        {/* <Ideas /> */}
      </div>
    )
  } 
}

export default App