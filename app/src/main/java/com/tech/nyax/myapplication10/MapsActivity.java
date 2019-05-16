//package com.tech.nyax.myapplication10;
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.text.SpannableString;
//import android.text.SpannableStringBuilder;
//import android.text.style.ForegroundColorSpan;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.EditText;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.List;
//
//public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
//
//private final static String TAG = MapsActivity.class.getSimpleName();
//private GoogleMap mMap;
//
//@Override
//protected void onCreate(Bundle savedInstanceState) {
//super.onCreate(savedInstanceState);
//setContentView(R.layout.maps_activity_layout);
//SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//.findFragmentById(R.id.map);
//mapFragment.getMapAsync(this);
//}
//
//@Override
//public void onMapReady(GoogleMap googleMap) {
//mMap = googleMap;
//// Add a marker in Sydney, Australia, and move the camera.
//LatLng sydney = new LatLng(-34, 151);
//mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//}
//
//}
//
//
