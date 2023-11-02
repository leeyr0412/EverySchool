import 'package:flutter/material.dart';
import 'package:everyschool/page/community/create_post_body.dart';

class CreatePost extends StatefulWidget {
  const CreatePost({Key? key}) : super(key: key);

  @override
  State<CreatePost> createState() => _CreatePostState();
}

class _CreatePostState extends State<CreatePost> {
  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: () async {
        Navigator.pop(context); // 이전 페이지로 돌아가기
        return true;
      },
      child: Scaffold(
        appBar: AppBar(
          backgroundColor: Colors.grey[50],
          elevation: 0,
          centerTitle: true,
          leading: IconButton(
            iconSize: 30,
            icon: Icon(Icons.arrow_back, color: Color(0XFF15075F)),
            onPressed: () {
              Navigator.pop(context); // 이전 페이지로 돌아가기
            },
          ),
          title: Text(
            '글쓰기',
            style: TextStyle(
              color: Colors.black,
              fontSize: 24,
              fontWeight: FontWeight.w700,
            ),
          ),
        ),
        body: CreatePostBody(), // PostList 위젯을 직접 호출
      ),
    );
  }
}
