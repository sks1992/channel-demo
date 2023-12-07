package com.example.channel_demo

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import java.text.SimpleDateFormat
import java.util.Date
import com.google.android.gms.location.Priority as pro

class MainActivity : FlutterActivity() {
    private val channelName = "Location"
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val permissionRequestCode = 0

    @SuppressLint("NewApi")
    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val db = DatabaseHelper(context)
        val channel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, channelName)
        channel.setMethodCallHandler { call, result ->

            when (call.method) {
                "getLastLocation" -> {
                    val locationList = db.getAllLocationData().last()
                    if (db.getAllLocationData().isEmpty()) {
                        getLastLocation()
                        val lastLocationList = db.getAllLocationData().last()
                        Toast.makeText(
                            this,
                            "first location${lastLocationList.time}",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        result.success(
                            "{\"id\":\"${lastLocationList.id}\",\"latitude\":\"${lastLocationList.latitude}\",\"longitude\":\"${lastLocationList.longitude}\",\"time\":\"${lastLocationList.time}\"}"
                        )
                    } else {
                        Toast.makeText(this, "last location${locationList.time}", Toast.LENGTH_SHORT)
                            .show()
                        result.success(
                            "{\"id\":\"${locationList.id}\",\"latitude\":\"${locationList.latitude}\",\"longitude\":\"${locationList.longitude}\",\"time\":\"${locationList.time}\"}"
                        )
                    }
                }
                "getLocation" -> {
                        getLastLocation()
                        val lastLocationList = db.getAllLocationData().last()
                        Toast.makeText(
                            this,
                            "first location${lastLocationList.time}",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        result.success(
                            "{\"id\":\"${lastLocationList.id}\",\"latitude\":\"${lastLocationList.latitude}\",\"longitude\":\"${lastLocationList.longitude}\",\"time\":\"${lastLocationList.time}\"}"
                        )
                }
                else -> result.notImplemented()
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission", "SimpleDateFormat")
    private fun getLastLocation() {
        val db = DatabaseHelper(context)
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                val resultLocation = fusedLocationClient.getCurrentLocation(
                    pro.PRIORITY_BALANCED_POWER_ACCURACY,
                    CancellationTokenSource().token
                )
                resultLocation.addOnCompleteListener { location ->
                    val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
                    val currentDate = sdf.format(Date())
                    val locationModel =
                        LocationModel(
                            0,
                            location.result.latitude,
                            location.result.longitude,
                            currentDate
                        )
                    db.insertLocation(locationModel)
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    //This will check if the user has turned on location from the setting, Cause user may grant the app to user location but if the location setting is off then it’ll be of no use
    private fun checkPermissions(): Boolean {
        if (
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }


    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionRequestCode
        )
    }

    @SuppressLint("NewApi")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionRequestCode) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }
}
