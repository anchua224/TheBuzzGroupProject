import React, { useState,useEffect } from 'react';
import axios from 'axios';
import { getSessionKey } from '../Situation';
import Comment from './Comment';

export default function Comments() {

  const [Comment, setComment] = useState([]);

  const getComments = async (props) => {
    axios.get(`https://cse216-fl22-team14.herokuapp.com/ideas/${props.id}/comment`)
        .then(response =>{
            console.log(response);
            setComment(response.data.mData);
        })
        .catch(error => {
            console.log(error);
        })
  };

  useEffect(() => {
    getComments();
  }, [])


  return (
    <div>
      {Comment.map(comment => {
            return (
                <div className='commentList' key={comment.id}>
                    <Comment 
                        id={comment.id}
                        user_id={comment.user_id}
                        com_id={comment.com_id}
                        content={comment.content}
                    />
                </div>
            )
        })}
    </div>
  )
}
