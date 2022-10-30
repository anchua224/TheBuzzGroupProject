import React,{useEffect, useState} from 'react';
import AddIdea from './AddIdea';

export default function Header() {

  const [isOpen, setIsOpen] = useState(false);
  return (
    <div id="header">
        <button onClick={() => setIsOpen(true)}> AddIdea </button>
        <AddIdea 
          open={isOpen}
          onClose = {() => setIsOpen(false)}
        />
    </div>
  )
}
