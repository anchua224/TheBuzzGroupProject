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

export default function Profile({profileOpen, profileOnClose, user_id}) {

  const [userInfor, serUserInfor] = useState();

  const [note,setNote] = useState();

  const [gi,setGI] = useState();

  const [so,setSO] = useState();

  const getUserInformation = async() =>{
    axios.get(`https://cse216-fl22-team14.herokuapp.com/profile/${user_id}?sessionKey=${getSessionKey()}`)
        .then(response =>{
          serUserInfor(response.data.mData);
          setGI(response.data.mData.GI);
          setSO(response.data.mData.SO);
          setNote(response.data.mData.note);
        })
        .catch(error => {
            console.log(error.massage)
        });
  };
  
  const changeUserInfor = async() =>{
    axios.put(`https://cse216-fl22-team14.herokuapp.com/profile/${user_id}?sessionKey=${getSessionKey()}`,{
      mName: userInfor.name,
      mGI: gi,
      mSO: so,
      mNote: note
    })
        .then(response =>{
          serUserInfor(response.data.mData);
        })
        .catch(error => {
            console.log(error.massage)
        });
        profileOnClose();
  };

  useEffect(() => {
    getUserInformation();
  }, [profileOpen]);

  if(!profileOpen){
    return null;
  }else{
  };

  return ReactDOM.createPortal(
    <div style={OVERLAY_STYLE} data-testid='profile'>
      { userInfor !== undefined  &&
        <div style={PROFILE_STYLES}>
          <h3>Name: {userInfor.name}</h3>
          <h3>Email: {userInfor.email}</h3>
          <label id='gi'><b>GI: </b></label>
          <select id="gi" value={gi} onChange={event => setGI(event.target.value)}>
                      <option value="Male">Male</option>
                      <option value="Female">Female</option>
                      <option value="trans_man">Transgender man/trans man</option>
                      <option value="trans_woman">Transgender woman/trans woman</option>
                      <option value="nor">Genderqueer/gender nonconforming neither exclusively male nor female</option>
                      <option value="no_anw">Decline to answer</option>
          </select>
          <br></br>
          <label id='so'><b>SO: </b></label>
          <select id="so" value={so} onChange={event => setSO(event.target.value)}>
                      <option value="Straight">Straight or heterosexual</option>
                      <option value="Lesbian">Lesbian or gay</option>
                      <option value="Bisexual">Bisexual</option>
                      <option value="qpq">Queer, pansexual, and/or questioning</option>
                      <option value="dont">Don't know</option>
                      <option value="no_anw">Decline to answer</option>
          </select>
          <br></br>
          <label id='note'><b>Note: </b></label>
          <input
            id='note'
            value={note}
            onChange={event => setNote(event.target.value)}
          />
          <button id="changeButton" onClick={() => changeUserInfor()}>Change</button>
          <button id="cancelCancel" onClick={profileOnClose}>Cancel</button>
        </div>
      }
    </div>,
    document.getElementById('portal')
  )
}
