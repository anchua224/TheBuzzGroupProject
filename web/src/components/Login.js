import React,{useEffect, useState} from 'react';
import jwt_decode from "jwt-decode";
import axios from 'axios';
import Situation, { getLogin, setSessionKey,logout } from '../Situation';
import Ideas from './Ideas';
import Header from './Header';
import Profile from './Profile';


const Login = () => {

    const [user, setUser] = useState({});
    

    const handleCallbackResponse = async(response) =>{
        // console.log("Encoded JWT ID token: ", response.credential);
        var userObject = jwt_decode(response.credential);
        // console.log(userObject);
        

        document.getElementById("signInDiv").hidden = true;

        axios.post(`https://cse216-fl22-team14.herokuapp.com/login?token=${response.credential}`)
            .then(response =>{
                setSessionKey(response.data);
            })
            .catch(error => {
                console.log(error)
            })

        setUser(userObject);
    }

    function handleSignOut(event){
        setUser({});
        logout();
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
                <div>
                    <Header />
                    <Ideas />
                </div>
            }
        </div>
    )
}

export default Login