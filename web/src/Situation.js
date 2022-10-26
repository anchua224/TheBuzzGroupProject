import React from 'react'

let sessionKey = null;
let loginCheck = false;

export const setSessionKey = (key) =>{
    sessionKey = key;
    loginCheck = true;
}

export const getSessionKey = (key) =>{
    return sessionKey;
}

export const getLogin = (key) =>{
    return loginCheck;
}

export default function Situation() {
  return (
    <div>Situation</div>
  )
}
