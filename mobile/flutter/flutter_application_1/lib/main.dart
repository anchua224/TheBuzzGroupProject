// ignore_for_file: non_constant_identifier_names, avoid_print

import 'package:flutter/material.dart';
import 'dart:developer' as developer;
import 'dart:convert';
import 'package:http/http.dart' as http;

// This is going to be the post method that will post new ideas
// that will go into the database
void my_async_post_method() async {
  var data = {'title': 'My first post'};
  var resp = await http.post(
    Uri.parse(''),
    headers: {'Content-Type': 'application/json; charset=UTF-8'},
    body: json.encode(data),
  );
  print(resp.body);
}

// This is gping to be the get method that will get the ideas
// from the database and display them in the app
void my_async_get_method() async {
  var url = Uri.https('');
  var response = await http.get(url, headers: {});
  print('Response status: ${response.statusCode}');
  print('Response body: ${response.body}');
}

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
        primarySwatch: Colors.blue,
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
  @override
  Widget build(BuildContext context) {
    // This method is rerun every time setState is called
    //
    // The Flutter framework has been optimized to make rerunning build methods
    // fast, so that you can just rebuild anything that needs updating rather
    // than having to individually change instances of widgets.
    return Scaffold(
      appBar: AppBar(
        // Here we take the value from the MyHomePage object that was created by
        // the App.build method, and use it to set our appbar title.
        title: Text(widget.title), centerTitle: true,
      ),
      body: const Center(
        child: ListofIdeas(),
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

  void _retry() {
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    var fb = FutureBuilder<List<Idea>>(
      future: fetchIdeas(),
      builder: (BuildContext context, AsyncSnapshot<List<Idea>> snapshot) {
        Widget child;
        if (snapshot.hasData) {
          // developer.log('`using` ${snapshot.data}', name: 'my.app.category');
          // create  listview to show one row per array element of json response
          child = ListView.builder(
              //shrinkWrap: true, //expensive! consider refactoring. https://api.flutter.dev/flutter/widgets/ScrollView/shrinkWrap.html
              padding: const EdgeInsets.all(26.0),
              itemCount: snapshot.data!.length,
              itemBuilder: /*1*/ (context, i) {
                return Column(
                  children: <Widget>[
                    ListTile(
                      title: Text(
                        snapshot.data![i].str,
                        style: const TextStyle(fontWeight: FontWeight.bold),
                      ),
                      tileColor: const Color.fromARGB(100, 181, 126, 220),
                    ),
                    const Divider(height: 8.0),
                  ],
                );
              });
        } else if (snapshot.hasError) {
          child = Text('${snapshot.error}');
        } else {
          // awaiting snapshot data, return simple text widget
          // child = Text('Calculating answer...');
          child = const CircularProgressIndicator(); //show a loading spinner.
        }
        return child;
      },
    );
    return fb;
  }
}

class Idea {
  /// The string representation of the number
  final String str;

  const Idea({
    required this.str,
  });

  factory Idea.fromJson(Map<String, dynamic> json) {
    return Idea(
      str: json['title'],
    );
  }
}

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

class Messages {
  /// The string representation of the number
  final String str;

  const Messages({
    required this.str,
  });

  factory Messages.fromJson(Map<String, dynamic> json) {
    return Messages(
      str: json['massage'],
    );
  }
}

Future<List<Messages>> fetchMessages() async {
  final response = await http
      .get(Uri.parse('https://cse216-fl22-team14.herokuapp.com/ideas'));

  if (response.statusCode == 200) {
    // If the server did return a 200 OK response, then parse the JSON.
    final List<Messages> returnData;
    var res = jsonDecode(response.body);
    res = res['mData'];
    print('json decode: $res');

    if (res is List) {
      returnData = (res).map((x) => Messages.fromJson(x)).toList();
    } else if (res is Map) {
      returnData = <Messages>[Messages.fromJson(res as Map<String, dynamic>)];
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

//The concept of this class would be used to add an idea to the app.
//The button for it is present and clickable but I need to be able to add
//an action as soon as the button is pressed that would take you to another
//page which will maove you to a different state
class AddanIdea extends StatelessWidget {
  const AddanIdea({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          //This will take me to the page where I can make a post
          Navigator.push(
            context,
            MaterialPageRoute(builder: (context) => const AddIdeaState()),
          );
        },
        tooltip: 'Add an Idea',
        child: const Icon(Icons.add),
      ),
    );
  }
}

//The class LikeIdea would be used to like an idea when you are currently
//viewing that post. Once the button is clicked it would be clickable again
//but instead remove the like from the post
class LikeIdea extends StatelessWidget {
  const LikeIdea({super.key});
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () {
          // Add your onPressed code here!
        },
        label: const Text('Like'),
        icon: const Icon(Icons.favorite),
        backgroundColor: Colors.pink,
      ),
    );
  }
}

//The class FirstRoute is just a test class for testing whether I am able
//to move between states using widgets. THe concept of this would be used
//to be able to clikc on a post and take you to a state where you can directly
//read the entire post and actions that can be taken from there
class FirstRoute extends StatelessWidget {
  const FirstRoute({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: ElevatedButton(
            child: const Text('Open route'),
            // Within the `FirstRoute` widget
            onPressed: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (context) => const AddIdeaState()),
              );
            }),
      ),
    );
  }
}

//The AddIdeaState class would be the state of the app once you are in the post
//and is a test class t see how you can return to the previous state with information
//that has been saved without changing any of the older information
class AddIdeaState extends StatelessWidget {
  const AddIdeaState({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Create Post'),
      ),
      body: const Center(
        child: LikeIdea(),
      ),
    );
  }
}

// Define a custom Form widget.
class MyCustomForm extends StatefulWidget {
  const MyCustomForm({super.key});

  @override
  State<MyCustomForm> createState() => _MyCustomFormState();
}

// Define a corresponding State class.
// This class holds the data related to the Form.
class _MyCustomFormState extends State<MyCustomForm> {
  // Create a text controller and use it to retrieve the current value
  // of the TextField.
  final myController = TextEditingController();

  @override
  void dispose() {
    // Clean up the controller when the widget is disposed.
    myController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return TextField(
      controller: myController,
    );
  }
}
