import 'package:flutter/material.dart';
import 'package:google_sign_in/google_sign_in.dart';

class NewUserPage extends StatefulWidget {
  final GoogleSignInAccount user;

  const NewUserPage({
    Key? key,
    required this.user,
  }) : super(key: key);

  @override
  State<StatefulWidget> createState() => NewUserState();
}

class NewUserState extends State<NewUserPage> {
  // Require User to Complete Profile
  @override
  Widget build(BuildContext context) {
    return Scaffold();
  }
}