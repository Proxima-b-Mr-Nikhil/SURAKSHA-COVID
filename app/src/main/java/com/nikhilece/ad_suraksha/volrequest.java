package com.nikhilece.ad_suraksha;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.Objects;

public class volrequest extends AppCompatActivity {
    private EditText nameEditText;
    private EditText phoneEditText, emailEditText,address;
    ProgressBar progressBar;
    User user;
    String id;
    public ImageView responce;
    DatabaseReference reference;
    FirebaseUser fuser;

    private Button registerButton;
    FirebaseDatabase database;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volrequest);

        nameEditText = findViewById(R.id.name);
        phoneEditText = findViewById(R.id.phone);
        emailEditText = findViewById(R.id.email);

        address = findViewById(R.id.address);
        progressBar = findViewById(R.id.progressBar3);

        registerButton = findViewById(R.id.register_button);
        responce=findViewById(R.id.rspmsg);
        database = FirebaseDatabase.getInstance();

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        assert fuser != null;
        if (!fuser.getUid().isEmpty()) {
            reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Userprofil user = dataSnapshot.getValue(Userprofil.class);
                    if (user!=null){
                    nameEditText.setText(Objects.requireNonNull(user).getName());
                    emailEditText.setText(Objects.requireNonNull(user).getEmail());
                    phoneEditText.setText(Objects.requireNonNull(user).getPhone());
                    address.setText(Objects.requireNonNull(user).getAddress());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        ref = database.getReference("volunteersRequest");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(volrequest.this, "unable to process try again", Toast.LENGTH_SHORT).show();
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname = nameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String addres = address.getText().toString();

                if (TextUtils.isEmpty(fname)) {

                    nameEditText.setError("enter name");
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    emailEditText.setError("enter email");
                    return;
                }
                if (TextUtils.isEmpty(phone)) {

                    phoneEditText.setError("enter phone no.");
                    return;
                }
                if (TextUtils.isEmpty(addres)) {

                    phoneEditText.setError("enter Full Address.");
                    return;
                }
                if (!fuser.getUid().isEmpty()) {
                    id = fuser.getUid();
                }
                else {
                    id="";
                }
                    user = new User(fname, email, addres, phone, id);
                    progressBar.setVisibility(View.VISIBLE);
                    String tx = String.valueOf(android.text.format.DateFormat.format("dd-MM-yyyy       hh:mm:ss a", new Date()));
                    ref.child(tx).setValue(user);
                    nameEditText.setText("");
                    phoneEditText.setText("");
                    emailEditText.setText("");
                    address.setText("");
                    responce.setVisibility(View.VISIBLE);
                    Toast.makeText(volrequest.this, "Uploaded successfully", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        startActivity ( new Intent(volrequest.this, home.class));
        Animatoo.animateSlideRight(volrequest.this);
    }
}
