package com.nikhilece.ad_suraksha;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class checkstatus extends AppCompatActivity {
    TextView name,mobile,address,email,aadhar,bank,ifsc,dec,lat,lng,id,time,status;
    EditText inpemail;
    FirebaseDatabase database;
    DatabaseReference ref;
    Button button;
    ProgressBar progressBar;
    private static final String USER="SaveOurSoul";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkstatus);
        name=findViewById(R.id.name);
        mobile=findViewById(R.id.mobile);
        address=findViewById(R.id.address);
        aadhar=findViewById(R.id.aadhar);
        email=findViewById(R.id.email);
        bank=findViewById(R.id.bank);
        ifsc=findViewById(R.id.ifsc);
        dec=findViewById(R.id.dec);
        lat=findViewById(R.id.lat);
        lng=findViewById(R.id.lng);
        id=findViewById(R.id.id);
        time=findViewById(R.id.time);
        status=findViewById(R.id.status);

        inpemail=findViewById(R.id.editText);
        button=findViewById(R.id.button);
        progressBar=findViewById(R.id.progressBar3);
        database=FirebaseDatabase.getInstance();
        ref=database.getReference(USER);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds:dataSnapshot.getChildren()){
                            String em=  inpemail.getText().toString();
                            String key = ds.child("aadhar").getValue(String.class);
                            if (em.equals(key)){
                                email.setText(ds.child("email").getValue(String.class));
                                name.setText(ds.child("name").getValue(String.class));
                                address.setText(ds.child("address").getValue(String.class));
                                aadhar.setText(ds.child("aadhar").getValue(String.class));
                                mobile.setText(ds.child("phone").getValue(String.class));
                                bank.setText(ds.child("bankacc").getValue(String.class));
                                ifsc.setText(ds.child("ifsc").getValue(String.class));
                                dec.setText(ds.child("description").getValue(String.class));
                                lat.setText(ds.child("lat").getValue(String.class));
                                lng.setText(ds.child("lng").getValue(String.class));
                                id.setText(ds.child("uid").getValue(String.class));
                                time.setText(ds.child("time").getValue(String.class));
                                status.setText(ds.child("status").getValue(String.class));
                                progressBar.setVisibility(View.GONE);

                            }else {
                              progressBar.setVisibility(View.GONE);
                            }
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        startActivity ( new Intent(checkstatus.this, sos.class));
        Animatoo.animateSlideRight(checkstatus.this);
    }
}

