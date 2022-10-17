package com.nikhilece.ad_suraksha;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.skyfishjy.library.RippleBackground;

import java.util.HashMap;


public class nearbycases extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    TextView tv,mTextView;

    private FusedLocationProviderClient fusedLocationProviderClient;


    private Location mLastKnownLocation;
    private LocationCallback locationCallback;

    private View mapView;
    private Button btnFind;
    private RippleBackground rippleBg;
    EditText latt,lngg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearbycases);
hi();
        tv = (TextView) this.findViewById(R.id.tv);
        tv.setSelected(true); tv = (TextView) this.findViewById(R.id.tv);

        btnFind = findViewById(R.id.btn_find);
        rippleBg = findViewById(R.id.ripple_bg);
        latt=findViewById(R.id.plat);
        lngg=findViewById(R.id.plang);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(nearbycases.this);

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lt=latt.getText().toString();
                String lg=lngg.getText().toString();
                if (lt.equals("")&&lg.equals("")){
                    Toast.makeText(nearbycases.this, "Please enable your location ", Toast.LENGTH_SHORT).show();
                    return;
                }

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), 14));

                rippleBg.startRippleAnimation();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rippleBg.stopRippleAnimation();

                       search();

                    }
                }, 3000);

            }

        });
    }


    private void hi() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please allow location to use this app")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            Intent intent = new Intent(nearbycases.this, home.class);
                            startActivity(intent);
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();

        }
    }
    private void search() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("nearbycases");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String previousChildName) {



                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                    String key = dataSnapshot.getKey();
                    HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
                    double lat = Double.parseDouble(value.get("latitude").toString());
                    double lng = Double.parseDouble(value.get("longitude").toString());

                    String lt=latt.getText().toString();
                    String lg=lngg.getText().toString();
                    if (lt.equals("")&&lg.equals("")){

                        return;
                    }

                    double a = Double.parseDouble(latt.getText().toString());
                    double b = Double.parseDouble(lngg.getText().toString());



                    Location loc1 = new Location("");
                    loc1.setLatitude(a);
                    loc1.setLongitude(b);

                    Location loc2 = new Location("");
                    loc2.setLatitude(lat);
                    loc2.setLongitude(lng);

                    float distanceInMeters = loc1.distanceTo(loc2);
                    float distanceinkm =distanceInMeters/1000;

                    if (distanceinkm <=10){


                        LatLng location = new LatLng(lat, lng);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12));

                        mMap.addMarker(new MarkerOptions().position(location).title(key)
                                .icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ic_wifi_tethering_black_24dp)) );

                    }

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }



    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId){
        Drawable vectorDrawable= ContextCompat.getDrawable(context,vectorResId);
        vectorDrawable.setBounds(0,0,vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap= Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas=new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
hi();
        rippleBg.startRippleAnimation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rippleBg.stopRippleAnimation();

                search();

            }
        }, 3000);

        mMap = googleMap;

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMaxZoomPreference(30);
        LatLngBounds INDIA = new LatLngBounds(
                new LatLng(17.3850, 78.4867), new LatLng(17.3850, 78.4867));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(INDIA.getCenter(), 15));

        loginToFirebase();
        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 40, 180);
        }

        //check if gps is enabled or not and then request user to enable it
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(nearbycases.this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(nearbycases.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getDeviceLocation();


            }
        });

        task.addOnFailureListener(nearbycases.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    try {
                        resolvable.startResolutionForResult(nearbycases.this, 51);
                    } catch (IntentSender.SendIntentException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {

                return false;
            }
        });
    }

    private void loginToFirebase() {
        String email = getString(R.string.firebase_email);
        String password = getString(R.string.firebase_password);
        // Authenticate with Firebase and subscribe to updates
        FirebaseAuth.getInstance().signInWithEmailAndPassword(
                email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) {


                } else {
                    Toast.makeText(nearbycases.this, "unable to login the database ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 51) {
            if (resultCode == RESULT_OK) {
                getDeviceLocation();
                search();

            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        fusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                latt.setText(String.valueOf(mLastKnownLocation.getLatitude()));
                                lngg.setText(String.valueOf(mLastKnownLocation.getLongitude()));
                                mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener(){
                                    @Override
                                    public boolean onMyLocationButtonClick()
                                    {
                                        String lt=latt.getText().toString();
                                        String lg=lngg.getText().toString();

                                        if (lt.equals("")&&lg.equals("")){

                                            finish();
                                        }
                                        latt.setText(String.valueOf(mLastKnownLocation.getLatitude()));
                                        lngg.setText(String.valueOf(mLastKnownLocation.getLongitude()));

                                        return true;
                                    }
                                });

                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), 15));
                            } else {
                                final LocationRequest locationRequest = LocationRequest.create();
                                locationRequest.setInterval(10000);
                                locationRequest.setFastestInterval(5000);
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                locationCallback = new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        super.onLocationResult(locationResult);
                                        if (locationResult == null) {
                                            return;
                                        }
                                        mLastKnownLocation = locationResult.getLastLocation();

                                        latt.setText(String.valueOf(mLastKnownLocation.getLatitude()));
                                        lngg.setText(String.valueOf(mLastKnownLocation.getLongitude()));

                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), 15));
                                        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                                        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener(){
                                            @Override
                                            public boolean onMyLocationButtonClick()
                                            {

                                                finish();

                                                return true;
                                            }
                                        });

                                    }
                                };
                                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

                            }
                        } else {
                            Toast.makeText(nearbycases.this, "unable to get last location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        startActivity ( new Intent(nearbycases.this, home.class));
        Animatoo.animateSlideRight(nearbycases.this);
    }
}

