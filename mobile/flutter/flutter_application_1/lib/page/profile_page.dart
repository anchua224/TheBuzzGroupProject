import 'package:flutter/material.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'package:flutter_application_1/api/google_signin_api.dart';

import '../main.dart';
import 'create_post_page.dart';

// States for the navigation bar
enum NavState { home, profile}
const Map<NavState, String> navState = {
  NavState.home: 'Home',
  //NavState.create: 'create',
  NavState.profile: 'Profile',
};

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

class ProfileState extends State<ProfilePage> {
  late final GoogleSignInAccount user;

  int selectedIndex = 1; // Navigation bar, shows profile is selected
  // This method will make it so the bottom navigation bar works and highlights
  // whatever tab ur supposed to be in
  void itemTapped(int index) {
    setState(() {
      selectedIndex = index;
      if (selectedIndex == 0) { // When home icon is selected, push HomePage
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
      title: const Text('The Buzz'),
      centerTitle: true,
      actions: [
        ElevatedButton( // Logout button function
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
      color: Colors.white,
      child: Column(
        mainAxisAlignment: MainAxisAlignment.start,
        // Layout of profile page
        children: [
          const SizedBox(height: 15),
          const Center( // Profile Picture
            child: CircleAvatar(
              radius: 50,
            //FIXME: If photo is null, causes error. Change to if user has a profile picture, display it
            // if not, display generic profile picture
            //foregroundImage: NetworkImage(user.photoUrl!),
            ),
          ),
          // Spacing between fields
          const SizedBox(height: 15),
          // Username
          Container (
            width: 350,
            height: 35,
            alignment: Alignment.centerLeft,
            child: const Text(
            'Username  ',
            style: TextStyle(color: Colors.pink, fontSize: 22, fontWeight: FontWeight.bold),
            textAlign: TextAlign.left,
            ),
          ),
          Container (
            width: 350,
            height: 45,
            decoration: BoxDecoration(
              color: Color.fromARGB(255, 238, 141, 149),
              border: Border.all(
                color: Color.fromARGB(255, 226, 89, 101),
                width: 3,
              ),
              borderRadius: BorderRadius.circular(12),
              ),    
            alignment: Alignment.centerLeft,
            child: Text(
            '  ${((widget.user.email!).split('@')[0])}', // Parse email for username
            style: const TextStyle(color: Colors.white, fontSize: 20,),
            textAlign: TextAlign.left,
            ),
          ),
          const SizedBox(height: 5),
          // Name
          Container (
            width: 350,
            height: 35,
            alignment: Alignment.centerLeft,
            child: const Text(
            'Name  ',
            style: TextStyle(color: Colors.pink, fontSize: 22, fontWeight: FontWeight.bold),
            textAlign: TextAlign.left,
            ),
          ),
          Container (
            alignment: Alignment.centerLeft,
            width: 350,
            height: 45,
            decoration: BoxDecoration(
              color: Color.fromARGB(255, 238, 141, 149),
              border: Border.all(
                color: Color.fromARGB(255, 226, 89, 101),
                width: 3,
              ),
              borderRadius: BorderRadius.circular(12),
              ),    
            child: Text(
              '  ${widget.user.displayName!}',
              style: const TextStyle(color: Colors.white, fontSize: 20),
              textAlign: TextAlign.left,
            ),
          ),
          const SizedBox(height: 5),
          // User short description
          Container (
            width: 350,
            height: 35,
            alignment: Alignment.centerLeft,
            child: const Text(
            'Bio  ',
            style: TextStyle(color: Colors.pink, fontSize: 22, fontWeight: FontWeight.bold),
            textAlign: TextAlign.left,
            ),
          ),
          Container (
            alignment: Alignment.topLeft,
            width: 350,
            height: 150,
            decoration: BoxDecoration(
              color: Color.fromARGB(255, 238, 141, 149),
              border: Border.all(
                color: Color.fromARGB(255, 226, 89, 101),
                width: 3,
              ),
              borderRadius: BorderRadius.circular(12),
              ),    
            child: const Text(
              ' n/a ',
              style: TextStyle(color: Colors.white, fontSize: 20),
              textAlign: TextAlign.left,
            ),
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
            MaterialPageRoute(builder: (context) => AddIdeaState(user: widget.user)),
          );
        },
      ), // This trailing comma makes auto-formatting nicer for build methods.
  );
}
