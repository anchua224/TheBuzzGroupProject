import React,{ useState,useEffect }  from 'react';
import ReactDOM from 'react-dom';
import axios from 'axios';
import { getSessionKey } from '../Situation';

const PROFILE_STYLES = {
  position: 'fixed',
  top: '33%',
  left: '40%',
  right: '40%',
  border: '3px solid pink',
  borderRadius: '20px',
  backgroundColor: '#FFF',
  textAlign: 'center',
  padding: '20px',
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

  const [userInfo, setUserInfo] = useState();

  const getUserInformation = async(e) =>{
    axios.get(`https://cse216-fl22-team14-new.herokuapp.com/profile/${user_id}?sessionKey=${getSessionKey()}`)
        .then(response =>{
          setUserInfo(response.data.mData);
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
      { userInfo !== undefined  &&
        <div style={PROFILE_STYLES}>
          <h3 className='text'>Name: {userInfo.name}</h3>
          <h3 className='text'>Email: {userInfo.email}</h3>
          <h3 className='text'>Note: {userInfo.note}</h3>
          <button className='acu-buttons' id="close" onClick={profileOnClose}>Close</button>
        </div>
      }
    </div>,
    document.getElementById('portal')
  )
}
