import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Header from './Header';
import AddIdea from "./AddIdea";

export default function Navbar() {
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

  return (<nav className="nav">
    <a className="site-title">The Buzz</a>
    <ul>
      <li>
        <Header />
        <button className="button" id='addIdeaButton' onClick={() => setIsOpen(true)}> Create Idea </button>
        <AddIdea
          open={isOpen}
          onClose={() => { setIsOpen(false); getPosts()}}
        />
      </li>
    </ul>
  </nav>)
}