import React, { useState,useEffect } from 'react';
import axios from 'axios';
import { getSessionKey } from '../Situation';
import Comment from './Comment';
import AddComment from './AddComment';

export default function Comments(props) {

  
  const [isOpen, setIsOpen] = useState(false);

  const [Comments, setComments] = useState([]);

  const getComments = async () => {
    
    axios.get(`https://cse216-fl22-team14.herokuapp.com/ideas/${props.idea_id}/comment`)
        .then(response =>{
            setComments(response.data.mData);
        })
        .catch(error => {
            console.log(error);
        })
  };

  useEffect(() => {
    getComments();
  }, [isOpen])
  
  

  return (
    <div>
      <b>Comments:</b>
      {Comments.map(comment => {
            return (
                <div className='commentList' key={comment.com_id}>
                  
                    <Comment 
                        idea_id={comment.idea_id}
                        user_id={comment.user_id}
                        com_id={comment.com_id}
                        content={comment.content}
                        date={comment.createdDate}
                    />
                </div>
            )
        })}
      <button onClick={() => setIsOpen(true)}> Add Comments</button>
      <AddComment 
          idea_id={props.idea_id}
          open={isOpen}
          onClose = {() => {setIsOpen(false);getComments();}}
      />
    </div>
  )
}
