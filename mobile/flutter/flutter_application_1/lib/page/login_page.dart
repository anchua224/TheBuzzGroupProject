import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'package:http/http.dart' as http;

import '../api/google_signin_api.dart';
import 'profile_page.dart';
import '../objects/user.dart';

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
              padding: const EdgeInsets.all(100.0),
              child: ElevatedButton.icon(
                style: ElevatedButton.styleFrom(
                  backgroundColor: Colors.pink,
                  foregroundColor: Colors.white,
                  fixedSize: const Size(240, 80), 
                  minimumSize: const Size(double.infinity, 50),
                ),
                icon: Image.asset(
                  'assets/images/google_icon.png',
                  height: 32,
                  width: 32
                  ),
                label: const Text('Sign-in with Google'),
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

    //print(googleSignInAuthentication.accessToken);
    //print(googleSignInAuthentication.idToken);

    // If user is not in database, create new user object and send to a new page
      // to complete their profile information
    var currentUser = User(user);
    //currentUser = User(user).initializeUser(user, currentUser);
    // Else, pull user information from database and send to home page
    
    if (user == null) { // If the user login is not valid, show that sign-in failed
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Sign in Failed')));
      GoogleSignInApi.logout();
    } else if (((user.email).split('@'))[1] != 'lehigh.edu'){ // If the user is not under the domain lehigh.edu, login fails
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Unauthorized User')));
      GoogleSignInApi.logout();
    }else {
      // If user logging in is valid, send user to profile page
      // FIXME: change to send user to home page
      String? idToken = googleSignInAuthentication.idToken;
      print(idToken);
      print(idToken!.substring(1023));
      print(user.id);
       final response = await http.post(
        Uri.parse('https://cse216-fl22-team14.herokuapp.com/login?token=$idToken'),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
        });
         if (response.statusCode == 200) {
            // If the server did return a 200 CREATED response,
            // then parse the JSON.
            print(response.body);
            currentUser.setSessionKey((response.body).split('"')[1]);
            print(currentUser.sessionKey);
          } else {
            // If the server did not return a 200 CREATED response,
            // then throw an exception.
            throw Exception('Failed to get session key.');
          }
     // currentUser.setSessionKey(key!);
      // Get user information from database
      currentUser = await getUserInfo(currentUser);
      Navigator.of(context).pushReplacement(MaterialPageRoute(builder: (context) => ProfilePage(user: currentUser),
      ));
    }
  }
  Future<User> getUserInfo(User user) async{
    final response = await http
        .get(Uri.parse('https://cse216-fl22-team14.herokuapp.com/profile/${user.sessionKey}'));
    if (response.statusCode == 200) {
      // If the server did return a 200 OK response, then parse the JSON.
      final List<dbUser> returnData;
      var res = jsonDecode(response.body);
      res = res['mData'];
      // print('json decode: $res');
      user.setSO(res['SO']);
      // print('json so: ${res['SO']}');
      user.setNewName(res['name']);
      user.setGI(res['GI']);
      user.setNote(res['note']);
      return user;
    } else {
      // If the server did not return a 200 OK response,
      // then throw an exception.
      throw Exception('Did not receive success status code from request.');
    }
  }
}

