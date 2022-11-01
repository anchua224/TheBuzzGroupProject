import 'package:flutter/material.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'package:meta/meta.dart';

import 'profile_page.dart';
import 'login_page.dart';
import 'edit_profile_page.dart';
import '../main.dart';

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