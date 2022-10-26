import React from 'react'

export default function Header() {
  return (
    <div id="header">
        <button id={props.id}  onClick={(e) => handleLike(e)}>
                Profile
        </button>
    </div>
  )
}
