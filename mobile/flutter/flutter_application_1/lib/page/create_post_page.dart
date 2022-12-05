import 'dart:io';

import 'package:flutter/material.dart';
import 'package:google_sign_in/google_sign_in.dart';

import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:image_picker/image_picker.dart';
import 'profile_page.dart';
import '../objects/user.dart';
import '../main.dart';
import 'package:mime/mime.dart';

// AddIdeaState class would be the state of the app once you are trying to post
// an idea
class AddIdeaState extends StatelessWidget {
  final User user;

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
        children: <Widget>[
          TextBox(
            user: user,
          )
        ],
      ),
    );
  }
}

// createPost method is the method called in order to post an idea to the
// database

class TextBox extends StatefulWidget {
  final User user;

  const TextBox({
    Key? key,
    required this.user,
  }) : super(key: key);

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
  // The field in which users can upload files
  final uploadController = "";

  @override
  void dispose() {
    // Clean up the controller when the widget is disposed.
    titleController.dispose();
    messageController.dispose();
    super.dispose();
  }

  late File _image;
  String? _image_type;
  String? _mimeType_image;
  String? mimeType_Camera; // jpeg, png, etc...
  String? mimeType_Gallery; // jpeg, png, etc...
  String? mimeType_File; //pdf
  String? mimeType_Video; //MP3, MP4, etc...

  bool isImage(String path) {
    final mimeType = lookupMimeType(path);
    _mimeType_image = lookupMimeType(path);
    if (mimeType!.endsWith('image/')) {
      _image_type = path.split(".").last; //stores the extension type
    }
    return mimeType!.endsWith('jpg') ||
        mimeType!.endsWith('png') ||
        mimeType!.endsWith('jpeg') ||
        mimeType.endsWith('image/');
  }

  // bool isVideo(String path) {
  //   final mimeType = lookupMimeType(path);
  // }

  bool isPDF(String file) {
    final mimeType = lookupMimeType(file);
    return mimeType!.endsWith('pdf') || mimeType.endsWith('application/pdf');
  }

  // createLinks(String url) async {}
  _getImageFromCamera() async {
    // var image = await ImagePicker.pickImage(source: ImageSource.camera);
    var image = await ImagePicker()
        .pickImage(source: ImageSource.camera, imageQuality: 50);

    if (image != null) {
      setState(() {
        _image = File(image.path);
      });
    }
  }

  _getImageFromGallery() async {
    // var image = await ImagePicker.pickImage(source: ImageSource.gallery, imageQuality: 50);
    final XFile? image =
        await ImagePicker().pickImage(source: ImageSource.gallery);

    if (image != null) {
      setState(() {
        _image = File(image.path);
      });
    }
  }

  //gallery or camera option:
  void _showPicked(context) {
    showModalBottomSheet(
      context: context,
      builder: (BuildContext bc) {
        return SafeArea(
          child: Wrap(
            children: [
              ListTile(
                leading: Icon(Icons.photo_library),
                title: Text('Gallery'),
                onTap: () {
                  _getImageFromGallery();
                  Navigator.of(context).pop();
                },
              ),
              ListTile(
                leading: Icon(Icons.camera_alt),
                title: Text('Camera'),
                onTap: () {
                  _getImageFromCamera();
                  Navigator.of(context).pop();
                },
              ),
            ],
          ),
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          const SizedBox(height: 0),
          Padding(
            padding: const EdgeInsets.fromLTRB(20, 5, 20, 10),
            child: TextField(
              controller: titleController,
              maxLength: 420,
              keyboardType:
                  TextInputType.multiline, //Expands text and creates a new line
              minLines: 1,
              maxLines: 20,
              decoration: const InputDecoration(
                  hintText: 'Enter Title', border: OutlineInputBorder()),
              //autofocus: true, // Opens keyboard to start typing on text box
            ),
          ),
          Padding(
            padding: const EdgeInsets.fromLTRB(20, 5, 20, 10),
            child: SizedBox(
              child: TextField(
                keyboardType: TextInputType.multiline,
                minLines: 1,
                maxLines: 20,
                controller: messageController,
                maxLength: 420,
                decoration: const InputDecoration(
                  border: OutlineInputBorder(),
                  hintText: 'Enter Message',
                ),
                textAlignVertical: TextAlignVertical.top,
              ),
            ),
          ),
          Padding(
            padding: const EdgeInsets.fromLTRB(20, 5, 20, 10),
            child: GestureDetector(
              onTap: (() {
                _showPicked(context);
              }),
              child: Container(
                color: Colors.pink,
                width: 200.0,
                height: 120.0,
                child: const Text(
                  "Select Image",
                  style: TextStyle(color: Colors.white, fontSize: 28),
                ),
              ),
            ),
          ),
          // Padding(
          //   padding: const EdgeInsets.fromLTRB(20, 5, 20, 10),
          //   child: SizedBox(
          //     child: IconButton(
          //       icon: const Icon(
          //         Icons.camera_alt,
          //         size: 35,
          //       ),
          //       color: Color.fromARGB(255, 188, 89, 122),
          //       onPressed: () {
          //         setState(() {
          //           // camera functionality:
          //           // make post request to the google drive
          //         });
          //       },
          //     ),
          //   ),
          // ),
          // Padding(
          //   padding: const EdgeInsets.fromLTRB(20, 5, 20, 10),
          //   child: SizedBox(
          //     child: Text(
          //       "Upload Picture",
          //       style: TextStyle(
          //         color: Color.fromARGB(255, 234, 107, 149),
          //         fontWeight: FontWeight.bold,
          //       ),
          //     ),
          //   ),
          // ),
          Padding(
            padding: const EdgeInsets.fromLTRB(20, 5, 20, 10),
            child: SizedBox(
              child: IconButton(
                icon: const Icon(
                  Icons.upload_file,
                  size: 35,
                ),
                color: Color.fromARGB(255, 188, 89, 122),
                onPressed: () {
                  setState(() {
                    // upload file base64
                    // send file name and MIME type
                    // we want to make a post requets here to creating new posts
                    // url ==> /resources/:id/:com_id?sessionKey
                    // createFileSender();

                    // first access the camera:

                    //below we want this to be in the main file or a newly created file
                    // fetch the content (images) ==> /resources/:id/:com_id?sessionKey

                    String credentials =
                        "demo_test"; //for now it's just nothing important we need to make a get request for retrieving the link
                    Codec<String, String> stringToBase64 = utf8.fuse(base64);
                    String encoded = stringToBase64.encode(
                        credentials); // encoding will produce random characters
                    String decoded = stringToBase64.decode(
                        encoded); //decoding will output the original credentials that had been encoded
                    //
                    //
                  });
                },
              ),
            ),
          ),
          Padding(
            padding: const EdgeInsets.fromLTRB(20, 5, 20, 10),
            child: SizedBox(
              height: 18,
              child: Text(
                "Upload File",
                style: TextStyle(
                  color: Color.fromARGB(255, 234, 107, 149),
                  fontWeight: FontWeight.bold,
                ),
              ),
            ),
          ),
          const SizedBox(height: 0),
          ElevatedButton(
              style: ElevatedButton.styleFrom(
                backgroundColor: Colors.pink,
                foregroundColor: Colors.white,
                fixedSize: const Size(150, 55),
              ),
              onPressed: () {
                // Send user back to home page, ideas displayed up-to-date
                Navigator.push(
                  context,
                  MaterialPageRoute(
                      builder: (context) =>
                          MyHomePage(title: 'The Buzz', user: widget.user)),
                );
                setState(() {
                  createPost(titleController.text, messageController.text);
                });
              },
              child: const Text(
                'Create Post',
                style: TextStyle(fontSize: 20),
              )),
        ],
      ),
    );
  }

  // createCameraImage(FileImage file) async {
  //   //convert image to base64 and send to google drive
  //   // we want to send the name and the MIME type

  //   final response = await http.post(
  //     Uri.parse(
  //         'https://cse216-fl22-team14-new.herokuapp.com/resources/:${widget.user.id}/:com_id?sessionKey=${widget.user.sessionKey}'),
  //     headers: <String, String>{
  //       'Content-Type': 'application/json',
  //     },
  //     body: jsonEncode(<String, dynamic>{
  //       'mId': widget.user.sessionKey,
  //       'mImageFile': file,
  //     }),
  //   );
  // }

  createFileSender(File filesender) async {
    //convert to base64
  }
  var _imageBase64;

  _CreateCameraGalleryPost(File _image_copy) async {
    //convert to base64
    var FileName = (_image_copy.toString().split('/').last);
    http.Response imageRep = await http.get(Uri.parse(_image_copy.path));
    _imageBase64 = base64Encode(imageRep.bodyBytes);

    final response = await http.post(Uri.parse(//uncertain on what :com_id is
            'https://cse216-fl22-team14-new.herokuapp.com/resources/${widget.user.id}/com_id?sessionKey=${widget.user.sessionKey}'),
        headers: <String, String>{
          'Content-Type': 'image/${_image_type}',
        },
        body: jsonEncode(<String, dynamic>{
          'mId': widget.user.sessionKey,
          'mImage': _imageBase64,
          'mMimeType': _mimeType_image,
          'mFileName': FileName,
        }));
  }

  createPost(String title, String message) async {
    final response = await http.post(
      Uri.parse(
          'https://cse216-fl22-team14-new.herokuapp.com/ideas?${widget.user.sessionKey}'),
      headers: <String, String>{
        'Content-Type': 'application/json',
      },
      body: jsonEncode(<String, String>{
        'mId': widget.user.sessionKey,
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
}
