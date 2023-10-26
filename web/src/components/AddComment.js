import React,{ useState,useEffect }  from 'react';
import ReactDOM from 'react-dom';
import axios from 'axios';
import { getSessionKey } from '../Situation';

const AddIdea_STYLES = {
  position: 'fixed',
  top: '33%',
  left: '33%',
  right: '33%',
  border: '3px solid pink',
  borderRadius: '20px',
  backgroundColor: '#FFF',
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
export default function AddComment({open, onClose,idea_id}) {
    const [content, setContent] = useState();
    
    if(!open){
        return null;
      }
      const addNewLink = async(e) =>{
        axios.post(`https://cse216-fl22-team14-new.herokuapp.com/resources/:${idea_id}/:${com_id}?sessionKey=${getSessionKey()}`,{
          mLink: link,
        })
        .catch(error => {
          console.log(error.massage)
        });
        onClose();
      }
    const addNewComment = async(e) =>{
      axios.post(`https://cse216-fl22-team14-new.herokuapp.com/ideas/${idea_id}/comment?sessionKey=${getSessionKey()}`,{
        mContent: content
      })
      .then(response => {
        console.log(response.data);
      })
      .catch(error => {
      console.log(error.massage)
      });
      addNewLink();
      onClose();
    }
    
    return ReactDOM.createPortal(
        <div style={OVERLAY_STYLE}>
          <div style={AddIdea_STYLES}>
            <label className='text'><b>Idea Comment:</b></label>
            <input placeholder="Share your thoughts..." type="text" id="newConnent" onChange={(e) => setContent(e.target.value)}/>
            <label className='text'><b>Link</b></label>
            <input placeholder='Provide link here if needed' id="newLink" onChange={(e) => setContent(e.target.value)}/>
            <hr></hr>
            <button className="acu-buttons" id="addButton" onClick={(e) => addNewComment(e)}>Add</button>
            <button className="acu-buttons" id="addCancel" onClick={onClose}>Cancel</button>
            <br></br>
        <button className='acu-buttons' id="addFile" onClick={handleFile} type="file" >Upload File</button>
          </div>
        </div>,
        document.getElementById('portal')
      )
}
