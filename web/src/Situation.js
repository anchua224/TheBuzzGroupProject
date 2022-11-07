import React from 'react'

let sessionKey = null;
let loginCheck = false;

export const setSessionKey = (key) =>{
    sessionKey = key;
    loginCheck = true;
}

export const getSessionKey = () =>{
    return sessionKey;
}


export const getLogin = () =>{
    return loginCheck;
}

export const logout = () =>{
  sessionKey = null;
  loginCheck = false;
}

export default function Situation() {
  return (
    <div>Situation</div>
  )
}
