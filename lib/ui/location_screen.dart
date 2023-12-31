import 'package:channel_demo/core/controller/location_controller.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

class LocationScreen extends StatelessWidget {
  LocationScreen({Key? key}) : super(key: key);

  final _controller = Get.put(LocationController());

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Get Location"),
        actions: [IconButton(onPressed: () {}, icon: const Icon(Icons.place))],
      ),
      body: SafeArea(
        child: Center(
          child: Obx(
            () => Column(
              mainAxisSize: MainAxisSize.max,
              children: [
                Text(_controller.locationId.value.toString().isEmpty
                    ? "no Data"
                    : _controller.locationId.value.toString()),
                Text(_controller.latitude.value.toString().isEmpty
                    ? "no Data"
                    : _controller.latitude.value.toString()),
                Text(_controller.longitude.value.toString().isEmpty
                    ? "no data"
                    : _controller.longitude.value.toString()),
                Text(_controller.time.value.isEmpty
                    ? "no data"
                    : _controller.time.value.toString()),
                _controller.isLoading.value
                    ? const CircularProgressIndicator()
                    : ElevatedButton(
                        onPressed: () {
                          _controller.getLocationFromDb();
                        },
                        child: const Text("Get Current Location"),
                      ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
