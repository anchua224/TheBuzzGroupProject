import React, { useState,useEffect } from 'react';
import axios from 'axios';
import Idea from './Idea';


// type Idea = {
//     id: number;
//     username: string;
//     description: string;
// };

export default function Ideas() {

    const [Ideas, setIdeas] = useState([]);

    const getPosts = async () => {
        axios.get('https://cse216-fl22-team14.herokuapp.com/ideas')
            .then(response =>{
                console.log(response.data.mData)
                setIdeas(response.data.mData)
            })
            .catch(error => {
                console.log(error)
            })
    };
    
    useEffect(() => {
        getPosts();
    }, [])

    return (
    <div>
        {Ideas.map(idea => {
            return (
                <div className='ideaList' key={idea.id}>
                    <Idea 
                        id={idea.id}
                        title={idea.title}
                        massage={idea.massage}
                        createDate={idea.createdDate}
                    />
                </div>
            )
        })}
    </div>
    )
    
}