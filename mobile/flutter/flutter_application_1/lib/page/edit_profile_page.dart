import 'package:flutter/material.dart';

import 'dart:convert';
import 'package:http/http.dart' as http;

import 'profile_page.dart';
import '../objects/user.dart';

class EditProfilePage extends StatefulWidget {
  final User user;

  const EditProfilePage({
    Key? key,
    required this.user,
  }) : super(key: key);

  @override
  State<StatefulWidget> createState() => EditProfileState();
}


class EditProfileState extends State<EditProfilePage> {
  // CDC Lists for Sexual Orientation
  final SOMenu = [
    ' Straight or Heterosexual',
    ' Lesbian or Gay',
    ' Bisexual',
    ' Queer, Pan, and/or Questioning',
    ' Other', //If selected other, create a input textbox
    ' Don\'t Know',
    ' Decline to Answer'
  ];
  // CDC Lists for Gender Identity
  final GIMenu = [
    ' Male',
    ' Female',
    ' Transgender Man/ Trans Man',
    ' Transgender Woman/ Trans Woman',
    ' Genderqueer/ gender nonconforming neither exclusively male nor female', 
    ' Decline to Answer'
  ];
  String? valueSO = null; 
  bool hintSO = true;
  String? valueGI = null; 

  // The field in which users can text
  final displayNameController = TextEditingController();
  final noteController = TextEditingController();

  @override
  void dispose() {
    displayNameController.dispose();
    noteController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    //var user = User();
    //FIXME: initialize user in another page
    //user = User().initializeUser(widget.user, user);
    if (widget.user.so != 'NO SO YET') {
      valueSO = widget.user.so;
      hintSO = false;
    } 
    if (widget.user.gi != 'NO GI YET') {
      valueGI = widget.user.gi;
    }
    // user.setName();
    return Scaffold (
      appBar: AppBar(
        title: const Text('Edit Profile'),
        centerTitle: true,
        actions: [
          ElevatedButton.icon(
            label: const Text('Done'),
            icon: const Icon(Icons.check_circle),
            onPressed: () {
              updateProfile(widget.user);
              Navigator.of(context).pushReplacement(MaterialPageRoute(builder: (context) => ProfilePage(user: widget.user),
              ));
            },
          )
        ],
      ),
        body: SingleChildScrollView(
          child: Padding(
          padding: const EdgeInsets.fromLTRB(20, 5, 20, 10),
          child: Column(
          crossAxisAlignment: CrossAxisAlignment. start,
          children: [
            const SizedBox(height: 15),
            Center(
              child: GestureDetector(
                child: const CircleAvatar(
                    radius: 45,
                    backgroundColor: Color.fromARGB(255, 251, 207, 126),),
                onTap: () async{
                  // when picture is tapped, user can upload an image
                },
              ),
            ),
            const SizedBox(height: 20),
            const Text(
              'Private Information',
              style: TextStyle(fontSize: 20, color: Colors.pink, fontWeight: FontWeight.bold),
            ),
            const Text(
              'Email',
              style: TextStyle(fontSize: 14, color: Color.fromARGB(175, 233, 30, 98),),
            ),
            Column(
              children: [
                Container (
                  width: 375,
                  height: 40,
                  alignment: Alignment.centerLeft,
                  decoration: BoxDecoration(
                    color:  const Color.fromARGB(35, 233, 30, 98),
                    border: Border.all(
                      color: Colors.pink,
                      width: 1,
                    ),
                    borderRadius: BorderRadius.circular(5),
                    ),
                  child: Text(
                  ' ${widget.user.email}',
                  style: const TextStyle(color: Colors.black, fontSize: 16,),
                  textAlign: TextAlign.left,
                  ),
                ),
                const SizedBox(height: 5),
                Container (
                  alignment: Alignment.centerLeft,
                  child: const Text(
                    'Sexual Orientation',
                    style: TextStyle(fontSize: 14, color: Color.fromARGB(175, 233, 30, 98),),
                  ),
                ),
                Container(
                  alignment: Alignment.centerLeft,
                  width: 375,
                  height: 50, 
                  decoration: BoxDecoration(
                    color: Colors.white,
                    border: Border.all(
                      color: const Color.fromARGB(150, 233, 30, 98),
                      width: 1,
                    ),
                    borderRadius: BorderRadius.circular(5),
                    ),
                  child: DropdownButton(
                    isExpanded: true,
                    disabledHint: const Text("Select Sexual Orientation"),
                    // Initial Value
                    value: valueSO,
                    // Down Arrow Icon
                    icon: const Icon(Icons.keyboard_arrow_down),   
                    // Array list of items
                    items: SOMenu.map((String SOMenu) {
                      return DropdownMenuItem(
                        value: SOMenu,
                        child: Text(SOMenu),
                      );
                    }).toList(),
                    // After selecting the desired option,it will
                    // change button value to selected value
                    onChanged: (String? value) {
                      setState(() {
                        valueSO = value ?? "";
                      });
                      widget.user.setSO(value!);
                    },
                  ),
                ),
                const SizedBox(height: 5),
                Container (
                  alignment: Alignment.centerLeft,
                  child: const Text(
                    'Gender Identity',
                    style: TextStyle(fontSize: 14, color: Color.fromARGB(175, 233, 30, 98),),
                  ),
                ),
                Container(
                  alignment: Alignment.centerLeft,
                  width: 375,
                  height: 50, 
                  decoration: BoxDecoration(
                    color: Colors.white,
                    border: Border.all(
                      color: const Color.fromARGB(150, 233, 30, 98),
                      width: 1,
                    ),
                    borderRadius: BorderRadius.circular(5),
                    ),
                  child: DropdownButton(
                    isExpanded: true,
                    disabledHint: const Text("Select Gender Identity"),
                    // Initial Value
                    value: valueGI,
                    // Down Arrow Icon
                    icon: const Icon(Icons.keyboard_arrow_down),   
                    // Array list of items
                    items: GIMenu.map((String GIMenu) {
                      return DropdownMenuItem(
                        value: GIMenu,
                        child: Text(GIMenu),
                      );
                    }).toList(),
                    // After selecting the desired option,it will
                    // change button value to selected value
                    onChanged: (String? value) {
                      setState(() {
                        valueGI = value ?? "";
                      });
                      widget.user.setGI(value!);
                    },
                  ),
                ),
              ],
            ),
            const SizedBox(height: 20),
            const Text(
              'Public Information',
              style: TextStyle(fontSize: 20, color: Colors.pink, fontWeight: FontWeight.bold),
            ),
            const Text(
              'Username',
              style: TextStyle(fontSize: 14, color: Color.fromARGB(175, 233, 30, 98),),
            ),
            Container (
                  width: 375,
                  height: 40,
                  alignment: Alignment.centerLeft,
                  decoration: BoxDecoration(
                    color:  const Color.fromARGB(35, 233, 30, 98),
                    border: Border.all(
                      color: Colors.pink,
                      width: 1,
                    ),
                    borderRadius: BorderRadius.circular(5),
                    ),
                  child: Text(
                  ' ${widget.user.uid}',
                  style: const TextStyle(color: Colors.black, fontSize: 16,),
                  textAlign: TextAlign.left,
                  ),
                ),
            const SizedBox(height: 5),
            const Text(
              'Display Name',
              style: TextStyle(fontSize: 14, color: Color.fromARGB(175, 233, 30, 98),),
            ),
            SizedBox(
              width: 375,
              child: TextField(
                keyboardType: TextInputType.multiline,
                minLines: 1,
                maxLines: 20,
                controller: displayNameController..text = '${widget.user.displayName}',
                maxLength: 50,
                decoration: const InputDecoration(
                  border: OutlineInputBorder(),   
                  ),
                textAlignVertical: TextAlignVertical.top,
                onChanged: (String text) {
                  widget.user.setNewName(text);
                },
                ),
              ),
            const Text(
              'Note',
              style: TextStyle(fontSize: 14, color: Color.fromARGB(175, 233, 30, 98),),
            ),
            SizedBox(
              width: 375,
              child: TextField(
                keyboardType: TextInputType.multiline,
                minLines: 1,
                maxLines: 20,
                controller: noteController..text = '${widget.user.note}',
                maxLength: 500,
                decoration: const InputDecoration(
                  border: OutlineInputBorder(), 
                  ),
                textAlignVertical: TextAlignVertical.top,
                onChanged: (String text) {
                  widget.user.setNote(text);
                },
                ),
              ),
          ],
        ),
        ),
    ),
    );
  }
  // Update Profile Information with put request
  void updateProfile(User user) async {
    Uri url = Uri.parse('https://cse216-fl22-team14.herokuapp.com/profile/${user.sessionKey}?sessionKey=${user.sessionKey}');
    final response = await http.put(url,
      headers: <String, String>{
        'content-type': 'application/json',
      },
      body: jsonEncode(<String, String>{
        'mName': user.displayName!,
        'mNote': user.note!,
        'mSO': user.so!,
        'mGI': user.gi!,
      }),
    );
    if (response.statusCode == 200) {
      // If the server did return a 200 CREATED response,
      // then parse the JSON.
      // print("Updated note: "+ user.note!); // Used for debugging
    } else {
      // If the server did not return a 200 CREATED response,
      // then throw an exception.
      throw Exception('Failed to update profile.');
    }
  }
}

