import 'package:flutter/material.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'package:meta/meta.dart';

import 'profile_page.dart';

// CDC Lists for Sexual Orientation and Gender Identity
const List<String> sexualOrientation = <String>['Straight or Heterosexual', 'Lesbian or Gay', 'Bisexual', 
  'Queer, Pan, and/or Questioning', 'Other', 'Don\'t Know', 'Decline to Answer'];
const List<String> genderIdentity = <String>['Male', 'Female', 'Transgender Man/ Trans Man', 
  'Transgender Woman/ Trans Woman', 'Genderqueer/ gender nonconforming neither exclusively male nor female', 
  'Other', 'Decline to Answer'];

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
    var user = CurrentUser();
    user = initializeUser(widget.user, user);
    // user.setName();
    return Scaffold (
      appBar: AppBar(
        title: const Text('Edit Profile'),
        centerTitle: true,
        actions: [
          ElevatedButton.icon(
            label: const Text('Done'),
            icon: const Icon(Icons.check_circle),
            onPressed: () {
              Navigator.of(context).pushReplacement(MaterialPageRoute(builder: (context) => ProfilePage(user: widget.user),
              ));
            },
          )
        ],
      ),
      body: Column(
        children: [
          Center (
            child: Padding(
                padding: EdgeInsets.only(bottom: 20),
                child: Text(
                  '${user.uid}',
                  style: const TextStyle(
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
  
  CurrentUser initializeUser(GoogleSignInAccount _user, CurrentUser user) {
    user.setID(_user);
    user.setEmail(_user);
    user.setName(_user);
    user.setUID(_user);

    return user;
  }
}

// Class contains information for user currently logged into the app
class CurrentUser {
   CurrentUser();

  // User properties
  late String? id; late String? email; late final String? uid;
  late String? displayName;
  late String? photoURL;
  late String? so;
  late String? gi;
  late String? note;
  GoogleSignInAccount? _user;
  GoogleSignInAccount get user => _user!;

  // Setter Methods
  void setID(GoogleSignInAccount user) {
    id = user.id;
  }
  void setEmail(GoogleSignInAccount user) {
    email = user.email;
  }
  void setName(GoogleSignInAccount user) {
    displayName = user.displayName;
  }
  void setNewName(String newName) {
    displayName = newName;
  }
  void setPhoto(String photoURL) {
    this.photoURL = photoURL;
  }
  void setUID(GoogleSignInAccount user) {
    uid = (((user.email).split('@'))[0]);
  }
  void setGI(String gi) {
    this.gi = gi;
  }
  void setSO(String so) {
    this.so = so;
  }
  void setNote(String note) {
    this.note = note;
  }

}

// class AddUser extends StatelessWidget {
//   final GoogleSignInAccount googleAccount;
//   var newUser = CurrentUser();
//   newUser.id = googleAccount.id;

//   // Future<void> addUser() {
//   // // Call the user's CollectionReference to add a new user
//   // return users
//   //     .add({
//   //       'uid': uid,
//   //       'displayName': displayName,
//   //       'email': email,
//   //       'photoURL': photoURL,
//   //       'lastSeen': DateTime.now()
//   //     })
//   //     .then((value) => print("User Added"))
//   //     .catchError((error) => print("Failed to add user: $error"));
//   // }
// }

