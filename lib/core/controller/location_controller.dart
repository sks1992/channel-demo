import 'dart:convert';

import 'package:channel_demo/core/utils/constants.dart';
import 'package:flutter/services.dart';
import 'package:get/get.dart';

class LocationController extends GetxController {
  final channel = const MethodChannel(methodChannelName);

  var isLoading = false.obs;
  var locationId = "".obs;
  var latitude = "".obs;
  var longitude = "".obs;
  var time = "".obs;

  @override
  void onInit() async {
    await getLastLocationFromDb();
    super.onInit();
  }

  Future<void> getLastLocationFromDb() async {
    isLoading.value = true;
    var result = await channel.invokeMethod(getLastLocation);
    isLoading.value = false;
    var jsonData = json.decode(result);
    locationId.value = jsonData['id'] as String;
    latitude.value = jsonData['latitude'] as String;
    longitude.value = jsonData['longitude'] as String;
    time.value = jsonData['time'] as String;
    print(time);
  }

  Future<void> getLocationFromDb() async {
    isLoading.value = true;
    var result = await channel.invokeMethod(getLocation);
    isLoading.value = false;
    var jsonData = json.decode(result);
    locationId.value = jsonData['id'] as String;
    latitude.value = jsonData['latitude'] as String;
    longitude.value = jsonData['longitude'] as String;
    time.value = jsonData['time'] as String;
    print(time);
  }
}
