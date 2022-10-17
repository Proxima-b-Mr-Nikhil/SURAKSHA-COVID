package com.nikhilece.ad_suraksha;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Healthassess extends AppCompatActivity {
    TextView one,two,three,four,five,a,b,c;
    EditText editText;
    FirebaseUser firebaseUser;
    private FirebaseAuth auth;
    DatabaseReference reference;
    ScrollView scrollView;
    ImageButton send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthassess);

        one=findViewById(R.id.one);
        two=findViewById(R.id.two);
        three=findViewById(R.id.three);
        four=findViewById(R.id.four);
        five=findViewById(R.id.five);
        editText=findViewById(R.id.text_send);
        send=findViewById(R.id.btn_send);
        a=findViewById(R.id.a);
        b=findViewById(R.id.b);
        c=findViewById(R.id.c);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sym=editText.getText().toString();
                if( !sym.equals("")){
                    a.setText(sym);

                    editText.setText("");
                    a.setVisibility(View.VISIBLE);
                    one.setVisibility(View.VISIBLE);
                    two.setVisibility(View.VISIBLE);
                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    reference = FirebaseDatabase.getInstance().getReference("assessment").child(firebaseUser.getUid());
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("symptoms", sym);
                    hashMap.put("sym1", "N/A");
                    hashMap.put("sym2", "N/A");

                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                            }
                        }
                    });
                    send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String sym=editText.getText().toString();
                            if( !sym.equals("")) {
                                b.setText(sym);
                                editText.setText("");
                                b.setVisibility(View.VISIBLE);
                                three.setVisibility(View.VISIBLE);
                                four.setVisibility(View.VISIBLE);
                                scrollView = findViewById(R.id.body_scroll);
                                scrollView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        scrollView.fullScroll(View.FOCUS_DOWN);
                                    }
                                });

                                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                assert firebaseUser != null;
                                if (!firebaseUser.getUid().isEmpty()) {
                                    reference = FirebaseDatabase.getInstance().getReference("assessment").child(firebaseUser.getUid());
                                    HashMap<String, String> hashMap = new HashMap<>();
                                    hashMap.put("symptoms", a.getText().toString());
                                    hashMap.put("sym1", sym);
                                    hashMap.put("sym2", "N/A");

                                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                            }
                                        }
                                    });
                                    send.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String sym = editText.getText().toString();
                                            if (!sym.equals("")) {
                                                c.setText(sym);
                                                editText.setText("");
                                                c.setVisibility(View.VISIBLE);
                                                five.setVisibility(View.VISIBLE);

                                                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                                reference = FirebaseDatabase.getInstance().getReference("assessment").child(firebaseUser.getUid());
                                                HashMap<String, String> hashMap = new HashMap<>();
                                                hashMap.put("symptoms", a.getText().toString());
                                                hashMap.put("sym1", b.getText().toString());
                                                hashMap.put("sym2", sym);

                                                reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            editText.setEnabled(false);

                                                        }
                                                    }
                                                });
                                            }

                                        }
                                    });
                                }

                            }
                        }
                    });

                }


            }
        });

    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        startActivity ( new Intent(Healthassess.this, home.class));
        Animatoo.animateSlideRight(Healthassess.this);
    }
}
