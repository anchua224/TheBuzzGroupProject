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
export default function AddComment({open, onClose,idea_id}) {
    const [content, setContent] = useState();
    
    if(!open){
        return null;
      }
    
    const addNewComment = async(e) =>{
        axios.post(`https://cse216-fl22-team14.herokuapp.com/ideas/${idea_id}/comment?sessionKey=${getSessionKey()}`,{
            mContent: content
        })
            .catch(error => {
                console.log(error.massage)
            });
            onClose();
    }

      
    return ReactDOM.createPortal(
        <div style={OVERLAY_STYLE}>
          <div style={AddIdea_STYLES}>
            <label>Idea Comment:</label>
            <input type="text" id="newConnent" onChange={(e) => setContent(e.target.value)}/>
            <button id="addButton" onClick={(e) => addNewComment(e)}>Add</button>
            <button id="addCancel" onClick={onClose}>Cancel</button>
          </div>
        </div>,
        document.getElementById('portal')
      )
}
