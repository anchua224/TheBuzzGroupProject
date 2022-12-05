import React,{useEffect, useState} from 'react';
import { getSessionKey } from '../Situation';
import Profile from './Profile';

export default function Header() {

  const [profileIsOpen, setProfileIsOpen] = useState(false);

  return (
    <div id="header">
      <button className="button" id='myProfileButton' onClick={() => setProfileIsOpen(true)}>My Profile</button>
        <Profile 
          profileOpen={profileIsOpen}
          profileOnClose = {() => setProfileIsOpen(false)}
          user_id={getSessionKey()}
      />
    </div>
  )
}
