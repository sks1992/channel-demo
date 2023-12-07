import 'package:channel_demo/ui/location_screen.dart';
import 'package:get/get.dart';

class RouteName {
  static String locationScreen = "/location_screen";
}

class AppRoute {
  static final route = [
    GetPage(name: RouteName.locationScreen, page: () => LocationScreen())
  ];
}
