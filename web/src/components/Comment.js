import React, { useState,useEffect } from 'react';
import axios from 'axios';
import { getSessionKey } from '../Situation';

export default function Comment(props) {

    
    const [content, setContent] = useState();

    const editComment = async () => {
        axios.get(`https://cse216-fl22-team14.herokuapp.com/ideas/${props.id}/comment/${props.com_id}?sessionKey=${getSessionKey()}`,{
                content: content
            })
            .then((response) =>{
                console.log(response);
            })
            .catch((error) => {
                console.log(error);
            })
    };

    const getComment = async (props) => {
        axios.get(`https://cse216-fl22-team14.herokuapp.com/ideas/${props.id}/comment/${props.com_id}`)
            .then(response =>{
                console.log(response);
                props.content = response.data.mData.content;
            })
            .catch(error => {
                console.log(error);
            })
    };

    useEffect(() => {
        getComment();
    }, [])

    return (
        <div className='comment' key={props.com_id}>
            <h5>{props.content}</h5>
        </div>
    )
}
