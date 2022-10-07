// ignore_for_file: non_constant_identifier_names, avoid_print

import 'package:flutter/material.dart';
import 'dart:developer' as developer;
import 'dart:convert';
import 'package:http/http.dart' as http;

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
      home: const MyHomePage(title: 'The Buzz'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});
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
            MaterialPageRoute(builder: (context) => const AddIdeaState()),
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
    // boolean baraible stating that the likes icon should be white
    bool postLiked = false;
    var fb = FutureBuilder<List<Idea>>(
      future: fetchIdeas(),
      builder: (BuildContext context, AsyncSnapshot<List<Idea>> snapshot) {
        Widget child;
        if (snapshot.hasData) {
          // create listview to show one row per array element of json response
          child = ListView.builder(
              padding: const EdgeInsets.all(26.0),
              itemCount: snapshot.data!.length,
              itemBuilder: (context, i) {
                return Column(
                  children: <Widget>[
                    ListTile(
                        // This is supposed to remove a like
                        onLongPress: () {
                          //updateLike()
                        },
                        title: Text(
                          snapshot.data![i].title,
                          style: const TextStyle(fontWeight: FontWeight.bold),
                        ),
                        subtitle: Text(snapshot.data![i].message),
                        tileColor: const Color.fromARGB(200, 240, 128, 128),
                        trailing: GestureDetector(
                          onTap: () {
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
              });
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
      .get(Uri.parse('https://cse216-fl22-team14.herokuapp.com//ideas'));
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

// Likes class holds the num of likes of an individual idea
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

// fetchLikes method is supposed to fetch the like # for a specific id
Future<List<Likes>> fetchLikes(int id) async {
  final response = await http
      .get(Uri.parse('https://cse216-fl22-team14.herokuapp.com/likes/:id'));
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

// updateLike method will post the like to the specific post with the specific id
Future<Likes> updateLike(int id) async {
  final response = await http.put(
      Uri.parse('https://cse216-fl22-team14.herokuapp.com/likes/:id'),
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

// AddIdeaState class would be the state of the app once you are trying to post
// an idea
class AddIdeaState extends StatelessWidget {
  const AddIdeaState({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Create Post'),
      ),
      body: Column(
        children: const <Widget>[TextBox()],
      ),
    );
  }
}

// createPost method is the method called in order to post an idea to the
// database
createPost(String title, String message) async {
  final response = await http.post(
    Uri.parse('https://cse216-fl22-team14.herokuapp.com/ideas'),
    headers: <String, String>{
      'Content-Type': 'application/json; charset=UTF-8',
    },
    body: jsonEncode(<String, String>{
      'mTitle': title,
      'mMessage': message,
    }),
  );
  if (response.statusCode == 200) {
    // If the server did return a 200 CREATED response,
    // then parse the JSON.
  } else {
    // If the server did not return a 200 CREATED response,
    // then throw an exception.
    throw Exception('Failed to create Post.');
  }
}

class TextBox extends StatefulWidget {
  const TextBox({super.key});

  @override
  State<TextBox> createState() => _TextBoxState();
}

class _TextBoxState extends State<TextBox> {
  // Create a text controller and use it to retrieve the current value
  // of the TextField.
  @override
  void initState() {
    super.initState();
  }

  // The field in which users can text
  final titleController = TextEditingController();
  final messageController = TextEditingController();

  @override
  void dispose() {
    // Clean up the controller when the widget is disposed.
    titleController.dispose();
    messageController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: <Widget>[
        TextField(
          controller: titleController,
          decoration: const InputDecoration(
              hintText: 'Enter Title', border: OutlineInputBorder()),
          autofocus: true,
        ),
        TextField(
          controller: messageController,
          decoration: const InputDecoration(
              hintText: 'Enter Message', border: OutlineInputBorder()),
        ),
        ElevatedButton(
            onPressed: () {
              setState(() {
                createPost(titleController.text, messageController.text);
              });
            },
            child: const Text('Create Post'))
      ],
    );
  }
}
