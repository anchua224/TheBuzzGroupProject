import React, { useState,useEffect } from 'react';
import axios from 'axios';
import { getSessionKey } from '../Situation';
import EditComment from './EditComment';
import ProfileSimple from './ProfileSimple';

export default function Comment(props) {

    const [IsEditable, setIsEditable] = useState(false);
    const [User, setUser] = useState();
    const [content, setContent] = useState();
    const [isOpen, setIsOpen] = useState(false);
  
    const [profileIsOpen, setProfileIsOpen] = useState(false);

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
                setContent(response.data.mData.content);
            })
            .catch(error => {
                console.log(error);
            })
    };

    const getUserInfor = async () => {
        axios.get(`https://cse216-fl22-team14.herokuapp.com/profile/${props.user_id}`)
          .then(response =>{
            setUser(response.data.mData.name);
            if(getSessionKey() === response.data.mData.user_id){
                setIsEditable(true);
            }
          })
          .catch(error => {
            console.log(error);
          })
    }

    useEffect(() => {
        getComment();
        getUserInfor();
    }, [isOpen])

    
    const handleEdit = async(e) =>{


        axios.post(`https://cse216-fl22-team14.herokuapp.com/ideas/${props.idea_id}/comment/${props.com_id}?sessionKey=${getSessionKey()}`)
            .catch(error => {
                console.log(error.massage)
            });
        getComment();
    }


    return (
        <div className='comment' key={props.com_id}>
            <h5>{
                    <button id={props.com_id}  onClick={() => setProfileIsOpen(true)}>
                    {User}
                    </button>
            }: {content} {IsEditable  &&
                    <button onClick={() => setIsOpen(true)}>Edit</button>
            }</h5>
            <EditComment
                        idea_id={props.idea_id}
                        com_id={props.com_id}
                        open={isOpen}
                        onClose = {() => {setIsOpen(false);getComment();}}
                        old_content={props.content}
            />
            <ProfileSimple 
                    profileOpen={profileIsOpen}
                    profileOnClose = {() => setProfileIsOpen(false)}
                    user_id={props.user_id}
            />
        </div>
    )
}
