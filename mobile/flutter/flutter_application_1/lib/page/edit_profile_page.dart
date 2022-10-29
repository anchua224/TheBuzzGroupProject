import 'package:flutter/material.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'package:meta/meta.dart';

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
  // CDC Lists for Sexual Orientation
  final SOMenu = [
    ' Straight or Heterosexual',
    ' Lesbian or Gay',
    ' Bisexual',
    ' Queer, Pan, and/or Questioning',
    ' Other', //If selected other, create a input textbox
    ' Don\'t Know',
    ' Decline to Answer'
  ];
  // CDC Lists for Gender Identity
  final GIMenu = [
    ' Male',
    ' Female',
    ' Transgender Man/ Trans Man',
    ' Transgender Woman/ Trans Woman',
    ' Genderqueer/ gender nonconforming neither exclusively male nor female', 
    ' Decline to Answer'
  ];
  String? valueSO = null; 
  bool hintSO = true;
  String? valueGI = null; 

  // The field in which users can text
  final displayNameController = TextEditingController();
  final noteController = TextEditingController();

  @override
  void dispose() {
    displayNameController.dispose();
    noteController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    var user = CurrentUser();
    user = initializeUser(widget.user, user);
    if (user.so != "n/a") {
      valueSO = user.so;
      hintSO = false;
    }
    if (user.gi != "n/a") {
      valueGI = user.gi;
    }
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
        body: SingleChildScrollView(
          child: Padding(
          padding: const EdgeInsets.fromLTRB(20, 5, 20, 10),
          child: Column(
          crossAxisAlignment: CrossAxisAlignment. start,
          children: [
            const SizedBox(height: 15),
            Center(
              child: GestureDetector(
                child: CircleAvatar(
                    radius: 45,
                    backgroundColor: Color.fromARGB(255, 251, 207, 126),),
                onTap: () async{
                  // when picture is tapped, user can upload an image
                },
              ),
            ),
            SizedBox(height: 20),
            const Text(
              'Private Information',
              style: TextStyle(fontSize: 20, color: Colors.pink, fontWeight: FontWeight.bold),
            ),
            const Text(
              'Email',
              style: TextStyle(fontSize: 14, color: Color.fromARGB(175, 233, 30, 98),),
            ),
            Column(
              children: [
                Container (
                  width: 375,
                  height: 40,
                  alignment: Alignment.centerLeft,
                  decoration: BoxDecoration(
                    color:  Color.fromARGB(35, 233, 30, 98),
                    border: Border.all(
                      color: Colors.pink,
                      width: 1,
                    ),
                    borderRadius: BorderRadius.circular(5),
                    ),
                  child: Text(
                  ' ${user.email}',
                  style: const TextStyle(color: Colors.black, fontSize: 16,),
                  textAlign: TextAlign.left,
                  ),
                ),
                const SizedBox(height: 5),
                Container (
                  alignment: Alignment.centerLeft,
                  child: const Text(
                    'Sexual Orientation',
                    style: TextStyle(fontSize: 14, color: Color.fromARGB(175, 233, 30, 98),),
                  ),
                ),
                Container(
                  alignment: Alignment.centerLeft,
                  width: 375,
                  height: 50, 
                  decoration: BoxDecoration(
                    color: Colors.white,
                    border: Border.all(
                      color: const Color.fromARGB(150, 233, 30, 98),
                      width: 1,
                    ),
                    borderRadius: BorderRadius.circular(5),
                    ),
                  child: DropdownButton(
                    isExpanded: true,
                    disabledHint: const Text("Select Sexual Orientation"),
                    // Initial Value
                    value: valueSO,
                    // Down Arrow Icon
                    icon: const Icon(Icons.keyboard_arrow_down),   
                    // Array list of items
                    items: SOMenu.map((String SOMenu) {
                      return DropdownMenuItem(
                        value: SOMenu,
                        child: Text(SOMenu),
                      );
                    }).toList(),
                    // After selecting the desired option,it will
                    // change button value to selected value
                    onChanged: (String? value) {
                      setState(() {
                        valueSO = value ?? "";
                      });
                      user.setSO(value!);
                    },
                  ),
                ),
                const SizedBox(height: 5),
                Container (
                  alignment: Alignment.centerLeft,
                  child: const Text(
                    'Gender Identity',
                    style: TextStyle(fontSize: 14, color: Color.fromARGB(175, 233, 30, 98),),
                  ),
                ),
                Container(
                  alignment: Alignment.centerLeft,
                  width: 375,
                  height: 50, 
                  decoration: BoxDecoration(
                    color: Colors.white,
                    border: Border.all(
                      color: const Color.fromARGB(150, 233, 30, 98),
                      width: 1,
                    ),
                    borderRadius: BorderRadius.circular(5),
                    ),
                  child: DropdownButton(
                    isExpanded: true,
                    disabledHint: const Text("Select Gender Identity"),
                    // Initial Value
                    value: valueGI,
                    // Down Arrow Icon
                    icon: const Icon(Icons.keyboard_arrow_down),   
                    // Array list of items
                    items: GIMenu.map((String GIMenu) {
                      return DropdownMenuItem(
                        value: GIMenu,
                        child: Text(GIMenu),
                      );
                    }).toList(),
                    // After selecting the desired option,it will
                    // change button value to selected value
                    onChanged: (String? value) {
                      setState(() {
                        valueGI = value ?? "";
                      });
                      user.setGI(value!);
                    },
                  ),
                ),
              ],
            ),
            SizedBox(height: 20),
            const Text(
              'Public Information',
              style: TextStyle(fontSize: 20, color: Colors.pink, fontWeight: FontWeight.bold),
            ),
            const Text(
              'Username',
              style: TextStyle(fontSize: 14, color: Color.fromARGB(175, 233, 30, 98),),
            ),
            Container (
                  width: 375,
                  height: 40,
                  alignment: Alignment.centerLeft,
                  decoration: BoxDecoration(
                    color:  Color.fromARGB(35, 233, 30, 98),
                    border: Border.all(
                      color: Colors.pink,
                      width: 1,
                    ),
                    borderRadius: BorderRadius.circular(5),
                    ),
                  child: Text(
                  ' ${user.uid}',
                  style: const TextStyle(color: Colors.black, fontSize: 16,),
                  textAlign: TextAlign.left,
                  ),
                ),
            const SizedBox(height: 5),
            const Text(
              'Display Name',
              style: TextStyle(fontSize: 14, color: Color.fromARGB(175, 233, 30, 98),),
            ),
            SizedBox(
              width: 375,
              child: TextField(
                keyboardType: TextInputType.multiline,
                minLines: 1,
                maxLines: 20,
                controller: displayNameController..text = '${user.displayName}',
                maxLength: 50,
                decoration: InputDecoration(
                  border: OutlineInputBorder(),   
                  ),
                textAlignVertical: TextAlignVertical.top,
                ),
              ),
            const Text(
              'Note',
              style: TextStyle(fontSize: 14, color: Color.fromARGB(175, 233, 30, 98),),
            ),
            SizedBox(
              width: 375,
              child: TextField(
                keyboardType: TextInputType.multiline,
                minLines: 1,
                maxLines: 20,
                controller: noteController..text = '${user.note}',
                maxLength: 500,
                decoration: const InputDecoration(
                  border: OutlineInputBorder(), 
                  ),
                textAlignVertical: TextAlignVertical.top,
                
                ),
              ),
          ],
        ),
        ),
    ),
    );
  }
  
  CurrentUser initializeUser(GoogleSignInAccount _user, CurrentUser user) {
    String? none = "n/a";
    user.setID(_user);
    user.setEmail(_user);
    user.setName(_user);
    user.setUID(_user);
    user.setSO(none);
    user.setGI(none);
    user.setNote(user.displayName!+" has not written a note.");

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

