package com.example.app.user.track;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.app.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.HashMap;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private static final String TAG = "MapActivity";
    private static final String USER_PHONE_NUMBER = "+919940807458";
    private LatLng myLocation;
    private LatLng otherUserLocation;
    private Polyline polyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        enableMyLocation();
        disableScrolling();
        getUserLocationByPhoneNumber(USER_PHONE_NUMBER);
    }

    @SuppressLint("MissingPermission")
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            getLastKnownLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @SuppressLint("MissingPermission")
    private void getLastKnownLocation() {
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                addMarkerOnMap(myLocation, "My Location");

                if (otherUserLocation != null) {
                    drawPolylineAndShowDistance();
                }
            }
        });
    }

    private void getUserLocationByPhoneNumber(String phoneNumber) {
        HashMap<String, LatLng> userLocations = new HashMap<>();
        userLocations.put("+919940807458", new LatLng(13.0417591, 80.2340761)); // Example Location

        if (userLocations.containsKey(phoneNumber)) {
            otherUserLocation = userLocations.get(phoneNumber);
            if (otherUserLocation != null) {
                addMarkerOnMap(otherUserLocation, "User's Location");

                if (myLocation != null) {
                    drawPolylineAndShowDistance();
                }
            }
        } else {
            Log.e(TAG, "User location not found for phone: " + phoneNumber);
        }
    }

    private void addMarkerOnMap(LatLng location, String title) {
        mMap.addMarker(new MarkerOptions().position(location).title(title));
    }

    private void drawPolylineAndShowDistance() {
        if (myLocation == null || otherUserLocation == null) return;

        // Draw polyline
        if (polyline != null) polyline.remove(); // Remove old polyline if exists
        polyline = mMap.addPolyline(new PolylineOptions()
                .add(myLocation, otherUserLocation)
                .width(8)
                .color(0xFF0077FF)); // Blue Line

        // Calculate and display distance
        float distance = calculateDistance(myLocation, otherUserLocation);
        Toast.makeText(this, "Distance: " + distance + " km", Toast.LENGTH_LONG).show();

        // Adjust camera to fit both markers
        adjustCameraView();
    }

    private float calculateDistance(LatLng start, LatLng end) {
        float[] results = new float[1];
        Location.distanceBetween(start.latitude, start.longitude, end.latitude, end.longitude, results);
        return results[0] / 1000; // Convert to KM
    }

    private void adjustCameraView() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(myLocation);
        builder.include(otherUserLocation);
        LatLngBounds bounds = builder.build();

        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100)); // Padding 100
    }

    private void disableScrolling() {
        mMap.getUiSettings().setScrollGesturesEnabled(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();
            }
        }
    }
}
