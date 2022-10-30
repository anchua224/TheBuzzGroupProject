import React, { useState,useEffect } from 'react';
import axios from 'axios';
import Comments from './Comments';
import { getSessionKey } from '../Situation';

export default function Idea(props) {

    const [like, setLike] = useState();
    const [dislike, setDislike] = useState();

    const getLike = async () => {
        axios.get(`https://cse216-fl22-team14.herokuapp.com/ideas/${props.id}/like`)
            .then(response =>{
                setLike(response.data.mData);
            })
            .catch(error => {
                console.log(error);
            })
    };

    const getDislike = async () => {
        axios.get(`https://cse216-fl22-team14.herokuapp.com/ideas/${props.id}/dislike`)
            .then(response =>{
                setDislike(response.data.mData);
            })
            .catch(error => {
                console.log(error);
            })
    };

    const handleLike = async(e) =>{

        axios.post(`https://cse216-fl22-team14.herokuapp.com/ideas/${props.id}/like?sessionKey=${getSessionKey()}`)
            .catch(error => {
                console.log(error.massage)
            });

        getLike();
        getDislike();
    }

    const handleDisike = async(e) =>{


        axios.post(`https://cse216-fl22-team14.herokuapp.com/ideas/${props.id}/dislike?sessionKey=${getSessionKey()}`)
            .catch(error => {
                console.log(error.massage)
            });
            
        getLike();
        getDislike();
    }

    useEffect(()=>{
        getLike();
        getDislike();
    },[like,dislike]);

    return (
        <div className='idea' key={props.id}>
            <h3>{props.title}</h3>
            <h5>{props.massage}</h5>
            {/* <p>{props.createDate}</p> */}
            <button id={props.id}  onClick={(e) => handleLike(e)}>
                Like: {like}
            </button>
            <button id={props.id}  onClick={(e) => handleDisike(e)}>
                DisLike: {dislike}
            </button>
            <Comments 
                idea_id={props.id}
            />
        </div>
    )
}
