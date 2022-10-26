import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'dart:developer' as developer;
import 'dart:convert';
import 'package:http/http.dart' as http;

import '../api/google_signin_api.dart';
import 'profile_page.dart';
import '../main.dart';

// AddIdeaState class would be the state of the app once you are trying to post
// an idea
class AddIdeaState extends StatelessWidget {
  final GoogleSignInAccount user;

  const AddIdeaState({
    Key? key,
    required this.user,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Create Post'),
      ),
      body: Column(
        children: <Widget>[TextBox(user: user,)],
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
  final GoogleSignInAccount user;

  const TextBox({
    Key? key,
    required this.user,
  }) : super(key: key);

  @override
  State<TextBox> createState() => _TextBoxState();
}

class _TextBoxState extends State<TextBox> {
  late final GoogleSignInAccount user;
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
          maxLength: 50,
          decoration: const InputDecoration(
              hintText: 'Enter Title', border: OutlineInputBorder()),
          autofocus: true,
        ),
        TextField(
          controller: messageController,
          maxLength: 500,
          decoration: const InputDecoration(
              hintText: 'Enter Message', border: OutlineInputBorder()),
        ),
        ElevatedButton(
            onPressed: () {
                // Send user back to home page, ideas displayed up-to-date
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (context) => MyHomePage(title: 'The Buzz', user: widget.user)),
              );
              setState(() {
                createPost(titleController.text, messageController.text);
              });
            },
            child: const Text('Create Post'))
      ],
    );
  }
}


