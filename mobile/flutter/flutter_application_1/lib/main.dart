// ignore_for_file: non_constant_identifier_names, avoid_print

import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'dart:developer' as developer;
import 'dart:convert';
import 'package:http/http.dart' as http;

import 'api/google_signin_api.dart';
import 'page/profile_page.dart';
import 'page/create_post_page.dart';


// To the next person writing flutter code. flutter.io and geeksforgeeks is your
// best friend. Flutter.io to see sample code on how each individual widget works
// and geeksforgeeks to see the properties of each widget and the things you can do
// with them
void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'The Buzz',
      theme: ThemeData(
        primarySwatch: Colors.pink,
      ),
      home: const MyLoginPage(title: 'The Buzz'),
      //home: const MyHomePage(title: 'The Buzz'),
    );
  }
}
class MyLoginPage extends StatefulWidget {
  const MyLoginPage({super.key, required this.title});

  final String title;
  @override
  State<MyLoginPage> createState() => _MyLoginPage();
}

class _MyLoginPage extends State<MyLoginPage> {
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
    var snackBar = SnackBar(content: Text('Sign in Failed'));
    GoogleSignInAuthentication googleSignInAuthentication = await user!.authentication;

    print(googleSignInAuthentication.accessToken);
    print(googleSignInAuthentication.idToken);
    // If the user login is not valid, show that sign-in failed
    if (user == null) {
      ScaffoldMessenger.of(context).showSnackBar(snackBar);
    } else {
      // If user logging in is valid, send user to profile page
      Navigator.of(context).pushReplacement(MaterialPageRoute(builder: (context) => ProfilePage(user: user),
      ));
    }
  }
}


class MyHomePage extends StatefulWidget {
  final GoogleSignInAccount user;
  const MyHomePage({super.key, required this.title, required this.user});
  // This widget is the home page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".
  final String title;
  @override
  State<MyHomePage> createState() => _MyHomePageState();
}


class _MyHomePageState extends State<MyHomePage> {
  int selectedIndex = 0;
  // This method will make it so the bottom navigation bar works and highlights
  // whatever tab ur supposed to be in
  void itemTapped(int index) {
    setState(() {
      selectedIndex = index;
      // When profile icon is selected, push Profile Page
      if (selectedIndex == 1) {
        Navigator.of(context).pushReplacement(MaterialPageRoute( builder: (context) => ProfilePage(user: widget.user)));
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    // This method is rerun every time setState is called
    return Scaffold(
      // Here we take the value from the MyHomePage object that was created by
      // the App.build method, and use it to set our appbar title.
      appBar: AppBar(
        title: Text(widget.title),
        centerTitle: true,
      ),
      body: const Center(
        child: ListofIdeas(),
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
}



class ListofIdeas extends StatefulWidget {
  const ListofIdeas({super.key});

  @override
  State<ListofIdeas> createState() => _ListofIdeasState();
}

class _ListofIdeasState extends State<ListofIdeas> {
  @override
  void initState() {
    super.initState();
  }

  void retry() {
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    // boolean varaible stating that the likes icon should be white
    // FIXME: create an enabled and disabled button color and functionality
    bool postLiked = false; // Default State
    var fb = FutureBuilder<List<Idea>>(
      future: fetchIdeas(),
      builder: (BuildContext context, AsyncSnapshot<List<Idea>> snapshot) {
        Widget child;
        if (snapshot.hasData) {
          // create listview to show one row per array element of json response
          child= Scrollbar(
            child: ListView.builder(
              padding: const EdgeInsets.all(26.0),
              itemCount: snapshot.data!.length,
              itemBuilder: (context, i) {
                return Column(
                  children: <Widget>[
                    ListTile(
                        // FIXME:
                        // This is supposed to remove a like
                        onLongPress: () {
                          updateLike(i);
                        },
                        title: Text(
                          snapshot.data![i].title,
                          style: const TextStyle(fontWeight: FontWeight.bold),
                        ),
                        subtitle: Text(snapshot.data![i].message),
                        tileColor: const Color.fromARGB(200, 240, 128, 128), // Message Color
                        trailing: GestureDetector(
                          onDoubleTap: () {
                            setState(() {
                              //pass in snapshot.data.id
                              postLiked = !postLiked;
                            });
                          },
                          child: Icon(
                            Icons.favorite,
                            color: postLiked ? Colors.pink : Colors.white,
                          ),
                        )),
                    const Divider(height: 8.0),
                  ],
                );
              }));
        } else if (snapshot.hasError) {
          child = Text('${snapshot.error}');
        } else {
          child = const CircularProgressIndicator(); //show a loading spinner.
        }
        return child;
      },
    );
    return fb;
  }
}

// Idea class holds the title and message of every idea
class Idea {
  final String title;
  final String message;

  const Idea({required this.title, required this.message});

  factory Idea.fromJson(Map<String, dynamic> json) {
    return Idea(
      title: json['title'],
      message: json['massage'],
    );
  }
}

// fetchIdeas cast is in charge of pulling the title and message from the database
Future<List<Idea>> fetchIdeas() async {
  final response = await http
      .get(Uri.parse('https://cse216-fl22-team14.herokuapp.com/ideas'));
  if (response.statusCode == 200) {
    // If the server did return a 200 OK response, then parse the JSON.
    final List<Idea> returnData;
    var res = jsonDecode(response.body);
    res = res['mData'];
    print('json decode: $res');

    if (res is List) {
      returnData = (res).map((x) => Idea.fromJson(x)).toList();
    } else if (res is Map) {
      returnData = <Idea>[Idea.fromJson(res as Map<String, dynamic>)];
    } else {
      developer
          .log('ERROR: Unexpected json response type (was not a List or Map).');
      returnData = List.empty();
    }
    return returnData;
  } else {
    // If the server did not return a 200 OK response,
    // then throw an exception.
    throw Exception('Did not receive success status code from request.');
  }
}
// TODO: Add dislike button and functionality, connect to database
class Likes {
  final int numLikes;

  const Likes({
    required this.numLikes,
  });

  factory Likes.fromJson(Map<int, dynamic> json) {
    return Likes(
      numLikes: json['id'],
    );
  }
}
// FIXME:
// fetchLikes method is supposed to fetch the like # for a specific id
Future<List<Likes>> fetchLikes(int id) async {
  final response = await http
      .get(Uri.parse('https://cse216-fl22-team14.herokuapp.com/ideas/${id}'));
  if (response.statusCode == 200) {
    // If the server did return a 200 OK response, then parse the JSON.
    final List<Likes> returnData;
    var res = jsonDecode(response.body);
    res = res['mData'];
    print('json decode: $res');

    if (res is List) {
      returnData = (res).map((x) => Likes.fromJson(x)).toList();
    } else if (res is Map) {
      returnData = <Likes>[Likes.fromJson(res as Map<int, dynamic>)];
    } else {
      developer
          .log('ERROR: Unexpected json response type (was not a List or Map).');
      returnData = List.empty();
    }
    return returnData;
  } else {
    // If the server did not return a 200 OK response,
    // then throw an exception.
    throw Exception('Did not receive success status code from request.');
  }
}
// FIXME:
// updateLike method will post the like to the specific post with the specific id
Future<Likes> updateLike(int id) async {
  final response = await http.put(
      Uri.parse('https://cse216-fl22-team14.herokuapp.com/likes/${id}'),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(<String, int>{'id': id}));
  if (response.statusCode == 200) {
    // If the server did return a 200 CREATED response,
    // then parse the JSON.
    return Likes.fromJson(jsonDecode(response.body));
  } else {
    // If the server did not return a 200 CREATED response,
    // then throw an exception.
    throw Exception('Failed to like post.');
  }
}



