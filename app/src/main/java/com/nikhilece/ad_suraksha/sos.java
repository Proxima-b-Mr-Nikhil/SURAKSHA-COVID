package com.nikhilece.ad_suraksha;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class sos extends AppCompatActivity implements OnMapReadyCallback{
    EditText name,aadhar,phone,email,paddress,bankacc,ifsc,latt,lng,descripition;
    ProgressBar progressBar;
    sUser us;
    TextView uid,ui;
    Button check,camera;


    private Button registerButton;
    FirebaseDatabase database;
    DatabaseReference ref;
    ImageView responce;
    TextView location;

    private Uri mImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;
    private StorageTask mUploadTask;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    DatabaseReference reference;
    FirebaseUser fuser;

    private Location mLastKnownLocation;
    private LocationCallback locationCallback;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 200;
    private GoogleMap mMap;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);
        spinner = (Spinner) findViewById(R.id.spin);

        List<String> Categories = new ArrayList<>();

        Categories.add(0, "Choose categories");
        Categories.add("Orphan");
        Categories.add("Vulnerable category");
        Categories.add("Other");



        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter<>(sos.this, android.R.layout.simple_spinner_dropdown_item,Categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter((dataAdapter));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                if (position>0){
                    String item = parent.getItemAtPosition(position).toString();


                }


            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        hi();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) sos.this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        aadhar=findViewById(R.id.aadhar);
        email = findViewById(R.id.email);
        paddress = findViewById(R.id.address);
        bankacc=findViewById(R.id.bankacc);
        ifsc=findViewById(R.id.ifsc);
        latt=findViewById(R.id.plat);
        lng=findViewById(R.id.plang);
        descripition=findViewById(R.id.des);
        uid=findViewById(R.id.uid);
        ui=findViewById(R.id.ui);
        check=findViewById(R.id.checkstatus);
        camera=findViewById(R.id.choose);
        progressBar = findViewById(R.id.progress);
        location=findViewById(R.id.location);


        fuser = FirebaseAuth.getInstance().getCurrentUser();
        assert fuser != null;
        if (!fuser.getUid().isEmpty()) {
            reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Userprofil use = dataSnapshot.getValue(Userprofil.class);
                    if (use != null) {
                        name.setText(Objects.requireNonNull(use).getName());
                        aadhar.setText(Objects.requireNonNull(use).getAadhar());
                        email.setText(Objects.requireNonNull(use).getEmail());
                        phone.setText(Objects.requireNonNull(use).getPhone());
                        paddress.setText(Objects.requireNonNull(use).getAddress());
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        mStorageRef = FirebaseStorage.getInstance().getReference("sos");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("SaveOurSoul");


        responce=findViewById(R.id.rspmsg);

        registerButton = findViewById(R.id.register_button);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("SaveOurSoul");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(sos.this, "unable to process try again", Toast.LENGTH_SHORT).show();
            }
        });
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity ( new Intent(sos.this, checkstatus.class));
                Animatoo.animateSlideLeft(sos.this);
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
                String fbankacc = bankacc.getText().toString();
                String fifsc = ifsc.getText().toString();
                String flat =latt.getText().toString();
                String flng = lng.getText().toString();
                String fdescription = descripition.getText().toString();
                String fstatus = "Your Application is under review.";
                String fuid = database.getReference("SaveOurSoul").push().getKey();
                String ftime =  String.valueOf(android.text.format.DateFormat.format("dd-MM-yyyy  hh:mm:ss a", new Date()));

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
                if (TextUtils.isEmpty(fbankacc)) {

                    bankacc.setError("enter bank ac.");
                    return;
                }
                if (TextUtils.isEmpty(fifsc)){
                    ifsc.setError("enter IFSC code");
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
                if (TextUtils.isEmpty(fdescription)) {

                    descripition.setError("Explain");
                    return;
                }




                progressBar.setVisibility(View.VISIBLE);

                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    progressBar.setVisibility(View.VISIBLE);

                    Toast.makeText(sos.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    if (mImageUri != null) {

                        final StorageReference fileReference = mStorageRef.child(faadhar
                                + "." + getFileExtension(mImageUri));

                        mUploadTask=  fileReference.putFile(mImageUri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                            }
                                        }, 500);
                                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String fa=uri.toString();

                                                String fname = name.getText().toString();

                                                String faadhar = aadhar.getText().toString();
                                                String fphone = phone.getText().toString();
                                                String femail = email.getText().toString();
                                                String fpaddress = paddress.getText().toString();
                                                String fbankacc = bankacc.getText().toString();
                                                String fifsc = ifsc.getText().toString();
                                                String flat =latt.getText().toString();
                                                String flng = lng.getText().toString();
                                                String fdescription = descripition.getText().toString();
                                                String fstatus = "Your Application is under review.";
                                                String fuid = database.getReference("SaveOurSoul").push().getKey();
                                                String ftime =  String.valueOf(android.text.format.DateFormat.format("dd-MM-yyyy  hh:mm:ss a", new Date()));

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
                                                if (TextUtils.isEmpty(fbankacc)) {

                                                    bankacc.setError("enter bank ac.");
                                                    return;
                                                }
                                                if (TextUtils.isEmpty(fifsc)){
                                                    ifsc.setError("enter IFSC code");
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
                                                if (TextUtils.isEmpty(fdescription)) {

                                                    descripition.setError("Explain");
                                                    return;
                                                }




                                                us = new sUser(fname,faadhar,fphone,femail,fpaddress,fbankacc,fifsc,flat,flng,fdescription,fuid,ftime,fstatus,fa);
                                                progressBar.setVisibility(View.VISIBLE);

                                                ref.child(faadhar).setValue(us);
                                                name.setText("");
                                                aadhar.setText("");
                                                phone.setText("");
                                                email.setText("");
                                                paddress.setText("");
                                                bankacc.setText("");
                                                ifsc.setText("");
                                                latt.setText("");
                                                lng.setText("");
                                                descripition.setText("");
                                                responce.setVisibility(View.VISIBLE);
                                                Toast.makeText(sos.this, "Uploaded successfully", Toast.LENGTH_SHORT).show();
                                                ui.setText("Unique id :");
                                                uid.setText(fuid);
                                                progressBar.setVisibility(View.INVISIBLE);



                                            }
                                        });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(sos.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                    }
                                });
                    } else {
                        Toast.makeText(sos.this, "No file selected", Toast.LENGTH_SHORT).show();
                    }

                }



            }
        });

    }

    private void hi() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please allow location to use this app")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            Intent intent = new Intent(sos.this, home.class);
                            startActivity(intent);
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();

        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        // Authenticate with Firebase when the Google map is loaded
        mMap = googleMap;
        hi();
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

        SettingsClient settingsClient = LocationServices.getSettingsClient(sos.this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener(){
            @Override
            public boolean onMyLocationButtonClick()
            {
                getDeviceLocation();
                return false;
            }
        });

        task.addOnSuccessListener(sos.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getDeviceLocation();

            }

        });

        task.addOnFailureListener(sos.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    try {
                        resolvable.startResolutionForResult(sos.this, 51);
                    } catch (IntentSender.SendIntentException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            String a=mImageUri.toString();
            location.setText(a);

        }
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
                            Toast.makeText(sos.this, "unable to get last location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        startActivity ( new Intent(sos.this, home.class));
        Animatoo.animateSlideRight(sos.this);
    }
}
