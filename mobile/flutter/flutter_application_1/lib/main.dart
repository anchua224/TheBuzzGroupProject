// ignore_for_file: non_constant_identifier_names, avoid_print
import 'dart:collection';

import 'package:flutter/material.dart';
import 'package:flutter_application_1/page/view_idea.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'dart:developer' as developer;
import 'dart:convert';
import 'package:http/http.dart' as http;

import 'page/profile_page.dart';
import 'page/create_post_page.dart';
import 'page/login_page.dart';
import 'page/view_idea.dart';

import '../objects/user.dart';

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
      home: const LoginPage(title: 'The Buzz'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  final User user;
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
        Navigator.of(context).pushReplacement(MaterialPageRoute(
            builder: (context) => ProfilePage(user: widget.user)));
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
      body: Center(
        child: ListOfIdeas(user: widget.user),
      ),
      bottomNavigationBar: BottomNavigationBar(
        backgroundColor: Colors.pinkAccent,
        items: const <BottomNavigationBarItem>[
          BottomNavigationBarItem(
            icon: Icon(Icons.home),
            label: 'Home',
          ),
          BottomNavigationBarItem(
            icon: Icon(Icons.person),
            label: 'Profile',
          ),
        ],
        currentIndex: selectedIndex,
        selectedItemColor: Colors.white,
        onTap: itemTapped,
      ),
      floatingActionButtonLocation: FloatingActionButtonLocation.centerDocked,
      floatingActionButton: FloatingActionButton(
        child: const Icon(
          Icons.add,
        ),
        onPressed: () {
          //This will take me to the page where I can make a post
          Navigator.push(
            context,
            MaterialPageRoute(
                builder: (context) => AddIdeaState(user: widget.user)),
          );
        },
      ), // This trailing comma makes auto-formatting nicer for build methods.
    );
  }
}

class ListOfIdeas extends StatefulWidget {
  final User user;

  const ListOfIdeas({
    Key? key,
    required this.user,
  }) : super(key: key);

  @override
  State<ListOfIdeas> createState() => _ListOfIdeasState();
}

class _ListOfIdeasState extends State<ListOfIdeas> {
  var name = "";
  Map<String, String> names = HashMap();
  @override
  void initState() {
    name = "hi";
    super.initState();
  }

  void retry() {
    setState(() {});
  }

  int n = 0;
  // List<bool> postLiked = List.generate(6, (i) => false); // Default State
  // List<bool> postDisliked = List.generate(6, (i) => false);
  @override
  Widget build(BuildContext context) {
    Color _icon_fav_color = Colors.red;
    int INDEX = 0;
    var fb = FutureBuilder<List<Idea>>(
      future: fetchIdeas(),
      builder: (BuildContext context, AsyncSnapshot<List<Idea>> snapshot) {
        Widget child;
        if (snapshot.hasData) {
          // create listview to show one row per array element of json response
          child = Scrollbar(
              child: ListView.builder(
                  padding: const EdgeInsets.all(26.0),
                  itemCount: snapshot.data!.length,
                  itemBuilder: (context, i) {
                    // Hash names into a hash map with user id
                    getProfileData(snapshot.data![i].userid, widget.user)
                        .then((String val) {
                      names[snapshot.data![n].userid] = val;
                    });
                    return Column(
                      children: <Widget>[
                        ListTile(
                          title: Text(
                            names[snapshot.data![i]
                                .userid]!, //////////////////////null error here
                            style: const TextStyle(fontSize: 14),
                          ),
                          subtitle: Row(
                            children: [
                              Column(
                                children: [
                                  const SizedBox(height: 10),
                                  Container(
                                    width: 280,
                                    alignment: Alignment.centerLeft,
                                    child: Text(
                                      snapshot.data![i].title,
                                      style: const TextStyle(
                                          fontWeight: FontWeight.bold,
                                          fontSize: 16),
                                    ),
                                  ),
                                  Container(
                                    width: 280, //originally 300
                                    alignment: Alignment.centerLeft,
                                    child: Text(
                                      snapshot.data![i].message,
                                      style: const TextStyle(fontSize: 14),
                                    ),
                                  ),
                                  const SizedBox(height: 10),
                                ],
                              ),
                              Column(
                                /////////problems: heart buttons are all connected!
                                // this is an issue for when we want to send an update to the DB
                                // we want to change only one of the heart buttons per post
                                children: [
                                  Container(
                                    alignment: Alignment.centerRight,
                                    height: 20,
                                    width: 20,
                                    child: IconButton(
                                      icon: Icon(Icons.favorite),
                                      color: _icon_fav_color,
                                      //color: postLiked[i] ? Colors.pink : Colors.white,
                                      onPressed: () {
                                        setState(() {
                                          //pass in snapshot.data.id
                                          // postLiked[i] = !postLiked[i];
                                          print(
                                              "Color: should've change to red!");
                                          INDEX = 1;
                                          print(INDEX);
                                          // _icon_fav_color = Colors.red;
                                          // icon:
                                          // Icon(Icons.favorite,
                                          //     color: _icon_fav_color);
                                        });
                                      },
                                    ),
                                  ),
                                  const SizedBox(height: 5),
                                  Container(
                                    alignment: Alignment.centerRight,
                                    height: 30,
                                    width: 20,
                                    child: IconButton(
                                      //color: postDisliked[i] ? Colors.blue : Colors.white,
                                      onPressed: () {
                                        setState(() {
                                          INDEX = 2;
                                          print(INDEX);
                                          //pass in snapshot.data.id
                                          // postDisliked[i] = !postDisliked[i];
                                          // postLiked[i] = !postLiked[i];
                                        });
                                      },
                                      icon: const Icon(Icons.heart_broken),
                                    ),
                                  ),
                                  const SizedBox(height: 5),
                                  Container(
                                    alignment: Alignment.centerRight,
                                    height: 20,
                                    width: 20,
                                    child: IconButton(
                                      color: Colors.white,
                                      onPressed: () {
                                        // View Idea Page
                                        Navigator.of(context).pushReplacement(
                                            MaterialPageRoute(
                                                builder: (context) =>
                                                    ViewIdeaPage(
                                                      user: widget.user,
                                                      idea: snapshot.data![i],
                                                      id: i,
                                                      title: 'The Buzz',
                                                    )));
                                      },
                                      icon: const Icon(Icons.open_in_new),
                                    ),
                                  ),
                                  const SizedBox(height: 10),
                                ],
                              ),
                            ],
                          ),

                          tileColor: const Color.fromARGB(
                              200, 251, 207, 126), // Message Color
                        ),
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

  void getName(String userid, User user) {
    getProfileData(userid, user);
  }

//////////////////////////////////////////////////////////////////////////////
  /// error for getting the profile data
//////////////////////////////////////////////////////////////////////////////
  Future<String> getProfileData(String userid, User user) async {
    final response = await http.get(Uri.parse(
        'https://cse216-fl22-team14-new.herokuapp.com/profile/${userid}?sessionKey=${user.sessionKey}'));
    var res = jsonDecode(response.body);
    res = res['mData'];
    // print(res);
    var name = res['name'];
    // print(name);
    if (response.statusCode == 200) {
      // If the server did return a 200 OK response, then parse the JSON.
      // var returnData = JSON.parse(response.body);

      // return jsonDecode(response.body)['mData'];
    } else {
      // If the server did not return a 200 OK response,
      // then throw an exception.
      throw Exception('Did not receive success status code from request.');
    }
    return name;
  }
}

void color_change_func(Color _icon_fav_color) {
  if (_icon_fav_color == Colors.red) {
    _icon_fav_color = Colors.grey;
  } else {
    _icon_fav_color = Colors.red;
  }
}

// Idea class holds the title and message of every idea
class Idea {
  final String title;
  final String message;
  final int id;
  final int validity;
  final String userid;
  final String dateCreated;

  const Idea(
      {required this.title,
      required this.message,
      required this.id,
      required this.validity,
      required this.userid,
      required this.dateCreated});

  factory Idea.fromJson(Map<String, dynamic> json) {
    return Idea(
      title: json['title'],
      message: json['massage'],
      id: json['id'],
      validity: json['validity'],
      userid: json['userid'],
      dateCreated: json['createdDate'],
    );
  }
}

// fetchIdeas cast is in charge of pulling the title and message from the database
Future<List<Idea>> fetchIdeas() async {
  final response = await http
      .get(Uri.parse('https://cse216-fl22-team14-new.herokuapp.com/ideas'));
  if (response.statusCode == 200) {
    // If the server did return a 200 OK response, then parse the JSON.
    final List<Idea> returnData;
    var res = jsonDecode(response.body);
    res = res['mData'];
    // print('json decode: $res');
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
Future<List<Likes>> fetchLikes(int id, User user) async {
  final response = await http.get(Uri.parse(
      'https://cse216-fl22-team14-new.herokuapp.com/ideas/${id}/like?sessionKey=${user.sessionKey}'));
  if (response.statusCode == 200) {
    // If the server did return a 200 OK response, then parse the JSON.
    final List<Likes> returnData;
    var res = jsonDecode(response.body);
    res = res['mData'];
    print('json decode: $res');
    print("No refernece");

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
Future<Likes> updateLike(int id, User user) async {
  final response = await http.put(
      Uri.parse(
          'https://cse216-fl22-team14-new.herokuapp.com/likes/$id/like?sessionKey=${user.sessionKey}'),
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
