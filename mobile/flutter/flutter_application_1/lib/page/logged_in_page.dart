import 'package:flutter/material.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'package:flutter_application_1/api/google_signin_api.dart';

import '../main.dart';

class LoggedInPage extends StatelessWidget {
  final GoogleSignInAccount user;

  LoggedInPage({
    Key? key,
    required this.user,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) => Scaffold(
    appBar: AppBar(
      title: Text('Welcome Back!'),
      centerTitle: true,
      actions: [
        ElevatedButton(
          child: Text('Logout'),
          onPressed: () async {
            await GoogleSignInApi.logout();

            Navigator.of(context).pushReplacement(MaterialPageRoute(builder: (context) => MyLoginPage(title: 'The Buzz'),
            ));
          },
        )
      ],
    ),
    body: Container(
      alignment: Alignment.center,
      color: Colors.blueGrey.shade900,
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Text(
            'Profile',
            style: TextStyle(fontSize: 24),
          ),
          SizedBox(height: 32),
          CircleAvatar(
            radius: 40,
            //backgroundImage: NetworkImage(user.photoUrl!),
          ),
          SizedBox(height: 8),
          Text(
            'Name' + user.displayName!,
            style: TextStyle(color: Colors.white, fontSize: 12), //Might change font size
          ),
          SizedBox(height: 8),
          Text(
            'Email: ' + user.email,
            style: TextStyle(color: Colors.white, fontSize: 12),
          ),
        ],
      ),
    ),
  );
}

