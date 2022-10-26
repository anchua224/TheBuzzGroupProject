import 'package:flutter/material.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'package:flutter_application_1/api/google_signin_api.dart';

import '../main.dart';

// CDC Lists for Sexual Orientation and Gender Identity
const List<String> sexualOrientation = <String>['Straight or Heterosexual', 'Lesbian or Gay', 'Bisexual', 
  'Queer, Pan, and/or Questioning', 'Other', 'Don\'t Know', 'Decline to Answer'];
const List<String> genderIdentity = <String>['Male', 'Female', 'Transgender Man/ Trans Man', 
  'Transgender Woman/ Trans Woman', 'Genderqueer/ gender nonconforming neither exclusively male nor female', 
  'Other', 'Decline to Answer'];

class ProfilePage extends StatefulWidget {
  final GoogleSignInAccount user;

  const ProfilePage({
    Key? key,
    required this.user,
  }) : super(key: key);

  @override
  State<StatefulWidget> createState() => ProfileState();
}

//TODO: Create different profile page
class ProfileState extends State<ProfilePage> {
  GoogleSignInAccount? user;

  int selectedIndex = 1;
  // This method will make it so the bottom navigation bar works and highlights
  // whatever tab ur supposed to be in
  void itemTapped(int index) {
    setState(() {
      selectedIndex = index;
      if (selectedIndex == 0) {
        Navigator.push(
            context,
            MaterialPageRoute(builder: (context) => MyHomePage(title: 'The Buzz', user: widget.user)),
          );
      } 
    });
  }

  @override
  Widget build(BuildContext context) => Scaffold(
    appBar: AppBar(
      title: const Text('Edit Profile'),
      centerTitle: true,
      actions: [
        ElevatedButton(
          child: const Text('Logout'),
          onPressed: () async {
            await GoogleSignInApi.logout();

            Navigator.of(context).pushReplacement(MaterialPageRoute(builder: (context) => const MyLoginPage(title: 'The Buzz'),
            ));
          },
        )
      ],
    ),
    body: Container(
      alignment: Alignment.topLeft,
      color: const Color.fromARGB(200, 240, 128, 128),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.start,
        children: [
          const SizedBox(height: 8),
          const CircleAvatar(
            radius: 40,
            //FIXME: If photo is null, causes error. Change to if user has a profile picture, display it
            // if not, display generic profile picture
            //backgroundImage: NetworkImage(user.photoUrl!),
          ),
          // const Text(
          //   'Profile',
          //   style: TextStyle(fontSize: 24),
          // ),
          const SizedBox(height: 8),
          Text(
            'Name: ${widget.user.displayName!}',
            style: const TextStyle(color: Colors.white, fontSize: 16), //Might change font size
          ),
          const SizedBox(height: 8),
          Text(
            'Email: ${widget.user.email}',
            style: const TextStyle(color: Colors.white, fontSize: 16),
          ),
        ],
      ),
    ),
    bottomNavigationBar: BottomNavigationBar(
        backgroundColor: Colors.pinkAccent,
        items: const <BottomNavigationBarItem>[
          BottomNavigationBarItem(
            icon: Icon(Icons.home),
            label: 'Home',
          ),
          BottomNavigationBarItem(
            icon: Icon(Icons.circle),
            label: 'Profile',
          ),
        ],
        currentIndex: selectedIndex,
        selectedItemColor: Colors.white,
        onTap: itemTapped,
      ),
      floatingActionButtonLocation: FloatingActionButtonLocation.centerDocked,
      floatingActionButton: FloatingActionButton(
        child: const Icon(Icons.add),
        onPressed: () {
          //This will take me to the page where I can make a post
          Navigator.push(
            context,
            MaterialPageRoute(builder: (context) => const AddIdeaState()),
          );
        },
      ), // This trailing comma makes auto-formatting nicer for build methods.
  );
}

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
        children: [
          InkWell(
            onTap: () {
              navigateEditImagePage(EditImagePage());
            }
            child: DisplayImage(
              imagePath: user?.photoUrl,
              onPressed: (){},
            )
          )
        ],
      ),
    );
  }
}