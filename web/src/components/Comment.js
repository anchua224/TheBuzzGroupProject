import React, { useState,useEffect } from 'react';
import axios from 'axios';
import { getSessionKey } from '../Situation';
import EditComment from './EditComment';

export default function Comment(props) {

    const [IsEditable, setIsEditable] = useState(false);
    const [CommentNow, setCommentNow] = useState();
    const [User, setUser] = useState();
    
    const [isOpen, setIsOpen] = useState(false);

    const editComment = async () => {
        axios.get(`https://cse216-fl22-team14.herokuapp.com/ideas/${props.idea_id}/comment/${props.com_id}?sessionKey=${getSessionKey()}`,{
                // content: content
            })
            .then((response) =>{
                console.log(response);
            })
            .catch((error) => {
                console.log(error);
            })
    };

    const getComment = async () => {
        axios.get(`https://cse216-fl22-team14.herokuapp.com/ideas/${props.idea_id}/comment/${props.com_id}`)
            .then(response =>{
                console.log(response);
                props.content = response.data.mData.content;
            })
            .catch(error => {
                console.log(error);
            })
    };

    const getUserInfor = async () => {
        axios.get(`https://cse216-fl22-team14.herokuapp.com/profile/${props.user_id}`)
          .then(response =>{
            setUser(response.data.mData.name);
            console.log(response.data.mData.name);
            console.log(getSessionKey());
            if(getSessionKey() === response.data.mData.user_id){
                setIsEditable(true);
                console.log(IsEditable);
            }
          })
          .catch(error => {
            console.log(error);
          })
    }

    useEffect(() => {
        // getComment();
        getUserInfor();
    }, [CommentNow])

    
    const handleEdit = async(e) =>{


        axios.post(`https://cse216-fl22-team14.herokuapp.com/ideas/${props.idea_id}/comment/${props.com_id}?sessionKey=${getSessionKey()}`)
            .catch(error => {
                console.log(error.massage)
            });
    }

    const checkUser = async(e) =>{

    }

    return (
        <div className='comment' key={props.com_id}>
            <h5>{
                <button id={props.com_id}  onClick={(e) => checkUser(e)}>
                {User}
                </button>
            }: {props.content} {IsEditable  &&
                    <button onClick={() => setIsOpen(true)}>Edit</button>
            }</h5>
            <EditComment
                        idea_id={props.idea_id}
                        com_id={props.com_id}
                        open={isOpen}
                        onClose = {() => setIsOpen(false)}
                        old_content={props.content}
            />
        </div>
    )
}
