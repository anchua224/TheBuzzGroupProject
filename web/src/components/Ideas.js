import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Idea from './Idea';
import AddIdea from './AddIdea';

export default function Ideas() {
  const [Ideas, setIdeas] = useState([]);
  const [isOpen, setIsOpen] = useState(false);

  const getPosts = async () => {
    axios.get('https://cse216-fl22-team14-new.herokuapp.com/ideas')
      .then(response => {
        setIdeas(response.data.mData);
      })
      .catch(error => {
        console.log(error)
      })
  };

  useEffect(() => {
    getPosts();
  }, [isOpen])

  return (
    <div>
      <AddIdea
        open={isOpen}
        onClose={() => { setIsOpen(false); getPosts(); }}
      />
      {Ideas.map(idea => {
        return (
          <div className='ideaList' key={idea.id}>
            <Idea
              id={idea.id}
              title={idea.title}
              massage={idea.massage}
              createDate={idea.createdDate}
              validity={idea.validity}
              user_id={idea.userid}
            />
          </div>
        )
      })}
    </div>
  )
}


