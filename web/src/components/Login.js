import React,{useEffect, useState} from 'react';
import jwt_decode from "jwt-decode";
import axios from 'axios';

const Login = () => {

    const [user, setUser] = useState({});

    function handleCallbackResponse(response){
        console.log("Encoded JWT ID token: ", response.credential);
        var userObject = jwt_decode(response.credential)
        console.log(userObject);
        setUser(userObject);

        document.getElementById("signInDiv").hidden = true;


        console.log('https://cse216-fl22-team14.herokuapp.com/login/',response.credential);
        axios.post('https://cse216-fl22-team14.herokuapp.com/login',{
                params: {
                    token: response.credential,
                },
            })
            .then(response =>{
                console.log(response)
            })
            .catch(error => {
                console.log(error)
            })
    }

    function handleSignOut(event){
        setUser({});
        document.getElementById("signInDiv").hidden = false;
    }

    useEffect(() =>{
        /* global google */
        google.accounts.id.initialize({
            client_id: "259939040609-4gh5cug157ecmc3igr6qtpqojjl6813g.apps.googleusercontent.com",
            callback: handleCallbackResponse
        })

        google.accounts.id.renderButton(
            document.getElementById("signInDiv"),
            {theme: "outline", size: "large"}
        )

        google.accounts.id.prompt();
    },[]);

    return (
        <div className='login'>
            <div id='signInDiv'></div>
            {   Object.keys(user).length !== 0 && 
                <button onClick={ (e) => handleSignOut} >Sign out</button>
            }
            { user &&
                <div className='userInfor'>
                    <img src={user.picture} alt="user image"></img>
                    <h3>{user.name}</h3>
                    <h3>{user.email}</h3>
                </div>
            }
        </div>
    )
}

export default Login