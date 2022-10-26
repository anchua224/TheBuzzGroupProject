import 'package:flutter/material.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'package:flutter_application_1/api/google_signin_api.dart';

import '../main.dart';
import 'profile_page.dart';


class EditProfilePage extends StatefulWidget {
  final GoogleSignInAccount user;

  const EditProfilePage({
    Key? key,
    required this.user,
  }) : super(key: key);

  @override
  State<StatefulWidget> createState() => EditProfileState();
}


class EditProfileState extends State<EditProfilePage> {
  @override
  Widget build(BuildContext context) {
    GoogleSignInAccount? user;
    return Scaffold (
      appBar: AppBar(
        title: const Text('Edit Profile'),
        centerTitle: true,
        actions: [
          ElevatedButton.icon(
            label: const Text('Done'),
            icon: Icon(Icons.check_circle),
            onPressed: () {
              Navigator.of(context).pushReplacement(MaterialPageRoute(builder: (context) => ProfilePage(user: widget.user),
              ));
            },
          )
        ],
      ),
      body: Column(
        children: const [
          Center (
            child: Padding(
                padding: EdgeInsets.only(bottom: 20),
                child: Text(
                  'Edit Profile',
                  style: TextStyle(
                    fontSize: 30,
                    fontWeight: FontWeight.w700,
                    color: Colors.pink,
                    ),
                  textAlign: TextAlign.left,
                  )
                ),
          )
        ],
      ),
    );
  }
}


// class AddUser extends StatelessWidget {
//   final String uid;
//   final String email;
//   final String displayName;
//   final String photoURL;

//   AddUser(this.uid, this.email, this.displayName, this.photoURL);
  
//   @override
//   Widget build(BuildContext context) {
//     // TODO: implement build
//     throw UnimplementedError();
//   }
//   Future<void> addUser() {
//   // Call the user's CollectionReference to add a new user
//   return users
//       .add({
//         'uid': uid,
//         'displayName': displayName,
//         'email': email,
//         'photoURL': photoURL,
//         'lastSeen': DateTime.now()
//       })
//       .then((value) => print("User Added"))
//       .catchError((error) => print("Failed to add user: $error"));
//   }
// }

