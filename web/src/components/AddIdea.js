import React,{ useState,useEffect }  from 'react';
import ReactDOM from 'react-dom';
import axios from 'axios';
import { getSessionKey } from '../Situation';

const AddIdea_STYLES = {
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

export default function AddIdea({open, onClose}) {

  const [title, setTitle] = useState();
  const [message, setMessage] = useState();

  if(!open){
    return null;
  }

  const addNewIdea = async(e) =>{
    axios.post(`https://cse216-fl22-team14.herokuapp.com/ideas?sessionKey=${getSessionKey()}`,{
      mTitle: title,
      mMessage: message
    })
      .catch(error => {
          console.log(error.massage)
      });
      onClose();
  }

  
  return ReactDOM.createPortal(
    <div style={OVERLAY_STYLE}>
      <div style={AddIdea_STYLES}>
        <label>Idea Title:</label>
        <input type="text" id="newTitle" onChange={(e) => setTitle(e.target.value)}/>
        <label>Idea Message:</label>
        <textarea id="newMessage" onChange={(e) => setMessage(e.target.value)}></textarea>
        <button id="addButton" onClick={(e) => addNewIdea(e)}>Add</button>
        <button id="addCancel" onClick={onClose}>Cancel</button>
      </div>
    </div>,
    document.getElementById('portal')
  )
}
