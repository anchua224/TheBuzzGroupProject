import 'package:flutter/material.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'package:meta/meta.dart';


// Class contains information for user currently logged into the app
class User {
   User(GoogleSignInAccount user){
    id = user.id;
    email = user.email;
    uid = (((user.email).split('@'))[0]);
    displayName = user.displayName;
    so = "n/a";
    gi = "n/a";
    note  = "n/a";
   }

  // User properties
  late String? id; late String? email; late final String? uid;
  late String? displayName;
  late String? photoURL;
  late String? so;
  late String? gi;
  late String? note;
  late String sessionKey;
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
  void setSessionKey(String key) {
    sessionKey = key;
  }
  User initializeUser(GoogleSignInAccount gUser, User user) {
    String? none = "n/a";
    user.setID(gUser);
    user.setEmail(gUser);
    user.setName(gUser);
    user.setUID(gUser);
    user.setSO(none);
    user.setGI(none);
    user.setNote(user.displayName!+" has not written a note.");

    return user;
  }
}

class DBUser {
  String? userid;
  String? email;
  String? displayName;
  String? gi;
  String? so;
  String? note;
  String? createdDate;

  DBUser({required this.userid, required this.email, required this.displayName, required this.gi, 
    required this.so, required this.note, required this.createdDate});

  factory DBUser.fromJson(Map<String, dynamic> json) {
    return DBUser(
      userid: json['user_id'],
      email: json['email'],
      displayName: json['name'],
      gi: json['GI'],
      so: json['SO'],
      note: json['note'],
      createdDate: json['createdDate'],
    );
  }
}
