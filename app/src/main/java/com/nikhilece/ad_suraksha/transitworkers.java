package com.nikhilece.ad_suraksha;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.Objects;

public class transitworkers extends AppCompatActivity implements OnMapReadyCallback{
    EditText name,aadhar,phone,email,paddress,profession,company,latt,lng,destination;
    ProgressBar progressBar;
    tUser use;


    private Button registerButton;
    FirebaseDatabase database;
    DatabaseReference ref;
    ImageView responce;
    private Location mLastKnownLocation;
    private LocationCallback locationCallback;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 200;
    private GoogleMap mMap;
    DatabaseReference reference;
    FirebaseUser fuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transitworkers);
        hi();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) transitworkers.this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        aadhar = findViewById(R.id.aadhar);
        email = findViewById(R.id.email);
        paddress = findViewById(R.id.address);
        profession = findViewById(R.id.profession);
        company = findViewById(R.id.company);
        latt = findViewById(R.id.plat);
        lng = findViewById(R.id.plang);
        destination = findViewById(R.id.destination);
        progressBar = findViewById(R.id.progress);


        responce = findViewById(R.id.rspmsg);

        registerButton = findViewById(R.id.register_button);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        assert fuser != null;
        if (!fuser.getUid().isEmpty()) {
            reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Userprofil user = dataSnapshot.getValue(Userprofil.class);
                    name.setText(Objects.requireNonNull(user).getName());
                    aadhar.setText(Objects.requireNonNull(user).getAadhar());
                    email.setText(Objects.requireNonNull(user).getEmail());
                    phone.setText(Objects.requireNonNull(user).getPhone());
                    paddress.setText(Objects.requireNonNull(user).getAddress());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
            database = FirebaseDatabase.getInstance();
            ref = database.getReference("transitworkersReg");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(transitworkers.this, "unable to process try again", Toast.LENGTH_SHORT).show();
                }
            });
            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String fname = name.getText().toString();

                    String faadhar = aadhar.getText().toString();
                    String fphone = phone.getText().toString();
                    String femail = email.getText().toString();
                    String fpaddress = paddress.getText().toString();
                    String fprofession = profession.getText().toString();
                    String fcompany = company.getText().toString();
                    String flat = latt.getText().toString();
                    String flng = lng.getText().toString();
                    String fdestination = destination.getText().toString();

                    if (TextUtils.isEmpty(fname)) {

                        name.setError("enter name");
                        return;
                    }
                    if (TextUtils.isEmpty(faadhar)) {

                        aadhar.setError("enter aadhar no.");
                        return;
                    }
                    if (TextUtils.isEmpty(fphone)) {

                        phone.setError("enter phone no.");
                        return;
                    }
                    if (TextUtils.isEmpty(femail)) {
                        email.setError("enter email");
                        return;
                    }

                    if (TextUtils.isEmpty(fpaddress)) {

                        paddress.setError("enter Full Address.");
                        return;
                    }
                    if (TextUtils.isEmpty(fprofession)) {

                        profession.setError("enter profession.");
                        return;
                    }
                    if (TextUtils.isEmpty(fcompany)) {
                        company.setError("enter company name");
                        return;
                    }

                    if (TextUtils.isEmpty(flat)) {

                        latt.setError("enter Accurate latitude.");
                        return;
                    }
                    if (TextUtils.isEmpty(flng)) {

                        lng.setError("enter Accurate longitude.");
                        return;
                    }
                    if (TextUtils.isEmpty(fdestination)) {

                        destination.setError("enter full destination address.");
                        return;
                    }
                    use = new tUser(fname, faadhar, fphone, femail, fpaddress, fprofession, fcompany, flat, flng, fdestination);
                    progressBar.setVisibility(View.VISIBLE);
                    String tx = String.valueOf(android.text.format.DateFormat.format("dd-MM-yyyy       hh:mm:ss a", new Date()));
                    ref.child(tx).setValue(use);
                    name.setText("");
                    aadhar.setText("");
                    phone.setText("");
                    email.setText("");
                    paddress.setText("");
                    profession.setText("");
                    company.setText("");
                    latt.setText("");
                    lng.setText("");
                    destination.setText("");
                    responce.setVisibility(View.VISIBLE);
                    Toast.makeText(transitworkers.this, "Uploaded successfully", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });

        }
    private void hi() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please allow location to use this app")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            Intent intent=new Intent(transitworkers.this,home.class);
                            startActivity(intent);
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();

        }


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        // Authenticate with Firebase when the Google map is loaded
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        mMap.setMaxZoomPreference(30);
        LatLngBounds INDIA = new LatLngBounds(
                new LatLng(17.3850, 78.4867), new LatLng(17.3850, 78.4867));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(INDIA.getCenter(), 15));

        mMap= googleMap;

        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(transitworkers.this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener(){
            @Override
            public boolean onMyLocationButtonClick()
            {
                getDeviceLocation();
                mMap.setMyLocationEnabled(true);
                return false;
            }
        });

        task.addOnSuccessListener(transitworkers.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getDeviceLocation();
            }

        });

        task.addOnFailureListener(transitworkers.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    try {
                        resolvable.startResolutionForResult(transitworkers.this, 51);
                    } catch (IntentSender.SendIntentException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });


    }

    private void getDeviceLocation() {
        fusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                latt.setText(String.valueOf(mLastKnownLocation.getLatitude()));
                                lng.setText(String.valueOf(mLastKnownLocation.getLongitude()));
                                mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener(){
                                    @Override
                                    public boolean onMyLocationButtonClick()
                                    {
                                        String lt=latt.getText().toString();
                                        String lg=lng.getText().toString();

                                        if (lt.equals("")&&lg.equals("")){

                                            finish();
                                        }
                                        latt.setText(String.valueOf(mLastKnownLocation.getLatitude()));
                                        lng.setText(String.valueOf(mLastKnownLocation.getLongitude()));

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
                                        lng.setText(String.valueOf(mLastKnownLocation.getLongitude()));

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
                            Toast.makeText(transitworkers.this, "unable to get last location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        startActivity ( new Intent(transitworkers.this, home.class));
        Animatoo.animateSlideRight(transitworkers.this);
    }
}
