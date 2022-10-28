import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'dart:developer' as developer;
import 'dart:convert';
import 'package:http/http.dart' as http;

import '../api/google_signin_api.dart';
import 'profile_page.dart';
import 'create_post_page.dart';
import '../main.dart';

class LoginPage extends StatefulWidget {
  const LoginPage({super.key, required this.title});

  final String title;
  @override
  State<LoginPage> createState() => _LoginPage();
}

class _LoginPage extends State<LoginPage> {
  TextEditingController nameController = TextEditingController();
  TextEditingController passwordController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
        centerTitle: true,
      ),
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          Container(
            child: Padding(
              padding: EdgeInsets.all(100.0),
              child: ElevatedButton.icon(
                style: ElevatedButton.styleFrom(
                  backgroundColor: Colors.pink,
                  foregroundColor: Colors.white,
                  fixedSize: const Size(240, 80), 
                  minimumSize: Size(double.infinity, 50),
                ),
                icon: Image.asset(
                  'assets/images/google_icon.png',
                  height: 32,
                  width: 32
                  ),
                label: Text('Sign-in with Google'),
                onPressed: signin, //signin method
                ),
              )

          ),
        ]
      ),
    );
  }
  // Signin Method from GoogleSignInApi
  Future signin() async {
    final user = await GoogleSignInApi.login();
    GoogleSignInAuthentication googleSignInAuthentication = await user!.authentication;

    print(googleSignInAuthentication.accessToken);
    print(googleSignInAuthentication.idToken);
    
    if (user == null) { // If the user login is not valid, show that sign-in failed
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Sign in Failed')));
      GoogleSignInApi.logout();
    } else if (((user.email).split('@'))[1] != 'lehigh.edu'){ // If the user is not under the domain lehigh.edu, login fails
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Unauthorized User')));
      GoogleSignInApi.logout();
    }else {
      // If user logging in is valid, send user to profile page
      Navigator.of(context).pushReplacement(MaterialPageRoute(builder: (context) => ProfilePage(user: user),
      ));
    }
  }
}

