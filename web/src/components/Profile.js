import React,{ useState,useEffect }  from 'react';
import ReactDOM from 'react-dom';
import axios from 'axios';
import { getSessionKey } from '../Situation';

const PROFILE_STYLES = {
  position: 'fixed',
  top: '33%',
  left: '33%',
  right: '33%',
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

export default function Profile({profileOpen, profileOnClose, user_id}) {

  const [userInfor, serUserInfor] = useState();

  const [note,setNote] = useState();

  const [gi,setGI] = useState();

  const [so,setSO] = useState();

  const getUserInformation = async() =>{
    axios.get(`https://cse216-fl22-team14-new.herokuapp.com/profile/${user_id}?sessionKey=${getSessionKey()}`)
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
    axios.put(`https://cse216-fl22-team14-new.herokuapp.com/profile/${user_id}?sessionKey=${getSessionKey()}`,{
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
  };

  return ReactDOM.createPortal(
    <div style={OVERLAY_STYLE}>
      { userInfor !== undefined  &&
        <div style={PROFILE_STYLES}>
          <h3 className='text'>Name: {userInfor.name}</h3>
          <h3 className='text'>Email: {userInfor.email}</h3>
          <label className='text' id='gi'><b>Gender Identity: </b></label>
          <br></br>
          <select className='text' id="gi" value={gi} onChange={event => setGI(event.target.value)}>
                      <option value="Male">Male</option>
                      <option value="Female">Female</option>
                      <option value="trans_man">Transgender man/trans man</option>
                      <option value="trans_woman">Transgender woman/trans woman</option>
                      <option value="nor">Genderqueer/gender nonconforming neither exclusively male nor female</option>
                      <option value="no_anw">Decline to answer</option>
          </select>
          <br></br>
          <label className='text' id='so'><b>Sexual Orientation: </b></label>
          <br></br>
          <select className='text' id="so" value={so} onChange={event => setSO(event.target.value)}>
                      <option value="Straight">Straight or Heterosexual</option>
                      <option value="Lesbian">Lesbian or Gay</option>
                      <option value="Bisexual">Bisexual</option>
                      <option value="qpq">Queer, Pansexual, and/or Questioning</option>
                      <option value="dont">Don't know</option>
                      <option value="no_anw">Decline to answer</option>
          </select>
          <br></br>
          <label className='text' id='note'><b>Note: </b></label>
          <br></br>
          <input
            placeholder='Quick fun info about you...'
            id='note'
            value={note}
            onChange={event => setNote(event.target.value)}
          />
          <hr></hr>
          <button className='acu-buttons' id="changeButton" onClick={() => changeUserInfor()}>Change</button>
          <button className='acu-buttons' id="cancelCancel" onClick={profileOnClose}>Cancel</button>
        </div>
      }
    </div>,
    document.getElementById('portal')
  )
}
