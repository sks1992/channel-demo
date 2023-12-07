import 'package:channel_demo/app_route.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return GetMaterialApp(
      title: 'Flutter Demo',
      debugShowCheckedModeBanner: false,
      getPages: AppRoute.route,
      initialRoute: RouteName.locationScreen,
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
    );
  }
}
