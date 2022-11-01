import React,{ useState,useEffect }  from 'react';
import ReactDOM from 'react-dom';
import axios from 'axios';
import { getSessionKey } from '../Situation';

/**
 * 
 */
const PROFILE_STYLES = {
  position: 'fixed',
  top: '50%',
  left: '50%',
  transform: 'translate (-50%,-50%)',
  backgroundColor: '#FFF',
  padding: '50px',
  zIndex: 1000
}

const OVERLAY_STYLE = {
  position: 'fixed',
  top:0,
  left:0,
  right:0,
  bottom:0,
  backgroundColor: 'rgba(0,0,0,.7)',
  zIndex:1000
}

export default function ProfileSimple({profileOpen, profileOnClose, user_id}) {

  const [userInfor, serUserInfor] = useState();

  const getUserInformation = async(e) =>{
    axios.get(`https://cse216-fl22-team14.herokuapp.com/profile/${user_id}?sessionKey=${getSessionKey()}`)
        .then(response =>{
          serUserInfor(response.data.mData);
        })
        .catch(error => {
            console.log(error.massage)
        });
  };

  useEffect(() => {
    getUserInformation();
  }, []);

  if(!profileOpen){
    return null;
  };

  return ReactDOM.createPortal(
    <div style={OVERLAY_STYLE}>
      { userInfor !== undefined  &&
        <div style={PROFILE_STYLES}>
          <h3>Name: {userInfor.name}</h3>
          <h3>Email: {userInfor.email}</h3>
          <h3>Note: {userInfor.note}</h3>
          <button id="close" onClick={profileOnClose}>Close</button>
        </div>
      }
    </div>,
    document.getElementById('portal')
  )
}
