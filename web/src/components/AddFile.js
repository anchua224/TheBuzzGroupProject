import React, { useState } from "react";

function FileUploadPage(){
  const [selectedFile, setSelectedFile] = useState();
  const [isFilePicked, setIsFilePicked] = useState(False);

  const changeHandler = (e) => {
    setSelectedFile(e.target.files[0]);
    setIsFilePicked(true);
  };

  const handleSubmission = () => {

  };
  return(
    <div>
      <input type="file" name="file" onChange={changeHandler}/>
      {isSelected ? (
        <div>
          <p>Filename: {</p>
      ) : (

      )}
      <div>
        <button onClick={handleSubmission}>Submit</button>
      </div>
    </div>
  )
}