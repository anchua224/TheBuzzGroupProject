import React, { useState,useEffect } from 'react';
import axios from 'axios';
import Comments from './Comments';

export default function Idea(props) {

    const [like, setLike] = useState();
    const [dislike, setDislike] = useState();

    const getLike = async () => {
        axios.get(`https://cse216-fl22-team14.herokuapp.com/likes/${props.id}`)
            .then(response =>{
                console.log(response);
                console.log(response.data.mData);
                setLike(response.data.mData);
                console.log(like)
            })
            .catch(error => {
                console.log(error);
            })
    };

    const getDislike = async () => {
        axios.get(`https://cse216-fl22-team14.herokuapp.com/ideas/${props.id}/dislike`)
            .then(response =>{
                console.log(response);
                setDislike(response.data.mData);
            })
            .catch(error => {
                console.log(error);
            })
    };

    const handleLike = async(e) =>{


        axios.post(`https://cse216-fl22-team14.herokuapp.com/likes/${props.id}`);

        getLike();
    }

    const handleDisike = async(e) =>{


        axios.post(`https://cse216-fl22-team14.herokuapp.com/dislikes/${props.id}`);

        getDislike();
    }

    useEffect(()=>{
        getLike();
        getDislike();
    },[]);

    return (
        <div className='idea' key={props.id}>
            <h3>{props.title}</h3>
            <h5>{props.massage}</h5>
            <p>{props.createDate}</p>
            <button id={props.id}  onClick={(e) => handleLike(e)}>
                Like: {like}
            </button>
            <button id={props.id}  onClick={(e) => handleDisike(e)}>
                DisLike: {dislike}
            </button>
            <Comments />
        </div>
    )
}
