import React, { Component } from 'react'
import Ideas from './components/Ideas'
import Login from './components/Login'
import './App.css'
import { getLogin } from './Situation'


class App extends Component {
  render() {
    return (
      <div className='App'>
        { getLogin() === false &&
          <Login />
        }
        { getLogin() === false &&
          <Ideas />
        }
      </div>
    )
  } 
}

export default App